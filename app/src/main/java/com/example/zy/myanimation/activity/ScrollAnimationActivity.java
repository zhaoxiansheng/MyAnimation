package com.example.zy.myanimation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.view.scroll.Triangle;

import java.util.ArrayList;

/**
 * Create on 17/10/31
 * 数字下落
 *
 * @author zhaoy
 */
public class ScrollAnimationActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> winningNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_animation);
        initData();
        initView();
    }

    private void initView() {
        Triangle triangle = findViewById(R.id.triangle);
        triangle.setWinningNum(winningNum);
        Button startBtn = findViewById(R.id.start_anim_btn);
        Button endBtn = findViewById(R.id.end_anim_btn);
        startBtn.setOnClickListener(this);
        endBtn.setOnClickListener(this);
    }

    private void initData() {
        winningNum = new ArrayList<>(15);
        winningNum.add("0");
        winningNum.add("1");
        winningNum.add("2");
        winningNum.add("3");
        winningNum.add("4");
        winningNum.add("5");
        winningNum.add("6");
        winningNum.add("7");
        winningNum.add("8");
        winningNum.add("9");
        winningNum.add("0");
        winningNum.add("1");
        winningNum.add("2");
        winningNum.add("3");
        winningNum.add("4");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_anim_btn:
                break;
            case R.id.end_anim_btn:
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
