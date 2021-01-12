package com.example.zy.myanimation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.zy.myanimation.R;

public class JetpackActivity extends AppCompatActivity {

    private static final String TAG = JetpackActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
    }
}