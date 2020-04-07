package com.example.zy.myanimation.view.lifecycle.scene;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class CustomScene extends RelativeLayout {

    /**
     * 场景类型
     */
    private int mSceneType;
    /**
     * 页码数
     */
    private int mPage;

    private int mWeight;

    public CustomScene(Context context, int mSceneType, int mPage) {
        super(context);
        this.mSceneType = mSceneType;
        this.mPage = mPage;
    }

    public CustomScene(Context context, AttributeSet attrs, int mSceneType, int mPage) {
        super(context, attrs);
        this.mSceneType = mSceneType;
        this.mPage = mPage;
    }

    public CustomScene(Context context, AttributeSet attrs, int defStyleAttr, int mSceneType, int mPage) {
        super(context, attrs, defStyleAttr);
        this.mSceneType = mSceneType;
        this.mPage = mPage;
    }

    private void init() {

    }

    public int getSceneType() {
        return mSceneType;
    }

    public int getPage() {
        return mPage;
    }
}
