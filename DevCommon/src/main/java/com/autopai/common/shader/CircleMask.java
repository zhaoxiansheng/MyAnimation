package com.autopai.common.shader;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;

public class CircleMask extends AbstructMask {

    private final boolean mMode;

    public CircleMask(boolean mode) {
        this.mMode = mode;
    }

    @Override
    public void setBound(Rect bound) {
        mBound = new RectF(bound);
        initShader();
    }

    @Override
    public void setValue(float ratio) {

    }

    @Override
    public void drawMask(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(((int) mBound.width()), ((int) mBound.height()), Bitmap.Config.ARGB_8888);
        bitmap.setHasAlpha(true);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        float radius;

        if (mMode) {
            radius = Math.max(mBound.width(), mBound.height()) / 2f;
        } else {
            radius = Math.min(mBound.width(), mBound.height()) / 2f;
        }

        Canvas circleCanvas = new Canvas(bitmap);
        circleCanvas.drawCircle(mBound.width() / 2f,
                mBound.height() / 2f,
                radius,
                paint);

        canvas.drawBitmap(bitmap, 0, 0, mPaint);
    }

    private void initShader() {
        mPaint = getMastPaint(PorterDuff.Mode.DST_IN);
    }
}
