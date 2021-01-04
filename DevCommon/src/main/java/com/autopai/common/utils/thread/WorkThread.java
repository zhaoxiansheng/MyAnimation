package com.autopai.common.utils.thread;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.RequiresApi;

public class WorkThread {
    private volatile HandlerThread mServiceThread;
    private volatile Handler mServiceHandler;
    private FinishRunnable mFinishNotify = new FinishRunnable();

    public WorkThread(String name)
    {
        mServiceThread = new HandlerThread("WorkThread_" + name);
        mServiceThread.start();
        mServiceHandler = new Handler(mServiceThread.getLooper());
    }

    public WorkThread(){
        start();
    }
    public WorkThread(boolean bStart)
    {
        if(bStart) {
            start();
        }
    }

    public void start(){
        if(mServiceThread != null) {
            quitSync(false);
        }
        mFinishNotify.reset();
        mServiceThread = new HandlerThread("WorkThread_" + this.hashCode());
        mServiceThread.start();
        mServiceHandler = new Handler(mServiceThread.getLooper());
    }

    public Handler getThreadHandler(){
        return mServiceHandler;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isIdle(){
        if(mServiceHandler != null) {
            return mServiceHandler.getLooper().getQueue().isIdle();
        }
        return true;
    }

    public void post(Runnable run){
        synchronized (mFinishNotify) {
            if (mServiceHandler != null && run != null)
                mServiceHandler.post(run);
        }
    }

    public void postDelayed(Runnable run, long delay){
        synchronized (mFinishNotify) {
            if (mServiceHandler != null && run != null)
                mServiceHandler.postDelayed(run, delay);
        }
    }

    public void postAtFrontOfQueue(Runnable run){
        synchronized (mFinishNotify) {
            if (mServiceHandler != null && run != null)
                mServiceHandler.postAtFrontOfQueue(run);
        }
    }

    public void remove(Runnable run){
        if(mServiceHandler != null) {
            if (run != null) {
                mServiceHandler.removeCallbacks(run);
            } else {
                mServiceHandler.removeCallbacksAndMessages(null);
            }
        }
    }

    public void runOnce(Runnable run){
        if(run != null) {
            remove(run);
            post(run);
        }
    }

    public boolean isAlive(){
        synchronized (mFinishNotify) {
            if (mServiceThread != null) {
                return mServiceThread.isAlive();
            }
        }
        return false;
    }

    public boolean quit(boolean bSafe){
        boolean ret = false;
        if(mServiceThread != null) {
            ret = bSafe ? mServiceThread.quitSafely() : mServiceThread.quit();
            mServiceThread = null;
            mServiceHandler = null;
        }
        return ret;
    }

    private static class FinishRunnable implements Runnable{
        private boolean mFinished = false;

        @Override
        public synchronized void run() {
            if(!mFinished) {
                mFinished = true;
                this.notifyAll();
            }
        }

        private synchronized void waitFinished(){
            while(!mFinished) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private synchronized void reset(){
            mFinished = false;
        }
    }

    public boolean quitSync(boolean bSafe) {
        synchronized (mFinishNotify) {
            if (mServiceHandler != null) {
                if (Looper.myLooper() != mServiceThread.getLooper()) {
                    mServiceHandler.postAtFrontOfQueue(mFinishNotify);
                    mFinishNotify.waitFinished();
                }
            }
            return quit(bSafe);
        }
    }
}
