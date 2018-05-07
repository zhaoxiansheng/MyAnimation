package com.example.zy.myanimation.view.scroll;

import android.animation.TypeEvaluator;

/**
 * Created on 2018/5/4.
 *
 * @author zhaoy
 */
public class CustomPointEvaluator implements TypeEvaluator {

    /**
     *
     * @param fraction 系数
     * @param startValue 起始值
     * @param endValue 终点值
     * @return
     */
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        MyPoint startPoint = (MyPoint) startValue;
        MyPoint endPoint = (MyPoint) endValue;
        float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
        float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
        return new MyPoint(x, y);
    }
}
