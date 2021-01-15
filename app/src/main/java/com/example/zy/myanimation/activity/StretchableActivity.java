package com.example.zy.myanimation.activity;

import android.os.Bundle;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.view.stretchable.StretchableFloatingButton;

import androidx.appcompat.app.AppCompatActivity;

public class StretchableActivity extends AppCompatActivity {

    StretchableFloatingButton stretchFloatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stretchable);

        stretchFloatBtn = findViewById(R.id.stretch_float_btn);

        stretchFloatBtn.setFoldListener((isIncrease) -> {
            stretchFloatBtn.startScroll();
        });
    }
}
