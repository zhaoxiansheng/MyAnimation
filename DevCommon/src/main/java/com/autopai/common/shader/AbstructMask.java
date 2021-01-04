package com.autopai.common.shader;

import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

public abstract class AbstructMask implements IMask {
    protected Paint mPaint;
    protected RectF mBound;

    protected Paint getMastPaint(PorterDuff.Mode mode){
        Paint paint = new Paint();
        paint.setColor(0xFF000000);//A.R.G.B设置不透明
        paint.setXfermode(new PorterDuffXfermode(mode));//DST_IN
        return paint;
    }
}
