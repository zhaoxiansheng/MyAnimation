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
import android.view.View;

import com.example.zy.myanimation.R;

/**
 * Created  on 2017/10/31.
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
    private boolean isFirstInit = false;
    /**
     * 左侧数字实时点
     */
    private MyPoint leftPoint;
    /**
     * 中间的数字的实时点
     */
    private MyPoint middlePoint;
    /**
     * 右边数字实时点
     */
    private MyPoint rightPoint;
    /**
     * 中间数字动画
     */
    private ValueAnimator leftAnim;
    private ValueAnimator middleAnim;
    private ValueAnimator rightAnim;
    private String leftNum = "9";
    private String middleNum = "9";
    private String rightNum = "9";

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
        mPaint.getTextBounds(leftNum, 0, leftNum.length(), textRect);
        mPaint.getTextBounds(rightNum, 0, rightNum.length(), textRect);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isFirstInit) {
            leftPoint = new MyPoint(getMeasuredWidth() / 2 - roundRadius / 2 - textRect.width() / 2,
                    getMeasuredHeight() / 2 - roundRadius / 2);
            middlePoint = new MyPoint(getMeasuredWidth() / 2 - textRect.width() / 2,
                    getMeasuredHeight() / 2 - roundRadius + textRect.height());
            rightPoint = new MyPoint(getMeasuredWidth() / 2 + roundRadius / 2 - textRect.width() / 2,
                    getMeasuredHeight() / 2 - roundRadius / 2);
            drawText(canvas);
            //开始动画
            startLeftAnimation();
            startMiddleAnimation();
            startRightAnimation();
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
        mPaint.setColor(roundColor);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, roundRadius, mPaint);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        if (isMiddleNumInvalidate) {
            canvas.drawText(leftNum, leftPoint.getX(), leftPoint.getY(), mPaint);
            canvas.drawText(middleNum, middlePoint.getX(), middlePoint.getY(), mPaint);
            canvas.drawText(rightNum, rightPoint.getX(), rightPoint.getY(), mPaint);
            isMiddleNumInvalidate = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //设置一个默认值，就是这个View的默认宽度为500，这个看我们自定义View的要求
        int result = 500;
        //相当于我们设置为wrap_content
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
            //相当于我们设置为match_parent或者为一个具体的值
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 500;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        return result;
    }

    /**
     * 开始动画
     */
    private void startLeftAnimation() {
        //初始化中间数字的开始点的位置
        final MyPoint startPoint = new MyPoint(getMeasuredWidth() / 2 - roundRadius / 2 - textRect.width() / 2,
                getMeasuredHeight() / 2 - roundRadius / 2);
        //初始化中间数字的结束点的位置
        final MyPoint endPoint = new MyPoint(getMeasuredWidth() / 2 - roundRadius / 2 - textRect.width() / 2,
                getMeasuredHeight() / 2 + roundRadius / 2);
        leftAnim = ValueAnimator.ofObject(new CustomPointEvaluator(), startPoint, endPoint);
        //监听从起始点到终点过程中点的变化,并获取点然后重新绘制界面
        leftAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                leftNum = getRandom();
                leftPoint = (MyPoint) animation.getAnimatedValue();
                isMiddleNumInvalidate = true;
                invalidate();
            }
        });
        leftAnim.setDuration(300);
        leftAnim.setRepeatCount(ValueAnimator.INFINITE);
        leftAnim.start();
    }

    /**
     * 开始动画
     */
    private void startMiddleAnimation() {
        //初始化中间数字的开始点的位置
        final MyPoint startPoint = new MyPoint(getMeasuredWidth() / 2 - textRect.width() / 2, getMeasuredHeight() / 2 - roundRadius);
        //初始化中间数字的结束点的位置
        final MyPoint endPoint = new MyPoint(getMeasuredWidth() / 2 - textRect.width() / 2, getMeasuredHeight() / 2 + roundRadius);
        middleAnim = ValueAnimator.ofObject(new CustomPointEvaluator(), startPoint, endPoint);
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
     * 开始动画
     */
    private void startRightAnimation() {
        //初始化中间数字的开始点的位置
        final MyPoint startPoint = new MyPoint(getMeasuredWidth() / 2 + roundRadius / 2 - textRect.width() / 2,
                getMeasuredHeight() / 2 - roundRadius / 2);
        //初始化中间数字的结束点的位置
        final MyPoint endPoint = new MyPoint(getMeasuredWidth() / 2 + roundRadius / 2 - textRect.width() / 2,
                getMeasuredHeight() / 2 + roundRadius / 2);
        rightAnim = ValueAnimator.ofObject(new CustomPointEvaluator(), startPoint, endPoint);
        //监听从起始点到终点过程中点的变化,并获取点然后重新绘制界面
        rightAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rightNum = getRandom();
                rightPoint = (MyPoint) animation.getAnimatedValue();
                isMiddleNumInvalidate = true;
                invalidate();
            }
        });
        rightAnim.setDuration(600);
        rightAnim.setRepeatCount(ValueAnimator.INFINITE);
        rightAnim.start();
    }

    /**
     * 获取0-9之间的随机数
     *
     * @return
     */
    private String getRandom() {
        int random = (int) (Math.random() * 9);
        return String.valueOf(random);
    }

    private StartAnim startAnim;
    public void setStartAnim(StartAnim startAnim) {
        this.startAnim = startAnim;
    }

    public interface StartAnim{
        void running(ValueAnimator leftAnim, ValueAnimator middleAnim, ValueAnimator rightAnim);
    }
}
