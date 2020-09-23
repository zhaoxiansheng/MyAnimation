package com.example.zy.myanimation.anim.demo;

import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimatorDemo {

    private static final String TAG = AnimatorDemo.class.toString();

    public static void singleAnimator(View view, String property) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, 180, 0);
        animator.start();

        //对比 必须使用setFillAfter使之停留
        Animation downAnim = new TranslateAnimation(0, 0, 0, 200);
        downAnim.setFillAfter(true);
        downAnim.setDuration(2000);
        view.startAnimation(downAnim);
    }

    // 将一个view在1s旋转并且向上平移
    public static void animators(View view) {
        //1.
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(view, "rotation", 0, 360);
        ObjectAnimator topYAnimator = ObjectAnimator.ofFloat(view, "translationY", 0, -500);

        //属性动画集
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotationAnimator, topYAnimator);
        animatorSet.setDuration(1000);
        animatorSet.start();
    }

    public static void animatorProperties(View view) {
        //2.
        PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofFloat("rotation", 0, 360);
        PropertyValuesHolder topYHolder = PropertyValuesHolder.ofFloat("translationY", 0, -500);
        ObjectAnimator prop = ObjectAnimator.ofPropertyValuesHolder(view, rotationHolder, topYHolder);
        prop.setDuration(1000);
        prop.start();
    }

    public static void animatorKeyFrame(View view) {
        //3. 如果需求变成 500ms旋转360  同时1000ms平移500
        Keyframe keyframe = Keyframe.ofFloat(0f, 0);
        Keyframe keyframe1 = Keyframe.ofFloat(0.5f, 360);
        Keyframe keyframe2 = Keyframe.ofFloat(1f, 360);

        Keyframe keyframe3 = Keyframe.ofFloat(0f, 0);
        Keyframe keyframe4 = Keyframe.ofFloat(1f, -500);

        PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofKeyframe("rotation", keyframe, keyframe1, keyframe2);
        PropertyValuesHolder topYHolder = PropertyValuesHolder.ofKeyframe("translationY", keyframe3, keyframe4);

        ObjectAnimator prop = ObjectAnimator.ofPropertyValuesHolder(view, rotationHolder, topYHolder);
        prop.setDuration(1000);
        prop.start();
    }

    public static void animatorKeyFrameInterpolator(View view) {
        //4. 如果需求变成 500ms旋转360 旋转是匀速  同时1000ms平移500 平移是减速
        Keyframe keyframe = Keyframe.ofFloat(0f, 0);
        Keyframe keyframe1 = Keyframe.ofFloat(0.5f, 360);
        keyframe.setInterpolator(new LinearInterpolator());
        Keyframe keyframe2 = Keyframe.ofFloat(1f, 360);

        Keyframe keyframe3 = Keyframe.ofFloat(0f, 0);
        Keyframe keyframe4 = Keyframe.ofFloat(1f, -500);
        keyframe4.setInterpolator(new DecelerateInterpolator());

        PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofKeyframe("rotation", keyframe, keyframe1, keyframe2);
        PropertyValuesHolder topYHolder = PropertyValuesHolder.ofKeyframe("translationY", keyframe3, keyframe4);

        ObjectAnimator prop = ObjectAnimator.ofPropertyValuesHolder(view, rotationHolder, topYHolder);
        prop.setDuration(1000);
        prop.start();
    }
}
