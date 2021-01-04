package com.autopai.common.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class CustomImageView extends View {

    private static final String TAG = CustomImageView.class.getSimpleName();

    private Bitmap mOriginalImage;

    private Rect mSrcRect;

    private Rect mDstRect;

    private Rect mBackgroundRect;

    private boolean isFirst = true;

    private float mScaleX = 1f, mScaleY = 1f;

    private int mWidth;
    private int mHeight;

    private int mCenterX, mCenterY = 0;

    private boolean isChanged;

    private ColorDrawable mBackground;
    private int mSrcCenterX;
    private int mSrcCenterY;

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public CustomImageView(Context context) {
        this(context, null);
        init();
    }

    private void init() {
        mSrcRect = new Rect();
        mDstRect = new Rect();
        mBackgroundRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOriginalImage != null && isFirst) {
            if (mWidth == 0) {
                mWidth = measureSize(widthMeasureSpec, mOriginalImage.getWidth());
            }
            if (mHeight == 0) {
                mHeight = measureSize(heightMeasureSpec, mOriginalImage.getHeight());
            }
            isFirst = false;

            if (getParent() instanceof LinearLayout) {
                LinearLayout layout = (LinearLayout) getParent();
                int count = layout.getChildCount();
                int orientation = layout.getOrientation();
                int weightSum = 0;
                for (int i = 0; i < count; i++) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getChildAt(i).getLayoutParams();
                    if (layout.getChildAt(i).getVisibility() != View.GONE) {
                        weightSum += params.weight;
                    }
                }
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
                if (orientation == LinearLayout.HORIZONTAL) {
                    mWidth = (int) ((int) (MeasureSpec.getSize(widthMeasureSpec) / weightSum * params.weight) / mScaleX);
                } else {
                    mHeight = (int) ((int) (MeasureSpec.getSize(heightMeasureSpec) / weightSum * params.weight) / mScaleY);
                }

                Log.d(TAG, "onMeasure getSize: " + MeasureSpec.getSize(widthMeasureSpec));
                Log.d(TAG, "onMeasure: " + mWidth + " * " + mHeight + ", weightSum: " + weightSum + ", weight: " + params.weight + ", ScaleX: " + mScaleX);

            }

            mCenterX = mWidth / 2;
            mCenterY = mHeight / 2;

            mSrcCenterX = mOriginalImage.getWidth() / 2;
            mSrcCenterY = mOriginalImage.getHeight() / 2;

            if (mCenterX >= mSrcCenterX) {
                mDstRect.left = mCenterX - mSrcCenterX;
                mDstRect.right = mCenterX + mSrcCenterX;
            } else {
                mDstRect.left = 0;
                mDstRect.right = mWidth;
            }
            if (mCenterY >= mSrcCenterY) {
                mDstRect.top = mCenterY - mSrcCenterY;
                mDstRect.bottom = mCenterY + mSrcCenterY;
            } else {
                mDstRect.top = 0;
                mDstRect.bottom = mHeight;
            }

            mBackgroundRect.left = 0;
            mBackgroundRect.right = mWidth;
            mBackgroundRect.top = 0;
            mBackgroundRect.bottom = mHeight;
        }

        setMeasuredDimension(mWidth + getPaddingLeft() + getPaddingRight(), mHeight + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBackground != null) {
            mBackground.setBounds(mBackgroundRect);
            mBackground.draw(canvas);
        }
        if (mOriginalImage != null) {
            canvas.drawBitmap(mOriginalImage, null, mDstRect, null);
        }
    }

    public void handlerScale(float scaleX, float scaleY) {
        if (mOriginalImage != null) {

            mScaleX = scaleX;
            mScaleY = scaleY;

            setX(getX() / scaleX);
            setY(getY() / scaleY);

            if (mCenterX >= mSrcCenterX) {
                mDstRect.left = (int) ((mCenterX - mSrcCenterX) / mScaleX);
                mDstRect.right = (int) ((mCenterX + mSrcCenterX) / mScaleX);
            } else {
                mDstRect.left = 0;
                mDstRect.right = mWidth;
            }

            if (mCenterY >= mSrcCenterY) {
                mDstRect.top = (int) ((mCenterY - mSrcCenterY) / mScaleY);
                mDstRect.bottom = (int) ((mCenterY + mSrcCenterY) / mScaleY);
            } else {
                mDstRect.top = 0;
                mDstRect.bottom = mHeight;
            }

            isChanged = true;

            mBackgroundRect.left = (int) (mBackgroundRect.left / scaleX);
            mBackgroundRect.top = (int) (mBackgroundRect.top / scaleY);
            mBackgroundRect.right = (int) (mBackgroundRect.right / scaleX);
            mBackgroundRect.bottom = (int) (mBackgroundRect.bottom / scaleY);

            requestLayout();
        }
    }

    public void setOriginalImage(Bitmap bmp) {
        this.mOriginalImage = bmp;

        mSrcRect.left = 0;
        mSrcRect.top = 0;
        mSrcRect.right = mOriginalImage.getWidth();
        mSrcRect.bottom = mOriginalImage.getHeight();
    }

    public void setOriginalImage(int drawableID) {
        mOriginalImage = BitmapFactory.decodeResource(getResources(), drawableID);

        mSrcRect.left = 0;
        mSrcRect.top = 0;
        mSrcRect.right = mOriginalImage.getWidth();
        mSrcRect.bottom = mOriginalImage.getHeight();
    }

    public void reset() {
        if (isChanged) {

            setX(getX() * mScaleX);
            setY(getY() * mScaleY);

            if (mCenterX >= mSrcCenterX) {
                mDstRect.left = mCenterX - mSrcCenterX;
                mDstRect.right = mCenterX + mSrcCenterX;
            } else {
                mDstRect.left = 0;
                mDstRect.right = mWidth;
            }
            if (mCenterY >= mSrcCenterY) {
                mDstRect.top = mCenterY - mSrcCenterY;
                mDstRect.bottom = mCenterY + mSrcCenterY;
            } else {
                mDstRect.top = 0;
                mDstRect.bottom = mHeight;
            }

            mBackgroundRect.left = (int) (mBackgroundRect.left * mScaleX);
            mBackgroundRect.top = (int) (mBackgroundRect.top * mScaleY);
            mBackgroundRect.right = (int) (mBackgroundRect.right * mScaleX);
            mBackgroundRect.bottom = (int) (mBackgroundRect.bottom * mScaleY);

            mScaleX = 1f;
            mScaleY = 1f;

            mCenterX = mWidth / 2;
            mCenterY = mHeight / 2;

            isChanged = false;
            requestLayout();
        }
    }

    public int measureSize(int measureSpec, int result) {
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.AT_MOST) {
            return Math.min(result, specSize);
        } else if (specMode == View.MeasureSpec.EXACTLY) {
            return specSize;
        } else {
            return result;
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        mBackground = new ColorDrawable(color);
    }
}
