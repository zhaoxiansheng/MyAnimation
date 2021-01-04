package com.autopai.common.callback;

import android.content.Intent;

public interface HostContext {
    interface ActivityRequestCb{
        void onActivityRequestByHost(int requestCode, int resultCode, Intent data);
    }

    boolean isPaused();
    void registerLauncherCallback(LauncherCallback callback);
    void unRegisterLauncherCallback(LauncherCallback callback);
    void dispatchStart();
    void dispatchResume();
    void dispatchPause();
    void dispatchStop();
    void dispatchDestory();

    void setActivityRequestCb(HostContext.ActivityRequestCb cb);
    boolean onActivityRequestByHost(int requestCode, int resultCode, Intent data);
}
