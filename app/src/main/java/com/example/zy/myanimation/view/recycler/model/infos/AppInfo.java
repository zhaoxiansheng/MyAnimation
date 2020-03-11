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
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.zy.myanimation.view.recycler.manager.AppIconManager;
import com.example.zy.myanimation.view.recycler.model.compat.LauncherActivityInfoCompat;
import com.example.zy.myanimation.view.recycler.model.compat.UserHandleCompat;
import com.example.zy.myanimation.view.recycler.model.compat.UserManagerCompat;
import com.example.zy.myanimation.view.recycler.model.config.AppsConfig;
import com.example.zy.myanimation.view.recycler.utils.Utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents an app in All Apps View.
 */
public class AppInfo implements Serializable {

    private static final String TAG = "AppInfo";

    /**
     * Intent extra to store the profile. Format: UserHandle
     */
    public static final String EXTRA_PROFILE = "profile";

    public static final int NO_ID = -1;

    /**
     * The id in the settings database for this item
     */
    public long id = NO_ID;


    public int itemType;

    public long container = NO_ID;

    /**
     * Iindicates the screen in which the shortcut appears.
     */
    public long screenId = -1;

    /**
     * Indicates the position in an ordered list.
     */
    public int rank = 0;

    /**
     * Indicates that this item needs to be updated in the db
     */
    public boolean requiresDbUpdate = false;

    /**
     * Title of the item
     */
    public CharSequence title;

    /**
     * Content description of the item.
     */
    public CharSequence contentDescription;

    /**
     * The intent used to start the application.
     */
    public Intent intent;

    /**
     * A bitmap version of the application icon.
     */
    public Drawable iconDrawable;

    /**
     * Indicates whether we're using a low res icon
     */
    boolean usingLowResIcon;

    /**
     * The time at which the app was first installed.
     */
    private long firstInstallTime;

    public ComponentName componentName;

    public String packageName;

    private static final int DOWNLOADED_FLAG = 1;
    private static final int UPDATED_SYSTEM_APP_FLAG = 2;
    private LauncherActivityInfoCompat mInfo;
    private int flags = 0;
    private static final Pattern sTrimPattern =
            Pattern.compile("^[\\s|\\p{javaSpaceChar}]*(.*)[\\s|\\p{javaSpaceChar}]*$");

    public UserHandleCompat user;

    public boolean isLocalIcon = false;

    AppInfo() {
        user = UserHandleCompat.myUserHandle();
//        itemType = LauncherSettings.BaseLauncherColumns.ITEM_TYPE_SHORTCUT;
    }

    public Intent getIntent() {
        return intent;
    }

    protected Intent getRestoredIntent() {
        return null;
    }

    /**
     * Must not hold the Context.
     */
    public AppInfo(Context context, LauncherActivityInfoCompat info, UserHandleCompat user) {
        Log.i(TAG, "IconCache - AppInfo: --------------"
                + "\n ActivityInfo.getLabel == " + info.getLabel()
                + "\n ActivityInfo.getClassName == " + info.getComponentName().getClassName()
        );
        mInfo = info;
        this.componentName = info.getComponentName();
        this.packageName = componentName.getPackageName();
        this.container = NO_ID;
        this.user = user;
        this.title = info.getLabel();
//        this.iconBitmap = Utilities.createIconBitmap(info.getBadgedIcon(DisplayMetrics.DENSITY_HIGH), context);
        updateIcon(context);
        //
        flags = initFlags(info);
        firstInstallTime = info.getFirstInstallTime();
        intent = makeLaunchIntent(context, info, user);
    }

