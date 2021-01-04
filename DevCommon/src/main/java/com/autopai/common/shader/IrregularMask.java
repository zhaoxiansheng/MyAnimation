package com.autopai.common.shader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

public class IrregularMask extends AbstructMask {

    private final Context mContext;
    private final int maskId;

    public IrregularMask(Context context, int maskId) {
        mContext = context;
        this.maskId = maskId;
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

        Drawable drawable = ContextCompat.getDrawable(mContext, maskId);
        Canvas bitmapCanvas = new Canvas(bitmap);
        drawable.setBounds(((int) mBound.left), ((int) mBound.top), ((int) mBound.right), ((int) mBound.bottom));
        drawable.draw(bitmapCanvas);

        canvas.drawBitmap(bitmap, 0, 0, mPaint);
    }

    private void initShader() {
        mPaint = getMastPaint(PorterDuff.Mode.DST_IN);
    }
}
