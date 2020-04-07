package com.example.zy.myanimation.view.lifecycle;

import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ViewReSizeAnimation extends Animation {
    private static final String TAG = "ViewReSizeAnimation";
    int initialHeight;
    int targetHeight;
    int initialWidth;
    int targetWidth;
    WeakReference<View> mView;
    float mScaleW = -1f;
    float mScaleH = -1f;
    boolean mEnded;
    ArrayList<AnimationListener> mListeners;

    private int targetLeft;
    private int initLeft;
    private boolean isStart;

    public ViewReSizeAnimation(View view, float scaleW, float scaleH) {
        mView = new WeakReference<>(view);
        mScaleW = scaleW;
        mScaleH = scaleH;
    }

    public ViewReSizeAnimation(View view, int left) {
        mView = new WeakReference<>(view);
        initLeft = left;
    }

    public ViewReSizeAnimation(View view, int targetW, int targetH) {
        mView = new WeakReference<>(view);
        targetWidth = targetW;
        targetHeight = targetH;
    }

    public void addListener(AnimationListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>(1);
        }
        mListeners.add(listener);
    }

    public void removeListener(AnimationListener listener) {
        if (mListeners != null) {
            mListeners.remove(listener);
        }
    }

    private void notifyStart() {
        if (mListeners != null) {
            for (AnimationListener listener : mListeners) {
                listener.onAnimationStart(this);
            }
        }
    }

    private void notifyEnd() {
        if (mListeners != null) {
            for (AnimationListener listener : mListeners) {
                listener.onAnimationEnd(this);
            }
        }
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if (mEnded) {
            return;
        }

        int newLeft = (int) (initLeft + ((targetLeft - initLeft) * interpolatedTime));
        if (newLeft != initLeft) {
            View view = mView.get();
            if (view != null) {
                view.setLeft(newLeft);
                view.requestLayout();
                Log.e(TAG, "zhaoy left: " + view.getLeft());
            }
        }

        if (newLeft >= targetLeft) {
            mEnded = true;
            notifyEnd();
            isStart = false;
        }
    }


    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }


    @Override
    public boolean willChangeBounds() {
        return true;
    }

    public void startAnimation(int duration) {
        this.setDuration(duration);
        mEnded = false;
        View view = mView.get();
        if (view != null) {
            initialWidth = view.getWidth();
            initialHeight = view.getHeight();
            if (mScaleW > -1 && mScaleH > -1) {
                targetWidth = (int) (initialWidth * mScaleW);
                targetHeight = (int) (initialHeight * mScaleH);
            }
            view.startAnimation(this);
        }
        notifyStart();
    }

    public void startXAnimation(int duration, int targetLeft) {
        this.setDuration(duration);
        if (!isStart) {
            mEnded = false;
            View view = mView.get();
            if (view != null) {
                this.targetLeft = targetLeft;
                view.startAnimation(this);
                isStart = true;
            }
            notifyStart();
        }
    }

    public Point getTargetSize() {
        return new Point(targetWidth, targetHeight);
    }
}
