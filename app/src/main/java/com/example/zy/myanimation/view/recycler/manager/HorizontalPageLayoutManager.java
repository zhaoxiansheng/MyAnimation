package com.example.zy.myanimation.view.recycler.manager;

import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.example.zy.myanimation.view.recycler.channel.ChannelConfig;
import com.example.zy.myanimation.view.recycler.model.config.AppsConfig;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by JonLuo on 2019/04/08.
 */

public class HorizontalPageLayoutManager extends RecyclerView.LayoutManager {

    private static final String TAG = "HorizontalLayoutManager";
    private Rect mDisplayRect = new Rect();
    private SparseArray<Rect> allItemFrames = new SparseArray<>();
    private ArrayList<View> mInvisibleViews = new ArrayList<>();

    private int totalHeight = 0;
    private int totalWidth = 0;
    private int offsetY = 0;
    private int offsetX = 0;
    private int[] mPageScrolls;

    private int rows;
    private int columns;
    private int pageSize = 0;
    private int itemWidth = 0;
    private int itemHeight = 0;
    private int onePageSize;
    private int itemWidthUsed;
    private int itemHeightUsed;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public HorizontalPageLayoutManager(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.onePageSize = rows * columns;
    }

    @Override
    public boolean canScrollHorizontally() {
        return !AppsConfig.SCROLL_VERTICALLY;
    }

    @Override
    public boolean canScrollVertically() {
        return AppsConfig.SCROLL_VERTICALLY;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int newX = offsetX + dx;
        int result = dx;
        if (newX > totalWidth) {
            result = totalWidth - offsetX;
        } else if (newX < 0) {
            result = 0 - offsetX;
        }
        offsetX += result;
        relayoutChildren(recycler, state);
        return result;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int newY = offsetY + dy;
        int result = dy;
        if (newY > totalHeight) {
            result = totalHeight - offsetY;
        } else if (newY < 0) {
            result = 0 - offsetY;
        }
        offsetY += result;
        relayoutChildren(recycler, state);
        return result;
    }

    private int getUsableWidth() {
        if (canScrollVertically()) {
            return getWidth() - getPaddingLeft() - getPaddingRight() - ChannelConfig.VERTICAL_MARGIN_LEFT;
        } else {
            Log.d(TAG, "getUsableWidth: " + getWidth() + ", " + getPaddingLeft() + ", " + getPaddingRight());
            return getWidth() - getPaddingLeft() - getPaddingRight();
        }
    }

