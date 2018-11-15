package com.example.zy.myanimation.view.stretchable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zy.myanimation.R;

public class StretchableFloatingButton extends ViewGroup {

    private static final int IS_SLIDE_DECREASE = 0x11;
    private static final int IS_SLIDE_INCREASE = 0X12;

    private int width;
    private int height;
    private int widthMode;
    private int heightMode;
    /**
     * 开始圆的画笔
     */
    private Paint mPaint;
    /**
     * 中间矩形
     */
    private RectF middleRect;
    /**
     * 开始圆的配置
     */
    private int startCircleRadius;
    private Point startCenter;
    /**
     * 结束圆的配置
     */
    private int endCircleRadius;
    private Point endCenter;
    /**
     * 小圆的配置
     */
    private int smallCircleRadius;
    private Point smallCenter;
    /**
     * 圆环之间的宽度
     */
    private float y = 20;
    /**
     * 矩形和圆环之间的比值
     */
    private float y_x;
    /**
     * 增加或者减少的标志
     */
    private boolean isIncrease = true;

    private TextView tvContent;
    private View child;
    private int tvWidth;
    private int tvHeight;
    /**
     * 文本可以伸缩的长度
     */
    private int tx;
    private float tx_x;

    /**
     * 控件背景颜色
     */
    private int bgColor;
    /**
     * 内圈小圆颜色
     */
    private int innerCircleColor;
    /**
     * 字体颜色
     */
    private int textColor;
    /**
     * 文字
     */
    private String text;
    /**
     * 字体大小
     */
    private float textSize;
    /**
     * 速度增减
     */
    private float speed;
    /**
     * 旋转角度
     */
    private float degrees;
    /**
     * 旋转比
     */
    private float d_x;
    /**
     * 是否为点击区域
     */
    private boolean canClick;

    private FoldListener foldListener;

