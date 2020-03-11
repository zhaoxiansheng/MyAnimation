package com.example.zy.myanimation.view.recycler.manager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.example.zy.myanimation.view.recycler.channel.ChannelConfig;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author JonLuo
 */
public class PagingItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "PagingItemDecoration";

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;

    /* zhaoy: 2019/4/9 indicator */
    protected int colorActive;
    protected int colorInactive;

    /**
     * Height of the space the indicator takes up at the bottom of the view.
     */
    protected float mIndicatorHeight;

    /**
     * Indicator width.
     */
    protected float mLongIndicatorItemLength;
    protected float mShortIndicatorItemLength;
    /**
     * Padding between indicators.
     */
    protected float mIndicatorItemPadding;

    protected final Paint mPaint = new Paint();

    /**
     * Some more natural animation interpolation
     */
    protected final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    /* zhaoy end */

    protected HorizontalPageLayoutManager layoutManager;

    public PagingItemDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();

        /* zhaoy: 2019/4/9 indicator  */
        init();
        /* zhaoy end */
    }

    private void init() {
        initColor();
        mIndicatorHeight = ChannelConfig.MARKER_HEIGHT;

        float mIndicatorStrokeWidth = ChannelConfig.MARKER_STROKE_WIDTH;
        mLongIndicatorItemLength = ChannelConfig.MARKER_LONG_ITEM_LENGTH;
        mShortIndicatorItemLength = ChannelConfig.MARKER_SHORT_ITEM_LENGTH;
        mIndicatorItemPadding = ChannelConfig.MARKER_ITEM_PADDING;

        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mIndicatorStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
    }

    public void initColor() {
        String[] markerColor = AppIconManager.getMarkerResourcedIds();
        colorActive = Color.parseColor(markerColor[0]);
        colorInactive = Color.parseColor(markerColor[1]);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        drawHorizontal(c, parent);
//        drawVertical(c, parent);
    }


    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int itemPosition = parent.getChildAdapterPosition(view);
        layoutManager = (HorizontalPageLayoutManager) parent.getLayoutManager();

        int itemWidth = layoutManager.getItemWidth();
        int itemHeight = layoutManager.getItemHeight();
        outRect.right = outRect.left = (itemWidth - ChannelConfig.ITEM_WIDTH) / 2;
        int heightOffset = itemHeight - ChannelConfig.ITEM_HEIGHT;
        if (itemPosition % layoutManager.getOnePageSize() < layoutManager.getColumns()) {
            outRect.top = heightOffset;
        } else {
            outRect.bottom = heightOffset;
        }
    }
}