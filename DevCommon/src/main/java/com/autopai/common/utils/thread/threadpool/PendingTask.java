package com.autopai.common.utils.thread.threadpool;

import android.util.Log;

import java.util.ArrayList;

public class PendingTask {
    private static final String TAG = "PendingTask";
    private final ArrayList<HandlerTask> mPenddings = new ArrayList<>();
    private volatile boolean mReadyed = false;

    public PendingTask(){
        prepare();
    }

    public void ready(){
        synchronized (mPenddings) {
            int size = mPenddings.size();
            for(int i=0; i<size; ++i){
                mPenddings.get(i).action.run();
            }
            if(size > 0){
                mPenddings.clear();
            }
            mReadyed = true;
        }
    }

    public void prepare(){
        synchronized (mPenddings) {
            mReadyed = false;
        }
    }

    public void removeCallBack(Runnable runnable){
        synchronized (mPenddings){
            int size = mPenddings.size();
            HandlerTask task;
            for(int i=0; i<size; ++i){
                task = mPenddings.get(i);
                if(task != null && task.matches(runnable)){
                    mPenddings.remove(task);
                    return;
                }
            }
        }
    }

    public void removeCallBackByToken(Object token){
        synchronized (mPenddings){
            int size = mPenddings.size();
            Log.e(TAG, "begin removeCallBackByToken size: " + size);
            HandlerTask task;
            for(int i=0; i<size; ++i){
                task = mPenddings.get(i);
                if(task != null && task.matchesToken(token)){
                    mPenddings.remove(task);

                    if(i < size-1) {
                        --i;
                    }
                    size = mPenddings.size();
                    Log.e(TAG, "removeCallBackByToken: " + token);
                }
            }
        }
    }

    public void post(Runnable runnabel){
        synchronized (mPenddings) {
            if (!mReadyed) {
                if (runnabel != null) {
                    HandlerTask task = new HandlerTask(mPenddings.size(), runnabel, 0);
                    mPenddings.add(task);
                }
            } else {
                runnabel.run();
            }
        }
    }

    public void post(Object token, Runnable runnabel){
        synchronized (mPenddings) {
            if (!mReadyed) {
                if (runnabel != null) {
                    HandlerTask task = new HandlerTask(token, runnabel, 0);
                    mPenddings.add(task);
                }
            } else {
                runnabel.run();
            }
        }
    }

    private static class HandlerTask {
        private final Runnable action;
        private final long delay;
        private final Object token;

        public HandlerTask(Object token, Runnable action, long delay) {
            this.action = action;
            this.delay = delay;
            this.token = token;
        }

        public boolean matches(Runnable otherAction) {
            return otherAction == null && action == null
                    || action != null && action.equals(otherAction);
        }

        public boolean matchesToken(Object otherToken) {
            return token != null && token.equals(otherToken);
        }
    }
}
