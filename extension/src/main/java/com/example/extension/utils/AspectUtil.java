package com.example.extension.utils;

import android.util.Log;
import android.view.WindowManager;

import org.aspectj.lang.ProceedingJoinPoint;

public class AspectUtil {
    public static final String TAG = "AspectUtils";
    public static final boolean DEBUG = true;

    public static void makeJoinPoint(ProceedingJoinPoint joinPoint) {
        long time = System.currentTimeMillis();
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        if (DEBUG) {
            long cost = System.currentTimeMillis() - time;
            Log.d(TAG, " take time = " + cost + "ms");
        }
    }
}
