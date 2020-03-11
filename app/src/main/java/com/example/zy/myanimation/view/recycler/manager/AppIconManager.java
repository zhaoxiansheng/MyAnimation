package com.example.zy.myanimation.view.recycler.manager;

import android.util.Log;

import com.example.zy.myanimation.view.recycler.model.config.AppsConfig;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class AppIconManager {

    private static final String TAG = "AppIconManager";

    public static String getAppIconResourcesConfigId(String packageName, String className) {
        Log.i(TAG, "getAppIconResourcesConfigId: --------> " + "\n packageName = " + packageName);
        String appIconResId = "";
        ArrayList<String> appsIconResIdArray = AppsConfig.listIcon;
        if (AppsConfig.favoritesAppList.contains(packageName) || AppsConfig.favoritesAppList.contains(packageName + "/" + className)) {
            Log.i(TAG, "getIconResourcesId: --------> ");
            for (int i = 0; i < AppsConfig.favoritesAppList.size(); i++) {
                String appPackageName = AppsConfig.favoritesAppList.get(i);
                if (appPackageName.equals(packageName) || appPackageName.equals(packageName + "/" + className)) {
                    appIconResId = appsIconResIdArray.get(i);
                    return appIconResId;
                }
            }
        }
        return appIconResId;
    }

    public static String getAppIconResourcesId(String packageName, String className) {
        Log.i(TAG, "getIconResourcesId: --------> " + "\n packageName = " + packageName);
        String appIconResId = "";
        LinkedHashMap<String, String> appsIconResIdMap = AppsConfig.getIcons();
        if (AppsConfig.favoritesAppList.contains(packageName) || AppsConfig.favoritesAppList.contains(packageName + "/" + className)) {
            Log.i(TAG, "getIconResourcesId: --------> ");
            for (int i = 0; i < AppsConfig.favoritesAppList.size(); i++) {
                String appPackageName = AppsConfig.favoritesAppList.get(i);
                if (appPackageName.equals(packageName) || appPackageName.equals(packageName + "/" + className)) {
                    appIconResId = appsIconResIdMap.get(appPackageName);
                    return appIconResId;
                }
            }
        }
        return appIconResId;
    }

    public static String getAppListBGResourceId(boolean isKidsMode) {
        return AppsConfig.getBackground(isKidsMode);
    }

    public static String[] getMarkerResourcedIds() {
        return AppsConfig.getMarkerType();
    }

}
