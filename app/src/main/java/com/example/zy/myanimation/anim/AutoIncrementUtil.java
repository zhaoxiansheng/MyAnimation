package com.example.zy.myanimation.anim;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

/**
 * @author zhaoy
 */
public class AutoIncrementUtil {

    public static final String FLOAT_TYPE = "FloatType";
    public static final String INT_TYPE = "IntType";

    public static void startAnimation(String type, final TextView tvView, String floatValue
            , boolean isRoundUp, final String unit, int duration) {
        ValueAnimator animator = null;
        if (type.equals(FLOAT_TYPE)) {
            animator = ValueAnimator.ofFloat(0, Float.parseFloat(floatValue));
            animator.addUpdateListener((valueAnimator) -> {
                float curValue = (float) valueAnimator.getAnimatedValue();
                if (curValue == Float.parseFloat(floatValue)) {
                    tvView.setText(floatValue + unit);
                } else {
                    tvView.setText(NumUtil.formatFloat(curValue) + unit);
                }
            });
        } else if (type.equals(INT_TYPE)) {
            String targetValueString = NumUtil.formatRoundUp(isRoundUp, Float.parseFloat(floatValue));
            animator = ValueAnimator.ofInt(0, Integer.parseInt(targetValueString));

            animator.addUpdateListener((valueAnimator) -> {
                int curValue = (int) valueAnimator.getAnimatedValue();
                if (curValue == Integer.valueOf(targetValueString)) {
                    tvView.setText(NumUtil.formatString(floatValue) + unit);
                } else {
                    tvView.setText(curValue + unit);
                }
            });
        }
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

}