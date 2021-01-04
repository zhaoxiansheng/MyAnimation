package com.autopai.common.shader;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;

/**
 * 工具集
 */
class CircleGradientMask extends AbstructMask {

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

	CircleGradientMask(final int start, final int end){
		mCenterColor = start;
		mEdgeColor = end;
	}

	@Override
	public void setBound(Rect bound) {
		//mBound = new RectF(bound);
		mCircleX = (bound.left + bound.right) / 2.0f;
		mCircleY = (bound.top + bound.bottom) / 2.0f;
		mRadius = (float) (Math.sqrt(Math.pow(bound.width(), 2) + Math.pow(bound.height(), 2)) / 2.0f);

		initShader();
	}

	@Override
	public void setValue(float ratio) {

	}

	private void initShader(){

		// Create a radial gradient
		RadialGradient gradient = new android.graphics.RadialGradient(
				mCircleX, mCircleY, mRadius,
				new int[] {mCenterColor, 0xEF000000, mEdgeColor}, new float[] {0, 0.5f, 1},
				android.graphics.Shader.TileMode.CLAMP);

		/*RadialGradient gradient = new android.graphics.RadialGradient(
				mCircleX, mCircleY, mRadius,
				mCenterColor, mEdgeColor,
				android.graphics.Shader.TileMode.CLAMP);//edgeColor smaller, alpha bigger*/

		// Draw transparent circle into tempBitmap
		mPaint = getMastPaint(PorterDuff.Mode.DST_IN);
		mPaint.setShader(gradient);
	}

	@Override
	public void drawMask(Canvas canvas) {
		canvas.drawCircle(mCircleX, mCircleY, mRadius, mPaint);
	}
}