    private StretchableFloatingButton sfb;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                //递减状态
                case IS_SLIDE_DECREASE:
                    endCenter.x -= 30;
                    if (endCenter.x >= startCenter.x + 30) {
                        y = y_x * endCenter.x;
                        tx = (int) tx_x * endCenter.x;
                        smallCircleRadius = startCircleRadius - (int) y;
                        mHandler.sendEmptyMessageDelayed(IS_SLIDE_DECREASE, 1);
                    } else {
                        endCenter.x = startCenter.x;
                        y = 0;
                        tx = startCircleRadius * 2 + 5;
                        smallCircleRadius = startCircleRadius;
                        setEnabled(true);
                    }
                    break;
                //递增状态
                case IS_SLIDE_INCREASE:
                    endCenter.x += 30;
                    if (endCenter.x < width - endCircleRadius) {
                        mHandler.sendEmptyMessageDelayed(IS_SLIDE_INCREASE, 1);
                        y = y_x * endCenter.x;
                        tx = (int) tx_x * endCenter.x;
                        smallCircleRadius = startCircleRadius - (int) y;
                    } else {
                        y = 20;
                        smallCircleRadius = startCircleRadius - (int) y;
                        tx = tvWidth + startCircleRadius * 2 + 10;
                        endCenter.x = width - endCircleRadius;
                        setEnabled(true);
                    }
                    break;
                default:
                    break;
            }
            requestLayout();
            invalidate();
            return false;
        }
    });

    private void initAttrs(Context context, AttributeSet attrs) {
        startCenter = new Point();
        endCenter = new Point();
        smallCenter = new Point();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StretchableFloatingButton);
        bgColor = typedArray.getColor(R.styleable.StretchableFloatingButton_stretch_bg_color, Color.YELLOW);
        innerCircleColor = typedArray.getColor(R.styleable.StretchableFloatingButton_stretch_inner_circle_color, Color.BLACK);
        textColor = typedArray.getColor(R.styleable.StretchableFloatingButton_stretch_text_color, Color.BLACK);
        textSize = typedArray.getFloat(R.styleable.StretchableFloatingButton_stretch_text_size, 20);
        text = typedArray.getString(R.styleable.StretchableFloatingButton_stretch_text);
        speed = typedArray.getFloat(R.styleable.StretchableFloatingButton_stretch_speed, 80);
        degrees = typedArray.getFloat(R.styleable.StretchableFloatingButton_stretch_degrees, 90);
        typedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);

        tvContent = new TextView(context);
        tvContent.setText(text);
        tvContent.setTextColor(textColor);
        tvContent.setTextSize(textSize);
        addView(tvContent);
    }

    public StretchableFloatingButton(Context context) {
        super(context, null);
    }

    public StretchableFloatingButton(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initAttrs(context, attrs);
    }

    public StretchableFloatingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        child.layout(startCircleRadius * 2 + 5, (height - tvHeight) / 2, tx,
                startCircleRadius + tvHeight / 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (width == 0) {
            widthMode = MeasureSpec.getMode(widthMeasureSpec);
            heightMode = MeasureSpec.getMode(heightMeasureSpec);

            if (widthMode == MeasureSpec.AT_MOST) {
                width = 600;
            } else if (widthMode == MeasureSpec.EXACTLY) {
                width = MeasureSpec.getSize(widthMeasureSpec);
            } else {
                width = MeasureSpec.getSize(widthMeasureSpec);
            }

            if (heightMode == MeasureSpec.AT_MOST) {
                height = 100;
            } else if (heightMode == MeasureSpec.EXACTLY) {
                height = MeasureSpec.getSize(heightMeasureSpec);
            } else {
                height = MeasureSpec.getSize(heightMeasureSpec);
            }
            startCircleRadius = height / 2;
            endCircleRadius = height / 2;
            smallCircleRadius = startCircleRadius - (int) y;
            startCenter.set(startCircleRadius, startCircleRadius);
            endCenter.set(width - endCircleRadius, endCircleRadius);
            smallCenter.set(startCircleRadius, startCircleRadius);
            //圆环伸缩比
            y_x = y / (float) endCenter.x;

            child = this.getChildAt(0);
            measureChild(child, width, height);
            tvHeight = child.getMeasuredHeight();
            tvWidth = child.getMeasuredWidth();

            //文本右侧的位置
            tx = startCircleRadius * 2 + 5 + tvWidth;
            //文本伸缩比
            tx_x = (float) (tvWidth + 10) / (float) (width - endCircleRadius * 2);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mPaint.setColor(bgColor);
        canvas.drawCircle(startCenter.x, startCenter.y, startCircleRadius, mPaint);

        middleRect = new RectF(startCenter.x, 0, endCenter.x, height);
        canvas.drawRect(middleRect, mPaint);

        canvas.drawCircle(endCenter.x, endCenter.y, endCircleRadius, mPaint);

        mPaint.setColor(innerCircleColor);
        canvas.drawCircle(smallCenter.x, smallCenter.y, smallCircleRadius, mPaint);
        super.dispatchDraw(canvas);
    }

    /**
     * 增加
     */
    private void startIncrease() {
        setEnabled(false);
        isIncrease = true;
        mHandler.sendEmptyMessageDelayed(IS_SLIDE_INCREASE, 40);
    }

    /**
     * 减小
     */
    private void startDecrease() {
        setEnabled(false);
        isIncrease = false;
        mHandler.sendEmptyMessageDelayed(IS_SLIDE_DECREASE, 40);
    }

    public void startScroll() {
        if (isIncrease) {
            startDecrease();
        } else {
            startIncrease();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                canClick = judgeCanClick(event.getX(), event.getY());
                if (!canClick) {
                    return super.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (foldListener != null && canClick) {
                    foldListener.onFold(isIncrease, sfb);
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean judgeCanClick(float x, float y) {
        if (isIncrease) {
            if (x < width && y < height) {
                return true;
            } else {
                return false;
            }
        } else {
            if (x < startCenter.x * 2 && y < startCenter.y * 2) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void setFoldListener(FoldListener foldListener) {
        this.foldListener = foldListener;
    }

    public interface FoldListener {
        /**
         * 监听控件的点击事件，继而增加或者减少
         *
         * @param isIncrease 增加减少的flag
         * @param sfb        控件参数
         */
        void onFold(boolean isIncrease, StretchableFloatingButton sfb);
    }
}