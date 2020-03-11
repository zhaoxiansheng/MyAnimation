package com.example.zy.myanimation.view.run;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import java.math.BigDecimal;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 顶部title,显示销量数字跑动效果View
 * Created on 2017/6/5.
 *
 * @author zhaoy
 */
public class NumberRunView extends AppCompatTextView {

    /**
     * 延迟
     */
    private static final int DELAY = 1 / 10;
    /**
     * 保留小数位数  默认2为
     */
    private static final int DECIMALS_COUNT = 2;
    private static final int START_RUN = 101;
    /**
     * 跑的次数
     */
    private static final int RUN_COUNT = 100;
    private double speed;
    private double startNum;
    private double endNum;
    /**
     * 保留小数位数
     */
    private int decimals = DECIMALS_COUNT;
    /**
     * 每次跑的次数
     */
    private int runCount = RUN_COUNT;
    /**
     * 动画延迟
     */
    private int delayMillis = DELAY;
    private boolean isAniming;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == START_RUN) {
                if (speed == 0) {
                    if (endNum != 0) {
                        speed = getSpeed();
                        startNum = speed;
                    } else {
                        return false;
                    }
                }
                isAniming = !running();
                if (isAniming) {
                    mHandler.sendEmptyMessageDelayed(START_RUN, delayMillis);
                } else {
                    speed = 0;
                    startNum = 0;
                }
            }
            return false;
        }
    });

    public NumberRunView(Context context) {
        super(context);
    }

    public NumberRunView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberRunView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 开始数字跳动动画
     *
     * @return 动画是否结束
     */
    private boolean running() {
        setText(String.valueOf(withDEC(String.valueOf(startNum))));
        startNum += speed;
        if (startNum >= endNum) {
            setText(String.valueOf(withDEC(String.valueOf(endNum))));
            return true;
        }
        return false;
    }

    /**
     * 计算速度
     *
     * @return 速度
     */
    private double getSpeed() {
        double speedFloat = withDEC(String.valueOf(endNum / runCount)).doubleValue();
        return speedFloat;
    }

    /**
     * 判断是否是非负数
     *
     * @return 是否是非负数
     */
    private boolean isNumber(double num) {
        if (num <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 取整四舍五入 保留小数
     *
     * @param num 数字
     * @return 保留后的数字
     */
    private BigDecimal withDEC(String num) {
        if (num == null || "".equals(num)){
            return new BigDecimal("0");
        } else {
            BigDecimal b = new BigDecimal(num).setScale(decimals, BigDecimal.ROUND_HALF_UP);
            return b;
        }
    }

    /**
     * 设置显示的数字
     *
     * @param num 显示的数字
     */
    public void setShowNum(double num) {
        setShowNum(num, DECIMALS_COUNT);
    }

    /**
     * 设置显示的数字
     *
     * @param num 显示的数字
     * @param decimals 要保留的小数位
     */
    public void setShowNum(double num, int decimals) {
        if (!isNumber(num)) {
            return;
        }
        setText(String.valueOf(num));
        setDecimals(decimals);
    }

    /**
     * 开始
     */
    public void startRun() {
        if (isAniming) {
            return;
        }
        if (isNumber(Double.parseDouble(getText().toString()))) {
            endNum = withDEC(getText().toString()).doubleValue();
            mHandler.sendEmptyMessage(START_RUN);
        }
    }

    public int getDecimals() {
        return decimals;
    }

    /**
     * 设置保留的小数位     0:不保留小数
     *
     * @param decimals 保留位数
     */
    public void setDecimals(int decimals) {
        if (decimals >= 0) {
            this.decimals = decimals;
        }
        setText(String.valueOf(withDEC(getText().toString())));

    }

    public int getRunCount() {
        return runCount;
    }

    /**
     * 设置动画跑的次数
     *
     * @param runCount 跑动次数
     */
    public void setRunCount(int runCount) {
        if (runCount <= 0) {
            return;
        }
        this.runCount = runCount;
    }

    public int getDelayMillis() {
        return delayMillis;
    }

    /**
     * 设置动画延迟
     *
     * @param delayMillis 延迟时间
     */
    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }

}
