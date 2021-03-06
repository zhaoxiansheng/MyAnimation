package com.example.zy.myanimation.view.jumptext;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 2018/06/13
 *
 * @author zhaoy
 */
public class JumpShowTextView extends FrameLayout {

    private int allTime = 700;

    private PercentTextView placeHolder = null;

    private PercentTextView realTextView = null;

    private boolean withAnimation = true;

    public JumpShowTextView(@NonNull Context context) {
        super(context);
    }

    public JumpShowTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public JumpShowTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
