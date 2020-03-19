package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.view.reversal.EasyFlipView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReversalActivity extends AppCompatActivity {

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            return false;
        }
    });

    @BindView(R.id.flip_view)
    EasyFlipView flipView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reversal);
        ButterKnife.bind(this);
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
