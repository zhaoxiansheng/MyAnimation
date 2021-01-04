package com.autopai.common.utils.anim;

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


    public ViewReSizeAnimation(View view, float scaleW, float scaleH) {
        mView = new WeakReference<>(view);
        mScaleW = scaleW;
        mScaleH = scaleH;
    }

    public ViewReSizeAnimation(View view, int targetW, int targetH) {
        mView = new WeakReference<>(view);
        targetWidth = targetW;
        targetHeight = targetH;
    }

    public void addListener(AnimationListener listener){
        if(mListeners == null){
            mListeners = new ArrayList<>(1);
        }
        mListeners.add(listener);
    }

    public void removeListener(AnimationListener listener) {
        if(mListeners != null) {
            mListeners.remove(listener);
        }
    }

    private void notifyStart(){
        if(mListeners != null) {
            for (AnimationListener listener : mListeners) {
                listener.onAnimationStart(this);
            }
        }
    }

    private void notifyEnd(){
        if(mListeners != null) {
            for (AnimationListener listener : mListeners) {
                listener.onAnimationEnd(this);
            }
        }
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if(!mEnded) {
            int newW = initialWidth + (int) ((targetWidth - initialWidth) * interpolatedTime);
            int newH = initialHeight + (int) ((targetHeight - initialHeight) * interpolatedTime);
            if (newW != initialWidth || newH != initialHeight) {
                View view = mView.get();
                if (view != null) {
                    view.getLayoutParams().width = newW;
                    view.getLayoutParams().height = newH;
                    view.requestLayout();
                }
                Log.e(TAG, this.hashCode() + " KKLMQ ResizeAnimation ScreenItem onAnimationEnd: " + view.getLayoutParams().width + "x" + view.getLayoutParams().height);
            }
            if (newW == targetWidth && newH == targetHeight) {
                mEnded = true;
                notifyEnd();
            }
        }
    }


    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        this.initialHeight = height;
        this.initialWidth = width;
        super.initialize(width, height, parentWidth, parentHeight);
    }


    @Override
    public boolean willChangeBounds() {
        return true;
    }

    public void startAnimation(int duration){
        this.setDuration(duration);
        mEnded = false;
        View view = mView.get();
        if(view != null) {
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

    public Point getTargetSize(){
        return  new Point(targetWidth, targetHeight);
    }

    public Point getInitTargetSize() {
        return new Point(initialWidth, initialHeight);
    }

    public WeakReference<View> getView() {
        return mView;
    }
}
