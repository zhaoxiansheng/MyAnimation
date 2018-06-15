package com.example.zy.myanimation.view.jumptext;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.WindowManager;

import com.example.zy.myanimation.R;

/**
 * 2018/06/13
 *
 * @author zhaoy
 */
public class PercentTextView extends AppCompatTextView {

    private float mTextSizePercent = 1f;
    private int baseScreenHeight = 1920;

    public PercentTextView(Context context) {
        super(context);
        setDefaultPercent(context);
        setTextSize(getTextSize());
    }

    public PercentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        setDefaultPercent(context);
        setTextSize(getTextSize());
    }

    public PercentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
        setDefaultPercent(context);
        setTextSize(getTextSize());
    }

    @Override
    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    @Override
    public void setTextSize(int unit, float size) {
        float varSize = size;
        varSize = (float) (int) (varSize * mTextSizePercent);
        super.setTextSize(unit, varSize);
    }

    public void setTextSizePercent(int unit, float mTextSizePercent) {
        this.mTextSizePercent = mTextSizePercent;
        setTextSize(unit, getTextSize());
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PercentTextView);
        baseScreenHeight = ta.getInt(R.styleable.PercentTextView_baseScreenHeight, baseScreenHeight);
        ta.recycle();
    }

    /**
     * 设置默认的百分比
     *
     * @param context 上下文
     */
    private void setDefaultPercent(Context context) {
        float screenHeight = (float) getScreenHeight(context);
        mTextSizePercent = screenHeight / baseScreenHeight;
    }

    /**
     * 获取当前设备屏幕的高度
     *
     * @param context 上下文
     * @return 返回屏幕高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm != null ? wm.getDefaultDisplay().getHeight() : 0;
    }
}
