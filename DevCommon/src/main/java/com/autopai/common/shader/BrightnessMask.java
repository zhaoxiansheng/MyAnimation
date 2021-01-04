package com.autopai.common.shader;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

public class BrightnessMask extends AbstructMask {
    private int mBrightness = 0x00;
    private int mDarkness = 0xFF;
    private int mColor = 0x60;

    BrightnessMask(final int low, final int high){
        mDarkness = clamp(Math.min(low, high), 0x36, 255);
        mBrightness = clamp(Math.max(low, high), 0x36, 255);
        mColor = brightnessToColor(mBrightness);
    }

    @Override
    public void setBound(Rect bound) {
        mBound = new RectF(bound);
        initShader();
    }

    @Override
    public void setValue(float ratio) {
        ratio = Math.min(1f, Math.max(0, ratio));
        int brightness = 255 - (mDarkness + (int)((mBrightness - mDarkness) * ratio + 0.5f));
        mColor = brightnessToColor(brightness);
        if(mPaint != null) {
            mPaint.setColor(mColor);
        }
    }

    @Override
    public void drawMask(Canvas canvas) {
        canvas.drawRect(mBound, mPaint);
    }

    private void initShader(){
        if(mPaint == null) {
            mPaint = new Paint();
        }
        mPaint.setColor(mColor);
    }

    private static int clamp(int value, int min, int max){
        int tmp = Math.min(min, max);
        max = Math.max(min,max);
        min = tmp;

        return Math.min(max, Math.max(min, value));
    }

    private static int brightnessToColor(int brightness){
        brightness = Math.min(255, Math.max(0, brightness));
        brightness = brightness << 24;
        return brightness;
    }
}
