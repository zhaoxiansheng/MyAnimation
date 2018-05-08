package com.example.zy.myanimation.anim;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Transformation;

public class MyAnimation extends Animation {

    private Camera camera;
    private int mCenterWidth;
    private int mCenterHeight;

    private float mRotateY;

    public MyAnimation(float mRotateY) {
        this.mRotateY = mRotateY;
        camera = new Camera();
    }

    public MyAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        camera = new Camera();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        setDuration(2000);
        setFillAfter(true);
        setInterpolator(new BounceInterpolator());
        mCenterWidth = width / 2;
        mCenterHeight = height / 2;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final Matrix matrix = t.getMatrix();
        camera.save();
        camera.rotateY(mRotateY * interpolatedTime);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(mCenterWidth, mCenterHeight);
        matrix.preTranslate(-mCenterWidth, -mCenterHeight);
    }
}
