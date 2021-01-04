package com.autopai.common.utils.thread.threadpool.bean;

import com.autopai.common.utils.thread.threadpool.IThreadCallBack;

public final class ThreadBean {
    /**
     * 线程优先级
     */
    public int mPriority;

    /**
     * 线程延迟执行时间
     */
    public long mDelay;

    /**
     * 线程名
     */
    public String mName;

    /**
     * 线程状态回调
     */
    public IThreadCallBack mCallBack;
}
