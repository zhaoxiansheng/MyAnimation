package com.autopai.common.config;

import com.autopai.common.R;

public class Config {

    /**
     * Key tag for view show in launcher
     */
    public static final int LAUNCHER_VIEW_ITEM_TAG = R.id.launcher_view_item_tag;

    /**
     * Screen id
     *
     * 同时控制着场景的显示层级，SCREEN_ID_DEFAULT在最上面，覆盖其他场景
     */
    public static final int SCREEN_ID_TRAVEL = 2;
    public static final int SCREEN_ID_MEDIA = 1;
    public static final int SCREEN_ID_HOME = 0;
    public static final int SCREEN_ID_DEFAULT = SCREEN_ID_HOME;
    public static final int SCREEN_ID_MAX = SCREEN_ID_TRAVEL;

    public static final String TARGET_SERVICE_NAME = "com.alion.mycanvas.service.TargetService";
    public static final String LAUNCHER_PKG = "com.autopai.launcher2";
    public static final String LAUNCHER_ACTIVITY = "com.autopai.launcher2.activity.LauncherActivity";

    /**
     * Widget状态
     */
    public static final int WIDGET_STATE_NORMAL = 0;
    public static final int WIDGET_STATE_ADDED = 1;
    public static final int WIDGET_STATE_READY = 2;

    public static int SCREEN_WIDGTH = 1920;
    public static final int SCREEN_HEIGHT = 720;

}
