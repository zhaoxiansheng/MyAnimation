package com.example.zy.myanimation.view.scroll;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.utils.ToolUtils;

import java.util.Random;

/**
 * Created  on 2018/5/4.
 *
 * @author zhaoy
 */
public class ScrollAnimView extends View {

    /**
     * 圆的颜色
     */
    private int roundColor;
    /**
     * 数字的颜色
     */
    private int textColor;
    /**
     * 数字字体大小
     */
    private float textSize;
    /**
     * 圆的半径
     */
    private float roundRadius;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 包裹数字的矩形
     */
    private Rect textRect;
    /**
     * 中间数字是否重绘界面
     */
    private boolean isMiddleNumInvalidate = true;
    /**
     * 是否是第一次初始化
     */
    private boolean isFirstInit = true;
    /**
     * 中间的数字的实时点
     */
    private MyPoint middlePoint;

    private String winningNum = "5";

    /**
     * 圆心坐标
     */
    private Point circleCenter;

    /**
     * 执行时间
     */
    private long firstTime;
    private ValueAnimator middleAnim;

    /**
     * 中间数字滚动的次数
     */
    private int count;
    private OnItemClickListener onItemClickListener;

    public ScrollAnimView(Context context) {
        super(context);
        initAttrs(context, null, 0);
    }

    public ScrollAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs, 0);
    }

    public ScrollAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        circleCenter = new Point();
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScrollAnimView, defStyleAttr, 0);
        roundColor = array.getColor(R.styleable.ScrollAnimView_round_color, ContextCompat.getColor(context, R.color.colorPrimary));
        textColor = array.getColor(R.styleable.ScrollAnimView_text_color, Color.WHITE);
        textSize = array.getDimension(R.styleable.ScrollAnimView_text_size, 30);
        array.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(textSize);
        textRect = new Rect();
        //得到数字矩形的宽高，以用来画数字的时候纠正数字的位置
        mPaint.getTextBounds(winningNum, 0, winningNum.length(), textRect);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.onClickListener(v);
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isFirstInit) {
            firstTime = System.currentTimeMillis();
        }
        long currentTime = System.currentTimeMillis();

        //10秒后停止
        if (currentTime - firstTime > 1000) {
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            //设置是否抖动，如果不设置感觉就会有一些僵硬的线条，如果设置图像就会看的更柔和一些，
            mPaint.setDither(true);
            mPaint.setColor(roundColor);
            canvas.drawCircle(circleCenter.x, circleCenter.y, roundRadius, mPaint);

            mPaint.setColor(textColor);
            mPaint.setTextSize(textSize);
            canvas.drawText(winningNum, circleCenter.x - textRect.width() / 2, circleCenter.y + textRect.height() / 2, mPaint);
            stopMiddleAnimation();
        } else {
            if (isFirstInit) {
                middlePoint = new MyPoint(getMeasuredWidth() / 2 - textRect.width() / 2,
                        getMeasuredHeight() / 2 - roundRadius + textRect.height());
                drawText(canvas);
                //开始动画
                startMiddleAnimation(100);
                isFirstInit = false;
            } else {
                drawText(canvas);
            }
        }

    }

    /**
     * 画圆形背景
     *
     * @param canvas 画布
     */
    private void drawText(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //设置是否抖动，如果不设置感觉就会有一些僵硬的线条，如果设置图像就会看的更柔和一些，
        mPaint.setDither(true);
        mPaint.setColor(roundColor);
        canvas.drawCircle(circleCenter.x, circleCenter.y, roundRadius, mPaint);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        if (isMiddleNumInvalidate) {
            canvas.drawText(String.valueOf(count), middlePoint.getX(), middlePoint.getY(), mPaint);
            isMiddleNumInvalidate = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = ToolUtils.measureWidth(widthMeasureSpec, 100);
        int height = ToolUtils.measureHeight(heightMeasureSpec, 100);

        int x = width / 2;
        int y = height / 2;

        if (x > y) {
            roundRadius = y;
        } else {
            roundRadius = x;
        }

        circleCenter.set(x, y);

        setMeasuredDimension(width, height);
    }

    /**
     * 开始动画
     */
    public void startMiddleAnimation(int duration) {
        //初始化中间数字的开始点的位置
        final MyPoint startPoint = new MyPoint(getMeasuredWidth() / 2 - textRect.width() / 2, getMeasuredHeight() / 2 - roundRadius);
        //初始化中间数字的结束点的位置
        final MyPoint endPoint = new MyPoint(getMeasuredWidth() / 2 - textRect.width() / 2, getMeasuredHeight() / 2 + roundRadius);
        middleAnim = ValueAnimator.ofObject(new CustomPointEvaluator(), startPoint, endPoint);
        //监听从起始点到终点过程中点的变化,并获取点然后重新绘制界面
        middleAnim.addUpdateListener((animator) -> {
            middlePoint = (MyPoint) animator.getAnimatedValue();
            float fraction = animator.getAnimatedFraction();
            if (fraction > 0.9) {
                count++;
                if (count > 9) {
                    count = 0;
                }
            }
            isMiddleNumInvalidate = true;
            postInvalidate();
        });
        middleAnim.setDuration(duration);
        middleAnim.setRepeatCount(ValueAnimator.INFINITE);
        middleAnim.start();
    }

    /**
     * 停止动画
     */
    public void stopMiddleAnimation() {
        middleAnim.cancel();
        this.clearAnimation();
    }

    public void setWinningNum(String winningNum) {
        this.winningNum = winningNum;
        postInvalidate();
    }

    public interface OnItemClickListener {
        void onClickListener(View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
