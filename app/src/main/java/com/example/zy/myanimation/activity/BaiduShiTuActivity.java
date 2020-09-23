package com.example.zy.myanimation.activity;

import android.os.Bundle;

import com.example.zhaoy.eyepetizer.net.BaseObserver;
import com.example.zy.myanimation.MyApplication;
import com.example.zy.myanimation.R;
import com.example.zy.myanimation.bean.Test;
import com.example.zy.myanimation.net.HttpRequest;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;

public class BaiduShiTuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_shi_tu);

        Observable observable = HttpRequest.getCategories();
        observable.subscribe(new BaseObserver<Test>(MyApplication.getContext(), false) {
            @Override
            protected void onSuccess(Test o) {

            }
        });
    }
}
