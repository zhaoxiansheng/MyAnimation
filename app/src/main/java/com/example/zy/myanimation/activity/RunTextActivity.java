package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.anim.AutoIncrementUtil;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RunTextActivity extends AppCompatActivity {

    @BindView(R.id.run_text)
    TextView runText;
    @BindView(R.id.run_text1)
    TextView runText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_text);
        ButterKnife.bind(this);
        AutoIncrementUtil.startAnimation(AutoIncrementUtil.INT_TYPE, runText, "41532601", true, "元", 1500);
        AutoIncrementUtil.startAnimation(AutoIncrementUtil.INT_TYPE, runText1, "41532601", true, "元", 1500);
    }
}
