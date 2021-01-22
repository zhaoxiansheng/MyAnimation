package com.example.leetcode;

import android.app.Application;

import androidx.multidex.MultiDex;

public class LeetcodeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);
    }
}
