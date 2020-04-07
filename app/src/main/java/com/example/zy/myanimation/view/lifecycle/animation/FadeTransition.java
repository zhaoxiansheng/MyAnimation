package com.example.zy.myanimation.view.lifecycle.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@TargetApi(21)
public class FadeTransition extends Transition {

    private static final String PROPNAME_BACKGROUND = "android:zhaoy:background";
    private static final String PROPNAME_TEXT_COLOR = "android:zhaoy:textColor";
    private static final String PROPNAME_ALPHA = "android:zhaoy:alpha";

    private TimeInterpolator timeInterpolator;
    private float values[];

    public FadeTransition(final TimeInterpolator timeInterpolator, float... values) {
        this.values = values;
        this.timeInterpolator = timeInterpolator;
    }

    public FadeTransition(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 记录rootView所需要的值
     * @param transitionValues
     */
    private void captureValues(final TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_BACKGROUND, transitionValues.view.getBackground());
        transitionValues.values.put(PROPNAME_ALPHA, transitionValues.view.getAlpha());
        if (transitionValues.view instanceof TextView) {
            transitionValues.values.put(PROPNAME_TEXT_COLOR, ((TextView) transitionValues.view).getCurrentTextColor());
        }
    }

    @Override
    public void captureStartValues(final TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(final TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @SuppressLint("NewApi")
    @Override
    public Animator createAnimator(final ViewGroup sceneRoot, final TransitionValues startValues,
                                   final TransitionValues endValues) {

        View view = endValues.view;

        ObjectAnimator fade = ObjectAnimator.ofFloat(view, View.ALPHA, values);
        fade.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mUpdateAnimation.onAnimationUpdate(valueAnimator);
            }
        });
        fade.setInterpolator(timeInterpolator);
        return fade;
    }

    private UpdateAnimation mUpdateAnimation;

    public void setUpdateAnimation(UpdateAnimation mUpdateAnimation) {
        this.mUpdateAnimation = mUpdateAnimation;
    }

    public interface UpdateAnimation {
        void onAnimationUpdate(ValueAnimator valueAnimator);
    }
}
