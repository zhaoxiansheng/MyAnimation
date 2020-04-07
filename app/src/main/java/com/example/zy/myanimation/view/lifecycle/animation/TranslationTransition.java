package com.example.zy.myanimation.view.lifecycle.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@TargetApi(21)
public class TranslationTransition extends Visibility {

    public static final int CENTER = 0;

    private TimeInterpolator timeInterpolator;
    private float values[];
    private int mode;

    public TranslationTransition(final TimeInterpolator timeInterpolator, float... values) {
        this.values = values;
        this.timeInterpolator = timeInterpolator;
    }

    public TranslationTransition(int mode) {
        this.mode = mode;
    }

    public TranslationTransition(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    private void captureValues(final TransitionValues transitionValues) {
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
        int width = 0;
        if (mode == CENTER) {
            width = sceneRoot.getRootView().getWidth() / 2;
        }
        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, values);
        translationX.setInterpolator(timeInterpolator);
        return translationX;
    }
}
