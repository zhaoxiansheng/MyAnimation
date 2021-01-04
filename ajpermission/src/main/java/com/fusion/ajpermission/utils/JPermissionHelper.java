package com.fusion.ajpermission.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/18
 * @description
 */

public class JPermissionHelper {

    public static final int DEFAULT_REQUEST_CODE = 0xABC1994;
    public static final String TAG = "JPermission";

    // 上下文
    private static volatile Context JPERMISSION_CONTEXT = null;

    private static SimpleArrayMap<String, Integer> MIN_SDK_PERMISSIONS;

    static {
        MIN_SDK_PERMISSIONS = new SimpleArrayMap<>(8);
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW", 23);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS", 23);
    }

    private static volatile int targetSdkVersion = -1;

    /**
     * 注入一个上下文，用于无法获取上下文时使用
     * 切记注入点在{@link android.app.Application}，否则会导致内存泄漏
     *
     * @param context 上下文
     */
    public static void injectContext(Context context) {
        JPERMISSION_CONTEXT = context;
    }

    public static Context getContext() {
        if(JPERMISSION_CONTEXT == null){
            synchronized (JPermissionHelper.class) {
                if(JPERMISSION_CONTEXT == null) {
                    JPERMISSION_CONTEXT = getApplicationByReflect();
                }
            }
        }
        return JPERMISSION_CONTEXT;
    }

    @SuppressLint("PrivateApi")
    private static Application getApplicationByReflect() {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }

    public static final class PermissionResult{
        public final ArrayList<String> mDeniedList = new ArrayList<>();
        public final ArrayList<String> mDeniedForverList = new ArrayList<>();
    }

    public static PermissionResult getResult(Activity activity, String[] permissions, int[] grantResults){
        PermissionResult result = new PermissionResult();
        for (int i = 0; i < grantResults.length; ++i) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])){
                result.mDeniedList.add(permissions[i]);
            }else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                result.mDeniedForverList.add(permissions[i]);
            }else{
                //granted
            }
        }
        return result;
    }

    // 判断是否是第一次启动程序 利用 SharedPreferences 将数据保存在本地
    private synchronized static boolean ajpermissionisFristRun(Context context) {
        //实例化SharedPreferences对象（第一步）
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "share", MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!isFirstRun) {
            return false;
        } else {
            //保存数据 （第三步）
            editor.putBoolean("isFirstRun", false);
            //提交当前数据 （第四步）
            editor.apply();
            return true;
        }
    }


    public static PermissionResult needRequest(Context context, String[] permissions){
        PermissionResult result = new PermissionResult();
        if(context instanceof Activity){
            boolean isFirst = isFristRun(context);
            for (String permission : permissions) {
                if (isFirst || ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, permission)) {
                    result.mDeniedList.add(permission);
                }else if(!hasSelfPermission(context, permission)){
                    result.mDeniedForverList.add(permission);
                }
            }
        }
        return result;
    }

    /**
     * @date 创建时间 2018/4/18
     * @author Jiang zinc
     * @Description 检查所有的权限是否被授权，被授予会返回0（即{@link PackageManager#PERMISSION_GRANTED}）
     * @version
     */
    public static boolean verifyPermissions(int... grantResults) {
        if (grantResults.length == 0) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * @date 创建时间 2018/4/18
     * @author Jiang zinc
     * @Description 检测 一些权限 是否都授权。 都授权则返回true，如果还未授权则返回false
     * @version
     */
    public static boolean hasSelfPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            //如果权限还未授权
            if (permissionExists(permission) && !hasSelfPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @date 创建时间 2018/4/18
     * @author Jiang zinc
     * @Description 如果在这个SDK版本存在的权限，则返回true
     * @version
     */
    private static boolean permissionExists(String permission) {
        Integer minVersion = MIN_SDK_PERMISSIONS.get(permission);
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion;
    }

    /**
     * @date 创建时间 2018/4/18
     * @author Jiang zinc
     * @Description 检测某个权限是否已经授权；如果已授权则返回true，如果未授权则返回false
     * @version
     */
    private static boolean hasSelfPermission(Context context, String permission) {
        try {
            // ContextCompat.checkSelfPermission，主要用于检测某个权限是否已经被授予。
            // 方法返回值为PackageManager.PERMISSION_DENIED或者PackageManager.PERMISSION_GRANTED
            // 当返回DENIED就需要进行申请授权了。
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException t) {
            return false;
        }
    }

    /**
     * @date 创建时间 2018/4/18
     * @author Jiang zinc
     * @Description 检查需要给予的权限被临时被拒绝true，已授权、永久禁用、还没询问过返回false
     * @version
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            // 这个API主要用于给用户一个申请权限的解释，该方法只有在用户在上一次已经拒绝过你的这个权限申请。
            // 也就是说，用户已经拒绝一次了，你又弹个授权框，你需要给用户一个解释，为什么要授权，则使用该方法。
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @date 创建时间 2018/4/18
     * @author Jiang zinc
     * @Description 获取sdk 版本
     * @version
     */
    public static int getTargetSdkVersion(Context context) {
        try {
            if (targetSdkVersion != -1) {
                return targetSdkVersion;
            }
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            targetSdkVersion = packageInfo.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return targetSdkVersion;
    }

    public static String[] getManifestPermission(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = packageInfo.requestedPermissions;
            Log.e(TAG, Arrays.asList(requestedPermissions).toString());
            return requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
