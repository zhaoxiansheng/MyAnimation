package com.example.zy.myanimation.view.lifecycle;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.view.lifecycle.scene.SceneParam;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class CustomLinearLayout extends LinearLayout implements ViewTreeObserver.OnGlobalLayoutListener, ViewChangeInterface {

    private static final String TAG = CustomLinearLayout.class.toString();

    private int mCurrentWidth = -1;
    private int mCurrentHeight = -1;
    private int mLastWidth = -1;
    private int mLastHeight = -1;

    // default values
    private final int DEF_ANIMATION_DURATION = 1000;
    private final int DEF_ANIMATION_TYPE = 0;

    // current settings
    private int mAnimationDuration = DEF_ANIMATION_DURATION;
    private int mAnimationTypes = DEF_ANIMATION_TYPE;

    private SceneParam mSceneParam;

    /**
     * 如果通过xml设置只能设置一个可变view
     */
    private View mUnUpdateView;
    private ArrayList<View> mUpdateViews;
    private int mUpdateViewId;
    private int mUnUpdateViewId;

    private ViewOnLayoutListener mOnViewLayoutListener;

    private boolean isFirst;

    public CustomLinearLayout(Context context) {
        this(context, null);
    }

    public CustomLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.CustomLinearLayout);
        if (styledAttrs != null) {
            final int count = styledAttrs.getIndexCount();
            for (int i = 0; i < count; ++i) {
                int attr = styledAttrs.getIndex(i);
                if (attr == R.styleable.CustomLinearLayout_animationDuration) {
                    this.mAnimationDuration = styledAttrs.getInt(R.styleable.CustomLinearLayout_animationDuration, DEF_ANIMATION_DURATION);
                } else if (attr == R.styleable.CustomLinearLayout_animationType) {
                    mAnimationTypes = styledAttrs.getInteger(R.styleable.CustomLinearLayout_animationType, DEF_ANIMATION_TYPE);
                } else if (attr == R.styleable.CustomLinearLayout_updateView) {
                    mUpdateViewId = styledAttrs.getResourceId(R.styleable.CustomLinearLayout_updateView, 0);
                } else if (attr == R.styleable.CustomLinearLayout_unUpdateView) {
                    mUnUpdateViewId = styledAttrs.getResourceId(R.styleable.CustomLinearLayout_unUpdateView, 0);
                }
            }
            styledAttrs.recycle();
        }
        init();
    }

    private void init() {
        mUpdateViews = new ArrayList<>();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private void unInit() {
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unInit();
    }

    @Override
    public void onGlobalLayout() {
        if (mCurrentWidth == -1 && mCurrentHeight == -1) {
            mCurrentWidth = mLastWidth = getWidth();
            mCurrentHeight = mLastHeight = getHeight();
            if (this.findViewById(mUpdateViewId) != null) {
                mUpdateViews.add(findViewById(mUpdateViewId));
            }
            if (this.findViewById(mUnUpdateViewId) != null) {
                mUnUpdateView = findViewById(mUnUpdateViewId);
            }
        } else {
            mCurrentWidth = getWidth();
            mCurrentHeight = getHeight();
            if (mCurrentWidth != mLastWidth || mCurrentHeight != mLastHeight) {
                mLastWidth = mCurrentWidth;
                mLastHeight = mCurrentHeight;
                if (mOnViewLayoutListener != null) {
                    Log.e("zhaoy", "onGlobalLayout: 111111");
                    mOnViewLayoutListener.onGlobalLayoutListener();
                }
            }
        }
    }

    public void setSceneParam(SceneParam sceneParam) {
        this.mSceneParam = sceneParam;
    }

    @Override
    public void update() {
    }

    @Override
    public void recover() {
    }

    @Override
    public int getAnimationDuration() {
        return mAnimationDuration;
    }

    public int getAnimationTypes() {
        return mAnimationTypes;
    }

    @Override
    public View getUnUpdateView() {
        return mUnUpdateView;
    }

    @Override
    public ArrayList<View> getUpdateView() {
        return mUpdateViews;
    }

    @Override
    public SceneParam getSceneParam() {
        return mSceneParam;
    }

    public void setAnimationDuration(int mAnimationDuration) {
        this.mAnimationDuration = mAnimationDuration;
    }

    public void setAnimationTypes(int mAnimationTypes) {
        this.mAnimationTypes = mAnimationTypes;
    }

    public void setUnUpdateView(View mUnUpdateView) {
        this.mUnUpdateView = mUnUpdateView;
    }

    public void setUpdateView(ArrayList<View> mUpdateView) {
        this.mUpdateViews = mUpdateView;
    }

    public int getLastWidth() {
        return mLastWidth;
    }

    public int getLastHeight() {
        return mLastHeight;
    }

    public int getCurrentWidth() {
        return mCurrentWidth;
    }

    public int getCurrentHeight() {
        return mCurrentHeight;
    }

    public void setOnViewLayoutListener(ViewOnLayoutListener viewOnGlobalLayoutListener) {
        this.mOnViewLayoutListener = viewOnGlobalLayoutListener;
    }
}
