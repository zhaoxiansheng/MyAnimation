package com.autopai.common.utils.thread.threadpool.utils;

import android.os.SystemClock;
import android.support.v4.util.Pools;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public interface IDispatchWorkQueue {

    public interface IPollPolicy {
        boolean isValidWork(Work work);
    }

    public static class Work {
        private static final String TAG = "Work";
        private static final boolean DEBUG = false;
        private Runnable mRunnable;
        long mDelay;
        private Integer mSequenceID = null;
        private WorkCallBack mCallBack;
        private boolean mCanceled = false;

        private static AtomicInteger token = null;
        private int mID;

        Work(Runnable runnable, long delay, WorkCallBack callBack, Integer sequenceId) {
            if(DEBUG){
                if(token == null) {
                    token = new AtomicInteger(0);
                }
                mID = token.getAndIncrement();
            }
            mRunnable = runnable;
            init(delay, callBack, sequenceId);
        }

        Work(Runnable runnable){
            if(DEBUG){
                if(token == null) {
                    token = new AtomicInteger(0);
                }
                mID = token.getAndIncrement();
            }
            mRunnable = runnable;
        }

        synchronized void init(long delay, WorkCallBack callBack, Integer sequenceId){
            mCallBack = callBack;
            mSequenceID = sequenceId;
            mDelay = Math.max(0, delay);
            mCanceled = false;
        }

        public Runnable getRunnable(){
            return mRunnable;
        }

        public synchronized void cancel() {
            mCanceled = true;
        }

        public synchronized boolean isCanceled(){
            return mCanceled;
        }

        public Integer getSequenceID(){
            return mSequenceID;
        }

        public void execute() {
            boolean ret = true;
            if(mCallBack != null){
                ret = mCallBack.beforeExecute(this);
            }

            try {
                if(!isCanceled()) {
                    if (ret) {
                        Log.e(TAG, "DispatcherThreadPool begin execute: " + this);
                        mRunnable.run();
                    } else {
                        Log.e(TAG, "not allowed execute: " + this);
                    }
                }else {
                    Log.e(TAG, "cancel Work: " + this);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(mCallBack != null){
                    mCallBack.afterExecute(this);
                }
            }
        }

        @Override
        public String toString() {
            return "[work" + " mID: " + mID + " ,SeqId: " + mSequenceID + " ,runnable: " + mRunnable + " ,Canceled: " + mCanceled + "]";
        }

        public interface WorkCallBack {
            boolean beforeExecute(Work work);

            boolean afterExecute(Work work);
        }
    }

    static final class AddResult{
        private static final Pools.SynchronizedPool<AddResult> sPool =
                new Pools.SynchronizedPool<>(3);

        private Sequencer.Sequence mSequence;
        private boolean mFresh = true;

        private AddResult(){

        }

        private void init(Sequencer.Sequence sequence, boolean fresh){
            mSequence = sequence;
            mFresh = fresh;
        }

        public Sequencer.Sequence getSequence(){
            return mSequence;
        }

        boolean isFresh(){
            return mFresh;
        }

        public static AddResult obtain(Sequencer.Sequence sequence, boolean fresh) {
            AddResult instance = sPool.acquire();
            if(instance == null){
                instance = new AddResult();
            }
            instance.init(sequence, fresh);
            return instance;
        }

        public void recycle() {
            // Clear state if needed.
            mSequence = null;
            mFresh = false;
            try {
                sPool.release(this);
            }catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }

    }

    boolean offer(Work work);
    boolean remove(Work work);
    Work poll(long timeout, TimeUnit unit, IPollPolicy policy);

    /**
     * wait until have a work get
     * @param policy
     * @return first of avalid work of the queue
     */
    Work take(IPollPolicy policy);
    Work peek();
    boolean contains(Work work);
    int size();
    void clear();
    boolean lockQueue();
    void unlockQueue();
    void signal(boolean bAll);

    AddResult addSequence(Integer sequenceId);
    Sequencer.Sequence removeSequence(Integer sequenceId);
    boolean isSequenceRunning(Work work);
    void setSequenceRunning(Sequencer.Sequence sequence, DispatchThreadPoolBase.Worker worker);
    void setSequenceRunning(Integer sequenceId, DispatchThreadPoolBase.Worker worker);
    boolean beforeExecute(Work work);
    boolean afterExecute(Work work);
    List<Work> getWorks();
}
