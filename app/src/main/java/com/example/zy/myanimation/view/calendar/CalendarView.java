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
import com.example.zy.myanimation.utils.ToolUtils;

import java.text.ParseException;

/**
 * Created on 2017/11/1.
 *
 * @author zhaoy
 */
public class CalendarView extends View {

    /**
     * 圆的颜色
     */
    private int circleColor;
    /**
     * 数字的颜色
     */
    private int dateColor;
    /**
     * 数字字体大小
     */
    private float dateSize;
    /**
     * 圆的半径
     */
    private float circleRadius;
    /**
     * 默认的行数
     */
    private int col = 6;
    /**
     * 默认的列数
     */
    private int row = 7;

    /**
     * 默认行列宽高
     */
    private float colHeight;
    private float colWidth;
    private float rowHeight;
    private float rowWidth;
    /**
     * 自定义画笔
     */
    private Paint mPaint;
    /**
     * title
     */
    private Rect textRect;
    private int textLength;
    private String year, month;

    private WheelCalendar wheelCalendar;

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
        circleColor = array.getColor(R.styleable.CalendarView_circle_color, ContextCompat.getColor(context, R.color.colorPrimary));
        circleRadius = array.getDimension(R.styleable.CalendarView_circle_radius, 10);
        dateColor = array.getColor(R.styleable.CalendarView_date_color, Color.WHITE);
        dateSize = array.getDimension(R.styleable.CalendarView_date_size, 30);
        rowHeight = array.getDimension(R.styleable.CalendarView_date_row_height, 35);
        rowWidth = array.getDimension(R.styleable.CalendarView_date_row_height, 25);
        colHeight = array.getDimension(R.styleable.CalendarView_date_col_width, 35);
        colWidth = array.getDimension(R.styleable.CalendarView_date_col_width, 25);
        array.recycle();

        mPaint = new Paint();
        textRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTitle(canvas);
        drawWeek(canvas);
        try {
            drawMonth(canvas);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(ToolUtils.measureWidth(widthMeasureSpec, 500), ToolUtils.measureHeight(heightMeasureSpec, 500));
    }

    private void drawTitle(Canvas canvas) {
        wheelCalendar = new WheelCalendar(System.currentTimeMillis());
        year = String.valueOf(wheelCalendar.year) + "年";
        month = ToolUtils.monthDay(wheelCalendar.month);
        String title = year + " " + month;

        initPaint();
        mPaint.getTextBounds(title, 0, title.length(), textRect);
        canvas.drawText(year + " " + month, getMeasuredWidth() / 2 - textRect.width() / 2,
                getMeasuredHeight() / 2 - textRect.height() / 2, mPaint);

    }

    private void drawWeek(Canvas canvas) {
        initPaint();
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        for (int i = 0; i < weekDays.length; i++) {
            mPaint.getTextBounds(weekDays[i], 0, weekDays[i].length(), textRect);
            canvas.drawText(weekDays[i], getMeasuredWidth() / 2 - textRect.width() / 2 - textRect.width() * (3 - i) - colWidth * (3 - i),
                    getMeasuredHeight() / 2 + textRect.height() / 2 + rowHeight, mPaint);
        }
        textLength = textRect.width();
    }

    private void drawMonth(Canvas canvas) throws ParseException {
        initPaint();
        int weekCount = ToolUtils.numberWeekDay(ToolUtils.millSeconds(wheelCalendar.year, wheelCalendar.month));
        int monthCount = ToolUtils.getLastDayOfMonth(wheelCalendar.year, wheelCalendar.month);
        if (weekCount + monthCount + 1 > row * 5) {
            col = 6;
        } else {
            col = 5;
        }
        for (int j = 0; j < col; j++) {
            for (int i = 0; i < row; i++) {
                if (j == 0) {
                    if (i > weekCount - 1 && i < 7) {
                        mPaint.getTextBounds(String.valueOf(i + 1 - weekCount), 0, String.valueOf(i + 1 - weekCount).length(), textRect);
                        canvas.drawText(String.valueOf(i + 1 - weekCount),
                                getMeasuredWidth() / 2 - textRect.width() / 2 - textLength * (weekCount - i) - colWidth * (weekCount - i),
                                getMeasuredHeight() / 2 + textRect.height() / 2 + rowHeight * (j + 2), mPaint);
                    }
                } else {
                    if (j == col - 1) {
                        if ((j + 1) * (i + 1) + weekCount <= monthCount) {
                            mPaint.getTextBounds(String.valueOf(i + j * 7 + 1 - weekCount), 0, String.valueOf(i + j * 7 + 1 - weekCount).length(), textRect);
                            canvas.drawText(String.valueOf(i + j * 7 + 1 - weekCount),
                                    getMeasuredWidth() / 2 - textRect.width() / 2 - textLength * (3 - i) - colWidth * (3 - i),
                                    getMeasuredHeight() / 2 + textRect.height() / 2 + rowHeight * (j + 2), mPaint);
                        }
                    } else {
                        mPaint.getTextBounds(String.valueOf(i + j * 7 + 1 - weekCount), 0, String.valueOf(i + j * 7 + 1 - weekCount).length(), textRect);
                        canvas.drawText(String.valueOf(i + j * 7 + 1 - weekCount),
                                getMeasuredWidth() / 2 - textRect.width() / 2 - textLength * (3 - i) - colWidth * (3 - i),
                                getMeasuredHeight() / 2 + textRect.height() / 2 + rowHeight * (j + 2), mPaint);
                    }
                }
            }
        }
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        if (mPaint != null) {
            mPaint.reset();
            mPaint.setColor(dateColor);
            mPaint.setTextSize(dateSize);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            //设置是否抖动，如果不设置感觉就会有一些僵硬的线条，如果设置图像就会看的更柔和一些，
            mPaint.setDither(true);
        }
    }
}
