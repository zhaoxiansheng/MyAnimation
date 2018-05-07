package com.example.zy.myanimation.view.scroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.zy.myanimation.R;

import java.util.ArrayList;

/**
 * Created on 2018/5/7.
 *
 * @author zhaoy
 */
public class MyView extends ViewGroup {

    private RelativeLayout mLayout;

    private ArrayList<String> winningNum;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initAttrs(Context context) {
        mLayout = new RelativeLayout(context);
        addView(mLayout);

        ScrollAnimView scrollAnimView0 = new ScrollAnimView(context);
        ScrollAnimView scrollAnimView1 = new ScrollAnimView(context);
        ScrollAnimView scrollAnimView2 = new ScrollAnimView(context);
        ScrollAnimView scrollAnimView3 = new ScrollAnimView(context);
        ScrollAnimView scrollAnimView4 = new ScrollAnimView(context);
        ScrollAnimView scrollAnimView5 = new ScrollAnimView(context);
        ScrollAnimView scrollAnimView6 = new ScrollAnimView(context);
        ScrollAnimView scrollAnimView7 = new ScrollAnimView(context);
        ScrollAnimView scrollAnimView8 = new ScrollAnimView(context);
        ScrollAnimView scrollAnimView9 = new ScrollAnimView(context);
        ScrollAnimView scrollAnimView10 = new ScrollAnimView(context);
        ScrollAnimView scrollAnimView11 = new ScrollAnimView(context);
        ScrollAnimView scrollAnimView12 = new ScrollAnimView(context);
        ScrollAnimView scrollAnimView13 = new ScrollAnimView(context);
        ScrollAnimView scrollAnimView14 = new ScrollAnimView(context);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLayout.getLayoutParams();

        mLayout.addView(scrollAnimView0);
        mLayout.addView(scrollAnimView1);
        mLayout.addView(scrollAnimView2);
        mLayout.addView(scrollAnimView3);
        mLayout.addView(scrollAnimView4);
        mLayout.addView(scrollAnimView5);
        mLayout.addView(scrollAnimView6);
        mLayout.addView(scrollAnimView7);
        mLayout.addView(scrollAnimView8);
        mLayout.addView(scrollAnimView9);
        mLayout.addView(scrollAnimView10);
        mLayout.addView(scrollAnimView11);
        mLayout.addView(scrollAnimView12);
        mLayout.addView(scrollAnimView13);
        mLayout.addView(scrollAnimView14);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void setWinningNum(ArrayList<String> winningNum) {
        this.winningNum = winningNum;
    }
}
