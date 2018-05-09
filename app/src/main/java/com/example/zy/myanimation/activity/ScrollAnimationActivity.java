package com.example.zy.myanimation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.view.scroll.ScrollAnimView;
import com.example.zy.myanimation.view.scroll.Triangle;

/**
 * Create on 17/10/31
 * 数字下落
 *
 * @author zhaoy
 */
public class ScrollAnimationActivity extends AppCompatActivity implements View.OnClickListener {

    private ScrollAnimView scrollAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_animation);
        initView();
    }

    private void initView() {
        scrollAnimView = findViewById(R.id.scroll_view1);
        Triangle triangle = findViewById(R.id.triangle);
        Button startBtn = findViewById(R.id.start_anim_btn);
        Button endBtn = findViewById(R.id.end_anim_btn);
        startBtn.setOnClickListener(this);
        endBtn.setOnClickListener(this);

        scrollAnimView.setWinningNum("5");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_anim_btn:
                scrollAnimView.startMiddleAnimation(100);
                break;
            case R.id.end_anim_btn:
                scrollAnimView.clearAnimation();
                break;
            default:
                break;
        }
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, ScrollAnimationActivity.class);
        activity.startActivity(intent);
    }
}
