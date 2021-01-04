package com.autopai.common.utils.thread.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public final class PoolDelayExecutor {

    private static ScheduledExecutorService mScheduled;
    private static PoolDelayExecutor mPool;

    private PoolDelayExecutor() {
        mScheduled = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("DelayExecutor");
                thread.setPriority(Thread.NORM_PRIORITY);
                return null;
            }
        });
    }

    public static synchronized PoolDelayExecutor getExecutor() {
        if (mPool == null) {
            mPool = new PoolDelayExecutor();
        }
        return mPool;
    }

    /**
     * 延迟执行线程
     *
     * @param delay
     * @param service
     * @param task
     */
    public void doExecutor(long delay, final ExecutorService service, final Runnable task) {
        if (delay == 0) {
            service.execute(task);
            return;
        }
        mScheduled.schedule(new Runnable() {
            @Override
            public void run() {
                service.execute(task);
            }
        }, delay, TimeUnit.MILLISECONDS);
    }
}
