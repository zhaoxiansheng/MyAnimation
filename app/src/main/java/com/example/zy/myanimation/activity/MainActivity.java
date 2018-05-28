package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.anim.runnum.AutoIncrementUtil;
import com.example.zy.myanimation.utils.ToolUtils;

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
        AutoIncrementUtil.startAnimation(AutoIncrementUtil.INT_TYPE, runText, "41532601", true, "元", 1500);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scroll_anim_btn:
                ToolUtils.startActivity(this, ScrollAnimationActivity.class);
                break;
            case R.id.calendar_view:
                ToolUtils.startActivity(this, CalendarActivity.class);
                break;
            case R.id.messenger_btn:
                ToolUtils.startActivity(this, MessengerActivity.class);
                break;
            case R.id.aidl_btn:
                ToolUtils.startActivity(this, BookManagerActivity.class);
                break;
            default:
                break;
        }
    }
}
