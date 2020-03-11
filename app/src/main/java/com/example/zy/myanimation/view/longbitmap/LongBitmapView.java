package com.example.zy.myanimation.view.longbitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.Nullable;

/**
 * 加载长图片
 */
public class LongBitmapView extends View {

    private BitmapRegionDecoder mRegionDecoder;
    private int mImageWidth;
    private int mImageHeight;
    private Rect mRect = new Rect();
    private static BitmapFactory.Options mOptions = new BitmapFactory.Options();

    static {
        mOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
    }

    public LongBitmapView(Context context) {
        super(context, null);
    }

    public LongBitmapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public LongBitmapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setInputStream(InputStream inputStream) {
        try {
            mRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            BitmapFactory.decodeStream(inputStream, null, options);
            mImageWidth = options.outWidth;
            mImageHeight = options.outHeight;

            requestLayout();
            invalidate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = width;
        mRect.bottom = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = mRegionDecoder.decodeRegion(mRect, mOptions);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    int downX = 0;
    int downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int curX = (int) event.getX();
                int curY = (int) event.getY();

                int moveX = curX - downX;
                int moveY = curY - downY;

                onMove(moveX, moveY);

                downX = curX;
                downY = curY;
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    private void onMove(int moveX, int moveY) {
        if (mImageWidth > getWidth()) {
            mRect.offset(-moveX, 0);
            checkWidth();
            invalidate();
        }
        if (mImageHeight > getHeight()) {
            mRect.offset(0, -moveY);
            checkHeight();
            invalidate();
        }
    }

    private void checkWidth() {
        Rect rect = mRect;
        if (rect.right > mImageWidth) {
            rect.right = mImageWidth;
            rect.left = mImageWidth - getWidth();
        }
        if (rect.left < 0) {
            rect.left = 0;
            rect.right = getWidth();
        }
    }

    private void checkHeight() {
        Rect rect = mRect;
        if (rect.bottom > mImageHeight) {
            rect.bottom = mImageHeight;
            rect.top = mImageHeight - getHeight();
        }
        if (rect.top < 0) {
            rect.top = 0;
            rect.bottom = getHeight();
        }
    }
}
