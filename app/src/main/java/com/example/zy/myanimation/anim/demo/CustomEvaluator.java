package com.example.zy.myanimation.anim.demo;

import android.animation.TypeEvaluator;

// 实现TypeEvaluator接口
public class CustomEvaluator implements TypeEvaluator<String> {

    // 复写evaluate（）
    // 在evaluate（）里写入对象动画过渡的逻辑
    @Override
    public String evaluate(float fraction, String startValue, String endValue) {
        int startInt = Integer.parseInt(startValue);
        int endInt = Integer.parseInt(endValue);
        int cur = (int) (startInt + fraction * (endInt - startInt));
        return cur + "";
    }

}