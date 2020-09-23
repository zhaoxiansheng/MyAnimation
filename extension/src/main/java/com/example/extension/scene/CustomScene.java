package com.example.extension.scene;

import android.content.Context;
import android.transition.Scene;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class CustomScene extends LinearLayout {

    /**
     * 场景类型
     */
    private int mSceneType;
    /**
     * 页码数
     */
    private int mPage;

    private int mWeight;

    private ViewGroup mViewGroup;
    private int mLayout = -1;

    public CustomScene(Context context, int mSceneType) {
        super(context);
        this.mSceneType = mSceneType;
    }

    public CustomScene(Context context, AttributeSet attrs, int mSceneType) {
        super(context, attrs);
        this.mSceneType = mSceneType;
    }

    public CustomScene(Context context, AttributeSet attrs, int defStyleAttr, int mSceneType) {
        super(context, attrs, defStyleAttr);
        this.mSceneType = mSceneType;
    }

    private void init() {

    }

    public int getSceneType() {
        return mSceneType;
    }
}
