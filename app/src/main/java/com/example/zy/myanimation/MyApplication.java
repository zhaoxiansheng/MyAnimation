package com.example.zy.myanimation;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.example.zhaoy.eyepetizer.net.IApi;
import com.example.zhaoy.eyepetizer.net.RetrofitFactory;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

/**
 * Created by zhaoy on 2017/11/30.
 * @author zhaoy
 */
public class MyApplication extends Application {

    public static boolean isStart;

    public static IApi iApi;
    private static WeakReference<Context> context;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);

        Logger.addLogAdapter(new AndroidLogAdapter(){
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        iApi = RetrofitFactory.INSTANCE.getRetrofitGsonService().create(IApi.class);
    }

    public static Context getContext() {
        return context.get();
    }
}
