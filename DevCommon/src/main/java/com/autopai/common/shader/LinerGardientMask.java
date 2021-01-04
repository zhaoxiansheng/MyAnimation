package com.autopai.common.shader;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

class LinerGardientMask extends AbstructMask {
    private int mStartColor = 0xFF000000;
    private int mEndColor = 0x00000000;

    LinerGardientMask(final int start, final int end){
        mStartColor = start;
        mEndColor = end;
    }

    @Override
    public void setBound(Rect bound) {
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
        LinearGradient gradient = new LinearGradient(mBound.left, mBound.top, mBound.right, mBound.bottom, new int[]{mStartColor, mEndColor},null, Shader.TileMode.MIRROR);

        // Draw transparent circle into tempBitmap
        mPaint = getMastPaint(PorterDuff.Mode.DST_IN);
        mPaint.setShader(gradient);
    }

    @Override
    public void drawMask(Canvas canvas) {
        canvas.drawRect(mBound, mPaint);
    }
}
