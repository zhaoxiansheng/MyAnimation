package com.autopai.common.shader;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * 此效果需要关闭硬件加速
 */
public class BlurMask extends AbstructMask {

    @Override
    public void setBound(Rect bound) {
        //NORMAL: 内外都模糊绘制
        mBound = new RectF(bound);
        mPaint = getMastPaint(PorterDuff.Mode.DST_IN);
        mPaint.setColor(Color.RED);
        float radius =  (float) (Math.sqrt(Math.pow(bound.width(), 2) + Math.pow(bound.height(), 2)) / 2.0f);
        mPaint.setMaskFilter(new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL));
    }

    @Override
    public void setValue(float ratio) {

    }

    @Override
    public void drawMask(Canvas canvas) {
        canvas.drawRect(mBound, mPaint);
    }
}