    private static int initFlags(LauncherActivityInfoCompat info) {
        int appFlags = info.getApplicationInfo().flags;
        int flags = 0;
        if ((appFlags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) == 0) {
            flags |= DOWNLOADED_FLAG;

            if ((appFlags & android.content.pm.ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                flags |= UPDATED_SYSTEM_APP_FLAG;
            }
        }
        return flags;
    }

    public AppInfo(Context context, long id, String label, String packageName) {
        this.id = id;
        this.title = trim(label);
        this.packageName = packageName;
        String name = AppIconManager.getAppIconResourcesId(packageName, null);
        int resourcesID = Utilities.getResourcesID(context, name, "drawable");
        if (resourcesID > 0) {
            this.iconDrawable = context.getResources().getDrawable(resourcesID);
        } else {
            this.iconDrawable = mInfo.getBadgedIcon(DisplayMetrics.DENSITY_HIGH);
        }

    }

    public AppInfo(AppInfo info) {
        id = info.id;
        rank = info.rank;
        screenId = info.screenId;
        itemType = info.itemType;
        container = info.container;
//        user = info.user;
        contentDescription = info.contentDescription;
        //
        componentName = info.componentName;
        title = trim(info.title);
        intent = new Intent(info.intent);
        flags = info.flags;
        firstInstallTime = info.firstInstallTime;
        iconDrawable = info.iconDrawable;
    }

    /**
     * Trims the string, removing all whitespace at the beginning and end of the string.
     * Non-breaking whitespaces are also removed.
     */
    public static String trim(CharSequence s) {
        if (s == null) {
            return null;
        }

        // Just strip any sequence of whitespace or java space characters from the beginning and end
        Matcher m = sTrimPattern.matcher(s);
        return m.replaceAll("$1");
    }

    @Override
    public String toString() {
        return "ApplicationInfo(title=" + title + " id=" + this.id + " intent=" + this.intent
                + " type=" + this.itemType + " container=" + this.container
                + " screen=" + this.screenId + " +  flags=" + this.flags
                + " firstInstallTime=" + this.firstInstallTime + ")";
    }

    /**
     * Helper method used for debugging.
     */
    public static void dumpApplicationInfoList(String tag, String label, ArrayList<AppInfo> list) {
        Log.d(tag, label + " size=" + list.size());
        for (AppInfo info : list) {
            Log.d(tag, "   title=\"" + info.title + "\" iconBitmap=" + info.iconDrawable
                    + " firstInstallTime=" + info.firstInstallTime
                    + " componentName=" + info.componentName.getPackageName());
        }
    }

    private static Intent makeLaunchIntent(Context context, LauncherActivityInfoCompat info,
                                           UserHandleCompat user) {
        long serialNumber = UserManagerCompat.getInstance(context).getSerialNumberForUser(user);
        return new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .setComponent(info.getComponentName())
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                .putExtra(EXTRA_PROFILE, serialNumber);
    }

    public void updateIcon(Context context) {
        Resources mResource;
        String name;
        int id;
        if (mInfo != null) {
            if (AppsConfig.READ_OUTSIDE_RESOURCE) {
                //成功读取了配置信息
                if (AppsConfig.mResourceConfig != null) {
                    mResource = AppsConfig.mResourceConfig;
                    name = AppIconManager.getAppIconResourcesConfigId(mInfo.getComponentName().getPackageName(), mInfo.getComponentName().getShortClassName());
                    id = mResource.getIdentifier(name, "drawable", AppsConfig.RESOURCE_CONFIG_PACKAGE_NAME);
                } else {
                    mResource = context.getResources();
                    name = AppIconManager.getAppIconResourcesId(mInfo.getComponentName().getPackageName(), mInfo.getComponentName().getShortClassName());
                    id = Utilities.getResourcesID(context, name, "drawable");
                }
            } else {
                mResource = context.getResources();
                name = AppIconManager.getAppIconResourcesId(mInfo.getComponentName().getPackageName(), mInfo.getComponentName().getShortClassName());
                id = Utilities.getResourcesID(context, name, "drawable");
            }
        } else {
            mResource = context.getResources();
            name = AppIconManager.getAppIconResourcesId(packageName, null);
            id = Utilities.getResourcesID(context, name, "drawable");
        }
        Log.e(TAG, "getAppIconResourcesId: " + id);
        if (id > 0) {
            this.iconDrawable = mResource.getDrawable(id);
            this.isLocalIcon = true;
        } else {
            this.iconDrawable = mInfo.getBadgedIcon(DisplayMetrics.DENSITY_HIGH);
            this.isLocalIcon = false;
        }
    }
}
