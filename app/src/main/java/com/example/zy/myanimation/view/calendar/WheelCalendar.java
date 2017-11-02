package com.example.zy.myanimation.view.calendar;

import com.example.zy.myanimation.utils.ToolUtils;

import java.util.Calendar;

/**
 * Created on 2017/11/1.
 *
 * @author zhaoy
 */
public class WheelCalendar {

    public int year, month, day, hour, minute, week;
    public String weekDay;

    public WheelCalendar(long millseconds) {
        initDate(millseconds);
    }

    private void initDate(long millseconds) {
        if (millseconds == 0) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(millseconds);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        week = calendar.get(Calendar.WEEK_OF_YEAR);
        weekDay = ToolUtils.chineseWeekDay(millseconds);
    }
}
