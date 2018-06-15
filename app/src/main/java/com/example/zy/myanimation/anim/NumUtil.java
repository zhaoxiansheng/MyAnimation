package com.example.zy.myanimation.anim;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author zhaoy
 */
public class NumUtil {

    private NumUtil() {
    }

    /**
     * 对数字进行处理
     *
     * @param value 值
     * @return 返回String类型
     */
    public static String formatFloat(float value) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value);
    }

    public static String format(float value) {
        DecimalFormat df = new DecimalFormat("###,##0");
        return df.format(value);
    }

    static String formatString(String value) {
        //先将字符串颠倒顺序
        value = new StringBuilder(value).reverse().toString();
        String str2 = "";
        for (int i = 0; i < value.length(); i++) {
            if (i * 3 + 3 > value.length()) {
                str2 += value.substring(i * 3, value.length());
                break;
            }
            str2 += value.substring(i * 3, i * 3 + 3) + ",";
        }
        if (str2.endsWith(",")) {
            str2 = str2.substring(0, str2.length() - 1);
        }
        return new StringBuilder(str2).reverse().toString();
    }

    public static double formatD(String d) {
        BigDecimal bg = new BigDecimal(d);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 对数字进行处理
     *
     * @param isRoundUp true 代表四舍五入 false 代表不四舍五入
     * @param value     值
     * @return 返回string类型的值
     */
    static String formatRoundUp(boolean isRoundUp, float value) {
        DecimalFormat df;
        if (isRoundUp) {
            //四色五入转换成整数
            df = new DecimalFormat("#####0");
            return df.format(value);
        } else {
            Float f = value;
            int i = f.intValue();
            return String.valueOf(i);
        }
    }

    public static String getDoubleValTest(Double value) {
        String str = String.valueOf(value);
        String[] arr = str.split("E");
        float res1 = Float.valueOf(arr[0]);
        int ci = Integer.valueOf(arr[1]);
        float num = 10;
        for (int i = 0; i < ci; i++) {
            num = num * 10;
        }
        float res = res1 * num;
        NumberFormat nf = new DecimalFormat("0.00");
        return nf.format(res);
    }

}