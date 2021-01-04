package com.autopai.common.utils.thread.threadpool.utils;

public interface IRejectPolicy {
    void doReject(IDispatchWorkQueue.Work work);
}
