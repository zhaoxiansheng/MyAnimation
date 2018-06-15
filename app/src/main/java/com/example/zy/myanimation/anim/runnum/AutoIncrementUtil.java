package com.example.zy.myanimation.anim.runnum;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author zhaoy
 * 模拟支付宝数字翻滚
 */
public class AutoIncrementUtil {

    public static final String FLOAT_TYPE = "FloatType";
    public static final String INT_TYPE = "IntType";

    /**
     * 动画
     * @param type 数字类型
     * @param tvView 需要执行动画的控件
     * @param floatValue 值
     * @param isRoundUp 是否进行四舍五入
     * @param unit 单位
     * @param duration 执行时间
     */
    public static void startAnimation(String type, final TextView tvView, String floatValue
            , boolean isRoundUp, final String unit, int duration) {
        ValueAnimator animator = null;
        if (type.equals(FLOAT_TYPE)) {
            animator = ValueAnimator.ofFloat(0, Float.valueOf(floatValue));
            animator.addUpdateListener((valueAnimator) -> {
                float curValue = (float) valueAnimator.getAnimatedValue();
                if (curValue == Float.valueOf(floatValue)){
                    tvView.setText(floatValue + unit);
                } else {
                    tvView.setText(NumUtil.formatFloat(curValue) + unit);
                }
            });
        } else if (type.equals(INT_TYPE)) {
            String targetValueString = NumUtil.formatRoundUp(isRoundUp, Float.valueOf(floatValue));
            animator = ValueAnimator.ofInt(0, Integer.parseInt(targetValueString));

            animator.addUpdateListener((valueAnimator) -> {
                int curValue = (int) valueAnimator.getAnimatedValue();
                if (curValue == Integer.valueOf(targetValueString)){
                    tvView.setText(NumUtil.formatString(floatValue) + unit);
                } else {
                    tvView.setText(NumUtil.format(curValue) + unit);
                }
            });
        }
        if (animator != null) {
            animator.setDuration(duration);
            animator.setInterpolator(new AccelerateInterpolator());
            animator.start();
        }
    }

}