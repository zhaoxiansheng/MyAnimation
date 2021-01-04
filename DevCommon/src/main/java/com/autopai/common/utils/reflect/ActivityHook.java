package com.autopai.common.utils.reflect;

import android.app.Activity;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ActivityHook {
    private static Field mTokenField;
    private static Constructor NonConfigurationInstancesConst;
    private static Field NonConfig_childrenField;
    private static Field mDecorField;
    private static Field mmWindowAddedField;
    private static Field mLastNonConfigurationInstancesField;
    private static Method setParentMethod;
    private static Method getActivityTokenMethod;
    private static Method isTopOfTaskMethod;

    static {
        mTokenField = ReflectUtil.getClassFiled(Activity.class, "mToken");
        mDecorField = ReflectUtil.getClassFiled(Activity.class, "mDecor");
        mmWindowAddedField = ReflectUtil.getClassFiled(Activity.class, "mWindowAdded");
        setParentMethod = ReflectUtil.getMethod(Activity.class, "setParent", new Class<?>[]{Activity.class});
        getActivityTokenMethod = ReflectUtil.getMethod(Activity.class, "getActivityToken", null);
        isTopOfTaskMethod = ReflectUtil.getMethod(Activity.class, "isTopOfTask", null);

        Class<?> nonConfgClz = ReflectUtil.getClazz("android.app.Activity$NonConfigurationInstances");
        NonConfigurationInstancesConst = ReflectUtil.getConstructor(nonConfgClz, null);
        NonConfig_childrenField = ReflectUtil.getClassFiled(nonConfgClz, "children");
        mLastNonConfigurationInstancesField = ReflectUtil.getClassFiled(Activity.class, "mLastNonConfigurationInstances");
    }

    public static Activity setParent(Activity curActivity, Activity parent){
        if(curActivity != null) {
            Activity oldParent = curActivity.getParent();
            if( ReflectUtil.callMethod(setParentMethod, curActivity, new Object[]{parent}, null) ){
                return oldParent;
            }
        }
        return null;
    }

    public static boolean setToken(Activity activity, IBinder token){
        if(activity != null){
            return ReflectUtil.setObjectByFiled(activity, token, mTokenField);
        }
        return false;
    }

    public static IBinder getToken(Activity activity){
        if(activity != null){
            return (IBinder)ReflectUtil.getObjectByFiled(activity, mTokenField);
        }
        return null;
    }

    public static IBinder getActivityToken(Activity activity){
        if(activity != null){
            Object[] ret = new Object[1];
            if( ReflectUtil.callMethod(getActivityTokenMethod, activity, null, ret) ){
                return (IBinder)ret[0];
            }
        }
        return null;
    }

    public static boolean isTopOfTask(Activity activity){
        if(activity != null){
            Object[] ret = new Object[1];
            if( ReflectUtil.callMethod(isTopOfTaskMethod, activity, null, ret) ){
                return (boolean)ret[0];
            }
        }
        return false;
    }

    public static Object getNonConfigurationInstances(){
        Object nonConfig = ReflectUtil.createObject(NonConfigurationInstancesConst, null);
        return nonConfig;
    }

    public static boolean setNonConfigurationChildren(Object nonConfig, HashMap children){
        boolean ret = false;
        if(nonConfig != null){
            ret = ReflectUtil.setObjectByFiled(nonConfig, children, NonConfig_childrenField);
        }
        return ret;
    }

    public static HashMap getNonConfigurationChildren(Object nonConfig){
        HashMap children = (HashMap)ReflectUtil.getObjectByFiled(nonConfig, NonConfig_childrenField);
        return children;
    }

    public static boolean setLastNonConfiguration(Activity activity, Object lastNonConfig){
        boolean ret = false;
        if(activity != null){
            ret = ReflectUtil.setObjectByFiled(activity, lastNonConfig, mLastNonConfigurationInstancesField);
        }
        return ret;
    }

    public static Object getLastNonConfiguration(Activity activity){
        Object lastNonConfig = ReflectUtil.getObjectByFiled(activity, mLastNonConfigurationInstancesField);
        return lastNonConfig;
    }

    public static boolean setDecorView(Activity activity, View decor) {
        boolean ret = false;
        if(activity != null){
            ret = ReflectUtil.setObjectByFiled(activity, decor, mDecorField);
        }
        return ret;
    }

    public static View getDecorView(Activity activity) {
        View decor = null;
        if(activity != null){
            decor = (View)ReflectUtil.getObjectByFiled(activity, mDecorField);
        }
        return decor;
    }

    public static boolean setWindowAdded(Activity activity, boolean added) {
        boolean ret = false;
        if(activity != null){
            ret = ReflectUtil.setObjectByFiled(activity, added, mmWindowAddedField);
        }
        return ret;
    }

    public static boolean getWindowAdded(Activity activity) {
        boolean added = false;
        if(activity != null){
            added = (boolean)ReflectUtil.getObjectByFiled(activity, mDecorField);
        }
        return added;
    }
}
