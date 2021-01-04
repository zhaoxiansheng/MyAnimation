package com.netease.screenadapter_percent;

import android.content.Context;
import android.content.res.TypedArray;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.autopai.common.R;

import java.lang.ref.WeakReference;

/**
 * child size ratio to mContainer size
 */
public class PercentAdapter{
    private static final int P_BASE = 1;
    private static final int BASE = 1;

    private WeakReference<ViewGroup> mContainer;
    private PercentParams mParams;

    public PercentAdapter(ViewGroup container){
        mContainer = new WeakReference<>(container);
    }

    protected void onPreMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup container = mContainer.get();
        if(container != null) {
            //获取父容器宽高
            int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
            //给子控件设置修改后的属性值
            int childCount = container.getChildCount();
            for (int i = 0; i < childCount; ++i) {
                //获取子控件
                View child = container.getChildAt(i);
                //获取子控件LayoutParams
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)child.getLayoutParams();
                //判断子控件是否是百分比布局属性
                if (checkLayoutParams()) {
                    //是
                    float widthPercent = mParams.widthPercent;
                    float heightPercent = mParams.heightPercent;
                    float marginLeftPercent = mParams.marginLeftPercent;
                    float marginRightPercent = mParams.marginRightPercent;
                    float marginTopPercent = mParams.marginTopPercent;
                    float marginBottomPercent = mParams.marginBottomPercent;
                    //compute size with scale
                    if (widthPercent > 0) {
                        layoutParams.width = (int) (widthSize * widthPercent);
                    }
                    if (heightPercent > 0) {
                        layoutParams.height = (int) (heightSize * heightPercent);
                    }

                    //compute margin with scale
                    if (marginLeftPercent > 0) {
                        layoutParams.leftMargin = (int) (widthSize * marginLeftPercent);
                    }
                    if (marginRightPercent > 0) {
                        layoutParams.rightMargin = (int) (widthSize * marginRightPercent);
                    }
                    if (marginTopPercent > 0) {
                        layoutParams.topMargin = (int) (heightSize * marginTopPercent);
                    }
                    if (marginBottomPercent > 0) {
                        layoutParams.bottomMargin = (int) (heightSize * marginBottomPercent);
                    }
                }
            }
        }
    }

    public void onGenerateLayoutParams(AttributeSet attrs) {
        ViewGroup container = mContainer.get();
        if(container != null) {
            mParams = new PercentParams(container.getContext(), attrs);
        }
    }

    protected boolean checkLayoutParams() {
        return mParams != null;
    }

    public void setWidthPercent(final float percent){
        if(mParams != null){
            mParams.widthPercent = percent;
        }
    }

    public void setHeightPercent(final float percent){
        if(mParams != null){
            mParams.heightPercent = percent;
        }
    }

    public void setLeftMarginPercent(final float percent){
        if(mParams != null){
            mParams.marginLeftPercent = percent;
        }
    }

    public void setRightMarginPercent(final float percent){
        if(mParams != null){
            mParams.marginRightPercent = percent;
        }
    }

    public void setTopMarginPercent(final float percent){
        if(mParams != null){
            mParams.marginTopPercent = percent;
        }
    }

    public void setBottomMarginPercent(final float percent){
        if(mParams != null){
            mParams.marginBottomPercent = percent;
        }
    }

    /**
     *     <TextView
     *         android:layout_width="wrap_content"
     *         android:layout_height="wrap_content"
     *         android:layout_centerHorizontal="true"
     *         android:background="@color/colorAccent"
     *         android:text="Hello World!"
     *         app:percent_height="50%"
     *         app:percent_width="25%"
     *         app:percent_marginLeft="30%"
     *         app:percent_marginTop="20%p"
     *         tools:ignore="MissingPrefix" />
     *
     * 1、xml创建自定义属性
     * 2、在容器中去创建一个静态内部类LayoutParams
     * 3、在LayoutParams构造方法中获取自定义属性
     * 4、onMeasure中给子控件设置修改后的属性值
     */

    private static class PercentParams{
        private float widthPercent;
        private float heightPercent;
        private float marginLeftPercent;
        private float marginRightPercent;
        private float marginTopPercent;
        private float marginBottomPercent;

        public PercentParams() {

        }

        public PercentParams(Context c, AttributeSet attrs) {
            //3、在LayoutParams构造方法中获取自定义属性 解析自定义属性
            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.PercentAttr);//declare-styleable name
            widthPercent = typedArray.getFraction(R.styleable.PercentAttr_percent_width, BASE, P_BASE, 0); //如果layout xml的属性值是i%,则最终结果是base*i%。如果layout xml的属性值是i%p, 则结果是pbase*i%
            heightPercent = typedArray.getFraction(R.styleable.PercentAttr_percent_height, BASE, P_BASE, 0);
            marginLeftPercent = typedArray.getFraction(R.styleable.PercentAttr_percent_marginLeft, BASE, P_BASE, 0);
            marginRightPercent = typedArray.getFraction(R.styleable.PercentAttr_percent_marginRight, BASE, P_BASE, 0);
            marginTopPercent = typedArray.getFraction(R.styleable.PercentAttr_percent_marginTop, BASE, P_BASE, 0);
            marginBottomPercent = typedArray.getFraction(R.styleable.PercentAttr_percent_marginBottom, BASE, P_BASE, 0);
            typedArray.recycle();//回收
        }
    }
}
