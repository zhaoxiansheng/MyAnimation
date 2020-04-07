package com.example.zy.myanimation.view.lifecycle;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class CustomImageView extends ImageView {

    private static final String TAG = CustomImageView.class.toString();

    private int width;
    private int height;
    private int widthMode;
    private int heightMode;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: " + widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout left : " + left + ", right: " + right);
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: ");
        super.onDraw(canvas);
    }

    @Override
    public void getLocationOnScreen(int[] outLocation) {
        Log.d(TAG, "getLocationOnScreen: " + outLocation[0] + ", " + outLocation[1]);
        super.getLocationOnScreen(outLocation);
    }

    @Override
    public void getLocationInWindow(int[] outLocation) {
        Log.d(TAG, "getLocationInWindow: " + outLocation[0] + ", " + outLocation[1]);
        super.getLocationInWindow(outLocation);
    }
}
