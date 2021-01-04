package com.autopai.common.callback;

public interface LauncherCallback {
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestory();
}
