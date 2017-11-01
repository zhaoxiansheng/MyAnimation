package com.example.zy.myanimation.view.calendar;

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
 * Created on 2017/11/1.
 *
 * @author zhaoy
 */
public class CalendarView extends View {

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
     * 默认的行数
     */
    private int col = 5;
    /**
     * 默认的列数
     */
    private int row = 7;

    /**
     * 自定义画笔
     */
    private Paint mPaint;
    /**
     * title
     */
    private Rect textRect;
    private String year, month;

    public CalendarView(Context context) {
        super(context);
        initAttrs(context, null, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, 0);
        roundColor = array.getColor(R.styleable.ScrollAnimView_round_color, ContextCompat.getColor(context, R.color.colorPrimary));
        roundRadius = array.getDimension(R.styleable.ScrollAnimView_round_radius, 10);
        textColor = array.getColor(R.styleable.ScrollAnimView_text_color, Color.WHITE);
        textSize = array.getDimension(R.styleable.ScrollAnimView_text_size, 30);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //设置是否抖动，如果不设置感觉就会有一些僵硬的线条，如果设置图像就会看的更柔和一些，
        mPaint.setDither(true);
        textRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void drawMouth(Canvas canvas) {
        mPaint.setColor(textColor);

    }
}
