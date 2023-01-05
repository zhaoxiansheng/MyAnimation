package com.example.zy.myanimation;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.example.zy.myanimation.utils.shitu.AuthService;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import androidx.multidex.MultiDex;

/**
 * Created by zhaoy on 2017/11/30.
 *
 * @author zhaoy
 */
public class MyApplication extends Application {

    public static boolean isStart;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);

        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

//        IApi iApi = RetrofitFactory.INSTANCE.getRetrofitGsonService().create(IApi.class);
//        iApi.getBaiduShitu();

        new Thread(new Runnable() {
            @Override
            public void run() {
                AuthService.getAuth();
            }
        }).start();

        MultiDex.install(this);
    }
}
