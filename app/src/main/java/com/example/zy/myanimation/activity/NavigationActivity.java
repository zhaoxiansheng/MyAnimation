package com.example.zy.myanimation.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.zy.myanimation.R;

public class NavigationActivity extends AppCompatActivity {

    private static final String TAG = NavigationActivity.class.getSimpleName();
    private int[] mData = new int[]{8, 4, 5, 10, 9, 6, 12, 7, 3, 11};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
    }
}