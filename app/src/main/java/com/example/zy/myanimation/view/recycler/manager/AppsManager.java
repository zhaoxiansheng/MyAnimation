package com.example.zy.myanimation.view.recycler.manager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import com.example.zy.myanimation.view.recycler.callback.AppsManagerCallback;
import com.example.zy.myanimation.view.recycler.model.compat.LauncherActivityInfoCompat;
import com.example.zy.myanimation.view.recycler.model.compat.LauncherAppsCompat;
import com.example.zy.myanimation.view.recycler.model.compat.UserHandleCompat;
import com.example.zy.myanimation.view.recycler.model.compat.UserManagerCompat;
import com.example.zy.myanimation.view.recycler.model.config.AppsConfig;
import com.example.zy.myanimation.view.recycler.model.infos.AllAppsList;
import com.example.zy.myanimation.view.recycler.model.infos.AppInfo;
import com.example.zy.myanimation.view.recycler.utils.Utilities;
import com.example.zy.myanimation.view.recycler.utils.thread.HandlerThreadUtil;

import java.util.Iterator;
import java.util.List;

import static com.example.zy.myanimation.view.recycler.model.infos.AppInfo.NO_ID;

public class AppsManager implements LauncherAppsCompat.OnAppsChangedCallbackCompat {

    private static final String TAG = "AppsManager";
    private static AllAppsList mAllAppsList;
    private Context mContext;
    private LauncherAppsCompat mLauncherApps;
    private final UserManagerCompat mUserManager;
    private final Object sBgLock = new Object();
    private AppsManagerCallback mCallback;
    private static final boolean DEBUG_LOADERS = true;
    Runnable updateR = null;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private LocaleBroadcastReceiver mLocaleReceiver;

