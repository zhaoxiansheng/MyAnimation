package com.example.zy.myanimation.view.scroll;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.utils.ToolUtils;

/**
 * Created  on 2018/4/26.
 *
 * @author zhaoy
 */
public class ScrollAnimView extends ViewGroup {

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
    private boolean isFirstInit = false;
    /**
     * 中间的数字的实时点
     */
    private MyPoint middlePoint;

    private String middleNum = "9";

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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScrollAnimView, defStyleAttr, 0);
        roundColor = array.getColor(R.styleable.ScrollAnimView_round_color, ContextCompat.getColor(context, R.color.colorPrimary));
        roundRadius = array.getDimension(R.styleable.ScrollAnimView_round_radius, 50);
        textColor = array.getColor(R.styleable.ScrollAnimView_text_color, Color.WHITE);
        textSize = array.getDimension(R.styleable.ScrollAnimView_text_size, 30);
        array.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(textSize);
        textRect = new Rect();
        //得到数字矩形的宽高，以用来画数字的时候纠正数字的位置
        mPaint.getTextBounds(middleNum, 0, middleNum.length(), textRect);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (!isFirstInit) {
            middlePoint = new MyPoint(getMeasuredWidth() / 2 - textRect.width() / 2,
                    getMeasuredHeight() / 2 - roundRadius + textRect.height());
            drawText(canvas);
            //开始动画
            startMiddleAnimation();
            isFirstInit = true;
        } else {
            drawText(canvas);
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
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, roundRadius, mPaint);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        if (isMiddleNumInvalidate) {
            canvas.drawText(middleNum, middlePoint.getX(), middlePoint.getY(), mPaint);
            isMiddleNumInvalidate = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = ToolUtils.measureWidth(widthMeasureSpec, 100);
        int height = ToolUtils.measureHeight(heightMeasureSpec, 100);

        if (width > height) {
            roundRadius = height / 2;
        } else {
            roundRadius = width / 2;
        }

        setMeasuredDimension(width, height);
    }

    /**
     * 开始动画
     */
    private void startMiddleAnimation() {
        //初始化中间数字的开始点的位置
        final MyPoint startPoint = new MyPoint(getMeasuredWidth() / 2 - textRect.width() / 2, getMeasuredHeight() / 2 - roundRadius);
        //初始化中间数字的结束点的位置
        final MyPoint endPoint = new MyPoint(getMeasuredWidth() / 2 - textRect.width() / 2, getMeasuredHeight() / 2 + roundRadius);
        ValueAnimator middleAnim = ValueAnimator.ofObject(new CustomPointEvaluator(), startPoint, endPoint);
        //监听从起始点到终点过程中点的变化,并获取点然后重新绘制界面
        middleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                middleNum = getRandom();
                middlePoint = (MyPoint) animation.getAnimatedValue();
                isMiddleNumInvalidate = true;
                invalidate();
            }
        });
        middleAnim.setDuration(450);
        middleAnim.setRepeatCount(ValueAnimator.INFINITE);
        middleAnim.start();
    }

    /**
     * 获取0-9之间的随机数
     *
     * @return 1-9 随机数
     */
    private String getRandom() {
        int random = (int) (Math.random() * 9);
        return String.valueOf(random);
    }
}