    private int getUsableHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom() - ChannelConfig.VERTICAL_MARGIN_TOP;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        if (getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        if (state.isPreLayout()) {
            return;
        }
        // Gets the average width and height of each Item
        itemWidth = getUsableWidth() / columns;
        itemHeight = getUsableHeight() / rows;

        // Calculate the amount of width and height already used, mainly for later measurement
        itemWidthUsed = (columns - 1) * itemWidth;
        itemHeightUsed = (rows - 1) * itemHeight;

        // Count the total number of pages
        computePageSize();
        Log.i(TAG, "itemCount=" + getItemCount() + " pageSize=" + pageSize);
        // Computes the maximum value that can be scrolled horizontally
        totalWidth = (pageSize - 1) * getWidth();
        totalHeight = (pageSize - 1) * getHeight();
        int count = getItemCount();
        for (int p = 0; p < pageSize; p++) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    int index = p * onePageSize + r * columns + c;
                    if (index == count) {
                        r = rows;
                        p = pageSize;
                        break;
                    }
                    // Record display range
                    Rect rect = allItemFrames.get(index);
                    if (rect == null) {
                        rect = new Rect();
                    }
                    int x, y;
                    if (canScrollHorizontally()) {
                        x = p * getWidth() + getPaddingLeft() + c * itemWidth;
                        y = r * itemHeight + getPaddingTop();
                    } else if (canScrollVertically()) {
                        x = c * itemWidth + getPaddingLeft() + ChannelConfig.VERTICAL_MARGIN_LEFT;
                        y = p * getHeight() + getPaddingTop() + r * itemHeight + ChannelConfig.VERTICAL_MARGIN_TOP;
                    } else {
                        throw new RuntimeException("must canScrollVertically or canScrollVertically");
                    }
                    rect.set(x, y, itemWidth + x, itemHeight + y);
                    allItemFrames.put(index, rect);
//                    Log.i("LayoutManager", rect.left + "-" + rect.top + "-" + rect.right + "-" + rect.bottom);
                }
            }
        }
        relayoutChildren(recycler, state);
    }

    public int computePageSize() {
        pageSize = getItemCount() / onePageSize + (getItemCount() % onePageSize == 0 ? 0 : 1);
        if (mPageScrolls == null || mPageScrolls.length != pageSize) {
            mPageScrolls = new int[pageSize];
            for (int i = 0; i < pageSize; i++) {
                if (canScrollHorizontally()) {
                    mPageScrolls[i] = getWidth() * i;
                } else if (canScrollVertically()) {
                    mPageScrolls[i] = getHeight() * i;
                }
            }
        }
        return pageSize;
    }

    public int getItemWidth() {
        return itemWidth;
    }

    public int getItemHeight() {
        return itemHeight;
    }

    public int getScrollForPage(int index) {
        if (mPageScrolls == null || index >= mPageScrolls.length || index < 0) {
            return 0;
        } else {
            return mPageScrolls[index];
        }
    }

    /* zhaoy: 2019/4/9  indicator */
    public int getPageSize() {
        return pageSize;
    }

    public int getOffsetX() {
        return offsetX;
    }

    /* zhaoy end */
    public int getOffsetY() {
        return offsetY;
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        offsetX = 0;
        offsetY = 0;
    }

    private void relayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) {
            return;
        }
        if (canScrollVertically()) {
            mDisplayRect.set(getPaddingLeft(), getPaddingTop() + offsetY, getWidth() - getPaddingRight(), getHeight() - getPaddingBottom() + offsetY);
        } else if (canScrollHorizontally()) {
            mDisplayRect.set(getPaddingLeft() + offsetX, getPaddingTop(), getWidth() - getPaddingRight() + offsetX, getHeight() - getPaddingBottom());
        } else {
            return;
        }
        recycleChildren(recycler);
        detachAndScrapAttachedViews(recycler);
        //no item show, remove all
        if (!onLayout(recycler, state)) {
            removeAndRecycleAllViews(recycler);
        }
    }

    /**
     * change view loaction by layout
     */
    private boolean onLayout(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int end;
        int start;
        View view = null;
        Rect rect;
        try {
            if (canScrollHorizontally()) {
                int currentIndex = offsetX / getWidth();
                int nextIndex = offsetX % getWidth() > 0 ? currentIndex + 1 : currentIndex;
                start = currentIndex * rows * columns;
                end = (nextIndex + 1) * rows * columns;
                if (end > state.getItemCount()) {
                    end = state.getItemCount();
                }
            } else {
                int currentIndex = offsetY / getHeight();
                int nextIndex = offsetY % getHeight() > 0 ? currentIndex + 1 : currentIndex;
                start = currentIndex * rows * columns;
                end = (nextIndex + 1) * rows * columns;
                if (end > state.getItemCount()) {
                    end = state.getItemCount();
                }
                Log.e(TAG, "onLayout: currentIndex: " + currentIndex + "/ nextIndex: " + nextIndex + "/ start: " + start + "/ end: " + end + "/ state.getItemCount(): " + state.getItemCount());
            }

            for (int i = start; i < end; i++) {
                if (allItemFrames.get(i) == null) {
                    return false;
                }
                if (Rect.intersects(mDisplayRect, allItemFrames.get(i))) {
                    view = recycler.getViewForPosition(i);
                    addView(view);
                    this.measureChildWithMargins(view, 0, 0);
                    rect = allItemFrames.get(i);
                    if (canScrollVertically()) {
                        layoutDecoratedWithMargins(view, rect.left, rect.top - offsetY, rect.right, rect.bottom - offsetY);
                    } else if (canScrollHorizontally()) {
                        layoutDecoratedWithMargins(view, rect.left - offsetX, rect.top, rect.right - offsetX, rect.bottom);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (view != null);
    }

    //recycle view that not in screen
    private void recycleChildren(RecyclerView.Recycler recycler) {
        getRecycleChildren();
        int size = mInvisibleViews.size();
        for (int i = 0; i < size; ++i) {
            View child = mInvisibleViews.get(i);
            removeAndRecycleView(child, recycler);
        }
    }

    private void getRecycleChildren() {
        //Log.e(TAG, "YYY recycleChildren<<<<<");
        mInvisibleViews.clear();
        int size = getChildCount();
        for (int i = 0; i < size; ++i) {
            View child = getChildAt(i);
            Rect childRect = allItemFrames.get(getPosition(child));
            if (!Rect.intersects(mDisplayRect, childRect)) {
                //Log.e(TAG, "YYY chilid" + i + ", pos: " + pos);
                mInvisibleViews.add(child);
            }
        }
        //Log.e(TAG, "YYY recycleChildren>>>>");
    }

    @Override
    public int computeHorizontalScrollRange(RecyclerView.State state) {
        computePageSize();
        return pageSize * getWidth();
    }

    @Override
    public int computeHorizontalScrollOffset(RecyclerView.State state) {
        return offsetX;
    }

    private int mCurrentPage = 0;

    @Override
    public int computeHorizontalScrollExtent(RecyclerView.State state) {
        return getWidth();
    }

    /* zhaoy: 2019/4/9 indicator */
    public int getPageIndex() {
        if (mPageScrolls == null) {
            return 0;
        }
        if (canScrollHorizontally()) {
            for (int i = 0; i < mPageScrolls.length; i++) {
                if (offsetX == mPageScrolls[i]) {
                    mCurrentPage = i;
                }
            }
        } else {
            for (int i = 0; i < mPageScrolls.length; i++) {
                if (offsetY == mPageScrolls[i]) {
                    mCurrentPage = i;
                }
            }

        }
        return mCurrentPage;
    }
    /* zhaoy end */

    public int getColumns() {
        return columns;
    }

    public int getOnePageSize() {
        return onePageSize;
    }

    public int getRows() {
        return rows;
    }
}
