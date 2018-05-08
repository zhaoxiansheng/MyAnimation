package com.example.zy.myanimation.anim;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

/**
 * @author zhaoy
 */
public class AutoIncrementUtil {

    public static final String FLOATTYPE = "FloatType";
    public static final String INTTYPE = "IntType";

    public static void startAnimation(String type, final TextView tvView, float floatValue
            , boolean isRoundUp, final String danwei, int duration) {
        ValueAnimator animator = null;
        if (type.equals(FLOATTYPE)) {
            animator = ValueAnimator.ofFloat(0, floatValue);
            animator.addUpdateListener((valueAnimator) -> {
                float curValue = (float) valueAnimator.getAnimatedValue();
                tvView.setText(NumUtil.formatFloat(curValue) + danwei);
            });
        } else if (type.equals(INTTYPE)) {
            String targetValueString = NumUtil.formatRoundUp(isRoundUp, floatValue);
            animator = ValueAnimator.ofInt(0, Integer.parseInt(targetValueString));

            animator.addUpdateListener((valueAnimator) -> {
                int curValue = (int) valueAnimator.getAnimatedValue();
                tvView.setText(curValue + danwei);
            });
        }
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

}