package com.example.zy.myanimation.view.longbitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.zy.myanimation.utils.ToolUtils;

public class CustomImageView extends View {

    private static final String TAG = CustomImageView.class.getSimpleName();

    private Bitmap mOriginalImage;

    private Rect mSrcRect;

    private Rect mDstRect;

    private boolean isFirst = true;

    private float mScaleX = 1f, mScaleY = 1f;

    private int mWidth;
    private int mHeight;

    private boolean mNeedMeasure;

    private int mCenterX, mCenterY = 0;

    private int mDstX;
    private int mDstY;

    private boolean isChanged;

    private String mName;

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
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOriginalImage != null && (isFirst)) {
            if (mWidth == 0) {
                mWidth = ToolUtils.measureWidth(widthMeasureSpec, mOriginalImage.getWidth());
            }
            if (mHeight == 0) {
                mHeight = ToolUtils.measureHeight(heightMeasureSpec, mOriginalImage.getHeight());
            }
            isFirst = false;
            mNeedMeasure = false;

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
                // TODO: 2020/10/13 关于6.0之前的LinearLayout测量子View都是MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED) 6.0之后是MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.UNSPECIFIED)
                if (orientation == LinearLayout.HORIZONTAL) {
                    mWidth = (int) ((int) (MeasureSpec.getSize(widthMeasureSpec) / weightSum * params.weight) / mScaleX);
                } else {
                    mHeight = (int) ((int) (MeasureSpec.getSize(heightMeasureSpec) / weightSum * params.weight) / mScaleY);
                }
            }

            mCenterX = mWidth / 2;
            mCenterY = mHeight / 2;

            mDstRect.left = mCenterX - mOriginalImage.getWidth() / 2;
            mDstRect.top = mCenterY - mOriginalImage.getHeight() / 2;
            mDstRect.right = mCenterX + mOriginalImage.getWidth() / 2;
            mDstRect.bottom = mCenterY + mOriginalImage.getHeight() / 2;
        }
        /*else {
            if (mNeedMeasure) {
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
                        mWidth = (int) (mWidth / weightSum * params.weight);
                    } else {
                        mHeight = (int) (mHeight / weightSum * params.weight);
                    }
                }
                mNeedMeasure = false;
            }
        }*/

        Log.d(TAG, "onMeasure name : " + mName + ", " + mWidth + " * " + mHeight + ", " + getX());
        setMeasuredDimension(mWidth + getPaddingLeft() + getPaddingRight(), mHeight + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mOriginalImage != null) {
            Log.d(TAG, "onDraw name : " + mName + ", " + mDstRect.left + " * " + mDstRect.right + ", ");
            canvas.drawBitmap(mOriginalImage, null, mDstRect, null);
        }
    }

    public void handlerScale(float scaleX, float scaleY) {
        if (mOriginalImage != null) {

            mScaleX = scaleX;
            mScaleY = scaleY;

            // TODO: 2020/10/12 使用width 或者 setX 选一
            mWidth = (int) (mWidth * (1f / scaleX));
            mHeight = (int) (mHeight * (1f / scaleY));
//            setX(getX() * (1f / scaleX));
//            setY(getY() * (1f / scaleY));

            mDstRect.left = (int) ((mCenterX - mOriginalImage.getWidth() / 2) / mScaleX);
            mDstRect.top = (int) ((mCenterY - mOriginalImage.getHeight() / 2) / mScaleY);
            mDstRect.right = (int) ((mCenterX + mOriginalImage.getWidth() / 2) / mScaleX);
            mDstRect.bottom = (int) ((mCenterY + mOriginalImage.getHeight() / 2) / mScaleY);

            mNeedMeasure = true;
            isChanged = true;

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
            mWidth = (int) (mWidth * mScaleX);
            mHeight = (int) (mHeight * mScaleY);
//            setX(getX() * mScaleX);
//            setY(getY() * mScaleY);

            mDstRect.left = mCenterX - mOriginalImage.getWidth() / 2;
            mDstRect.top = mCenterY - mOriginalImage.getHeight() / 2;
            mDstRect.right = mCenterX + mOriginalImage.getWidth() / 2;
            mDstRect.bottom = mCenterY + mOriginalImage.getHeight() / 2;

            mScaleX = 1f;
            mScaleY = 1f;

            mDstX = 0;
            mDstY = 0;

            mCenterX = mWidth / 2;
            mCenterY = mHeight / 2;

            mNeedMeasure = true;
            isChanged = false;
//            requestLayout();
        }
    }

    public void setName(String mName) {
        this.mName = mName;
    }
}
