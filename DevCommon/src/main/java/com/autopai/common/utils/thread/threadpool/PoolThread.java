package com.autopai.common.utils.thread.threadpool;

import com.autopai.common.utils.thread.threadpool.bean.ThreadBean;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PoolThread implements Executor {

    private ExecutorService mPool;
    /**
     * 线程名
     */
    private String mName;
    /**
     * 延迟执行线程
     */
    private int mDelay;
    /**
     * 线程优先级
     */
    private int mPriority;
    /**
     * 线程回调
     */
    private IThreadCallBack mCallBack;

    private ThreadLocal<ThreadBean> mThreadLoacal = new ThreadLocal<>();

    public PoolThread(int type, int size, int priority) {
        if (mPool == null) {
            mPriority = priority;
            mPool = createPool(type, size, priority);
            //((ThreadPoolExecutor)mPool).allowCoreThreadTimeOut(true);线程 pool等待keepAliveTime
        }
    }

    /**
     * 自定义线程池
     *
     * @param coreSize
     * @param maxSize
     * @param aliveTime
     * @param timeUnit
     * @param blockingQueue
     * @param factory
     * @param handler
     */
    public PoolThread(int coreSize, int maxSize, long aliveTime, TimeUnit timeUnit,
                      BlockingQueue blockingQueue, ThreadFactory factory, RejectedExecutionHandler handler) {
        mPool = new ThreadPoolExecutor(coreSize, maxSize, aliveTime, timeUnit, blockingQueue, factory, handler);
    }

    public PoolThread getPoolThread() {
        return this;
    }

    private synchronized ExecutorService createPool(int type, int size, int priority) {
        switch (type) {
            case ThreadBuilder.TYPE_CACHE:
                return Executors.newCachedThreadPool(new MyThreadFactory(priority));
            case ThreadBuilder.TYPE_FIXED:
                return Executors.newFixedThreadPool(size, new MyThreadFactory(priority));
            case ThreadBuilder.TYPE_SCHEDULED:
                return Executors.newScheduledThreadPool(size, new MyThreadFactory(priority));
            case ThreadBuilder.TYPE_SINGLE:
                return Executors.newSingleThreadExecutor(new MyThreadFactory(priority));
            default:
                return Executors.newSingleThreadExecutor(new MyThreadFactory(priority));
        }
    }

    /**
     * 设置当前线程名
     *
     * @param name
     * @return
     */
    public PoolThread setName(String name) {
        getThreadBean().mName = name;
        return this;
    }

    /**
     * 设置线程执行延迟时间，只有线程创建时才会产生延迟效果
     *
     * @param delay
     * @return
     */
    public PoolThread setDelay(long delay) {
        getThreadBean().mDelay = Math.max(0, delay);
        return this;
    }

    /**
     * 设置线程优先级
     *
     * @param priority
     * @return
     */
    public PoolThread setPriority(int priority) {
        getThreadBean().mPriority = priority;
        return this;
    }

    /**
     * 设置线程回调
     *
     * @param callBack
     * @return
     */
    public PoolThread setCallBack(IThreadCallBack callBack) {
        getThreadBean().mCallBack = callBack;
        return this;
    }

    private ThreadBean getThreadBean() {
        ThreadBean threadBean = mThreadLoacal.get();
        if (threadBean == null) {
            threadBean = new ThreadBean();
            threadBean.mName = mName;
            threadBean.mCallBack = mCallBack;
            threadBean.mDelay = mDelay;
            threadBean.mPriority = mPriority;
            mThreadLoacal.set(threadBean);
        }
        return threadBean;
    }

    /**
     * 执行线程任务
     *
     * @param runnable
     */
    @Override
    public void execute(Runnable runnable) {
        ThreadBean threadBean = getThreadBean();
        if (threadBean != null) {
            runnable = new RunnableWrapper(threadBean, runnable);
            if (threadBean.mDelay == 0) {
                mPool.execute(runnable);
            } else {
                PoolDelayExecutor.getExecutor().doExecutor(threadBean.mDelay, mPool, runnable);
            }
            resetLocalConfigs();
        }
    }

    /**
     * 关闭线程池
     */
    public void stop() {
        try {
            // shutdown只是起到通知的作用
            mPool.shutdown();
            if (!mPool.awaitTermination(0, TimeUnit.MILLISECONDS)) {
                //超时的时候向线程池中所有的线程发出中断(interrupt)。
                mPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mPool.shutdownNow();
            removeLocal();
        }
    }

    public void removeLocal() {
        if (mThreadLoacal != null) {
            mThreadLoacal.remove();
            mThreadLoacal = null;
        }
    }

    /**
     * 当启动任务或者发射任务之后需要调用该方法
     * 重置本地配置，置null
     */
    private synchronized void resetLocalConfigs() {
        mThreadLoacal.set(null);
    }

    /**
     * 线程池建造者模式
     */
    public static class ThreadBuilder implements IPollBuilder {

        static final int TYPE_CACHE = 0;
        static final int TYPE_FIXED = 1;
        static final int TYPE_SINGLE = 2;
        static final int TYPE_SCHEDULED = 3;
        static final int TYPE_CUSTOM = 4;

        /**
         * 给corePoolSize赋值：当前设备可用处理器核心数*2 + 1, 能够让cpu的效率得到最大程度执行（有研究论证的）
         */
        static int mFixCorePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;

        int mType;
        int mCoreSize;
        int mPriority = Thread.NORM_PRIORITY;

        public ThreadBuilder(int type, int size) {
            this.mType = type;
            this.mCoreSize = size;
        }

        public ThreadBuilder(int type) {
            this.mType = type;
        }

        /**
         * 通过Executors.newCachedThreadPool()创建线程池
         * 线程数量无限多的线程池，都是非核心线程，适合执行大量耗时小的任务
         *
         * @return
         */
        public static ThreadBuilder createCachePool() {
            return new ThreadBuilder(TYPE_CACHE, 1);
        }

        /**
         * 通过Executors.newFixedThreadPool()创建线程池
         * 线程数量固定的线程池，全部为核心线程，相应较快，不用担心线程被回收
         *
         * @return
         */
        public static ThreadBuilder createFixedPool() {
            return new ThreadBuilder(TYPE_FIXED, mFixCorePoolSize);
        }

        /**
         * 通过Executors.newScheduledThreadPool()创建线程池
         * 有数量固定的核心线程，且有数量无限多的非核心线程，适合用于执行定时任务和固定周期的重复任务
         *
         * @return
         */
        public static ThreadBuilder createScheduledPool() {
            return new ThreadBuilder(TYPE_SCHEDULED, mFixCorePoolSize);
        }

        /**
         * 通过Executors.newSingleThreadPool()创建线程池
         * 只有一个核心线程，所有任务进来都要排队按顺序执行
         *
         * @return
         */
        public static ThreadBuilder createSingle() {
            return new ThreadBuilder(TYPE_SINGLE, 1);
        }

        public static ThreadBuilder createCustomPool() {
            return new ThreadBuilder(TYPE_CUSTOM);
        }

        @Override
        public ThreadBuilder setPriority(int priority) {
            this.mPriority = priority;
            return this;
        }

        public PoolThread build() {
            mPriority = Math.min(Thread.MAX_PRIORITY, Math.max(Thread.MIN_PRIORITY, mPriority));
            mCoreSize = Math.max(1, mCoreSize);
            return new PoolThread(mType, mCoreSize, mPriority);
        }

        public PoolThread buildCustom(int coreSize, int maxSize, long aliveTime, TimeUnit timeUnit,
                        BlockingQueue blockingQueue, ThreadFactory factory, RejectedExecutionHandler handler) {
            return new PoolThread(coreSize, maxSize, aliveTime, timeUnit, blockingQueue, factory, handler);
        }
    }

    public interface IPollBuilder {
        ThreadBuilder setPriority(int priority);
    }
}
