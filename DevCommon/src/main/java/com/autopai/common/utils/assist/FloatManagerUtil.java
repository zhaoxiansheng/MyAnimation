package com.autopai.common.utils.assist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class FloatManagerUtil {
    private static final int OS_VERSION = Build.VERSION.SDK_INT;

    private WindowManager mWindowManager;
    private Context mContext;
    private volatile static FloatManagerUtil sInstance;
    private volatile static FloatManagerUtil sSrvInstance;

    private FloatManagerUtil(Context context) {
        mContext = context;
        mWindowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    public static FloatManagerUtil getInstance(Context context) {
        if (sInstance == null) {
            synchronized (FloatManagerUtil.class) {
                sInstance = new FloatManagerUtil(context);
            }
        }
        return sInstance;
    }

    public static FloatManagerUtil getSrvInstance(Context context) {
        if (sSrvInstance == null) {
            synchronized (FloatManagerUtil.class) {
                sSrvInstance = new FloatManagerUtil(context);
            }
        }
        return sSrvInstance;
    }
    /**
     * 添加悬浮窗的View
     * @param view
     * @param params
     * @return
     */
    public boolean addView(View view, WindowManager.LayoutParams params) {
        try {
            mWindowManager.addView(view, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 移除悬浮窗View
     * @param view
     * @return
     */
    public boolean removeView(View view) {
        try {
            mWindowManager.removeView(view);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新悬浮窗参数
     * @param view
     * @param params
     * @return
     */
    public boolean updateView(View view, WindowManager.LayoutParams params) {
        try {
            mWindowManager.updateViewLayout(view, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void requestWindowPermission(Context context)
    {
        if (OS_VERSION >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(context)) {
                requestPermission(context);
            }
        }
    }

    /**
     * 请求悬浮窗权限
     *
     * @param context 上下文
     */
    private static void requestPermission(Context context) {
        if (OS_VERSION >= 19 && OS_VERSION < 23) {
            jump2DetailActivity(context);
        } else if (OS_VERSION >= 23) {
            highVersionJump2PermissionActivity(context);
        }
    }

    /**
     * 跳转应用详情界面
     *
     * @param context
     */
    private static void jump2DetailActivity(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }

    /**
     * Android 6.0 版本及之后的跳转权限申请界面
     *
     * @param context 上下文
     */
    private static void highVersionJump2PermissionActivity(Context context) {
        try {
            Class clazz = Settings.class;
            Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
            Intent intent = new Intent(field.get(null).toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("zhaoy", Log.getStackTraceString(e));
        }
    }

}
