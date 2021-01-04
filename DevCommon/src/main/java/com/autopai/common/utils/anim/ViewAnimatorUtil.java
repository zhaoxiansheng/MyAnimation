package com.autopai.common.utils.anim;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

public class ViewAnimatorUtil {

    /**
     * 透明度渐变动画
     *
     * @param view
     * @param from
     * @param to
     * @param duration
     * @param listener 可以为空
     * @param start 是否直接启动
     * @return
     */
    public static ObjectAnimator alphaAnimator(View view, float from, float to, int duration, Animator.AnimatorListener listener, boolean start) {
        /**
         * @param fromAlpha 开始的透明度，取值是0.0f~1.0f，0.0f表示完全透明， 1.0f表示和原来一样
         * @param toAlpha 结束的透明度，同上
         */
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", from, to);
        alpha.setDuration(duration);
        if (start) {
            alpha.start();
        }
        if (listener != null) {
            alpha.addListener(listener);
        }
        return alpha;
    }

    /**
     * 左右平移
     *
     * @param view
     * @param listener
     * @param start 是否启动
     * @param duration
     * @param values >0 右移, <0 左移
     * @return
     */
    public static ObjectAnimator moveAnimatorX(View view, Animator.AnimatorListener listener, boolean start, int duration, float... values) {
        ObjectAnimator valueAnimator = ObjectAnimator.ofFloat(view, "translationX", values);
        valueAnimator.setDuration(duration);
        if (listener != null) {
            valueAnimator.addListener(listener);
        }
        if (start) {
            valueAnimator.start();
        }
        return valueAnimator;
    }

    /**
     * 上下平移
     *
     * @param view
     * @param listener
     * @param start
     * @param duration
     * @param values >0 上移, <0 下移
     * @return
     */
    public static ObjectAnimator moveAnimatorY(View view, Animator.AnimatorListener listener, boolean start, int duration, float... values) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", values);
        animator.setDuration(duration);
        if (listener != null) {
            animator.addListener(listener);
        }
        if (start) {
            animator.start();
        }
        return animator;
    }

    /**
     * 绕z轴旋转, 相当于平面旋转RotateAnimation
     *
     * @param view       要旋转的图片
     * @param x          旋转中心点x坐标
     * @param y          旋转中心点y坐标
     * @param fromDegree 旋转起始角度
     * @param toDegree   需要旋转的角度
     * @param duration   动画时间
     */
    public static void rotationAnimationZ(View view, int x, int y,
                                          float fromDegree, float toDegree, int duration) {
        /*Rotate3dAnimation rotate3dAnimationXY = new Rotate3dAnimation(fromDegree, toDegree,
                x, y, 0, Rotate3dAnimation.ROTATE_Z_AXIS, false);
        rotate3dAnimationXY.setDuration(duration);
        rotate3dAnimationXY.setFillAfter(true);
        view.startAnimation(rotate3dAnimationXY);*/

        Animation rotateAnimation = new RotateAnimation(fromDegree, toDegree, x, y);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(duration);
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        view.startAnimation(rotateAnimation);
    }

    /**
     * 绕x轴旋转
     *
     * @param view
     * @param fromDegree
     * @param toDegree
     * @param duration
     */
    public static void rotationAnimationX(View view, float x, float y,
                                          float z, float fromDegree, float toDegree, int duration) {
//        Rotate3dAnimation rotate3dAnimationXY = new Rotate3dAnimation(fromDegree, toDegree,
//                x, y, z, Rotate3dAnimation.ROTATE_X_AXIS, false);
//        rotate3dAnimationXY.setDuration(duration);
//        rotate3dAnimationXY.setFillAfter(true);
//        view.startAnimation(rotate3dAnimationXY);
    }

    /**
     * 绕Y轴旋转
     *
     * @param view
     * @param fromDegree
     * @param toDegree
     * @param duration
     */
    public static void rotationAnimationY(View view, float x, float y, float z,
                                          float fromDegree, float toDegree, int duration) {
//        Rotate3dAnimation rotate3dAnimationXY = new Rotate3dAnimation(fromDegree, toDegree,
//                x, y, z, Rotate3dAnimation.ROTATE_Y_AXIS, false);
//        rotate3dAnimationXY.setDuration(duration);
//        rotate3dAnimationXY.setFillAfter(true);
//        view.startAnimation(rotate3dAnimationXY);
    }

    /**
     * 同时绕Y和Z轴旋转，形成螺旋效果
     * @param view
     * @param x
     * @param y
     * @param z
     * @param yFromDegree
     * @param yToDegree
     * @param zFromDegree
     * @param zToDegree
     * @param duration
     */
    public static void rotationAnimationYZ(View view, float x, float y, float z,
                                           float yFromDegree, float yToDegree,
                                           float zFromDegree, float zToDegree, int duration) {
//        Rotate3dAnimation rotateY = new Rotate3dAnimation(yFromDegree, yToDegree,
//                x, y, z, Rotate3dAnimation.ROTATE_Y_AXIS, false);
//        RotateAnimation rotateZ = new RotateAnimation(zFromDegree, zToDegree, x, y);
//        AnimationSet set = new AnimationSet(true);
//        set.addAnimation(rotateY);
//        set.addAnimation(rotateZ);
//        set.setDuration(duration);
//        set.setFillAfter(true);
//        view.startAnimation(set);
    }

    /**
     *
     * @param view
     * @param fromX X轴水平缩放起始位置的大小（fromX）,1代表正常大小
     * @param fromY X轴水平缩放完了之后（toX）的大小,0代表完全消失了
     * @param toX Y轴垂直缩放起始时的大小（fromY）
     * @param toY Y轴垂直缩放结束后的大小（toY）
     * @param duration
     */
    public static void scaleAnimation(View view, float fromX, float fromY, float toX, float toY, int duration) {
        ScaleAnimation scale = new ScaleAnimation(fromX, fromY, toX, toY,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(duration);
        view.startAnimation(scale);
    }

}