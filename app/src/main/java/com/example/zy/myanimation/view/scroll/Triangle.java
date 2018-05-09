package com.example.zy.myanimation.view.scroll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

    @SuppressLint("ResourceType")
    private void initAttrs(Context context) {

        scrollAnimView0 = new ScrollAnimView(context);
        scrollAnimView0.setId(0);
        RelativeLayout.LayoutParams params0 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params0.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        scrollAnimView1 = new ScrollAnimView(context);
        scrollAnimView1.setId(1);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.BELOW, 0);

        scrollAnimView2 = new ScrollAnimView(context);
        scrollAnimView2.setId(2);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params2.addRule(RelativeLayout.BELOW, 0);
        params0.addRule(RelativeLayout.END_OF, 1);

        scrollAnimView3 = new ScrollAnimView(context);
        scrollAnimView3.setId(3);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params3.addRule(RelativeLayout.BELOW, 1);

        scrollAnimView4 = new ScrollAnimView(context);
        scrollAnimView4.setId(4);
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params4.addRule(RelativeLayout.BELOW, 1);
        params4.addRule(RelativeLayout.END_OF, 3);

        scrollAnimView5 = new ScrollAnimView(context);
        scrollAnimView5.setId(5);
        RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params5.addRule(RelativeLayout.BELOW, 1);
        params5.addRule(RelativeLayout.END_OF, 4);

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

        addView(scrollAnimView0, params0);
        addView(scrollAnimView1, params1);
        addView(scrollAnimView2, params2);
        addView(scrollAnimView3, params3);
        addView(scrollAnimView4, params4);
        addView(scrollAnimView5, params5);
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
        //ViewParent宽度(包含padding)
        int width = getWidth();
        //ViewParent 的右边x的布局限制值
        int rightLimit =  width - getPaddingRight();

        //存储基准的left top (子类.layout(),里的坐标是基于父控件的坐标，所以 x应该是从0+父控件左内边距开始，y从0+父控件上内边距开始)
        int baseLeft = getPaddingLeft();
        int baseTop = getPaddingTop();
        //存储现在的left top
        int curLeft = baseLeft;
        int curTop = baseTop;

        //子View
        View child;
        //子view用于layout的 l t r b
        int viewL,viewT,viewR,viewB;
        //子View的LayoutParams
        MarginLayoutParams params;
        //子View Layout需要的宽高(包含margin)，用于计算是否越界
        int childWidth;
        int childHeight;
        //子View 本身的宽高
        int childW,childH;

        //临时增加一个temp 存储上一个View的高度 解决过长的两行View导致显示不正确的bug
        int lastChildHeight =0;

        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            //如果gone，不布局了
            if (View.GONE == child.getVisibility()) {
                continue;
            }
            //获取子View本身的宽高:
            childW = child.getMeasuredWidth();
            childH = child.getMeasuredHeight();
            //获取子View的LayoutParams，用于获取其margin
            params = (MarginLayoutParams) child.getLayoutParams();
            //子View需要的宽高 为 本身宽高+marginLeft + marginRight
            childWidth =  childW + params.leftMargin + params.rightMargin;
            childHeight = childH + params.topMargin + params.bottomMargin;

            //这里要考虑padding，所以右边界为 ViewParent宽度(包含padding) -ViewParent右内边距
            if (curLeft + childWidth > rightLimit ) {
                //如果当前行已经放不下该子View了 需要换行放置：
                //在新的一行布局子View，左x就是baseLeft，上y是 top +前一行高(这里假设的是每一行行高一样)，
                curTop = curTop + lastChildHeight;
                //layout时要考虑margin
                viewL = baseLeft +params.leftMargin;
                viewT = curTop + params.topMargin;
                viewR = viewL + childW;
                viewB = viewT + childH;
                curLeft = baseLeft + childWidth;

            } else {
                //当前行可以放下子View:
                viewL = curLeft +params.leftMargin;
                viewT = curTop + params.topMargin;
                viewR = viewL + childW;
                viewB = viewT + childH;

                curLeft = curLeft + childWidth;
            }
            lastChildHeight = childHeight;
            //布局子View
            child.layout(viewL,viewT,viewR,viewB);
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
        int lineNum = (int) ((1 + Math.sqrt(1 + 4 * count)) / 2);

        View childView;

        //存储子View的LayoutParams
        MarginLayoutParams params;
        //子View Layout需要的宽高(包含margin)，用于计算是否越界
        int childWidth;
        int childHeight;

        childView = getChildAt(0);

        measureChild(childView, widthMeasureSpec, heightMeasureSpec);

        // 获取子View的LayoutParams，
        // (子View的LayoutParams的对象类型，
        // 取决于其ViewGroup的generateLayoutParams()方法的返回的对象类型，
        // 这里返回的是MarginLayoutParams)
        params = (MarginLayoutParams) childView.getLayoutParams();
        //子View需要的宽度 为 子View 本身宽度+marginLeft + marginRight
        childWidth = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
        childHeight = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;

        maxWidth = lineNum * childWidth;
        totalHeight = lineNum * childHeight;

        setMeasuredDimension(
                widthMode != MeasureSpec.EXACTLY ? maxWidth + getPaddingLeft() + getPaddingRight() : widthMeasure,
                heightMode != MeasureSpec.EXACTLY ? totalHeight + getPaddingTop() + getPaddingBottom() : heightMeasure);
    }

    public void setWinningNum(ArrayList<String> winningNum) {
        this.winningNum = winningNum;
    }
}
