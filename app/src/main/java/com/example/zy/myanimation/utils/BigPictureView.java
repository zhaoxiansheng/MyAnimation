package com.example.zy.myanimation.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.io.IOException;
import java.io.InputStream;

public class BigPictureView extends View implements GestureDetector.OnGestureListener {
    private static final String TAG = "BigPicView";
    public static int VERITCAL = 0;
    public static int HORIZONTAL = 1;
    private final Rect mImgRect;
    private final BitmapFactory.Options mOptions;
    private final GestureDetector mGesturer;
    private final Scroller mScroller;
    private ImgInfo mImgInfo;
    private Bitmap mBitmap;
    private BitmapRegionDecoder mDecoder;
    private int mOrientation = VERITCAL;
    private int mViewWidth;
    private int mViewHeight;
    private float mDrawScale;
    private Matrix mImgMatrix;

    private static class ImgInfo {
        private int imgWidth;
        private int imgHeight;
        private Bitmap.Config format = null;
    }

    public BigPictureView(Context context) {
        this(context, null);
    }

    public BigPictureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigPictureView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @SuppressLint("NewApi")
    public BigPictureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //1、设置需要成员变量
        mImgRect = new Rect();
        //内存复用
        mOptions = new BitmapFactory.Options();
        //手势识别
        mGesturer = new GestureDetector(context, this);
        //滚动类
        mScroller = new Scroller(context);
    }

    public void with(final String path) {
        //setImage()
    }

    //2、设置图片，获取图片信息(创建时调用)
    public boolean setImage(InputStream istream) {
        if (istream != null) {
            //获取图片宽高，只加载可见区域的图片
            getImageInfo(istream);
            //开启复用
            mOptions.inMutable = true;
            //设置解码format
            mOptions.inPreferredConfig = Bitmap.Config.RGB_565; //(5+6+5)/8 = 2B
            //区域解码器
            try {
                mDecoder = BitmapRegionDecoder.newInstance(istream, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mImgMatrix == null) {
                mImgMatrix = new Matrix();
            }
            requestLayout();
            return mDecoder != null;
        }
        return false;
    }

    private int getImageVisibleSpace() {
        if (mOrientation == VERITCAL) {
            return (int) (mViewHeight / mDrawScale);
        } else {
            return (int) (mViewWidth / mDrawScale);
        }
    }

    //3、开始测量
    private void measureImage(int viewW, int viewH) {
        mViewWidth = viewW;
        mViewHeight = viewH;
        if (mOrientation == VERITCAL) {
            if (mImgInfo.imgWidth > 0) {
                mDrawScale = (float) mViewWidth / mImgInfo.imgWidth;
            } else {
                mDrawScale = 1.0f;
            }
        } else {
            if (mImgInfo.imgHeight > 0) {
                mDrawScale = (float) mViewHeight / mImgInfo.imgHeight;
            } else {
                mDrawScale = 1.0f;
            }
        }

        //确定加载图片区域
        if (mOrientation == VERITCAL) {
            mImgRect.left = 0;
            mImgRect.top = getScrollY();
            mImgRect.right = mImgInfo.imgWidth;
            mImgRect.bottom = getImageVisibleSpace();
        } else {
            mImgRect.left = getScrollX();
            mImgRect.top = 0;
            mImgRect.right = getImageVisibleSpace();
            mImgRect.bottom = mImgInfo.imgHeight;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measureImage(w, h);
        Log.e(TAG, "onSizeChanged: " + w + "x" + h);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure: " + getMeasuredWidth() + "x" + getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e(TAG, "onLayout: " + (right - left) + "x" + (bottom - top));
    }

    //4、绘制mImgRect指定区域Bitmap
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDecoder != null) {
            //内存复用(比用的bitmap宽高和解码的宽高一至)
            mOptions.inBitmap = mBitmap;
            //指定解码区域
            mBitmap = mDecoder.decodeRegion(mImgRect, mOptions);
            //使用缩放矩阵缩放到全宽显示
            mImgMatrix.reset();
            mImgMatrix.setScale(mDrawScale, mDrawScale);
            canvas.drawBitmap(mBitmap, mImgMatrix, null);
        }
    }

    private ImgInfo getImageInfo(final InputStream istream) {
        if (mImgInfo == null) {
            mImgInfo = new ImgInfo();
        }
        mOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(istream, null, mOptions);
        mOptions.inJustDecodeBounds = false;

        mImgInfo.imgWidth = Math.max(0, mOptions.outWidth);
        mImgInfo.imgHeight = Math.max(0, mOptions.outHeight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mImgInfo.format = mOptions.outConfig;
        }

        return mImgInfo;
    }

    //5、处理点击事件
    @Override
    public boolean onDown(MotionEvent e) {
        //如果滚动没有完成，强制停止
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);

        }
        return true;//GestureDector消费此次事件
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    //6、处理滑动事件

    /**
     * @param e1        开始滚动手指按下的坐标
     * @param e2        当前手指位置坐标
     * @param distanceX 相较于上次位置x方向移动距离
     * @param distanceY 相较于上次位置y方向移动距离
     * @return
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        boolean bConsumed = false;
        if (mOrientation == VERITCAL) {
            if (distanceY > 0) {
                //向下滑动
                int avaliable = mImgInfo.imgHeight - mImgRect.bottom;
                if (avaliable > 0) {
                    mImgRect.offset(0, Math.min(avaliable, (int) distanceY));
                    mImgRect.bottom = mImgRect.top + getImageVisibleSpace();
                    bConsumed = true;
                }
            } else {
                //向上滑动
                if (mImgRect.top > 0) {
                    mImgRect.offset(0, -Math.min(mImgRect.top, (int) -distanceY));
                    mImgRect.bottom = mImgRect.top + getImageVisibleSpace();
                    bConsumed = true;
                }
            }
        } else {
            mImgRect.offset((int) distanceX, 0);
            mImgRect.right = mImgRect.left + getImageVisibleSpace();
        }
        invalidate();
        Log.e(TAG, "p2-p1: " + new PointF(e1.getX() - e2.getX(), e2.getY() - e1.getY()) + " dist: " + distanceX + " , " + distanceY);
        return bConsumed;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: " + event);
        return mGesturer.onTouchEvent(event);
    }
}
