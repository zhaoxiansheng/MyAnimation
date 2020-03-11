package com.example.zy.myanimation.view.recycler.channel;

import java.util.LinkedHashMap;

public class ChannelConfig {

    /********************************************************/
    public static final int MARKER_TOP_MARGIN = 0;
    public static final int MARKER_LEFT_MARGIN = 70;
    public static final int MARKER_HEIGHT = 56;
    public static final int MARKER_LONG_ITEM_LENGTH = 100;
    public static final int MARKER_SHORT_ITEM_LENGTH = 48;
    public static final int MARKER_ITEM_PADDING = 48;
    public static final int MARKER_STROKE_WIDTH = 4;
    public static final int APP_ICON_SIZE = 136;
    public static final int APP_ICON_SIZE_SMALL = 86;
    public static final int APP_TEXT_SIZE = 32;
    public static final int APP_DRAWABLE_PADDING = 16;
    public static final int APP_TEXTVIEW_HEIGHT = 48;

    public static final int ITEM_MARGIN_LEFT = 56;
    public static final int ITEM_MARGIN_RIGHT = 32;
    public static final int ITEM_MARGIN_BOTTOM = 24;
    public static final int ITEM_MARGIN_TOP = 24;
    public static final int VERTICAL_MARGIN_LEFT = 74;
    public static final int VERTICAL_MARGIN_TOP = 0;
    public static final int MARGIN_LEFT = 35;
    public static final int MARGIN_RIGHT = 51;
    //    public static final int MARGIN_LEFT = 164 - ITEM_MARGIN_LEFT;
//    public static final int MARGIN_RIGHT = 84 - ITEM_MARGIN_RIGHT;
    public static final int MARGIN_TOP = 98;
    public static final int MARGIN_BOTTOM = 94;
    public static final int ITEM_WIDTH = 264;
    public static final int ITEM_HEIGHT = 264;

    public static final int BUBBLE_TEXTVIEW_PADDING_TOP = 32;
    public static final int MARGIN_FOR_DEFFERENT_SIZE = 25;

    public static final int RETURN_IMG_MARGIN_LEFT = 36;
    public static final int RETURN_IMG_MARGIN_TOP = 12;

    public static final int PROGRESS_MARGIN_TOP = 50;
    public static final int PROGRESS_WIDTH = 136;
    public static final int PROGRESS_HEIGHT = 8;

    /********************************************************/

    //过滤白名单
    public static final LinkedHashMap<String, String> mConfigAppMap = new LinkedHashMap() {
        {
        }
    };
    /********************************************************/

    public static String[] mDefaultMarker = {
            "#FF0088FF",
            "#4DFFFFFF"
    };
}
