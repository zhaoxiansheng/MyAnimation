package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.anim.AutoIncrementUtil;
import com.example.zy.myanimation.utils.ToolUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create on 17/10/31
 *
 * @author zhaoy
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.scroll_anim_btn)
    Button scrollAnimBtn;
    @BindView(R.id.calendar_view)
    Button calendarView;
    @BindView(R.id.messenger_btn)
    Button messengerBtn;
    @BindView(R.id.aidl_btn)
    Button aidlBtn;
    @BindView(R.id.run_text)
    TextView runText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        AutoIncrementUtil.startAnimation(AutoIncrementUtil.INT_TYPE, runText, "41532601", true, "元", 1500);
    }


    @OnClick({R.id.scroll_anim_btn, R.id.calendar_view, R.id.messenger_btn, R.id.aidl_btn})
    public void onViewClicked(View view) {
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
