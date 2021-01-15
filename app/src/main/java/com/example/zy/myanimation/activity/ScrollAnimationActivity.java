package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.view.scroll.Triangle;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Create on 17/10/31
 * 数字下落
 *
 * @author zhaoy
 */
public class ScrollAnimationActivity extends AppCompatActivity implements View.OnClickListener {

    Triangle triangle;

    Button startAnimBtn;

    Button endAnimBtn;

    private ArrayList<String> winningNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_animation);
        initData();
        initView();
    }

    private void initView() {
        triangle = findViewById(R.id.triangle);
        startAnimBtn = findViewById(R.id.start_anim_btn);
        endAnimBtn = findViewById(R.id.end_anim_btn);

        startAnimBtn.setOnClickListener(this);
        endAnimBtn.setOnClickListener(this);

        triangle.setWinningNum(winningNum);
        triangle.setAnimation(true);
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
}
