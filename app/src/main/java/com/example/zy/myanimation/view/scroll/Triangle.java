package com.example.zy.myanimation.view.scroll;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.adapter.TriangleRecyclerAdapter;
import com.example.zy.myanimation.view.recycler.LeftAndRightDecoration;
import com.example.zy.myanimation.view.recycler.TopAndBottomDecoration;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created on 2018/5/7.
 *
 * @author zhaoy
 */
public class Triangle extends ViewGroup {

    private ArrayList<ScrollAnimView> scrollAnimViews;
    private ArrayList<Integer> nums;
    private boolean isAnimation;

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
        scrollAnimViews = new ArrayList<>(15);
        nums = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            nums.add(i);
        }

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

        initView(context, scrollAnimView0);
        initView(context, scrollAnimView1);
        initView(context, scrollAnimView2);
        initView(context, scrollAnimView3);
        initView(context, scrollAnimView4);
        initView(context, scrollAnimView5);
        initView(context, scrollAnimView6);
        initView(context, scrollAnimView7);
        initView(context, scrollAnimView8);
        initView(context, scrollAnimView9);
        initView(context, scrollAnimView10);
        initView(context, scrollAnimView11);
        initView(context, scrollAnimView12);
        initView(context, scrollAnimView13);
        initView(context, scrollAnimView14);
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
        //当前行数
        int currentLineNum;
        //上一次的行数
        int lineNum = 0;
        //列数
        int row = 0;

        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            //获取子View本身的宽高:
            childW = child.getMeasuredWidth();
            childH = child.getMeasuredHeight();

            currentLineNum = (int) Math.ceil(((-1) + Math.sqrt(1 + 8 * (i + 1))) / 2);

            if (currentLineNum > lineNum) {
                row = 0;
                lineNum = currentLineNum;
            } else if (currentLineNum == lineNum) {
                row++;
            }
            //当前行可以放下子View:
            viewL = childW * row + (5 - currentLineNum) * (childW / 2);
            viewT = childH * (lineNum - 1);
            viewR = viewL + childW;
            viewB = viewT + childH;

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
        int lineNum = (int) (int) Math.ceil(((-1) + Math.sqrt(1 + 8 * count)) / 2);

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

        setMeasuredDimension(maxWidth, totalHeight);
    }

    /**
     * 初始化所需控件
     *
     * @param context        上下文
     * @param scrollAnimView View
     */
    private void initView(final Context context, final ScrollAnimView scrollAnimView) {
        scrollAnimViews.add(scrollAnimView);
        addView(scrollAnimView);
        scrollAnimView.setOnItemClickListener(new ScrollAnimView.OnItemClickListener() {
            @Override
            public void onClickListener(View view) {
                popupChooseNum(context, scrollAnimView);
            }
        });
    }

    /**
     * 设置号码
     *
     * @param winningNum 号码
     */
    public void setWinningNum(ArrayList<String> winningNum) {
        if (winningNum.size() > 0) {
            for (int i = 0; i < winningNum.size(); i++) {
                scrollAnimViews.get(i).setWinningNum(winningNum.get(i));
            }
        }
    }

    /**
     * 设置动画是否执行
     *
     * @param animation true or false
     */
    public void setAnimation(boolean animation) {
        if (scrollAnimViews.size() > 0) {
            for (int i = 0; i < scrollAnimViews.size(); i++) {
                scrollAnimViews.get(i).setAnimation(animation);
            }
        }
    }

    /**
     * 点击小球弹出弹窗
     *
     * @param context        上下文
     * @param scrollAnimView 点击的view
     */

    private void popupChooseNum(Context context, final ScrollAnimView scrollAnimView) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_window_layout, null);

        RecyclerView recyclerView = popupView.findViewById(R.id.recycler_view);
        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 5, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        TopAndBottomDecoration topAndBottomDecoration = new TopAndBottomDecoration(context);
        recyclerView.addItemDecoration(topAndBottomDecoration);

        LeftAndRightDecoration leftAndRightDecoration = new LeftAndRightDecoration(context);
        recyclerView.addItemDecoration(leftAndRightDecoration);

        TriangleRecyclerAdapter mAdapter = new TriangleRecyclerAdapter(context, nums);
        recyclerView.setAdapter(mAdapter);

        final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(scrollAnimView, 10, 10);

        mAdapter.setOnItemClickListener(new TriangleRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClickListener(int num) {
                scrollAnimView.setWinningNum(String.valueOf(num));
                popupWindow.dismiss();
            }
        });
    }
}
