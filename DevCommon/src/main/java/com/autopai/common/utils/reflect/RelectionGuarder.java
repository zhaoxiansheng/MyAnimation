package com.autopai.common.utils.reflect;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import me.weishu.reflection.Reflection;

public class RelectionGuarder {
    public static void enableRefelication(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if(context instanceof Application)
                Reflection.unseal(context);
            else {
                Context application = context.getApplicationContext();
                Reflection.unseal(application);
            }
        }
    }

    public static void disablehidenAPIWarning(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
