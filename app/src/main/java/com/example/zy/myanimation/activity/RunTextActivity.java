package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.anim.AutoIncrementUtil;

import androidx.appcompat.app.AppCompatActivity;

public class RunTextActivity extends AppCompatActivity {

    TextView runText;
    TextView runText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_text);

        runText = findViewById(R.id.run_text);

        runText1 = findViewById(R.id.run_text1);

        AutoIncrementUtil.startAnimation(AutoIncrementUtil.INT_TYPE, runText, "41532601", true, "元", 1500);
        AutoIncrementUtil.startAnimation(AutoIncrementUtil.INT_TYPE, runText1, "41532601", true, "元", 1500);
    }
}
