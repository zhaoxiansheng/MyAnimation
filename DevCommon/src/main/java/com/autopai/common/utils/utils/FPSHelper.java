package com.autopai.common.utils.utils;

import android.os.SystemClock;
import android.util.Log;

public class FPSHelper {
    private static final String TAG = "FPSHelper";
    private long mPrevTime = -1;
    private long mLastTime = -1;
    private int mFrameNum = 0;
    private int mFps = Integer.MAX_VALUE;

    private long mFpsStartTime = -1;
    private long mFpsPrevTime = -1;
    private int mFpsNumFrames;

    public FPSHelper(int fps) {
        mFps = fps;
    }

    public FPSHelper() {
        mFps = Integer.MAX_VALUE;
    }

    public void postDraw() {
        if (mPrevTime < 0) {
            mPrevTime = SystemClock.uptimeMillis();
        }
        if (mLastTime > 0) {
            long sleepTime = (long) ((float) 1000 / mFps - (float) (SystemClock.uptimeMillis() - mLastTime) + 0.5f);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        mLastTime = SystemClock.uptimeMillis();

        ++mFrameNum;
        long duration;
        if ((duration = mLastTime - mPrevTime) > 999) {
            float fps = (float) mFrameNum / duration * 1000;
            Log.e(TAG, "fps: " + fps);
            mFrameNum = 0;
            mPrevTime = SystemClock.uptimeMillis();
        }
    }

    // Called from draw() when DEBUG_FPS is enabled
    private void trackFPS() {
        long nowTime = System.currentTimeMillis();
        if (mFpsStartTime < 0) {
            mFpsStartTime = mFpsPrevTime = nowTime;
            mFpsNumFrames = 0;
        } else {
            ++mFpsNumFrames;
            String thisHash = Integer.toHexString(System.identityHashCode(this));
            long frameTime = nowTime - mFpsPrevTime;
            long totalTime = nowTime - mFpsStartTime;
            Log.v(TAG, "0x" + thisHash + "\tFrame time:\t" + frameTime);
            mFpsPrevTime = nowTime;
            if (totalTime > 1000) {
                float fps = (float) mFpsNumFrames * 1000 / totalTime;
                Log.v(TAG, "0x" + thisHash + "\tFPS:\t" + fps);
                mFpsStartTime = nowTime;
                mFpsNumFrames = 0;
            }
        }
    }
}
