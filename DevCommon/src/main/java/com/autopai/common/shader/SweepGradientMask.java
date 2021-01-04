package com.autopai.common.shader;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;

class SweepGradientMask extends AbstructMask {
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

    SweepGradientMask(final int start, final int end) {
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

        // Create a radial gradient
		/*RadialGradient gradient = new android.graphics.RadialGradient(
				mCircleX, mCircleY, mRadius,
				new int[] {mCenterColor, 0x1F000000, mEdgeColor}, new float[] {0, 0.8f, 1},
				android.graphics.Shader.TileMode.CLAMP);
		* */
        SweepGradient gradient = new android.graphics.SweepGradient(mCircleX, mCircleY,
                new int[] {mCenterColor, mEdgeColor}, null);//edgeColor smaller, alpha bigger

        // Draw transparent circle into tempBitmap
        mPaint = getMastPaint(PorterDuff.Mode.DST_IN);
        mPaint.setShader(gradient);
    }

    @Override
    public void drawMask(Canvas canvas) {
        canvas.drawRect(mBound, mPaint);
    }
}
