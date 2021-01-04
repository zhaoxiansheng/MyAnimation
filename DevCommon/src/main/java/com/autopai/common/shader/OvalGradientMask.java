package com.autopai.common.shader;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;

class OvalGradientMask extends AbstructMask {

    private static final String TAG = "CircleGradientMask";
    /**
     * circle center position at x coordinate, orginal at lfet_top corner of the view
     */
    private float mCircleX;
    /**
     * circle center position at y coordinate, orginal at lfet_top corner of the view
     */
    private float mCircleY;
    private float mRadius;

    private int mCenterColor = 0xFF000000;
    private int mEdgeColor = 0x00000000;

    OvalGradientMask(final int start, final int end) {
        mCenterColor = start;
        mEdgeColor = end;
    }

    @Override
    public void setBound(Rect bound) {
        mCircleX = (bound.left + bound.right) / 2.0f;
        mCircleY = (bound.top + bound.bottom) / 2.0f;
        mRadius = (float) (Math.sqrt(Math.pow(bound.width(), 2) + Math.pow(bound.height(), 2)) / 2.0f);

        mBound = new RectF(bound);
        initShader();
    }

    @Override
    public void setValue(float ratio) {

    }

    private void initShader(){

        RadialGradient gradient = new android.graphics.RadialGradient(
                mCircleX, mCircleY, mRadius,
                mCenterColor, mEdgeColor,
                android.graphics.Shader.TileMode.CLAMP);

        // Draw transparent circle into tempBitmap
        mPaint = getMastPaint(PorterDuff.Mode.DST_IN);
        mPaint.setShader(gradient);
    }

    @Override
    public void drawMask(Canvas canvas) {
        canvas.drawOval(mBound, mPaint);
    }
}
