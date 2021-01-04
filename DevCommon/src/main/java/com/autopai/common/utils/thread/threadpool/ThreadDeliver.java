package com.autopai.common.utils.thread.threadpool;

import com.autopai.common.utils.thread.HandlerThreadUtil;

public class ThreadDeliver implements IThreadCallBack {

    public IThreadCallBack mCallBack;

    public ThreadDeliver(IThreadCallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    public void onStart(final String name) {
        HandlerThreadUtil.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (mCallBack != null) {
                    mCallBack.onStart(name);
                }
            }
        });
    }

    @Override
    public void onComplete(final String name) {
        HandlerThreadUtil.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (mCallBack != null) {
                    mCallBack.onComplete(name);
                }
            }
        });
    }

    @Override
    public void onError(final String name, final Throwable throwable) {
        HandlerThreadUtil.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (mCallBack != null) {
                    mCallBack.onError(name, throwable);
                }
            }
        });
    }
}
