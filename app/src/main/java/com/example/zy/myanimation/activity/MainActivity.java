package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.zy.myanimation.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

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
        Button scrollBtn = (Button) findViewById(R.id.scroll_anim_btn);
        scrollBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.scroll_anim_btn:
                ScrollAnimationActivity.startActivity(this);
                break;
            default:
                break;
        }
    }
}
