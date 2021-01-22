package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.anim.demo.AnimatorDemo;

import androidx.appcompat.app.AppCompatActivity;

public class AnimationDemoActivity extends AppCompatActivity implements View.OnClickListener {

    TextView text;
    TextView text1;
    TextView text2;
    TextView customText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_demo);

        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text = findViewById(R.id.text);
        customText = findViewById(R.id.custom_text);

        text.setOnClickListener(this);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        customText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text:
                AnimatorDemo.animatorProperties(text);
                break;
            case R.id.text1:
                AnimatorDemo.animatorKeyFrame(text1);
                break;
            case R.id.text2:
                AnimatorDemo.animatorKeyFrameInterpolator(text2);
                break;
            case R.id.custom_text:
                AnimatorDemo.animatorCustomProperty(customText);
                break;
        }
    }
}
