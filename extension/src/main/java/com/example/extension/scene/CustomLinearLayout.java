package com.example.extension.scene;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.alion.mycanvas.server.SceneParam;
import com.example.extension.R;
import com.example.extension.ViewChangeInterface;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;


public class CustomLinearLayout extends LinearLayout implements ViewTreeObserver.OnGlobalLayoutListener, ViewChangeInterface {

    private static final String TAG = CustomLinearLayout.class.toString();

    // default values
    private final int DEF_ANIMATION_DURATION = 1000;
    private final int DEF_ANIMATION_TYPE = 0x0001;

    // current settings
    private int mAnimationDuration = DEF_ANIMATION_DURATION;
    private int mAnimationTypes = DEF_ANIMATION_TYPE;

    private SceneParam mSceneParam;

    private WindowManager.LayoutParams mWinParams;

    public String name;

    /**
     * 如果通过xml设置只能设置一个可变view
     */
    private View mUnUpdateView;
    private ArrayList<View> mUpdateViews;
    private HashMap<Integer, View> mUpdateViewMap;
    private int mUpdateViewId;
    private int mUnUpdateViewId;

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
                    if (mUpdateViewId != 0) {
                        mUpdateViews.add(findViewById(mUpdateViewId));
                    }
                } else if (attr == R.styleable.CustomLinearLayout_unUpdateView) {
                    mUnUpdateViewId = styledAttrs.getResourceId(R.styleable.CustomLinearLayout_unUpdateView, 0);
                    if (mUnUpdateViewId != 0) {
                        mUnUpdateView = findViewById(mUnUpdateViewId);
                    }
                }
            }
            styledAttrs.recycle();
        }
        init();
    }

    private void init() {
        mUpdateViews = new ArrayList<>();
        mUpdateViewMap = new HashMap<>();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        initUpdateMap();
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
        initUpdateMap();
    }

    public void setSceneParams(SceneParam sceneParam) {
        this.mSceneParam = sceneParam;
    }

    public void setWinParams(WindowManager.LayoutParams params) {
        this.mWinParams = params;
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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mWinParams != null && mSceneParam != null) {
            if (mUnUpdateView != null && mUpdateViewMap != null) {
                layoutUnUpdateView(l, t, r, b);
                layoutUpdateViews(l, t, r, b);
            } else {
                Log.d("zhaoy", "onLayout: " + name + ", " + l + ", " + r);
                super.onLayout(changed, l, t, r, b);
            }
        } else {
            Log.d("zhaoy", "onLayout: " + ", " + l + ", " + r);
            super.onLayout(changed, l, t, r, b);
        }
    }

    private void layoutUnUpdateView(int l, int t, int r, int b) {
        float weight = getAllViewWeightSum();
        CustomLinearLayout.LayoutParams mUnUpdateParams = (CustomLinearLayout.LayoutParams) mUnUpdateView.getLayoutParams();
        if (mSceneParam.getSceneType() == SceneParam.ACTION_EXPAND) {
            if (getUpdateView().get(0).getVisibility() == GONE) {
                mUnUpdateView.layout((int) (mWinParams.width / weight * unUpdateViewIndex()), t,
                        (int) (mWinParams.width / weight * (unUpdateViewIndex() + mUnUpdateParams.weight)), b); // 延迟变换的问题
            } else {
                mUnUpdateView.layout((int) (mWinParams.width / weight * unUpdateViewIndex()), t,
                        (int) (mWinParams.width / weight * (getBeforeViewWeight(mUnUpdateView) + mUnUpdateParams.weight)), b);

                Log.d("asdasdasdasdr", "onLayout: " + (int) (mWinParams.width / weight * unUpdateViewIndex()) +
                        ", " + (mWinParams.width / weight * (getBeforeViewWeight(mUnUpdateView) + mUnUpdateParams.weight)));

                Log.d("asdasdasdasdr", "onLayout 11 : " + mWinParams.width + "，" + weight + "，" + unUpdateViewIndex() +
                        ", " + getBeforeViewWeight(mUnUpdateView) + "，" + mUnUpdateParams.weight);
            }
        } else {
            if (mSceneParam.getWinWidth() != mWinParams.width) {
                mUnUpdateView.layout((int) (mWinParams.width / weight * unUpdateViewIndex()), t,
                        (int) (mWinParams.width / getVisibleViewWeightSum() * (getBeforeViewWeight(mUnUpdateView) + mUnUpdateParams.weight)), b);
                Log.d("asdasdasdasdr", "onLayout Fold  : " + mWinParams.width + "，" + weight + "，" + unUpdateViewIndex() +
                        ", " + getBeforeViewWeight(mUnUpdateView) + "，" + mUnUpdateParams.weight + ", " + getVisibleViewWeightSum());
            } else {
                mUnUpdateView.layout(0, t, mWinParams.width, b);
            }
        }
    }

    private void layoutUpdateViews(int l, int t, int r, int b) {
        float weight = getVisibleViewWeightSum();
        for (int i : mUpdateViewMap.keySet()) {
            if (mUpdateViewMap.get(i) != null) {
                View view = mUpdateViewMap.get(i);
                if (mSceneParam.getSceneType() == SceneParam.ACTION_EXPAND) {
                    if (view.getVisibility() != GONE) {
                        CustomLinearLayout.LayoutParams params = (CustomLinearLayout.LayoutParams) view.getLayoutParams();
                        view.layout((int) (mWinParams.width / weight * getBeforeViewWeight(view)), t,
                                (int) (mWinParams.width / weight * (getBeforeViewWeight(view) + params.weight)), b);
                        Log.d("asdasdasdasdr", "onLayout: " + name + ", " + l + ", " + r);
                    }
                }
            }
        }
    }

    private int unUpdateViewIndex() {
        if (mUnUpdateView != null) {
            for (int i = 0; i < getChildCount(); i++) {
                if (mUnUpdateView.hashCode() == getChildAt(i).hashCode()) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    private void initUpdateMap() {
        if (mUpdateViews != null && mUnUpdateView != null) {
            for (int i = 0; i < getChildCount(); i++) {
                if (mUnUpdateView.hashCode() != getChildAt(i).hashCode()) {
                    mUpdateViewMap.put(i, getChildAt(i));
                }
            }
        }
    }

    /**
     * 获取所有显示view的权重
     *
     * @return
     */
    private float getVisibleViewWeightSum() {
        float weightSum = 0;
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i).getVisibility() == VISIBLE) {
                CustomLinearLayout.LayoutParams params = (CustomLinearLayout.LayoutParams) getChildAt(i).getLayoutParams();
                weightSum += params.weight;
            }
        }
        return weightSum;
    }

    /**
     * 获取所有view的权重
     *
     * @return
     */
    private float getAllViewWeightSum() {
        float weightSum = 0;
        for (int i = 0; i < getChildCount(); i++) {
            CustomLinearLayout.LayoutParams params = (CustomLinearLayout.LayoutParams) getChildAt(i).getLayoutParams();
            weightSum += params.weight;
        }
        return weightSum;
    }


    /**
     * 获取当前view之前的所有权重之和
     *
     * @param v
     * @return
     */
    private float getBeforeViewWeight(View v) {
        float weightNum = 0;
        if (v != null) {
            int index = indexOfChild(v);
            for (int i = 0; i < index; i++) {
                if (getChildAt(i).getVisibility() != View.GONE) {
                    float weight = getViewWeight(getChildAt(i));
                    if (weight != -1) {
                        weightNum += weight;
                    }
                }
            }
        }
        return weightNum;
    }

    /**
     * 获取当前view的权重
     *
     * @param view
     * @return
     */
    private float getViewWeight(View view) {
        if (view != null) {
            CustomLinearLayout.LayoutParams params = (CustomLinearLayout.LayoutParams) view.getLayoutParams();
            return params.weight;
        }
        return -1;
    }

}
