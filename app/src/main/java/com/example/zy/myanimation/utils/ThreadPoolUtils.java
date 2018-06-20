package com.example.zy.myanimation.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2018/6/20.
 *
 * @author zhaoy
 */
public class ThreadPoolUtils {

    private static ThreadPoolExecutor mThreadPoolExecutor;

    private ThreadPoolUtils() {
    }

    /**
     * @param corePoolSize    //核心线程数
     * @param maximumPoolSize //最大线程数
     * @param keepAliveTime   //保持时间
     * @param unit            //保持时间对应的单位
     * @param workQueue       线程池队列
     * @param threadFactory   //线程工厂
     * @param handler         //任务拒绝类型
     * @return 单例的线程池
     */
    public static ThreadPoolExecutor getInstance(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                                 BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        if (mThreadPoolExecutor == null) {
            synchronized (ThreadPoolUtils.class) {
                if (mThreadPoolExecutor == null) {

                    mThreadPoolExecutor = new ThreadPoolExecutor(
                            corePoolSize,
                            maximumPoolSize,
                            keepAliveTime,
                            unit,
                            workQueue,
                            threadFactory,
                            handler);
                }
            }
        }
        return mThreadPoolExecutor;
    }
}