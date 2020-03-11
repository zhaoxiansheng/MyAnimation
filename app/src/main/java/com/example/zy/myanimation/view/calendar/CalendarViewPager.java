package com.example.zy.myanimation.view.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created on 2017/11/29.
 *
 * @author zhaoy
 */

public class CalendarViewPager extends ViewPager {

    private boolean pagingEnabled = true;

    public CalendarViewPager(Context context) {
        super(context);
    }

    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPagingEnabled(boolean pagingEnabled) {
        this.pagingEnabled = pagingEnabled;
    }

    public boolean isPagingEnabled() {
        return pagingEnabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return pagingEnabled && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return pagingEnabled && super.onTouchEvent(ev);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return pagingEnabled && super.canScrollVertically(direction);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return pagingEnabled && super.canScrollHorizontally(direction);
    }
}
