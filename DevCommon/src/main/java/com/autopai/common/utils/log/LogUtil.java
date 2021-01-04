package com.autopai.common.utils.log;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;

public class LogUtil {
    /**
     * If  LOG is displayed, true displays
     */
    public static boolean DEBUG_LOG = true;
    private static String className;
    private static String methodName;
    private static int lineNumber;

    private static String createLog(String log) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void v(String msg) {
        if (DEBUG_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.v(className, createLog(msg));
        }
    }

    public static void v(Context context, int resId) {
        v(context.getApplicationContext().getString(resId));
    }

    public static void d(String msg) {
        if (DEBUG_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.d(className, createLog(msg));
        }
    }

    public static void d(Context context, int resId) {
        d(context.getApplicationContext().getString(resId));
    }

    public static void i(String msg) {
        if (DEBUG_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.i(className, createLog(msg));
        }
    }

    public static void i(Context context, int resId) {
        i(context.getApplicationContext().getString(resId));
    }

    public static void w(String msg) {
        if (DEBUG_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.w(className, createLog(msg));
        }
    }

    public static void w(Context context, int resId) {
        w(context.getApplicationContext().getString(resId));
    }

    public static void e(String msg) {
        if (DEBUG_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.e(className, createLog(msg));
        }
    }

    public static void e(Context context, int resId) {
        e(context.getApplicationContext().getString(resId));
    }

    public static void wtf(String message) {
        if (DEBUG_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.wtf(className, createLog(message));
        }
    }

    public static void wtf(Context context, int resId) {
        wtf(context.getApplicationContext().getString(resId));
    }

    public static void log(String tag, String message, Object... args) {
        String log = message + Arrays.deepToString(args);
        Log.i(tag, log);
    }

    public static void logTrace(String tag, String message) {
        StringBuffer traceLog = new StringBuffer("");
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        int length = elements.length;
        for (int i = 3; i < length; i++) {
            traceLog.append(elements[i].toString()).append("\n");
        }
        Log.i(tag, message + "\n" + traceLog);
    }

}
