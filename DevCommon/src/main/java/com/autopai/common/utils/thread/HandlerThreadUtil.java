package com.autopai.common.utils.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.view.Choreographer;

public class HandlerThreadUtil {

    private static HandlerThread mWorkerThread = new HandlerThread("launcher_loader");
    private static Handler mMainHandler = new Handler(Looper.getMainLooper());
    private static Choreographer mChoreographer;
    private static Handler mWorkerHandler;

    static {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mChoreographer = Choreographer.getInstance();
            }
        });
    }

    public static void runOnWorkerThread(Runnable runnable) {
        synchronized (mWorkerThread) {
            if (mWorkerHandler == null) {
                mWorkerThread.start();
                mWorkerHandler = new Handler(mWorkerThread.getLooper());
            }
        }
        mWorkerHandler.post(runnable);

    }

    public static void runOnMainThread(Runnable runnable) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            mMainHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    public static void runOnMainThread(Handler handler, Runnable runnable) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post(runnable);
        } else {
            runnable.run();
        }
    }

    public static void runAtFrame(final Runnable runnable, final int frame){
        if (mChoreographer == null && Looper.myLooper() == Looper.getMainLooper()) {
            mChoreographer = Choreographer.getInstance();
            if(mChoreographer == null){
                return;
            }
        }
        mChoreographer.postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                Log.e("HandlerThreadUtil", "doFrame at: " + frame);
                if(frame <= 0){
                    runnable.run();
                }else{
                    runAtFrame(runnable, frame - 1);
                }
            }
        });
    }

    public static void runAtFrame(final Choreographer choreographer, final Runnable runnable, final int frame){
        if (mChoreographer == null && Looper.myLooper() == Looper.getMainLooper()) {
            mChoreographer = choreographer;
            if(mChoreographer == null){
                return;
            }
        }
        mChoreographer.postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                Log.e("HandlerThreadUtil", "doFrame at: " + frame);
                if(frame <= 0){
                    runnable.run();
                }else{
                    runAtFrame(runnable, frame - 1);
                }
            }
        });
    }
}
