package com.example.zy.myanimation.view.lifecycle.animation;

import android.os.Build;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;

import static android.transition.TransitionSet.ORDERING_TOGETHER;

public class AnimationFactory {

    private static final String TAG = AnimationFactory.class.toString();

    private int mType;
    private int mDuration = 200;

    private Transition mCurrentAnimation;

    public AnimationFactory() {
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Transition getCurrentAnimation() {
        TransitionSet set = new TransitionSet();
        Fade fade = null;
        Slide slide = null;
        if ((mType & AnimationType.FADE) == AnimationType.FADE) {
            fade = new Fade();
        }
        if ((mType & AnimationType.SLIDE) == AnimationType.SLIDE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                slide = new Slide();
            }
        }
        if (fade != null) {
            set.addTransition(fade);
        }
        if (slide != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                set.addTransition(slide);
            }
        }
        mCurrentAnimation = set.setDuration(mDuration)
                .setOrdering(ORDERING_TOGETHER);
        return mCurrentAnimation;
    }

    public void changeScene(View unUpdateView, ArrayList<View> views) {
        changeVisibility(views);
        unUpdateView.setVisibility(View.VISIBLE);
    }

    /**
     * VISIBLE和INVISIBLE状态切换 * @param views
     * @param views
     */
    private void changeVisibility(ArrayList<View> views) {
        for (View view : views) {
            view.setVisibility(view.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        }
    }
}
