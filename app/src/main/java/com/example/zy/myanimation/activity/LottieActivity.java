package com.example.zy.myanimation.activity;

import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.example.zy.myanimation.R;

import androidx.appcompat.app.AppCompatActivity;

public class LottieActivity extends AppCompatActivity {

    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie_test);
        lottie = findViewById(R.id.lottie);
    }
}
