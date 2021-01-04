package com.autopai.common.utils.image;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;

import com.autopai.common.R;

public class LauncherBg {

    public static GradientDrawable getLauncherHomeBg(Context context) {
        int start = context.getResources().getColor(R.color.launcher_bg_color_home_start);
        int end = context.getResources().getColor(R.color.launcher_bg_color_home_end);
        int[] color = new int[]{start, end};
        return ImageUtil.getGradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, color);
    }

    public static GradientDrawable getLauncherMediaBg(Context context) {
        int start = context.getResources().getColor(R.color.launcher_bg_color_media_start);
        int end = context.getResources().getColor(R.color.launcher_bg_color_media_end);
        int[] color = new int[]{start, end};
        return ImageUtil.getGradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, color);
    }

    public static GradientDrawable getLauncherNaviBg(Context context) {
        int start = context.getResources().getColor(R.color.launcher_bg_color_navi_start);
        int end = context.getResources().getColor(R.color.launcher_bg_color_navi_end);
        int[] color = new int[]{start, end};
        return ImageUtil.getGradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, color);
    }

    public static GradientDrawable getDockHomeBg(Context context) {
        int start = context.getResources().getColor(R.color.dock_bg_color_home_start);
        int end = context.getResources().getColor(R.color.dock_bg_color_home_end);
        int[] color = new int[]{start, end};
        return ImageUtil.getGradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, color);
    }

    public static GradientDrawable getDockMediaBg(Context context) {
        int start = context.getResources().getColor(R.color.dock_bg_color_media_start);
        int end = context.getResources().getColor(R.color.dock_bg_color_media_end);
        int[] color = new int[]{start, end};
        return ImageUtil.getGradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, color);
    }

    public static GradientDrawable getDockNaviBg(Context context) {
        int start = context.getResources().getColor(R.color.dock_bg_color_navi_start);
        int end = context.getResources().getColor(R.color.dock_bg_color_navi_end);
        int[] color = new int[]{start, end};
        return ImageUtil.getGradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, color);
    }
}
