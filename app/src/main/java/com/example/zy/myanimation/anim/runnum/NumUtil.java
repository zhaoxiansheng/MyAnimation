package com.example.zy.myanimation.anim.runnum;

import java.text.DecimalFormat;

/**
 * @author zhaoy
 * 数字格式化
 */
public class NumUtil {

    private NumUtil() {
    }

    public static String formatFloat(float value) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value);
    }

    public static String formatRoundUp(boolean isRoundUp, float value) {
        DecimalFormat df;
        if (isRoundUp) {
            //四色五入转换成整数
            df = new DecimalFormat("######0");
            return df.format(value);
        } else {
            Float f = new Float(value);
            int i = f.intValue();
            return String.valueOf(i);
        }
    }
}