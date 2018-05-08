package com.example.zy.myanimation.anim.runnum;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

/**
 * @author zhaoy
 * 模拟支付宝数字翻滚
 */
public class AutoIncrementUtil {

    public static final String FLOATTYPE = "FloatType";
    public static final String INTTYPE = "IntType";

    public static void startAnimation(String type, final TextView tvView, float floatValue
            , boolean isRoundUp, final String unit, int duration) {
        ValueAnimator animator = null;
        if (type.equals(FLOATTYPE)) {
            animator = ValueAnimator.ofFloat(0, floatValue);
            animator.addUpdateListener((valueAnimator) -> {
                float curValue = (float) valueAnimator.getAnimatedValue();
                tvView.setText(NumUtil.formatFloat(curValue) + unit);
            });
        } else if (type.equals(INTTYPE)) {
            String targetValueString = NumUtil.formatRoundUp(isRoundUp, floatValue);
            animator = ValueAnimator.ofInt(0, Integer.parseInt(targetValueString));

            animator.addUpdateListener((valueAnimator) -> {
                int curValue = (int) valueAnimator.getAnimatedValue();
                tvView.setText(curValue + unit);
            });
        }
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

}