package com.example.zy.myanimation.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created on 2017/11/1.
 *
 * @author zhaoy
 */
public class ToolUtils {

    /**
     * 测量自定义控件的宽
     *
     * @param widthMeasureSpec
     * @param result           默认大小
     * @return 返回大小
     */
    public static int measureWidth(int widthMeasureSpec, int result) {
        int specMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int specSize = View.MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == View.MeasureSpec.AT_MOST) {
            return result;
        } else if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = specSize;
        }
        return result;
    }

    /**
     * 测定自定义控件的高
     *
     * @param heightMeasureSpec
     * @param result      默认的大小
     * @return 返回大小
     */
    public static int measureHeight(int heightMeasureSpec, int result) {
        int specMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int specSize = View.MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == View.MeasureSpec.AT_MOST) {
            return result;
        } else if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = specSize;
        }
        return result;
    }

    /**
     * 获取当前星期几
     *
     * @param millseconds 时间
     * @return 返回中文星期
     */
    public static String chineseWeekDay(long millseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(new Date(millseconds));
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        return weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * 获取当前星期几
     *
     * @param millseconds 时间
     * @return 返回数字
     */
    public static int numberWeekDay(long millseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(new Date(millseconds));
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 获取当前月份
     *
     * @param month 月份
     * @return 返回中文月份
     */
    public static String monthDay(int month) {
        String[] monthDays = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        return monthDays[month - 1];
    }

    /**
     * 把获取某年某月第一天的毫秒数
     *
     * @param year  年份
     * @param month 月份
     * @return 毫秒数
     * @throws ParseException
     */
    public static long millSeconds(int year, int month) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return simpleDateFormat.parse(year + "-" + month + "-01").getTime();
    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year  年
     * @param month 月
     * @return 最后一天
     */
    public static int getLastDayOfMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 0;
    }

    /**
     * 是否闰年
     *
     * @param year 年
     * @return
     */
    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 页面跳转
     * @param activity 当前的activity
     * @param tClass 即将跳转的activity
     * @param <T>
     */
    public static<T extends Activity> void startActivity(Activity activity, Class<T> tClass) {
        Intent intent = new Intent(activity, tClass);
        activity.startActivity(intent);
    }
}
