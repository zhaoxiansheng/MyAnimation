package com.autopai.common.utils.thread.threadpool.utils;

import android.util.Log;
import android.util.SparseArray;

import java.util.concurrent.atomic.AtomicInteger;

public class Sequencer {
    private static final String TAG = "Sequencer";
    protected final SparseArray<Sequence> mSequenceGroups = new SparseArray<>(0);//key is runnable caller hashCode

    final class Sequence {
        private int mID = -1;
        private AtomicInteger mRef = new AtomicInteger(1);
        private DispatchThreadPoolBase.Worker mRunning;

        public Sequence(final Integer id) {
            if(id != null) {
                mID = id;
            }
        }

        public void setWorker(final DispatchThreadPoolBase.Worker worker){
            if(worker != null) {
                if(mRunning != null) {
                    Log.e(TAG, "may have prev task not finished " + Log.getStackTraceString(new Throwable()));
                }
                mRunning = worker;
            }
        }

        public void clearWorker(){
            mRunning = null;
        }

        int incRef(){
            return mRef.incrementAndGet();
        }

        int descRef(){
            return mRef.decrementAndGet();
        }

        public boolean isRunning(){
            return (mRunning != null);
        }

        @Override
        public String toString() {
            return super.toString() +
                    "[running woke" + ", mID: " + mID + " running: " + mRunning + "]";
        }
    }

    IDispatchWorkQueue.AddResult addSequence(Integer sequenceId){
        IDispatchWorkQueue.AddResult result = null;
        Sequence sequence = mSequenceGroups.get(sequenceId);
        if (sequence != null) {
            sequence.incRef();
            result = IDispatchWorkQueue.AddResult.obtain(sequence, false);
        } else {
            sequence = new Sequence(sequenceId);
            mSequenceGroups.put(sequenceId, sequence);
            result = IDispatchWorkQueue.AddResult.obtain(sequence, true);
        }
        return result;
    }

    Sequence removeSequence(Integer sequenceId){
        Sequence sequence = mSequenceGroups.get(sequenceId);
        if (sequence != null) {
            int now = sequence.descRef();
            if(now == 0) {
                mSequenceGroups.remove(sequenceId);
            }else if(now < 0) {
                Log.e(TAG, "removeSequence " + sequenceId + " error now: " + now + " at " + Log.getStackTraceString(new Throwable()));
            }
        }
        return sequence;
    }

    Sequence get(Integer sequenceId){
        return mSequenceGroups.get(sequenceId);
    }

    void clear(){
        mSequenceGroups.clear();
    }
}
