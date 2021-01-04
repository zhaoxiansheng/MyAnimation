package com.autopai.common.utils.thread.threadpool;

import java.util.concurrent.ThreadFactory;

public class MyThreadFactory implements ThreadFactory {

    public int mPriority;

    public MyThreadFactory(int priority) {
        mPriority = priority;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setPriority(mPriority);
        return thread;
    }
}
