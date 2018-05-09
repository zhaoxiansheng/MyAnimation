package com.example.zy.myanimation.view.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.zy.myanimation.R;

import java.util.ArrayList;

/**
 * Created on 2018/5/7.
 *
 * @author zhaoy
 */
public class Triangle extends ViewGroup {

    private ArrayList<String> winningNum;

    private ScrollAnimView scrollAnimView0;
    private ScrollAnimView scrollAnimView1;
    private ScrollAnimView scrollAnimView2;
    private ScrollAnimView scrollAnimView3;
    private ScrollAnimView scrollAnimView4;
    private ScrollAnimView scrollAnimView5;

    public Triangle(Context context) {
        super(context);
        initAttrs(context);
    }

    public Triangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context);
    }

    public Triangle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context);
    }

    private void initAttrs(Context context) {
        scrollAnimView0 = new ScrollAnimView(context);
        scrollAnimView0.setId(R.id.scrollAnimView0);
//        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT);
//        params0.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        scrollAnimView1 = new ScrollAnimView(context);
        scrollAnimView1.setId(R.id.scrollAnimView1);
//        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT);
//        params1.addRule(RelativeLayout.BELOW, R.id.scrollAnimView0);

        scrollAnimView2 = new ScrollAnimView(context);
        scrollAnimView2.setId(R.id.scrollAnimView2);
//        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT);
//        params2.addRule(RelativeLayout.BELOW, R.id.scrollAnimView0);
//        params2.addRule(RelativeLayout.END_OF, R.id.scrollAnimView1);

        scrollAnimView3 = new ScrollAnimView(context);
        scrollAnimView3.setId(R.id.scrollAnimView3);
//        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT);
//        params3.addRule(RelativeLayout.BELOW, 1);
//
//        scrollAnimView4 = new ScrollAnimView(context);
//        scrollAnimView4.setId(4);
//        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT);
//        params4.addRule(RelativeLayout.BELOW, 1);
//        params4.addRule(RelativeLayout.END_OF, 3);
//
//        scrollAnimView5 = new ScrollAnimView(context);
//        scrollAnimView5.setId(5);
//        RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT);
//        params5.addRule(RelativeLayout.BELOW, 1);
//        params5.addRule(RelativeLayout.END_OF, 4);

//        ScrollAnimView scrollAnimView6 = new ScrollAnimView(context);
//        scrollAnimView6.setId(6);
//
//        ScrollAnimView scrollAnimView7 = new ScrollAnimView(context);
//        scrollAnimView7.setId(7);
//
//        ScrollAnimView scrollAnimView8 = new ScrollAnimView(context);
//        scrollAnimView8.setId(8);
//
//        ScrollAnimView scrollAnimView9 = new ScrollAnimView(context);
//        scrollAnimView9.setId(9);
//
//        ScrollAnimView scrollAnimView10 = new ScrollAnimView(context);
//        scrollAnimView10.setId(10);
//
//        ScrollAnimView scrollAnimView11 = new ScrollAnimView(context);
//        scrollAnimView11.setId(11);
//
//        ScrollAnimView scrollAnimView12 = new ScrollAnimView(context);
//        scrollAnimView12.setId(12);
//
//        ScrollAnimView scrollAnimView13 = new ScrollAnimView(context);
//        scrollAnimView13.setId(13);
//
//        ScrollAnimView scrollAnimView14 = new ScrollAnimView(context);
//        scrollAnimView14.setId(14);

        addView(scrollAnimView0);
        addView(scrollAnimView1);
        addView(scrollAnimView2);
//        addView(scrollAnimView3, params3);
//        addView(scrollAnimView4, params4);
//        addView(scrollAnimView5, params5);
//        mLayout.addView(scrollAnimView6);
//        mLayout.addView(scrollAnimView7);
//        mLayout.addView(scrollAnimView8);
//        mLayout.addView(scrollAnimView9);
//        mLayout.addView(scrollAnimView10);
//        mLayout.addView(scrollAnimView11);
//        mLayout.addView(scrollAnimView12);
//        mLayout.addView(scrollAnimView13);
//        mLayout.addView(scrollAnimView14);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //子控件的个数
        int count = getChildCount();

        //子View
        View child;
        //子view用于layout的 l t r b
        int viewL, viewT, viewR, viewB;
        //子View 本身的宽高
        int childW, childH;


        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            //获取子View本身的宽高:
            childW = child.getMeasuredWidth();
            childH = child.getMeasuredHeight();

            //当前行可以放下子View:
            viewL = childW * i;
            viewT = childH * i;
            viewR = viewL + childW;
            viewB = viewT + childH;

            System.out.println(viewL + "/" + viewT + "/" + viewR + "/" + viewB);
            //布局子View
            child.layout(viewL, viewT, viewR, viewB);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasure = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasure = MeasureSpec.getSize(heightMeasureSpec);

        //最大宽度
        int maxWidth;
        //总高度
        int totalHeight;

        int count = getChildCount();
        int lineNum = (int) Math.ceil((1 + Math.sqrt(1 + 4 * count)) / 2);

        View childView;
        //子View Layout需要的宽高(包含margin)，用于计算是否越界
        int childWidth;
        int childHeight;

        for (int i = 0; i < count; i++) {
            childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }

        childView = getChildAt(0);
        childWidth = childView.getMeasuredWidth();
        childHeight = childView.getMeasuredHeight();
        maxWidth = lineNum * childWidth;
        totalHeight = lineNum * childHeight;
        System.out.println(maxWidth + "/" + totalHeight);

        setMeasuredDimension(maxWidth, totalHeight);
    }

    public void setWinningNum(ArrayList<String> winningNum) {
        this.winningNum = winningNum;
    }
}
