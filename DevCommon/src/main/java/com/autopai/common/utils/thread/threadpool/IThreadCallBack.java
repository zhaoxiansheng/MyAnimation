package com.autopai.common.utils.thread.threadpool;

public interface IThreadCallBack {
    /**
     * 线程开始
     *
     * @param name 线程名
     */
    void onStart(String name);

    /**
     * 线程结束
     *
     * @param name 线程名
     */
    void onComplete(String name);

    /**
     * 线程出错
     *
     * @param name 线程名
     * @param throwable 异常
     */
    void onError(String name, Throwable throwable);
}
