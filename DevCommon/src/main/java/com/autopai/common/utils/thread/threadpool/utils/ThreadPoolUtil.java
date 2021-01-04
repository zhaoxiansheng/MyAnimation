package com.autopai.common.utils.thread.threadpool.utils;

import android.text.TextUtils;

import com.autopai.common.utils.thread.threadpool.PoolThread;
import com.autopai.common.utils.thread.threadpool.ThreadDeliver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtil {

    private static volatile ThreadPoolUtil sInstance;
    private static PoolThread mFixedPoolThread;
    private static PoolThread mSinglePoolThread;
    private static PoolThread mCachePoolThread;
    private static PoolThread mSchedulePoolThread;
    private static PoolThread mCustomPoolThread;

    private ThreadPoolUtil() {

    }

    public static ThreadPoolUtil getInstance() {
        if (sInstance == null) {
            synchronized (ThreadPoolUtil.class) {
                if (sInstance == null) {
                    sInstance = new ThreadPoolUtil();
                }
            }
        }
        return sInstance;
    }

    public static void resetThread(Thread thread, final String name, int priority, final ThreadDeliver deliver) {
        if(deliver != null) {
            thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    deliver.onError(name, e);
                }
            });
        }
        if (!TextUtils.isEmpty(name)) {
            thread.setName(name);
        }
        priority = Math.min(Thread.MAX_PRIORITY, Math.max(Thread.MIN_PRIORITY, priority));
        thread.setPriority(priority);
    }

    /**
     * 通过Executors.newFixedThreadPool()创建线程池
     * 线程数量固定的线程池，全部为核心线程，相应较快，不用担心线程被回收
     */
    public synchronized void initFixedPool() {
        if (mFixedPoolThread == null) {
            mFixedPoolThread = PoolThread.ThreadBuilder.createFixedPool().build();
        }
    }

    /**
     * 通过Executors.newSingleThreadPool()创建线程池
     * 只有一个核心线程，所有任务进来都要排队按顺序执行
     */
    public synchronized void initSinglePool() {
        if (mSinglePoolThread == null) {
            mSinglePoolThread = PoolThread.ThreadBuilder.createSingle().build();
        }
    }

    /**
     * 通过Executors.newCachedThreadPool()创建线程池
     * 线程数量无限多的线程池，都是非核心线程，适合执行大量耗时小的任务
     */
    public synchronized  void initCachePool() {
        if (mCachePoolThread == null) {
            mCachePoolThread = PoolThread.ThreadBuilder.createCachePool().build();
        }
    }

    /**
     * 通过Executors.newScheduledThreadPool()创建线程池
     * 有数量固定的核心线程，且有数量无限多的非核心线程，适合用于执行定时任务和固定周期的重复任务
     */
    public synchronized void initSchedulePool() {
        if (mSchedulePoolThread == null) {
            mSchedulePoolThread = PoolThread.ThreadBuilder.createScheduledPool().build();
        }
    }

    /**
     * 自定义线程池
     *
     * @param coreSize 核心线程数大小
     * @param maxSize 最大线程数
     * @param aliveTime 线程空闲后存活时间
     * @param timeUnit 存活时间单位
     * @param blockingQueue 线程队列
     * @param factory 生产线程工厂
     * @param handler 线程异常处理策略
     */
    public synchronized void initCustomPool(int coreSize, int maxSize, long aliveTime, TimeUnit timeUnit,
                               BlockingQueue blockingQueue, ThreadFactory factory, RejectedExecutionHandler handler) {
        if (mCustomPoolThread == null) {
            mCustomPoolThread = PoolThread.ThreadBuilder.createCustomPool().buildCustom(coreSize,
                    maxSize, aliveTime, timeUnit, blockingQueue, factory, handler);
        }
    }

    public PoolThread getFixedPool() {
        initFixedPool();
        return mFixedPoolThread;
    }
}
