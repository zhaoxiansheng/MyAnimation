/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.zy.myanimation.view.recycler.model.infos;

import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.example.zy.myanimation.view.recycler.model.compat.LauncherActivityInfoCompat;
import com.example.zy.myanimation.view.recycler.model.compat.LauncherAppsCompat;
import com.example.zy.myanimation.view.recycler.model.compat.UserHandleCompat;
import com.example.zy.myanimation.view.recycler.model.config.AppsConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.zy.myanimation.view.recycler.model.infos.AppInfo.NO_ID;


/**
 * Stores the list of all applications for the all apps view.
 */
public class AllAppsList {

    private static final String TAG = "AllAppsList";

    private static final int DEFAULT_APPLICATIONS_NUMBER = 42;

    /**
     * The list of all apps.
     */
//    public ArrayList<AppInfo> data = new ArrayList<>(DEFAULT_APPLICATIONS_NUMBER);
    public List<AppInfo> data = Collections.synchronizedList(new ArrayList<AppInfo>(DEFAULT_APPLICATIONS_NUMBER));
    /**
     * The list of apps that have been modified since the last notify() call.
     */
    public ArrayList<AppInfo> modified = new ArrayList<>();

    public ArrayList<Long> mListAppIds = new ArrayList<>();

    /**
     * Boring constructor.
     */
    public AllAppsList() {
    }

    /**
     * Add the supplied ApplicationInfo objects to the list, and enqueue it into the
     * list to broadcast when notify() is called.
     * <p>
     * If the app is already in the list, doesn't add it.
     */
    public void add(AppInfo info) {
        //筛选显示应用的情况下已经通过AppsConfig.favoritesAppList ID的形式去重，在不筛选应用的情况下才需要for循环去重
        if (!AppsConfig.FILTER_APP_LIST) {
            if (findActivity(data, info.componentName, info.user)) {
                return;
            }
        }
        data.add(info);
    }

    public void clear() {
        data.clear();
        modified.clear();
        mListAppIds.clear();
    }

    public int size() {
        return data.size();
    }

    public AppInfo get(int index) {
        return data.get(index);
    }

    /**
     * Add the icons for the supplied apk called packageName.
     */
    public void addPackage(Context context, String packageName, UserHandleCompat user) {
        Log.e(TAG, "IconCache -addPackage: packageName " + packageName);
        final LauncherAppsCompat launcherApps = LauncherAppsCompat.getInstance(context);
        final List<LauncherActivityInfoCompat> matches = launcherApps.getActivityList(packageName, user);
        Log.e(TAG, "addPackage: matches.size()== " + matches.size());
        for (LauncherActivityInfoCompat info : matches) {
            if (AppsConfig.FILTER_APP_LIST) {
                for (int i = 0; i < AppsConfig.favoritesAppList.size(); i++) {
                    if (AppsConfig.favoritesAppList.get(i).contains(info.getComponentName().getPackageName()) ||
                            AppsConfig.favoritesAppList.contains(info.getComponentName().getPackageName() + "/" + info.getComponentName().getShortClassName())) {
                        AppInfo appInfo = new AppInfo(context, info, user);
                        appInfo.id = AppsConfig.favoritesAppList.indexOf(info.getComponentName().getPackageName());
                        if (appInfo.id == NO_ID) {
                            appInfo.id = AppsConfig.favoritesAppList.indexOf(info.getComponentName().getPackageName() + "/" + info.getComponentName().getShortClassName());
                        }

                        if (appInfo.id != -1 && !mListAppIds.contains(appInfo.id)) {
                            mListAppIds.add(appInfo.id);
                            add(appInfo);
                        }
                        Log.e(TAG, "addPackage: appInfo.id== " + appInfo.id + "-- addPackage: appInfo.id== " + appInfo.title);
                    }
                }
            } else {
                add(new AppInfo(context, info, user));
            }
        }
    }

    /**
     * Remove the apps for the given apk identified by packageName.
     */
    public void removePackage(String packageName, UserHandleCompat user) {
        Log.e(TAG, "removePackage: packageName = " + packageName);
        for (int i = data.size() - 1; i >= 0; i--) {
            AppInfo info = data.get(i);
            final ComponentName component = info.intent.getComponent();
            if (component != null && info.user.equals(user) && packageName.equals(component.getPackageName())) {
                data.remove(i);
                int id = AppsConfig.favoritesAppList.indexOf(packageName);
                if (id == NO_ID) {
                    id = AppsConfig.favoritesAppList.indexOf(packageName + "/" + component.getShortClassName());
                }
                int idIndex = mListAppIds.indexOf((long) id);
                if (idIndex != -1) {
                    mListAppIds.remove(idIndex);
                }
            }
        }
    }

