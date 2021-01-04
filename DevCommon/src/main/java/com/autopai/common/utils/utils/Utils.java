package com.autopai.common.utils.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.autopai.common.utils.utils.thread.BasicThreadFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Utils {

    private static Application sApplication;

    static final Handler UTIL_HANDLER = new Handler(Looper.getMainLooper());

    private static final ExecutorService UTIL_POOL = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), new BasicThreadFactory.Builder().namingPattern("util-pool-%d").daemon(true).build());

    public static void init(final Context context) {
        if (context == null) {
            init(getApplicationByReflect());
            return;
        }
        init((Application) context.getApplicationContext());
    }

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(final Application app) {
        if (sApplication == null) {
            if (app == null) {
                sApplication = getApplicationByReflect();
            } else {
                sApplication = app;
            }
        } else {
            if (app != null && app.getClass() != sApplication.getClass()) {
                sApplication = app;
            }
        }
    }

    public static Application getApp() {
        if (sApplication != null) {
            return sApplication;
        }
        Application app = getApplicationByReflect();
        init(app);
        return app;
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

    @SuppressLint("PrivateApi")
    public static Activity getActivityByReflect() {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Field activitysField = activityThread.getDeclaredField("mActivities");
            activitysField.setAccessible(true);
            Map<Object, Object> activitys = (Map<Object, Object>)activitysField.get(thread);
            if (activitys == null || activitys.isEmpty()) {
                throw new NullPointerException("no activity");
            }
            Object activityRecord = activitys.values().iterator().next();
            if(activityRecord != null){
                Class<?> activityRecordClz = Class.forName("android.app.ActivityThread$ActivityClientRecord");
                Field activityField = activityRecordClz.getDeclaredField("activity");
                activityField.setAccessible(true);
                return (Activity) activityField.get(activityRecord);
            }
            return null;
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }

    public abstract static class Task<Result> implements Runnable {

        private static final int NEW = 0;
        private static final int COMPLETING = 1;
        private static final int CANCELLED = 2;
        private static final int EXCEPTIONAL = 3;

        private volatile int state = NEW;

        protected abstract Result doInBackground();

        private Callback<Result> mCallback;

        public Task(final Callback<Result> callback) {
            mCallback = callback;
        }

        @Override
        public void run() {
            try {
                final Result t = doInBackground();

                if (state != NEW) {
                    return;
                }
                state = COMPLETING;
                UTIL_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onCall(t);
                    }
                });
            } catch (Throwable th) {
                if (state != NEW) {
                    return;
                }
                state = EXCEPTIONAL;
            }
        }

        public void cancel() {
            state = CANCELLED;
        }

        public boolean isDone() {
            return state != NEW;
        }

        public boolean isCanceled() {
            return state == CANCELLED;
        }
    }

    public interface Callback<T> {
        void onCall(T data);
    }

    public static <T> Task<T> doAsync(final Task<T> task) {
        UTIL_POOL.execute(task);
        return task;
    }
}
