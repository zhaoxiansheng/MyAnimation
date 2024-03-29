package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.view.reversal.EasyFlipView;

import androidx.appcompat.app.AppCompatActivity;

public class ReversalActivity extends AppCompatActivity {

    private static Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            return false;
        }
    });
    // API 30 使用下方的方式在主线程执行
    //Executor mainExecutor = ContextCompat.getMainExecutor(this);

    EasyFlipView flipView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reversal);
        flipView = findViewById(R.id.flip_view);

        flipView.setFlipTypeFromLeft();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                flipView.flipTheView();
            }
        }, 1000);
    }
}
