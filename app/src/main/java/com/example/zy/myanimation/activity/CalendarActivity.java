package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.view.calendar.WheelCalendar;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Create on 2017/11/1
 *
 * @author zhaoy
 */
public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        WheelCalendar wheelCalendar = new WheelCalendar(System.currentTimeMillis());
        Toast.makeText(this, wheelCalendar.year + "/" + wheelCalendar.month + "/" + wheelCalendar.day + "/"
                + wheelCalendar.hour + "/" + wheelCalendar.minute + "/" + wheelCalendar.week + "/"
                + wheelCalendar.weekDay, Toast.LENGTH_SHORT).show();
    }
}
