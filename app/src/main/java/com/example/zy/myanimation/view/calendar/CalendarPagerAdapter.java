package com.example.zy.myanimation.view.calendar;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created zhaoy on 2017/11/30.
 * @author zhaoy
 */
public class CalendarPagerAdapter<V extends CalendarViewPager> extends PagerAdapter {

    private CalendarView calendarView;

    public CalendarPagerAdapter(CalendarView calendarView) {
        this.calendarView = calendarView;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
