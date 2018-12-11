package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.lottie.Cancellable;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.example.zy.myanimation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LottieTestActivity extends AppCompatActivity {

    @BindView(R.id.lottie)
    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie_test);
        ButterKnife.bind(this);
    }
}
