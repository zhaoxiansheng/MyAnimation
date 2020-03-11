package com.example.zy.myanimation.view.recycler.model.config;

import android.content.res.Resources;
import android.util.Log;
import android.util.SparseArray;

import com.example.zy.myanimation.view.recycler.channel.ChannelConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class AppsConfig {

    // 行数
    public static final int APPS_LIST_ROWS = 2;
    // 列数
    public static final int APPS_LIST_COLUMNS = 5;

    //切换页数的最小距离比例
    public static final float SLIDE_RATIO_OF_SCREEN = 0.2f;

    public static final String TAG = "AppsConfig";

    private static final String XML = ".xml";

    public static final String KEY_BADGE = "badge";
    public static final String KEY_UPDATE_PROGRESS = "update_progress";

    public static final String RESOURCE_CONFIG_PACKAGE_NAME = "com.android.zhaoyconfig";
    public static final String SHARED_PREFERENCES_KEY = "com.android.app.zhaoy.prefs";


    // 过滤applist开关
    public static final boolean FILTER_APP_LIST = false;

    // 是否读取外部资源
    public static final boolean READ_OUTSIDE_RESOURCE;

    static {
        READ_OUTSIDE_RESOURCE = false;
    }

    // 滑动方向 true上下翻页;false 左右翻页
    public static boolean SCROLL_VERTICALLY;

    static {
        SCROLL_VERTICALLY = false;
    }


    public static ArrayList<String> listIcon = new ArrayList<>();
    public static ArrayList<String> listPackage = new ArrayList<>();
    public static Resources mResourceConfig;

    public static void setChangeUI(Resources resources) {
        Log.i(TAG, "loadFromConfig: listPackage.size()= " + listPackage.size());
        if (listPackage.size() > 0 && listPackage.size() == listIcon.size()) {
            mResourceConfig = resources;
            favoritesAppList.clear();
            favoritesAppList.addAll(listPackage);
        } else {
            mResourceConfig = null;
        }
    }

    /********************************************************/

    public static final ArrayList<String> favoritesAppList = new ArrayList<>(ChannelConfig.mConfigAppMap.keySet());

    /********************************icon*************************************/
    private static int mCurrentTheme = 0;
    private static String mDefaultBackground = "bg";
    private static String mKidsModeBackground = "kids_mode_bg";
    public static String mConfigBackground = "bg";
    private static SparseArray<String[]> mMarkers = new SparseArray<>();
    private static SparseArray<String> mBackgrounds = new SparseArray<>();
    private static SparseArray<LinkedHashMap<String, String>> sIcons = new SparseArray<LinkedHashMap<String, String>>();

    static {
        mMarkers.put(0, ChannelConfig.mDefaultMarker);
        mBackgrounds.put(0, mDefaultBackground);
        sIcons.put(0, ChannelConfig.mConfigAppMap);
    }

    public static LinkedHashMap<String, String> getIcons() {
        LinkedHashMap<String, String> appsIconResIdMap = sIcons.get(mCurrentTheme);
        if (appsIconResIdMap == null) {
            appsIconResIdMap = ChannelConfig.mConfigAppMap;
        }
        return appsIconResIdMap;
    }

    public static String getBackground(boolean isKidsMode) {
        String resourceIds;
        if (isKidsMode) {
            resourceIds = mKidsModeBackground;
        } else {
            resourceIds = mBackgrounds.get(mCurrentTheme);
        }
        if (resourceIds != null) {
            return resourceIds;
        }
        return mDefaultBackground;
    }

    public static void setBackground(String background) {
        mConfigBackground = background;
    }

    public static String[] getMarkerType() {
        String[] resourceIds = mMarkers.get(mCurrentTheme);
        if (resourceIds != null) {
            return resourceIds;
        }
        return ChannelConfig.mDefaultMarker;
    }

    /********************************icon*************************************/

    private static boolean showTest() {
        Log.i("zhaoy", "status=" + readProp("sys.thirdapk.caninstall"));
        return "1".equals(readProp("sys.thirdapk.caninstall"));
    }

    private static String readProp(String prop) {
        try {
            Process process = Runtime.getRuntime().exec("getprop " + prop);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(ir);
            return input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}