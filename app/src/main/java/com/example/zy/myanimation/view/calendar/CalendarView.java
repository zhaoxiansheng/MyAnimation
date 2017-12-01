package com.example.zy.myanimation.view.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zy.myanimation.R;
import com.orhanobut.logger.Logger;

/**
 * Created on 2017/11/1.
 *
 * @author zhaoy
 */
public class CalendarView extends ViewGroup {

    /**
     * 圆的颜色
     */
    private int circleColor;
    /**
     * 日期的颜色
     */
    private int dateDayColor;
    /**
     * title的颜色
     */
    private int dateTitleColor;
    /**
     * 日期字体大小
     */
    private float dateDaySize;
    private float dateTitleSize;
    /**
     * 圆的半径
     */
    private float circleRadius;

    /**
     * 默认行列宽高
     */
    private float colHeight;
    private float colWidth;
    private float rowHeight;
    private float rowWidth;

    private WheelCalendar wheelCalendar;

    /**
     * 头布局
     */
    private CalendarViewPager pager;
    private ImageView imgPast;
    private ImageView imgFuture;
    private TextView title;
    private LinearLayout topBar;
    private Drawable rightArrowMask;
    private Drawable leftArrowMask;

    private final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == imgFuture) {
                pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                Logger.d("??????????????????");
            } else if (v == imgPast) {
                pager.setCurrentItem(pager.getCurrentItem() - 1, true);
                Logger.d("////////////////////");
            }
        }
    };

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
        wheelCalendar = new WheelCalendar(System.currentTimeMillis());
        imgPast = new ImageView(getContext());
        imgPast.setContentDescription(getContext().getString(R.string.previous));
        imgFuture = new ImageView(getContext());
        imgFuture.setContentDescription(getContext().getString(R.string.next));
        title = new TextView(getContext());
        topBar = new LinearLayout(getContext());
        topBar.setOrientation(LinearLayout.HORIZONTAL);
        topBar.setClipChildren(false);
        topBar.setClipToPadding(false);

        pager = new CalendarViewPager(getContext());

        imgPast.setOnClickListener(onClickListener);
        imgFuture.setOnClickListener(onClickListener);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, 0);
        try {
            circleColor = array.getColor(R.styleable.CalendarView_circle_color, ContextCompat.getColor(context, R.color.colorPrimary));
            circleRadius = array.getDimension(R.styleable.CalendarView_circle_radius, 10);

            dateDayColor = array.getColor(R.styleable.CalendarView_date_day_color, Color.WHITE);
            dateDaySize = array.getDimension(R.styleable.CalendarView_date_day_size, 20);
            dateTitleColor = array.getColor(R.styleable.CalendarView_date_title_color, Color.WHITE);
            dateTitleSize = array.getDimensionPixelSize(R.styleable.CalendarView_date_title_size, 30);

            rowHeight = array.getDimension(R.styleable.CalendarView_date_row_height, 35);
            rowWidth = array.getDimension(R.styleable.CalendarView_date_row_height, 25);
            colHeight = array.getDimension(R.styleable.CalendarView_date_col_width, 35);
            colWidth = array.getDimension(R.styleable.CalendarView_date_col_width, 25);

            Drawable leftMask = array.getDrawable(R.styleable.CalendarView_calendar_img_previous);
            if (leftMask == null) {
                leftMask = ContextCompat.getDrawable(getContext(), R.drawable.mcv_action_previous);
            }
            setLeftArrowMask(leftMask);

            Drawable rightMask = array.getDrawable(R.styleable.CalendarView_calendar_img_next);
            if (rightMask == null) {
                rightMask = ContextCompat.getDrawable(getContext(), R.drawable.mcv_action_next);
            }
            setRightArrowMask(rightMask);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            array.recycle();
        }

        setupChildren();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 如果是warp_content情况下，记录宽和高
        int width = 0;
        int height = 0;

        //记录最大宽度
        int lineHeight = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();

            int childWidth = childView.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;
            // 当前子空间实际占据的高度
            int childHeight = childView.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;

            // 取最大的
            height = Math.max(lineHeight, childHeight);
            // 重新开始记录
            lineHeight = childHeight;
            // 叠加当前宽度，
            width += childWidth;
        }
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        final int parentLeft = getPaddingLeft();
        final int parentWidth = right - left - parentLeft - getPaddingRight();

        int childTop = getPaddingTop();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            int delta = (parentWidth - width) / 2;
            int childLeft = parentLeft + delta;

            child.layout(childLeft, childTop, childLeft + width, childTop + height);

            childTop += height;
        }
    }

    /**
     * 设置头布局
     */
    private void setupChildren() {
        topBar = new LinearLayout(getContext());
        topBar.setOrientation(LinearLayout.HORIZONTAL);
        topBar.setClipChildren(false);
        topBar.setClipToPadding(false);
        addView(topBar, new MarginLayoutParams(LayoutParams.MATCH_PARENT, 100));

        imgPast.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        topBar.addView(imgPast, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));

        title.setGravity(Gravity.CENTER);
        title.setText(String.valueOf(wheelCalendar.year) + "年" + String.valueOf(wheelCalendar.month) + "月");
        title.setTextColor(dateTitleColor);
        title.setTextSize(dateTitleSize);
        topBar.addView(title, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 5));

        imgFuture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        topBar.addView(imgFuture, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));

        pager.setId(R.id.mcv_pager);
        pager.setOffscreenPageLimit(1);
        addView(pager, new MarginLayoutParams(LayoutParams.MATCH_PARENT, 200));
    }

    public void setLeftArrowMask(Drawable icon) {
        leftArrowMask = icon;
        imgPast.setImageDrawable(icon);
    }

    public void setRightArrowMask(Drawable icon) {
        rightArrowMask = icon;
        imgFuture.setImageDrawable(icon);
    }
}