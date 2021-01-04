package com.autopai.common.utils.common;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayManager {
    private static volatile DisplayManager mInstance = null;

    private DisplayManager(){
    }

    public static DisplayManager getInstance(){
        if(mInstance == null) {
            synchronized (DisplayManager.class) {
                if (mInstance == null) {
                    mInstance = new DisplayManager();
                }
            }
        }
        return mInstance;
    }

    public static int getScreenWidth(Context context){
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context){
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}
