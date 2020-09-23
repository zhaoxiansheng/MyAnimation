package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.anim.demo.AnimatorDemo;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnimationDemoActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_demo);
        ButterKnife.bind(this);
    }

    private void startAnimation() {
        AnimatorDemo.animators(text);
    }

    @OnClick({R.id.text, R.id.text1, R.id.text2})
    public void onViewClicked(View view) {
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
        }
    }
}
