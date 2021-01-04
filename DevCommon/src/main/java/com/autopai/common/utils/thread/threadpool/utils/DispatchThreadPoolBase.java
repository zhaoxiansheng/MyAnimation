package com.autopai.common.utils.thread.threadpool.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v4.util.Pools;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;

import com.autopai.common.utils.common.Utilities;
import com.autopai.common.utils.thread.threadpool.utils.IDispatchWorkQueue.Work;
import com.autopai.common.utils.thread.threadpool.utils.IDispatchWorkQueue.Work.WorkCallBack;
import com.autopai.common.utils.utils.Utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public abstract class DispatchThreadPoolBase {
    private static final String TAG = "DispatchThreadPoolBase";
    private static final Pools.SynchronizedPool<ScheduleTask> sTaskPool =
            new Pools.SynchronizedPool<>(6);

    protected int mCoreThreadNum = 1;
    protected int mMaxThreadNum = Runtime.getRuntime().availableProcessors() * 2 + 1;
    protected long mKeepAliveTime = 0L;
    protected boolean mAllowCoreRecycle = false;
    protected TimeUnit mUtil;
    protected ThreadFactory mThreadFactory;
    protected IRejectPolicy mRejectPolicy;
    private IDispatchWorkQueue.IPollPolicy mPollPolicy;

    protected final Scheduler mScheduler;
    protected IDispatchWorkQueue mWorkQueue;
    private float mIncrementRatio = 0.3f;//workQueue size / maxThreadNum
    private final ReentrantLock mainLock = new ReentrantLock();
    /**
     * Wait condition to support awaitTermination.
     */
    private final Condition termination = mainLock.newCondition();
    private static final int RUNNING = -1;
    private static final int TIDYING = 0;
    private static final int SHUTDOWN = 1;
    private int mState = RUNNING;

    @SuppressLint("NewApi")
    protected final Set<Worker> mWorkers = (Utilities.ATLEAST_MARSHMALLOW) ? new ArraySet<Worker>(3) : new HashSet<Worker>(3);

    private class DebugReentrantLock extends  ReentrantLock {
        @Override
        public void lock() {
            super.lock();
            Log.e(TAG, "+++++AA lock at " + Log.getStackTraceString(new Throwable()));
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
            super.lockInterruptibly();
            Log.e(TAG, "+++++AA lockInterruptibly at " + Log.getStackTraceString(new Throwable()));
        }

        @Override
        public void unlock() {
            Log.e(TAG, "----AA unlock at " + Log.getStackTraceString(new Throwable()));
            super.unlock();
        }
    }


    public final class ScheduleTask implements Runnable, Work.WorkCallBack {
        private Work mWork;
        private boolean mAutoRecycle = false;

        public ScheduleTask(Runnable runnable){
            init(runnable, false);
        }

        ScheduleTask(){
        }

        private void init(Runnable runnable, boolean autoRecycle){
            mWork = new Work(runnable);
            mAutoRecycle = autoRecycle;
        }

        void initWork(long delay, WorkCallBack callBack, Integer sequenceId){
            mWork.init(delay, callBack, sequenceId);
        }

        Work getWork(){
            return mWork;
        }

        public Runnable getRunnable(){
            return mWork.getRunnable();
        }

        @Override
        public void run() {
            boolean bCanceled = mWork.isCanceled();

            boolean ret = false;
            if(!bCanceled){
               //sequence task may enqueue for wait prev running task
                Integer sequenceId = mWork.getSequenceID();
                Log.e(TAG, "DispatcherThreadPool ScheduleTask: " + mWork + " sequence: " + sequenceId);
                IDispatchWorkQueue.AddResult sequenceResult = null;
                if(sequenceId != null) {
                    sequenceResult = mWorkQueue.addSequence(sequenceId);// should first add
                }

                //not a sequence task or not have the sequence yet
                if(sequenceResult == null || sequenceResult.isFresh()) {

                    //if current worker size < coreSize or can addNewWorker, run task on new Worker,otherwise offer it to workqueue
                    boolean bAddNewWorker = false;
                    try {
                        mainLock.lockInterruptibly();
                        bAddNewWorker = residueCoreSize() > 0 || (residueMaxSize() > 0 && isOverload());
                        if(bAddNewWorker) {
                            Worker newWorker = new Worker(mWork);
                            //record sequence running
                            if(sequenceResult != null && sequenceResult.getSequence() != null) {
                                mWorkQueue.setSequenceRunning(sequenceResult.getSequence(), newWorker);
                            }
                            ret = addWorker(newWorker);
                            /**
                             * we can not execute this work, so remove sequence
                             */
                            if(ret) {
                                Log.e(TAG, "DispatcherThreadPool work: " + mWork + "run first at once");
                            }else{
                                if(sequenceResult != null && sequenceResult.getSequence() != null) {
                                    mWorkQueue.setSequenceRunning(sequenceResult.getSequence(), null);
                                }
                                if(sequenceId != null){
                                    mWorkQueue.removeSequence(sequenceId);
                                }
                                Log.e(TAG, "DispatcherThreadPool work: " + mWork + "run first failed");
                            }

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        mainLock.unlock();
                    }
                    //not allow run at once,we offer to queue
                    if (!bAddNewWorker) {
                        Log.e(TAG, "DispatcherThreadPool work: " + mWork + " enqueue for no avaliable worker");
                        ret = enqueueWork(mWork);
                    }
                }else {
                    ret = enqueueWork(mWork); //we can not run at once case have prevTask running
                }
                if(sequenceResult != null) {
                    sequenceResult.recycle();
                }
            }
            if(!bCanceled && !ret){
                if(mRejectPolicy != null) {
                    mRejectPolicy.doReject(mWork);
                }
                Log.e(TAG, "reject task: " + mWork.getRunnable());
            }
        }

        @Override
        public boolean beforeExecute(Work work) {
           return true;
        }

        @Override
        public boolean afterExecute(Work work) {
            boolean ret = false;
            if(work == mWork) {
                ret = mWorkQueue.afterExecute(work);
            }else {
                Log.e(TAG, "DispatcherThreadPool afterExecute work not identical");
            }

            if(mAutoRecycle){
                recycle();
            }
            return ret;
        }

        public void recycle() {
            if(mAutoRecycle) {
                // Clear state if needed.
                //this.mWork = null;
                try {
                    sTaskPool.release(this);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Worker implements Runnable, IDispatchWorkQueue.IPollPolicy{
        private Thread mThread = null;
        Work mFirstTask;
        private volatile boolean mStoped;
        /**
         * Creates with given first task and thread from ThreadFactory.
         * @param firstTask the first task (null if none)
         */
        Worker(Work firstTask) {
            mStoped = true;
            mFirstTask = firstTask;
        }

        public synchronized void shutdown(){
            Log.e(TAG, "Thread: " + mThread.getName() + " shutdown, mStoped: " + mStoped);
            if(!mStoped) {
                mStoped = true;
                if (mThread != null) {
                    mThread.interrupt();
                }
            }else if(mThread == null){
                processWorkerExit();
            }
        }

        public synchronized boolean start(){
            mStoped = !isRunning();
            if(!mStoped) {
                if(mThread == null) {
                    mThread = getThreadFactory().newThread(this);
                }
                if(mThread != null)
                    mThread.start();
            }else{
                Log.e(TAG, "worker start mThread: " + mThread + ", stop: " + mStoped);
                mStoped = true;
            }
            return (mThread != null);
        }

        /** Delegates main run loop to outer runWorker. */
        public void run() {
            runWorker();
        }

        private void runWorker(){
            if(mFirstTask != null) {
                Log.e(TAG, "Thread: " + mThread.getName() + " execute first task: " + mFirstTask);
                mFirstTask.execute();
                mFirstTask = null;
            }

            Work work;
            while(true) {
                synchronized (this) {
                    if( mStoped || mThread.isInterrupted() ) {
                        break;
                    }
                }

                work = getWork(this);
                if(work != null) {
                    Log.e(TAG, "DispatcherThreadPool runWorker poll work: " + work);
                    work.execute();
                }else {
                    break;
                }
            }

            processWorkerExit();
        }

        private void processWorkerExit(){
            //
            mainLock.lock();
            if(!mWorkers.remove(this)){
                Log.e(TAG, "worker: " + mThread.getName() + ", finished error");
            }
            /**
             * add new Worker for deal work
             */
            if(isRunning() && !mAllowCoreRecycle && ( residueCoreSize() > 0 || (residueMaxSize() > 0 && isOverload()) )) {
                Worker newWorker = new Worker(null);
                if(!addWorker(newWorker)){
                    Log.e(TAG, "prepare a new worker failed");
                }
            }
            termination.signalAll();
            mainLock.unlock();

            mThread.interrupt();
            Log.e(TAG, "Thread: " + mThread.getName() + " quit");
            synchronized (this) {
                mThread = null;
                mStoped = true;
            }
        }

        @Override
        public boolean isValidWork(Work work){
            return !work.isCanceled() && !isWorkSequenceRunning(work);
        }
    }

    public DispatchThreadPoolBase(){
        mScheduler = new Scheduler();
        mWorkQueue = initWorkQueue();

        mUtil = TimeUnit.MILLISECONDS;
        mKeepAliveTime = 60 * 1000;
        mThreadFactory = Executors.defaultThreadFactory();
        mRejectPolicy = null;
        mPollPolicy = null;
    }

    public DispatchThreadPoolBase(int coreNum, int maxNum, long keepTime, TimeUnit timeUtil, ThreadFactory threadFactory, IRejectPolicy rejectPolicy, IDispatchWorkQueue.IPollPolicy pollPolicy){
        mScheduler = new Scheduler();
        mWorkQueue = initWorkQueue();

        mCoreThreadNum = Math.max(1, coreNum);
        mMaxThreadNum = Math.max(mCoreThreadNum, maxNum);
        mKeepAliveTime = keepTime;
        mUtil = timeUtil;
        if(threadFactory == null){
            mThreadFactory = Executors.defaultThreadFactory();
        }else {
            mThreadFactory = threadFactory;
        }
        mRejectPolicy = rejectPolicy;
        mPollPolicy = pollPolicy;
    }

    public void setAllowCoreRecycle(boolean bRecycle){
        mainLock.lock();
        mAllowCoreRecycle = bRecycle;
        mainLock.unlock();
    }

    public void setIncrementRatio(float ratio){
        mainLock.lock();
        mIncrementRatio = Math.max(ratio, 0);
        mainLock.unlock();
    }

    public void setKeepAliveTime(long time, TimeUnit util){
        mainLock.lock();
        mKeepAliveTime = time;
        mUtil = util;
        mainLock.unlock();
    }

    public boolean getAllowCoreRecycle(){
        boolean bRecycle;
        mainLock.lock();
        bRecycle = mAllowCoreRecycle;
        mainLock.unlock();
        return bRecycle;
    }

    public int getAliveSize(){
        int size = 0;
        mainLock.lock();
        size = mWorkers.size();
        mainLock.unlock();
        return size;
    }

    protected abstract IDispatchWorkQueue initWorkQueue();

    protected abstract boolean isBusy();

    public ScheduleTask obtainTask(Runnable runnable, boolean autoRecycle){
        if(runnable != null) {
            ScheduleTask task = sTaskPool.acquire();
            if (task == null) {
                task = new ScheduleTask();
            }
            task.init(runnable, autoRecycle);
            Log.e(TAG, "DispatcherThreadPool obtain ScheduleTask: " + task.hashCode());
            return task;
        }else {
            return null;
        }
    }

    public synchronized boolean post(final ScheduleTask task){
        postDelayed(task, 0);
        return true;
    }

    public synchronized boolean postDelayed(final ScheduleTask task, final long delay) {
        postSequenceDelayed(null, task, delay);
        return true;
    }

    public synchronized boolean postSequence(final Integer sequenceId, ScheduleTask task){
        postSequenceDelayed(sequenceId, task, 0);
        return true;
    }

    public synchronized boolean postSequenceDelayed(final Integer sequenceId, final ScheduleTask task, final long delay) {
        task.initWork(delay, task, sequenceId);
        mScheduler.schedule(task);
        return true;
    }

    public synchronized boolean runOnceSequenceDelayed(final Integer sequenceId, final ScheduleTask task, final long delay) {
        removeTask(task);
        return postSequenceDelayed(sequenceId, task, delay);
    }

    public synchronized boolean runOnceSequence(final Integer sequenceId, final ScheduleTask task) {
        removeTask(task);
        return postSequenceDelayed(sequenceId, task, 0);
    }

    public synchronized boolean runOnce(final ScheduleTask task){
        removeTask(task);
        return post(task);
    }

    public synchronized boolean removeTask(ScheduleTask task){
        boolean ret = false;
        if(task != null) {
            mScheduler.unschedule(task);
            ret = dequeueWork(task.mWork);
        }
        return ret;
    }

    public ThreadFactory getThreadFactory() {
        return mThreadFactory;
    }

    private boolean addWorker(Worker worker){
        boolean added = false;
        mainLock.lock();
        //add a new worker
        added = mWorkers.add(worker);
        mainLock.unlock();
        if (added) {
            if( !worker.start() ) {
                removeWorker(worker);
                added = false;
            }
        }

        return added;
    }

    private void removeWorker(Worker worker){
        try {
            mainLock.lockInterruptibly();
            mWorkers.remove(worker);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mainLock.unlock();
        }
    }

    private boolean enqueueWork(Work work){
        boolean added = false;
        added = mWorkQueue.offer(work);
        return added;
    }

    private boolean dequeueWork(Work work){
        boolean removed = false;
        removed = mWorkQueue.remove(work);
        return removed;
    }

    private Work getWork(Worker worker){
        Work work = null;
        boolean timed;
        while (true) {
            mainLock.lock();
            // Are workers subject to culling?
            timed = mAllowCoreRecycle || mWorkers.size() > mCoreThreadNum;
            mainLock.unlock();
            work = timed ?
                    mWorkQueue.poll(mKeepAliveTime, mUtil, (mPollPolicy == null) ? worker : mPollPolicy) :
                    mWorkQueue.take((mPollPolicy == null) ? worker : mPollPolicy);
            if (work != null) {
                break;
            } else {
                if (timed) {
                    break; //no avalidate work, the worker will recycle
                }
            }
        }
       return work;
    }

    private boolean isWorkSequenceRunning(Work work){
        boolean isRunning = mWorkQueue.isSequenceRunning(work);
        return isRunning;
    }

    private int residueCoreSize(){
        int residue = 0;
        try {
            mainLock.lockInterruptibly();
            residue = mCoreThreadNum - mWorkers.size();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mainLock.unlock();
        }
        return residue;
    }

    private boolean isOverload(){
        boolean overload = false;
        int workSize = mWorkQueue.size();
        try {
            mainLock.lockInterruptibly();
            int workerNum = mWorkers.size();
            if(workerNum > 0) {
                overload = ((float) workSize / workerNum) > mIncrementRatio;
            }else {
                overload = (workSize > 0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mainLock.unlock();
        }
        return overload;
    }

    private int residueMaxSize(){
        int residue = 0;
        try {
            mainLock.lockInterruptibly();
            residue = mMaxThreadNum - mWorkers.size();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mainLock.unlock();
        }
        return residue;
    }

    private float loadRatio(){
        float ratio = 0;
        int workSize = mWorkQueue.size();
        try {
            mainLock.lockInterruptibly();
            int workerNum = mWorkers.size();
            if(workerNum > 0) {
                ratio = (float) workSize / workerNum;
            }else{
                ratio = workSize;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mainLock.unlock();
        }
        return ratio;
    }

    public float getLoad(){
        float load = -1f;
        int workSize = mWorkQueue.size();
        try {
            mainLock.lockInterruptibly();
            int workerNum = mWorkers.size();
            if(workerNum > 0) {
                load = (float) workSize / workerNum;
                if(load > mIncrementRatio)
                    load = workSize / mMaxThreadNum;
            }else {
                load = workSize;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mainLock.unlock();
        }
        return load;
    }

    public List<Work> shutdown(long waitTime, boolean bGetResidues){
        mainLock.lock();
        if(mState == SHUTDOWN)
        {
            mainLock.unlock();
            return null;
        }
        mState = SHUTDOWN;
        mScheduler.quit(false);

        for(Worker worker : mWorkers){
            worker.shutdown();
        }
        long nanos = mUtil.toNanos(waitTime);
        while(mWorkers.size() > 0) {
            try {
                if(nanos <= 0){
                    break;
                }
                nanos = termination.awaitNanos(nanos);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mWorkers.clear();
        mainLock.unlock();

        List<Work> residues = null;
        if(bGetResidues) {
            residues = mWorkQueue.getWorks();
        }
        mWorkQueue.clear();
        return residues;
    }

    private boolean isRunning() {
        boolean isRunning = false;
        mainLock.lock();
        isRunning = (mState == RUNNING);
        mainLock.unlock();
        return isRunning;
    }

    @Override
    public String toString() {
        return "[ThreadPool coreSize: " + mCoreThreadNum + " ,maxSize: " + mMaxThreadNum + " ,mIncrementRatio:" + mIncrementRatio + "]";
    }
}
