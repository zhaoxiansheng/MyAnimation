package com.example.zy.myanimation.view.picker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2018/5/14
 *
 * @author zhaoy
 */
public class StringBaseScrollPicker extends BaseScrollPickerView<CharSequence> {

    public StringBaseScrollPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StringBaseScrollPicker(Context context, AttributeSet attrs,
                                  int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        init(attrs);

        setData(new ArrayList<CharSequence>(Arrays.asList("one", "two", "three", "four", "five", "six",
                "seven", "eight", "nine", "ten", "eleven", "twelve")));
    }


    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                    R.styleable.StringBaseScrollPicker);
            mMinTextSize = typedArray.getDimensionPixelSize(
                    R.styleable.StringBaseScrollPicker_spv_min_text_size, mMinTextSize);
            mMaxTextSize = typedArray.getDimensionPixelSize(
                    R.styleable.StringBaseScrollPicker_spv_max_text_size, mMaxTextSize);
            mStartColor = typedArray.getColor(
                    R.styleable.StringBaseScrollPicker_spv_start_color, mStartColor);
            mEndColor = typedArray.getColor(
                    R.styleable.StringBaseScrollPicker_spv_end_color, mEndColor);
            mMaxLineWidth = typedArray.getDimensionPixelSize(R.styleable.StringBaseScrollPicker_spv_max_line_width, mMaxLineWidth);
            int align = typedArray.getInt(R.styleable.StringBaseScrollPicker_spv_alignment, 1);
            if (align == 2) {
                mAlignment = Layout.Alignment.ALIGN_NORMAL;
            } else if (align == 3) {
                mAlignment = Layout.Alignment.ALIGN_OPPOSITE;
            } else {
                mAlignment = Layout.Alignment.ALIGN_CENTER;
            }
            typedArray.recycle();
        }
    }

    /**
     * @param startColor 正中间的颜色
     * @param endColor   上下两边的颜色
     */
    public void setColor(int startColor, int endColor) {
        mStartColor = startColor;
        mEndColor = endColor;
        invalidate();
    }

    /**
     * item文字大小
     *
     * @param minText 沒有被选中时的最小文字
     * @param maxText 被选中时的最大文字
     */
    public void setTextSize(int minText, int maxText) {
        mMinTextSize = minText;
        mMaxTextSize = maxText;
        invalidate();
    }

    public int getStartColor() {
        return mStartColor;
    }

    public int getEndColor() {
        return mEndColor;
    }

    public int getMinTextSize() {
        return mMinTextSize;
    }

    public int getMaxTextSize() {
        return mMaxTextSize;
    }


    public int getMaxLineWidth() {
        return mMaxLineWidth;
    }

    /**
     * 最大的行宽,默认为itemWidth.超过后文字自动换行
     *
     * @param maxLineWidth
     */
    public void setMaxLineWidth(int maxLineWidth) {
        mMaxLineWidth = maxLineWidth;
    }

    public Layout.Alignment getAlignment() {
        return mAlignment;
    }

    public void setAlignment(Layout.Alignment alignment) {
        mAlignment = alignment;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int mMeasureWidth = getMeasuredWidth();
        int mMeasureHeight = getMeasuredHeight();
        if (mMaxLineWidth < 0) {
            mMaxLineWidth = getItemWidth();
        }
    }

    @Override
    public void drawItem(Canvas canvas, List<CharSequence> data, int position, int relative, float moveLength, float top) {
        CharSequence text = data.get(position);
        int itemSize = getItemSize();

        // 设置文字大小
        if (relative == -1) {
            // 上一个
            if (moveLength < 0) {
                // 向上滑动
                mPaint.setTextSize(mMinTextSize);
            } else {
                // 向下滑动
                mPaint.setTextSize(mMinTextSize + (mMaxTextSize - mMinTextSize)
                        * moveLength / itemSize);
            }
            // 中间item,当前选中
        } else if (relative == 0) {
            mPaint.setTextSize(mMinTextSize + (mMaxTextSize - mMinTextSize)
                    * (itemSize - Math.abs(moveLength)) / itemSize);
            // 下一个
        } else if (relative == 1) {
            // 向下滑动
            if (moveLength > 0) {
                mPaint.setTextSize(mMinTextSize);
            } else { // 向上滑动
                mPaint.setTextSize(mMinTextSize + (mMaxTextSize - mMinTextSize)
                        * -moveLength / itemSize);
            }
        } else { // 其他
            mPaint.setTextSize(mMinTextSize);
        }

        StaticLayout layout = new StaticLayout(text, 0, text.length(), mPaint, mMaxLineWidth, mAlignment, 1.0F, 0.0F, true, null, 0);
        float x = 0;
        float y = 0;
        float lineWidth = layout.getWidth();

        // 水平滚动
        if (isHorizontal()) {
            x = top + (getItemWidth() - lineWidth) / 2;
            y = (getItemHeight() - layout.getHeight()) / 2;
            // 垂直滚动
        } else {
            x = (getItemWidth() - lineWidth) / 2;
            y = top + (getItemHeight() - layout.getHeight()) / 2;
        }
        // 计算渐变颜色
        computeColor(relative, itemSize, moveLength);

        canvas.save();
        canvas.translate(x, y);
        layout.draw(canvas);
        canvas.restore();
    }

    /**
     * 计算字体颜色，渐变
     *
     * @param relative 　相对中间item的位置
     */
    private void computeColor(int relative, int itemSize, float moveLength) {

        // 　其他默认为ｍEndColor
        int color = mEndColor;

        // 上一个或下一个
        if (relative == -1 || relative == 1) {
            // 处理上一个item且向上滑动　或者　处理下一个item且向下滑动　，颜色为mEndColor
            if ((relative == -1 && moveLength < 0) || (relative == 1 && moveLength > 0)) {
                color = mEndColor;
            } else {
                // 计算渐变的颜色
                float rate = (itemSize - Math.abs(moveLength))
                        / itemSize;
                color = ColorUtils.computeGradientColor(mStartColor, mEndColor, rate);
            }
            // 中间item
        } else if (relative == 0) {
            float rate = Math.abs(moveLength) / itemSize;
            color = ColorUtils.computeGradientColor(mStartColor, mEndColor, rate);
        }

        mPaint.setColor(color);
    }
}
