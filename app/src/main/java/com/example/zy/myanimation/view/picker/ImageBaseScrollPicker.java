package com.example.zy.myanimation.view.picker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

public class ImageBaseScrollPicker extends BaseScrollPickerView<Bitmap> {

    private List<Bitmap> data;
    private Context mContext;

    public ImageBaseScrollPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageBaseScrollPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        init(attrs);
        initData();

        setData(data);
    }

    private void initData() {
        data = new ArrayList<>();
        Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        data.add(bitmap);
        data.add(bitmap);
        data.add(bitmap);
        data.add(bitmap);
        data.add(bitmap);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                    R.styleable.ImageBaseScrollPicker);
            mMinTextSize = typedArray.getDimensionPixelSize(
                    R.styleable.ImageBaseScrollPicker_isp_min_text_size, mMinTextSize);
            mMaxTextSize = typedArray.getDimensionPixelSize(
                    R.styleable.ImageBaseScrollPicker_isp_max_text_size, mMaxTextSize);
            mStartColor = typedArray.getColor(
                    R.styleable.ImageBaseScrollPicker_isp_start_color, mStartColor);
            mEndColor = typedArray.getColor(
                    R.styleable.ImageBaseScrollPicker_isp_end_color, mEndColor);
            mMaxLineWidth = typedArray.getDimensionPixelSize(R.styleable.ImageBaseScrollPicker_isp_max_line_width, mMaxLineWidth);
            int align = typedArray.getInt(R.styleable.ImageBaseScrollPicker_isp_alignment, 1);
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

    @Override
    public void drawItem(Canvas canvas, List<Bitmap> data, int position, int relative, float moveLength, float top) {
        Bitmap bitmap = data.get(position);
        int itemSize = getItemSize();
        Bitmap newBitmap;

        // 设置文字大小
        if (relative == -1) {
            // 上一个
            if (moveLength < 0) {
                // 向上滑动
                newBitmap = scaleBitmap(bitmap, 1);
            } else {
                // 向下滑动
                newBitmap = scaleBitmap(bitmap, (float) (1 + 0.3 * moveLength / itemSize));
            }
            // 中间item,当前选中
        } else if (relative == 0) {
            newBitmap = scaleBitmap(bitmap, (float) 1.3);
            // 下一个
        } else if (relative == 1) {
            // 向下滑动
            if (moveLength > 0) {
                newBitmap = scaleBitmap(bitmap, 1);
            } else { // 向上滑动
                newBitmap = scaleBitmap(bitmap, (float) (1 + 0.3 * -moveLength / itemSize));
            }
        } else { // 其他
            newBitmap = scaleBitmap(bitmap, 1);
        }

        float x;
        float y;
        float lineWidth = newBitmap.getWidth();

        // 水平滚动
        if (isHorizontal()) {
            x = top + (getItemWidth() - lineWidth) / 2;
            y = (getItemHeight() - newBitmap.getHeight()) / 2;
            // 垂直滚动
        } else {
            x = (getItemWidth() - lineWidth) / 2;
            y = top + (getItemHeight() - newBitmap.getHeight()) / 2;
        }
        // 计算渐变颜色
        computeColor(relative, itemSize, moveLength);

        canvas.save();
        canvas.drawBitmap(newBitmap, x, y, mPaint);
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

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    private Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        origin = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        return origin;
    }

}
