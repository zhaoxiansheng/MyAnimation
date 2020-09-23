package com.example.extension.animation;

import android.os.Build;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;

public class AnimationFactory {

    private static final String TAG = AnimationFactory.class.toString();

    private final int ORDERING_TOGETHER = TransitionSet.ORDERING_TOGETHER;
    private final int ORDERING_SEQUENTIAL = TransitionSet.ORDERING_SEQUENTIAL;
    private int DEFAULT_ORDERING = ORDERING_TOGETHER;

    private int mAnimationType = -1;
    private int mDuration = 200;

    private Fade fade = null;
    private Slide slide = null;
    private AutoTransition autoTransition = null;
    private ChangeBounds changeBounds = null;

    private boolean mPlayTogether = true;

    /**
     * true expand
     */
    private boolean mType;

    public AnimationFactory() {
    }

    public void setAnimationType(int mType) {
        this.mAnimationType = mType;
        filterType();
    }

    public AnimationFactory setAnimationOrder(int order) {
        switch (order) {
            case ORDERING_TOGETHER:
                DEFAULT_ORDERING = ORDERING_TOGETHER;
                break;
            case ORDERING_SEQUENTIAL:
                DEFAULT_ORDERING = ORDERING_SEQUENTIAL;
                break;
            default:
                throw new AndroidRuntimeException("Invalid parameter for Animation order: " + order);
        }
        return this;
    }

    public int getAnimationOrder() {
        return mPlayTogether ? ORDERING_TOGETHER : ORDERING_SEQUENTIAL;
    }

    public AnimationFactory setDuration(int duration) {
        if (duration >= 0) {
            this.mDuration = duration;
        } else {
            throw new AndroidRuntimeException("Animation duration is not correct : " + duration);
        }
        return this;
    }

    public void setIsFold(boolean isFold) {
        mType = isFold;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public TransitionSet getCurrentAnimation() {
        TransitionSet set = new TransitionSet();

        if (autoTransition != null) {
            set.addTransition(autoTransition);
        }

        if (fade != null) {
            set.addTransition(fade);
        }

        if (slide != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                set.addTransition(slide);
            }
        }

        if (changeBounds != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                set.addTransition(changeBounds);
            }
        }

        return set.setDuration(mDuration)
                .setOrdering(DEFAULT_ORDERING);
    }

    private void filterType() {
        if ((mAnimationType & AnimationType.AUTO_TRANSITION) == AnimationType.AUTO_TRANSITION) {
            autoTransition = new AutoTransition();
        }

        if ((mAnimationType & AnimationType.FADE) == AnimationType.FADE) {
            fade = new Fade();
        }

        if ((mAnimationType & AnimationType.SLIDE) == AnimationType.SLIDE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                slide = new Slide();
            }
        }

        if ((mAnimationType & AnimationType.CHANGE_BOUNDS) == AnimationType.CHANGE_BOUNDS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                changeBounds = new ChangeBounds();
            }
        }
    }

    public void changeScene(View unUpdateView, ArrayList<View> views) {
        if (mAnimationType == AnimationType.FADE) {
            if (views != null && views.size() > 0) {
                changeVisibility(views);
            }
            if (unUpdateView != null) {
                unUpdateView.setVisibility(View.VISIBLE);
            }
        }
        if (mAnimationType == AnimationType.CHANGE_BOUNDS) {
            if (views != null && views.size() > 0) {
                changeBounds(views);
            }
        }
        if (mAnimationType == AnimationType.CHANGE_TRANSFORM) {
            if (views != null && views.size() > 0) {
                changeTransform(views);
            }
        }
    }

    /**
     * VISIBLE和INVISIBLE状态切换 * @param views
     *
     * @param views
     */
    private void changeVisibility(ArrayList<View> views) {
        for (View view : views) {
            if (view != null) {
                Log.d(TAG, "changeVisibility start : " + view.getVisibility());
                view.setVisibility(view.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                Log.d(TAG, "changeVisibility end : " + view.getVisibility());
            }
        }
    }

    private void changeBounds(ArrayList<View> views) {
        for (View view : views) {
            if (!mType) {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = layoutParams.width / 2;
                view.setLayoutParams(layoutParams);
            } else {
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = layoutParams.width * 2;
                view.setLayoutParams(layoutParams);
            }
        }
    }

    private void changeTransform(ArrayList<View> views) {
        for (View view : views) {
            if (!mType) {
                float pos = view.getRotation();
                view.setRotation(pos + 90);
            } else {
                float pos = view.getRotation();
                view.setRotation(pos - 90);
            }
        }
    }
}
