package com.example.zy.myanimation.view.lifecycle.scene;

import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.zy.myanimation.view.lifecycle.CustomLinearLayout;
import com.example.zy.myanimation.view.lifecycle.ViewOnLayoutListener;
import com.example.zy.myanimation.view.lifecycle.animation.AnimationFactory;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;

public class CustomChoreographer<T extends ViewGroup> implements ViewOnLayoutListener {

    private static final String TAG = CustomChoreographer.class.toString();

    private WindowManager.LayoutParams mWindowParams;

    private AnimationFactory mAnimationFactory;
    private Transition mTransition;

    private T mLayout;

    private SceneParam mSceneParam;
    private SceneParam mLastSceneParam;

    private View mUnUpdateView;
    private ArrayList<View> mUpdateViews;

    private boolean isFolded;

    public CustomChoreographer() {
        mAnimationFactory = new AnimationFactory();
        getSceneParams();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void play() {
        if (mUnUpdateView == null) {
            getUnUpdateView();
        }
        if (mUpdateViews == null || mUpdateViews.size() == 0) {
            getUpdateView();
        }
        calculateUnUpdateViewLocation();

    }

    private void setOnViewLayoutListener() {
        if (mLayout != null) {
            if (mLayout instanceof CustomLinearLayout) {
                ((CustomLinearLayout) mLayout).setOnViewLayoutListener(this);
            }
        }
    }

    private void getUpdateView() {
        if (mLayout != null) {
            if (mLayout instanceof CustomLinearLayout) {
                mUpdateViews = ((CustomLinearLayout) mLayout).getUpdateView();
            }
        }
    }

    private void getUnUpdateView() {
        if (mLayout != null) {
            if (mLayout instanceof CustomLinearLayout) {
                mUnUpdateView = ((CustomLinearLayout) mLayout).getUnUpdateView();
            }
        }
    }

    private void getSceneParams() {
        if (mLayout != null) {
            if (mLayout instanceof CustomLinearLayout) {
                mSceneParam = ((CustomLinearLayout) mLayout).getSceneParam();
            }
        }
    }

    private void getAnimationType() {
        if (mLayout != null) {
            if (mLayout instanceof CustomLinearLayout) {
                mAnimationFactory.setType(((CustomLinearLayout) mLayout).getAnimationTypes());
            }
        }
    }

    private void getAnimationDuration() {
        if (mLayout != null) {
            if (mLayout instanceof CustomLinearLayout) {
                mAnimationFactory.setDuration(((CustomLinearLayout) mLayout).getAnimationDuration());
            }
        }
    }

    public void setWindowParams(WindowManager.LayoutParams mWindowParams) {
        this.mWindowParams = mWindowParams;
    }

    public void setLayout(T mLayout) {
        if (mLayout != null) {
            this.mLayout = mLayout;
            setOnViewLayoutListener();
            getAnimationDuration();
            getAnimationType();
        }
    }

    /**
     * 计算不变化的view的位置
     */
    private void calculateUnUpdateViewLocation() {
        if (mUnUpdateView == null) {
            getUnUpdateView();
        }
        if (mUpdateViews == null || mUpdateViews.size() == 0) {
            getUpdateView();
        }
        if (mSceneParam != null) {
            mLastSceneParam = mSceneParam;
            getSceneParams();
        }
        gravityLeft();
    }

    private void gravityLeft() {
        if (mWindowParams.gravity == Gravity.LEFT || mWindowParams.gravity == Gravity.START) {
            if (((CustomLinearLayout) mLayout).getOrientation() == LinearLayout.HORIZONTAL) {
                if (!isFolded) {
                    CustomLinearLayout.LayoutParams params = (CustomLinearLayout.LayoutParams) mUnUpdateView.getLayoutParams();
                    float weight = params.weight;
                    int width = (int) (mLayout.getWidth() / getViewWeightSum() * weight);
                    int location = calculateView(mUnUpdateView);
                    mUnUpdateView.setLeft(location);
                    mUnUpdateView.setRight(location + width);
                } else {
                    mUnUpdateView.setLeft(0);
                    mUnUpdateView.setRight(mLayout.getWidth());
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onGlobalLayoutListener() {
        play();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void changeScene(boolean isFold) {
        isFolded = isFold;
        if (mUnUpdateView == null) {
            getUnUpdateView();
        }
        if (mUpdateViews == null || mUpdateViews.size() == 0) {
            getUpdateView();
        }
        mTransition = mAnimationFactory.getCurrentAnimation();
        if (!isFold) {
            TransitionManager.beginDelayedTransition(mLayout, mTransition);
        }
        mAnimationFactory.changeScene(mUnUpdateView, mUpdateViews);
    }

    /**
     * 获取所有view的权重
     *
     * @return
     */
    private float getViewWeightSum() {
        float weightSum = 0;
        if (mLayout != null) {
            for (int i = 0; i < mLayout.getChildCount(); i++) {
                CustomLinearLayout.LayoutParams params = (CustomLinearLayout.LayoutParams) mLayout.getChildAt(i).getLayoutParams();
                weightSum += params.weight;
            }
        }
        return weightSum;
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

    /**
     * 获取当前view之前的所有权重之和
     *
     * @param v
     * @return
     */
    private float getBeforeViewWeight(View v) {
        int weight = 0;
        if (mLayout != null && v != null) {
            int index = mLayout.indexOfChild(v);
            for (int i = 0; i < index; i++) {
                weight += getViewWeight(mLayout.getChildAt(i));
            }
        }
        return weight;
    }

    /**
     * 计算view的边界 left 或者 top
     *
     * @param v
     * @return
     */
    private int calculateView(View v) {
        float beforeWeightSum;
        int location = 0;
        if (mLayout != null && v != null) {
            beforeWeightSum = getBeforeViewWeight(v);
            if (((CustomLinearLayout) mLayout).getOrientation() == LinearLayout.HORIZONTAL) {
                location = (int) (mLayout.getWidth() / getViewWeightSum() * beforeWeightSum);
            } else {
                location = (int) (mLayout.getHeight() / getViewWeightSum() * beforeWeightSum);
            }
        }
        return location;
    }
}