    /**
     * Add and remove icons for this package which has been updated.
     */
    public void updatePackage(Context context, String packageName, UserHandleCompat user) {
        Log.e(TAG, "updatePackage: packageName == " + packageName);
        final LauncherAppsCompat launcherApps = LauncherAppsCompat.getInstance(context);
        final List<LauncherActivityInfoCompat> matches = launcherApps.getActivityList(packageName, user);
        Log.e(TAG, "updatePackage: matches.size() == " + matches.size());
        if (matches.size() > 0) {
            // Find disabled/removed activities and remove them from data and add them
            // to the removed list.
            for (int i = data.size() - 1; i >= 0; i--) {
                final AppInfo applicationInfo = data.get(i);
                final ComponentName component = applicationInfo.intent.getComponent();
                if (component != null && user.equals(applicationInfo.user) && packageName.equals(component.getPackageName())) {
                    if (!findActivity(matches, component)) {
                        data.remove(i);
                        int id = AppsConfig.favoritesAppList.indexOf(packageName);
                        if (id == NO_ID) {
                            id = AppsConfig.favoritesAppList.indexOf(packageName + "/" + component.getShortClassName());
                        }
                        int idIndex = mListAppIds.indexOf((long) id);
                        if (idIndex != -1) {
                            mListAppIds.remove(idIndex);
                        }
                    }
                }
            }

            // Find enabled activities and add them to the adapter
            // Also updates existing activities with new labels/icons
            for (final LauncherActivityInfoCompat info : matches) {
                AppInfo applicationInfo = findApplicationInfoLocked(info.getComponentName().getPackageName(), user, info.getComponentName().getClassName());
                if (applicationInfo == null) {
                    if (AppsConfig.FILTER_APP_LIST) {
                        for (int i = 0; i < AppsConfig.favoritesAppList.size(); i++) {
                            if (AppsConfig.favoritesAppList.get(i).contains(info.getComponentName().getPackageName()) ||
                                    AppsConfig.favoritesAppList.contains(info.getComponentName().getPackageName() + "/" + info.getComponentName().getShortClassName())) {
                                AppInfo appInfo = new AppInfo(context, info, user);
                                appInfo.id = AppsConfig.favoritesAppList.indexOf(info.getComponentName().getPackageName());
                                if (appInfo.id == NO_ID) {
                                    appInfo.id = AppsConfig.favoritesAppList.indexOf(info.getComponentName().getPackageName() + "/" + info.getComponentName().getShortClassName());
                                }
                                if (!mListAppIds.contains(appInfo.id)) {
                                    mListAppIds.add(appInfo.id);
                                    add(appInfo);
                                }
                                Log.e(TAG, "updatePackage: appInfo.id == " + appInfo.id);
                            }
                        }
                    } else {
                        add(new AppInfo(context, info, user));
                    }

                } else {
                    modified.add(applicationInfo);
                }
            }
        } else {
            // Remove all data for this package.
            for (int i = data.size() - 1; i >= 0; i--) {
                final AppInfo applicationInfo = data.get(i);
                final ComponentName component = applicationInfo.intent.getComponent();
                if (component != null && user.equals(applicationInfo.user) && packageName.equals(component.getPackageName())) {
                    data.remove(i);
                    int id = AppsConfig.favoritesAppList.indexOf(packageName);
                    if (id == NO_ID) {
                        id = AppsConfig.favoritesAppList.indexOf(packageName + "/" + component.getShortClassName());
                    }
                    int idIndex = mListAppIds.indexOf((long) id);
                    if (idIndex != -1) {
                        mListAppIds.remove(idIndex);
                    }
                }
            }
        }
        modifyDataList();
    }

    private void modifyDataList() {
        for (AppInfo app : modified) {
            int index = data.indexOf(app);
            if (index != -1) {
                data.remove(index);
                data.add(index, app);
            }
        }
        modified.clear();
    }


    /**
     * Returns whether <em>apps</em> contains <em>component</em>.
     */
    private static boolean findActivity(List<LauncherActivityInfoCompat> apps, ComponentName component) {
        for (LauncherActivityInfoCompat info : apps) {
            if (info.getComponentName().equals(component)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Query the launcher apps service for whether the supplied package has
     * MAIN/LAUNCHER activities in the supplied package.
     */
    static boolean packageHasActivities(Context context, String packageName, UserHandleCompat user) {
        final LauncherAppsCompat launcherApps = LauncherAppsCompat.getInstance(context);
        return launcherApps.getActivityList(packageName, user).size() > 0;
    }

    /**
     * Returns whether <em>apps</em> contains <em>component</em>.
     */
    private static boolean findActivity(List<AppInfo> apps, ComponentName component, UserHandleCompat user) {
        for (int i = 0; i < apps.size(); i++) {
            final AppInfo info = apps.get(i);
            if (info.user.equals(user) && info.componentName.equals(component)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find an ApplicationInfo object for the given packageName and className.
     */
    private AppInfo findApplicationInfoLocked(String packageName, UserHandleCompat user, String className) {
        for (AppInfo info : data) {
            final ComponentName component = info.intent.getComponent();
            if (component != null && user.equals(info.user) && packageName.equals(component.getPackageName()) && className.equals(component.getClassName())) {
                return info;
            }
        }
        return null;
    }
}
