package com.example.zy.myanimation.view.recycler.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class CustomImageView extends ImageView {

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mOnDrawableStateChangedListener != null) {
            if (isPressed()) {
                mOnDrawableStateChangedListener.onDrawableStateChanged((float) 0.6);
            } else {
                mOnDrawableStateChangedListener.onDrawableStateChanged(1);
            }

        }
    }

    private onDrawableStateChangedListener mOnDrawableStateChangedListener;

    public void setOnDrawableStateChangedListener(onDrawableStateChangedListener onDrawableStateChangedListener) {
        this.mOnDrawableStateChangedListener = onDrawableStateChangedListener;
    }

    public interface onDrawableStateChangedListener {
        void onDrawableStateChanged(float alpha);
    }
}
