package com.example.zy.myanimation.view.recycler.channel;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.zy.myanimation.view.recycler.adapter.AppListAdapter;
import com.example.zy.myanimation.view.recycler.callback.AppsManagerCallback;
import com.example.zy.myanimation.view.recycler.manager.AppIconManager;
import com.example.zy.myanimation.view.recycler.manager.AppsManager;
import com.example.zy.myanimation.view.recycler.manager.HorizontalPageLayoutManager;
import com.example.zy.myanimation.view.recycler.model.compat.LauncherAppsCompat;
import com.example.zy.myanimation.view.recycler.model.compat.UserHandleCompat;
import com.example.zy.myanimation.view.recycler.model.compat.UserManagerCompat;
import com.example.zy.myanimation.view.recycler.model.config.AppsConfig;
import com.example.zy.myanimation.view.recycler.model.infos.AppInfo;
import com.example.zy.myanimation.view.recycler.utils.Utilities;
import com.example.zy.myanimation.view.recycler.view.CustomRecyclerView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class AppsContentView extends FrameLayout implements AppListAdapter.OnItemClickListener {

    private static final String TAG = "AppsContentView";
    private Context mContext;
    private boolean mIsSafeModeEnabled;
    private AppListAdapter mAdapter;
    private AppsManager mAppsManager;

    public AppsContentView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AppsContentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AppsContentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Log.i(TAG, "init = " + System.currentTimeMillis());
        mContext = context;
        initView(context);
        initManager(context);
        mIsSafeModeEnabled = context.getPackageManager().isSafeMode();
    }

    private void initView(Context context) {
        setBackground(context);

        CustomRecyclerView mRecyclerView = new CustomRecyclerView(context);
        mRecyclerView.setPadding(ChannelConfig.MARGIN_LEFT, ChannelConfig.MARGIN_TOP, ChannelConfig.MARGIN_RIGHT, ChannelConfig.MARGIN_BOTTOM);
        mRecyclerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mRecyclerView.setFitsSystemWindows(true);
        mRecyclerView.setOverScrollMode(OVER_SCROLL_NEVER);

        mAdapter = new AppListAdapter(mContext);
        HorizontalPageLayoutManager layoutManager = new HorizontalPageLayoutManager(AppsConfig.APPS_LIST_ROWS, AppsConfig.APPS_LIST_COLUMNS);
        addView(mRecyclerView);
        mRecyclerView.setLayoutManager(layoutManager);
        ChannelItemDecoration mPagingItemDecoration = new ChannelItemDecoration(mContext);
        mRecyclerView.addItemDecoration(mPagingItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void initManager(Context context) {
        Log.i(TAG, "initManager");
        mAppsManager = new AppsManager(context);
        mAppsManager.setCallback(new AppsManagerCallback() {
            @Override
            public void changeTheme() {
                //暂未调用
                Log.i(TAG, "changeTheme");
            }

            @Override
            public void onAppDataChange(List<AppInfo> data) {
                Log.i(TAG, "onAppDataChange-data.size()=" + data.size());
                mAdapter.setData(data);
            }

            @Override
            public void localeChanged() {
                Log.i(TAG, "localeChanged");
                post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void setAppListAdapter(List<AppInfo> data) {
                Log.i(TAG, "setAppListAdapter");
                setAppListAdapterMethod(mAdapter, data);
            }
        });
    }

    public void setAppListAdapterMethod(AppListAdapter adapter, List<AppInfo> data) {
        adapter.setData(data);
    }

    @Override
    public void onItemClick(View view) {
        Log.i(TAG, "onClickAppsIcon: View view = " + view);
        Object tag = view.getTag();
        if (!(tag instanceof AppInfo)) {
            throw new IllegalArgumentException("Input must be a App icon ");
        }
        AppInfo appInfo = (AppInfo) tag;
        ComponentName componentName = appInfo.componentName;
        Intent intent = appInfo.intent;
        Log.i(TAG, "onClickAppsIcon: componentName = " + componentName);
        Log.i(TAG, "onClickAppsIcon: intent = " + intent);

        if (intent != null && componentName != null) {
            boolean flag = startAppsActivity(view, intent, tag);
            if (flag) {
                Log.i(TAG, "startActivity is success ");
            } else {
                Log.i(TAG, "startActivity is fail ");
            }
        } else {
            Log.e(TAG, "onItemClick intent: " + intent + ", componentName" + componentName);
        }
    }

    private boolean startAppsActivity(View view, Intent intent, Object tag) {
        boolean success = false;
        if (mIsSafeModeEnabled && !Utilities.isSystemApp(mContext, intent)) {
            Toast.makeText(mContext, Utilities.getResourcesID(getContext(), "safe_mode_error", "string"), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mOnAppClickListener != null) {
            mOnAppClickListener.onAppClick();
        }
        try {
            success = startActivity(view, intent, tag);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, Utilities.getResourcesID(getContext(), "activity_not_found", "string"), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Unable to launch. tag=" + tag + " intent=" + intent, e);
        }
        return success;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAppsManager.unInit();
    }

    private void setBackground(Context context) {
        int bgId = -1;
        Resources resources;
        if (AppsConfig.mResourceConfig != null) {
            resources = AppsConfig.mResourceConfig;
            bgId = AppsConfig.mResourceConfig.getIdentifier(AppsConfig.mConfigBackground, "drawable", AppsConfig.RESOURCE_CONFIG_PACKAGE_NAME);
            if (bgId <= 0) {
                resources = getResources();
                bgId = Utilities.getResourcesID(context, AppIconManager.getAppListBGResourceId(false), "drawable");
            }
        } else {
            resources = getResources();
            bgId = Utilities.getResourcesID(context, AppIconManager.getAppListBGResourceId(false), "drawable");
        }
        Log.i(TAG, "setBackground: bgId=" + bgId);
        if (bgId > 0) {
            setBackground(resources.getDrawable(bgId));
        }
    }

    private boolean startActivity(View v, Intent intent, Object tag) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //
        LauncherAppsCompat launcherApps = LauncherAppsCompat.getInstance(mContext);
        UserManagerCompat userManager = UserManagerCompat.getInstance(mContext);
        //
        UserHandleCompat user = null;
        if (intent.hasExtra(AppInfo.EXTRA_PROFILE)) {
            long serialNumber = intent.getLongExtra(AppInfo.EXTRA_PROFILE, -1);
            user = userManager.getUserForSerialNumber(serialNumber);
        }

        if (user == null || user.equals(UserHandleCompat.myUserHandle())) {
            // Could be launching some bookkeeping activity
            mContext.startActivity(intent, null);
        } else {
            // TODO Component can be null when shortcuts are supported for secondary user
            launcherApps.startActivityForProfile(intent.getComponent(), user,
                    intent.getSourceBounds(), null);
        }
        return true;
    }

    private OnAppClickListener mOnAppClickListener;

    public void setOnAppClickListener(OnAppClickListener onAppClickListener) {
        mOnAppClickListener = onAppClickListener;
    }

    public interface OnAppClickListener {
        public void onAppClick();
    }
}
