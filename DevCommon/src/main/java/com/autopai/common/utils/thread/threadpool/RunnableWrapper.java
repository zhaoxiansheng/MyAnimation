package com.autopai.common.utils.thread.threadpool;

import com.autopai.common.utils.thread.threadpool.bean.ThreadBean;
import com.autopai.common.utils.thread.threadpool.utils.ThreadPoolUtil;

public class RunnableWrapper implements Runnable {

    private ThreadBean mThreadBean;
    private Runnable mRunnable;
    private ThreadDeliver mThreadDeliver;

    public RunnableWrapper(ThreadBean threadBean, Runnable runnable) {
        mThreadBean = threadBean;
        mRunnable = runnable;
        mThreadDeliver = new ThreadDeliver(threadBean.mCallBack);
    }

    @Override
    public void run() {
        String name = mThreadBean.mName;
        int priority = mThreadBean.mPriority;
        Thread thread = Thread.currentThread();
        ThreadPoolUtil.resetThread(thread, name, priority, mThreadDeliver);
        mThreadDeliver.onStart(name);
        if (mRunnable != null) {
            mRunnable.run();
        }
        mThreadDeliver.onComplete(name);
    }
}
