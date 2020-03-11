package com.example.zy.myanimation.view.recycler.utils.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

public class HandlerThreadUtil {

    private static HandlerThread mWorkerThread = new HandlerThread("launcher_loader");
    private static Handler mMainHandler = new Handler(Looper.getMainLooper());
    private static final int mMainTid = Process.myTid();

    static {
        mWorkerThread.start();
    }

    private static Handler mWorkerHandler = new Handler(mWorkerThread.getLooper());

    public static void runOnWorkerThread(Runnable runnable) {
        if (mWorkerThread.getThreadId() == Process.myTid()) {
            runnable.run();
        } else {
            mWorkerHandler.post(runnable);
        }
    }

    public static void runOnMainThread(Runnable runnable) {
        if (Process.myTid() != mMainTid) {
            mMainHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    public static void runOnMainThread(Handler handler, Runnable runnable) {
        if (mWorkerThread.getThreadId() == Process.myTid()) {
            handler.post(runnable);
        } else {
            runnable.run();
        }
    }
}
