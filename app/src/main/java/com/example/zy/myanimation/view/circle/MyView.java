package com.example.zy.myanimation.view.circle;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.example.zy.myanimation.R;

import androidx.annotation.Nullable;

/**
 * Created on 2018/5/4.
 *
 * @author zhaoy
 */
public class MyView extends ViewGroup {

    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 背景颜色
     */
    private int bgColor;

    /**
     * 圆中文字颜色
     */
    private int textColor;

    /**
     * 文字大小
     */
    private int textSize;

    /**
     * 圆心坐标
     */
    private Point circleCenter;
    private int x;
    private int y;

    /**
     * 圆中文字控件
     */
    private TextView tvContent;

    /**
     * 圆中文字内容
     */
    private String text;

    private int width;
    private int height;

    private View child;
    private int tvWidth;
    private int tvHeight;

    private int circleRadius;

    public MyView(Context context) {
        super(context);
        initAttrs(context, null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        circleCenter = new Point();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyView);
        textColor = typedArray.getColor(R.styleable.MyView_my_view_text_color, Color.BLACK);
        textSize = typedArray.getInteger(R.styleable.MyView_my_view_text_size, 20);
        text = typedArray.getString(R.styleable.MyView_my_view_text);
        bgColor = typedArray.getColor(R.styleable.MyView_my_view_bg_color, Color.RED);
        typedArray.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);

        tvContent = new TextView(context);
        tvContent.setText(text);
        tvContent.setTextColor(textColor);
        tvContent.setTextSize(textSize);

        tvContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });
        addView(tvContent);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawCircle(circleCenter.x, circleCenter.y, circleRadius, mPaint);
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (width == 0) {
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);

            if (widthMode == MeasureSpec.AT_MOST) {
                width = 60;
                x = width / 2;
            } else if (widthMode == MeasureSpec.EXACTLY) {
                width = MeasureSpec.getSize(widthMeasureSpec);
                x = width / 2;
            } else {
                width = MeasureSpec.getSize(widthMeasureSpec);
                x = width / 2;
            }

            if (heightMode == MeasureSpec.AT_MOST) {
                height = 60;
                y = height / 2;
            } else if (heightMode == MeasureSpec.EXACTLY) {
                height = MeasureSpec.getSize(heightMeasureSpec);
                y = height / 2;
            } else {
                height = MeasureSpec.getSize(heightMeasureSpec);
                y = height / 2;
            }
        }

        if (y >= x) {
            circleRadius = x;
        } else {
            circleRadius = y;
        }

        circleCenter.set(x, y);

        child = this.getChildAt(0);
        measureChild(child, width, height);
        tvHeight = child.getMeasuredHeight();
        tvWidth = child.getMeasuredWidth();

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        child.layout(x - tvWidth / 2, y - tvHeight / 2, x + tvWidth / 2,
                y + tvHeight / 2);
    }

    public void startAnimation() {
        final int maxWidth = ScreenUtils.getScreenWidth();
        int maxHeight = ScreenUtils.getScreenHeight();
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, "translationX", 0f, maxWidth, 0f);
        //设置无限重复
//        anim.setRepeatCount(ValueAnimator.INFINITE);
        //设置重复模式
//        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setDuration(2000);
        anim.setInterpolator(new BounceInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (float) animation.getAnimatedValue();
                float y = 0.001f * x * x;
                if (Math.round(x + width) >= maxWidth) {
                    layout(Math.round(x), Math.round(y), Math.round(x + width), Math.round(y + height));
                } else {
                    System.out.println(x + "/" + maxWidth);
                    layout(Math.round(x), Math.round(y), Math.round(x + width), Math.round(y + height));
                }
                postInvalidate();
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(anim);
        animatorSet.start();
    }
}
