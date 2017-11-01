package com.example.zy.myanimation.utils;

import android.view.View;

/**
 * Created on 2017/11/1.
 *
 * @author zhaoy
 */
public class ToolUtils {

    /**
     * 测量自定义控件的宽
     * @param measureSpec
     * @param result 默认大小
     * @return 返回大小
     */
    public static int measureWidth(int measureSpec, int result) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        //相当于我们设置为wrap_content
        if (specMode == View.MeasureSpec.AT_MOST) {
            result = specSize;
            //相当于我们设置为match_parent或者为一个具体的值
        } else if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    /**
     * 测定自定义控件的高
     * @param measureSpec
     * @param result 默认的大小
     * @return 返回大小
     */
    public static int measureHeight(int measureSpec, int result) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        if (specMode == View.MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }
}