    public AppsManager(Context context) {
        if (mAllAppsList == null) {
            mAllAppsList = new AllAppsList();
        }
        mContext = context;
        mLauncherApps = LauncherAppsCompat.getInstance(context);
        mUserManager = UserManagerCompat.getInstance(context);
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_LOCALE_CHANGED);
        mLocaleReceiver = new LocaleBroadcastReceiver();
        context.registerReceiver(mLocaleReceiver, mIntentFilter);
        mLauncherApps.addOnAppsChangedCallback(this);
        loadAndBindAllAppsList(context);
    }

    public void setCallback(AppsManagerCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void onPackageRemoved(String packageName, UserHandleCompat user) {
        int op = PackageUpdatedTask.OP_REMOVE;
        enqueuePackageUpdated(new PackageUpdatedTask(op, new String[]{packageName}, user));
    }

    @Override
    public void onPackageAdded(String packageName, UserHandleCompat user) {
        int op = PackageUpdatedTask.OP_ADD;
        enqueuePackageUpdated(new PackageUpdatedTask(op, new String[]{packageName}, user));
    }

    @Override
    public void onPackageChanged(String packageName, UserHandleCompat user) {
        int op = PackageUpdatedTask.OP_UPDATE;
        enqueuePackageUpdated(new PackageUpdatedTask(op, new String[]{packageName}, user));
    }

    @Override
    public void onPackagesAvailable(String[] packageNames, UserHandleCompat user, boolean replacing) {
        if (!replacing) {
            enqueuePackageUpdated(new PackageUpdatedTask(PackageUpdatedTask.OP_ADD, packageNames, user));
        } else {
            // If we are replacing then just update the packages in the list
            enqueuePackageUpdated(new PackageUpdatedTask(PackageUpdatedTask.OP_UPDATE, packageNames, user));
        }
    }

    @Override
    public void onPackagesUnavailable(String[] packageNames, UserHandleCompat user, boolean replacing) {
        if (!replacing) {
            enqueuePackageUpdated(new PackageUpdatedTask(PackageUpdatedTask.OP_UNAVAILABLE, packageNames, user));
        }
    }

    private void enqueuePackageUpdated(PackageUpdatedTask task) {
        HandlerThreadUtil.runOnWorkerThread(task);
    }

    private class PackageUpdatedTask implements Runnable {
        int mOp;
        String[] mPackages;
        UserHandleCompat mUser;

        public static final int OP_NONE = 0;
        static final int OP_ADD = 1;
        static final int OP_UPDATE = 2;
        // uninstlled
        static final int OP_REMOVE = 3;
        // external media unmounted
        static final int OP_UNAVAILABLE = 4;


        PackageUpdatedTask(int op, String[] packages, UserHandleCompat user) {
            mOp = op;
            mPackages = packages;
            mUser = user;
        }

        @Override
        public void run() {
            final Context context = mContext;
            final String[] packages = mPackages;
            switch (mOp) {
                case OP_ADD: {
                    for (String aPackage : packages) {
                        if (DEBUG_LOADERS) {
                            Log.d(TAG, "mAllAppsList.addPackage " + aPackage);
                        }
                        mAllAppsList.addPackage(context, aPackage, mUser);
                    }
                    break;
                }
                case OP_UPDATE:
                    for (String aPackage : packages) {
                        if (DEBUG_LOADERS) {
                            Log.d(TAG, "mAllAppsList.updatePackage " + aPackage);
                        }
                        if (isPackageDisabled(context, aPackage, mUser)) {
                            Log.i(TAG, "OP_UPDATE --removePackage");
                            mAllAppsList.removePackage(aPackage, mUser);
                        } else {
                            Log.i(TAG, "OP_UPDATE --updatePackage");
                            mAllAppsList.updatePackage(context, aPackage, mUser);
                        }
                    }
                    break;
                case OP_REMOVE:
                case OP_UNAVAILABLE:
                    for (String aPackage : packages) {
                        if (DEBUG_LOADERS) {
                            Log.d(TAG, "mAllAppsList.removePackage " + aPackage);
                        }
                        mAllAppsList.removePackage(aPackage, mUser);
                    }
                    break;
                default:
                    break;
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) {
                        mCallback.onAppDataChange(mAllAppsList.data);
                    }
                }
            });
        }
    }

    private boolean isPackageDisabled(Context context, String packageName, UserHandleCompat user) {
        final LauncherAppsCompat launcherApps = LauncherAppsCompat.getInstance(context);
        return !launcherApps.isPackageEnabledForProfile(packageName, user);
    }

    private void loadAndBindAllAppsList(final Context context) {
        Log.d("zhaoy", "loadAndBindAllAppsList time-end = " + System.currentTimeMillis());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<UserHandleCompat> profiles = mUserManager.getUserProfiles();
                Log.d(TAG, "loadAndBindAllAppsList mUserManager.getUserProfiles()=" + profiles.size());
                // Clear the list of apps
                mAllAppsList.clear();
                for (UserHandleCompat user : profiles) {
                    // Query for the set of apps
                    final long qiaTime = DEBUG_LOADERS ? SystemClock.uptimeMillis() : 0;
                    final List<LauncherActivityInfoCompat> apps = mLauncherApps.getActivityList(null, user);
                    if (DEBUG_LOADERS) {
                        Log.d(TAG, "getActivityList took " + (SystemClock.uptimeMillis() - qiaTime) + "ms for user " + user);
                        Log.d(TAG, "getActivityList got " + apps.size() + " apps for user " + user);
                    }
                    // Fail if we don't have any apps
                    if (apps == null || apps.isEmpty()) {
                        Log.e(TAG, "load shortcut Failed if we don't have any apps");
                        continue;
                    }
                    // Create the ApplicationInfos
                    for (int i = 0; i < apps.size(); i++) {
                        LauncherActivityInfoCompat app = apps.get(i);
                        AppInfo appInfo = new AppInfo(context, app, user);
                        if (AppsConfig.FILTER_APP_LIST) {
                            if (AppsConfig.favoritesAppList.contains(app.getComponentName().getPackageName()) ||
                                    AppsConfig.favoritesAppList.contains(app.getComponentName().getPackageName() + "/" + app.getComponentName().getShortClassName())) {
                                appInfo.id = AppsConfig.favoritesAppList.indexOf(app.getComponentName().getPackageName());
                                if (appInfo.id == NO_ID) {
                                    appInfo.id = AppsConfig.favoritesAppList.indexOf(app.getComponentName().getPackageName() + "/" + app.getComponentName().getShortClassName());
                                }
                                Log.i(TAG, "loadAndBindAllAppsList: appLabel --- " + app.getLabel() + ", packageName: " + app.getComponentName().getPackageName() +
                                        ", className: " + app.getComponentName().getShortClassName());
                                // This builds the icon bitmaps.
                                if (!mAllAppsList.mListAppIds.contains(appInfo.id)) {
                                    mAllAppsList.mListAppIds.add(appInfo.id);
                                    mAllAppsList.add(appInfo);
                                }
                                Log.i(TAG, "loadAndBindAllAppsList: appInfo.id== " + appInfo.id);
                            }
                        } else {
                            Log.i(TAG, "loadAndBindAllAppsList: appLabel --- " + app.getLabel() + ", packageName: " + app.getComponentName().getPackageName());
                            // This builds the icon bitmaps.
                            mAllAppsList.add(appInfo);
                        }
                    }
                }
                Log.i(TAG, "loadAndBindAllAppsList: mAllAppsList.size() == " + mAllAppsList.size());
                verifyApplications();
            }
        };
        HandlerThreadUtil.runOnWorkerThread(runnable);
    }

    private void verifyApplications() {
        HandlerThreadUtil.runOnMainThread(mHandler, new Runnable() {
            @Override
            public void run() {
                setAppListAdapter();
            }
        });
    }

    private void setAppListAdapter() {
        if (mCallback != null) {
            mCallback.setAppListAdapter(mAllAppsList.data);
        }
    }

    private class LocaleBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_LOCALE_CHANGED.equals(intent.getAction())) {
                // If we have changed locale we need to clear out the labels in all apps/workspace.
                // ywq: 2018/10/12 change icon text for change system language
                HandlerThreadUtil.runOnWorkerThread(getRunnable());
            }
        }
    }


    private Runnable getRunnable() {
        if (updateR == null) {
            updateR = new Runnable() {
                @Override
                public void run() {
                    updateShortcutLabel();
                }
            };
        }
        return updateR;
    }

    private void updateShortcutLabel() {
        Log.e(TAG, "updateShortcutLabel:  ------->");
        // If any package icon has changed (app was updated while launcher was dead),
        // update the corresponding shortcuts.
        synchronized (mAllAppsList.data) {
            Iterator<AppInfo> mIterator = mAllAppsList.data.iterator();
            Log.i(TAG, "updateShortcutLabel: synchronized");
            while (mIterator.hasNext()) {
                AppInfo info = mIterator.next();
                CharSequence label = null;
                ComponentName cn = info.componentName;
                final List<LauncherActivityInfoCompat> newAppInfo = mLauncherApps.getActivityList(cn.getPackageName(), info.user);
                Log.i(TAG, "updateShortcutLabel: newAppInfo.size= " + newAppInfo.size());
                if (newAppInfo != null && !newAppInfo.isEmpty()) {
                    synchronized (newAppInfo) {
                        Iterator<LauncherActivityInfoCompat> mNewIterator = newAppInfo.iterator();
                        while (mNewIterator.hasNext()) {
                            LauncherActivityInfoCompat newInfo = mNewIterator.next();
                            Log.i(TAG, "updateShortcutLabel: newInfo = " + newInfo.getComponentName().getPackageName());
                            if ("com.wtcl.filemanager".equals(newInfo.getComponentName().getPackageName())) {
                                if (info.componentName.getShortClassName().equals(newInfo.getComponentName().getShortClassName())) {
                                    label = newInfo.getLabel();
                                }
                            } else {
                                label = newInfo.getLabel();
                            }
                        }
                        if (label != null) {
                            info.title = Utilities.trim(label);
                        }
                    }
                    Log.e(TAG, "updateShortcutLabel: " + label);
                }
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null) {
                        mCallback.localeChanged();
                    }
                }
            });
        }
    }

    public void unInit() {
        mContext.unregisterReceiver(mLocaleReceiver);
    }
}
