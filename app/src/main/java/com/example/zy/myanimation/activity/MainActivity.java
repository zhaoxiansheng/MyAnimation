package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.adapter.TestAdapter;
import com.example.zy.myanimation.anim.AutoIncrementUtil;
import com.example.zy.myanimation.bean.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Create on 17/10/31
 *
 * @author zhaoy
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Button scrollBtn = findViewById(R.id.scroll_anim_btn);
        Button calendarBtn = findViewById(R.id.calendar_view);
        Button messengerBtn = findViewById(R.id.messenger_btn);
        scrollBtn.setOnClickListener(this);
        calendarBtn.setOnClickListener(this);
        messengerBtn.setOnClickListener(this);

        TextView runText = findViewById(R.id.run_text);
        AutoIncrementUtil.startAnimation(AutoIncrementUtil.INTTYPE, runText, 153261.93f, false, "公里", 1500);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scroll_anim_btn:
                ScrollAnimationActivity.startActivity(this);
                break;
            case R.id.calendar_view:
                CalendarActivity.startActivity(this);
                break;
            case R.id.messenger_btn:
                MessengerActivity.startActivity(this);
                break;
            default:
                break;
        }
    }
}
