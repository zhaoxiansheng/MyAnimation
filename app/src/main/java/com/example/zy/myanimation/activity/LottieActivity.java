package com.example.zy.myanimation.activity;

import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.example.zy.myanimation.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LottieActivity extends AppCompatActivity {

    @BindView(R.id.lottie)
    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie_test);
        ButterKnife.bind(this);
    }
}
