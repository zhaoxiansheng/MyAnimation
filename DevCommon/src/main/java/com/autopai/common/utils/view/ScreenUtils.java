package com.autopai.common.utils.view;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.autopai.common.utils.utils.Utils;

public class ScreenUtils {

    private static final String TAG = ScreenUtils.class.getSimpleName();

    /**
     * 获取屏幕宽
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取分辨率
     *
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px (像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dp2px(final float dpValue) {
        final float scale = Utils.getApp().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 px (像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float px2sp(Context context, float pxValue) {
        return (pxValue / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static int[] getScreenSize(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return new int[]{dm.widthPixels, dm.heightPixels};
    }

    public static int getDpixel(Context activity, int value) {
        float density = activity.getResources().getDisplayMetrics().density;
        return (int) (density * value);
    }

    /**
     * 用于获取状态栏的高度. 使用Resource对象获取（推荐这种方式）
     *
     * @return 返回状态栏高度的像素值
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }


    /**
     * 设置状态栏颜色
     *
     * @param activity
     * @param color
     */
    public static void setStatusColor(Activity activity, String color) {
        View statusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtils.getStatusBarHeight(activity));
        statusBarView.setBackgroundColor(Color.parseColor(color));
        ViewGroup contentView = activity.findViewById(android.R.id.content);
        contentView.addView(statusBarView, lp);
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图
     *
     * @param context
     * @return
     */
    public static Bitmap snapShot(Context context, boolean withStatusBar) {
        View view = ((Activity) context).getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Rect frame = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        Bitmap bp = Bitmap.createBitmap(bitmap, 0,
                withStatusBar ? 0 : statusBarHeight, getScreenWidth(context),
                withStatusBar ? getScreenHeight(context) : getScreenHeight(context) - frame.top);
        view.destroyDrawingCache();
        return bp;

    }

    public static int getActionBarSize(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        } else {
            return 0;
        }
    }

    /**
     * Instrumentation a click event, Must call in worker Thread
     *
     * @param x down x
     * @param y down y
     */
    public static void singleClick(float x, float y) {
        Instrumentation inst = new Instrumentation();
        final float clickX = x;
        final float clickY = y;
        long downTime = SystemClock.uptimeMillis();
        inst.sendPointerSync(MotionEvent.obtain(downTime,
                downTime, MotionEvent.ACTION_DOWN, clickX, clickY, 0));
        inst.sendPointerSync(MotionEvent.obtain(downTime,
                downTime + 100, MotionEvent.ACTION_UP, clickX, clickY, 0));
    }

    /**
     * 获取App所占的屏幕宽度
     * @return
     */
    public static int getAppScreenWidth() {
        WindowManager wm = (WindowManager) Utils.getApp().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }

    /**
     * 获取App所占的屏幕高度
     * @return
     */
    public static int getAppScreenHeight() {
        WindowManager wm = (WindowManager) Utils.getApp().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

    public static void runAtFrame(final Runnable runnable, final int frame){
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                Log.e(TAG, "doFrame at: " + frame);
                if(frame <= 0){
                    runnable.run();
                }else{
                    runAtFrame(runnable, frame - 1);
                }
            }
        });
    }

    public static void runAtFrame(final Runnable runnable, final int frame, final String name){
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                Log.e(TAG, "doFrame at: " + frame + ", name: " + name);
                if(frame <= 0){
                    runnable.run();
                }else{
                    runAtFrame(runnable, frame - 1);
                }
            }
        });
    }
}
