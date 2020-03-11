package com.example.zy.myanimation.view.recycler.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.example.zy.myanimation.view.recycler.manager.HorizontalPageLayoutManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.zy.myanimation.view.recycler.model.config.AppsConfig.SLIDE_RATIO_OF_SCREEN;

public class CustomRecyclerView extends RecyclerView {

    private static final String TAG = "CustomRecyclerView";

    private Scroller mGravityScroller;

    private int offsetY = 0;
    private int offsetX = 0;

    private int startY = 0;
    private int startX = 0;

    private int endPoint;
    private int startPoint;

    private static final int VELOCITY_RATIO = 2;

    public CustomRecyclerView(Context context) {
        super(context);
        init();
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.mGravityScroller = new Scroller(getContext(), new DecelerateInterpolator());
        //获取滚动的方向
        updateLayoutManger();
    }

    private void updateLayoutManger() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        startX = 0;
        startY = 0;
        offsetX = 0;
        offsetY = 0;
    }

    private ValueAnimator mAnimator = null;

    @Override
    public void scrollToPosition(int position) {
        Log.i(TAG, "scrollToPosition: int position == " + position);
        if (mAnimator != null) {
            int startPoint = getLayoutManager().canScrollVertically() ? offsetY : offsetX;
            if (startPoint != endPoint) {
                mAnimator.setIntValues(startPoint, position);
                mAnimator.start();
            }
        }
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        Log.e(TAG, "onFling velocityX : " + velocityX + ", velocityY: " + velocityY + ", mRecyclerView.getMinFlingVelocity(): " + getMinFlingVelocity());
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null) {
            return false;
        } else {
            Adapter adapter = getAdapter();
            if (adapter == null) {
                return super.fling(velocityX, velocityY);
            } else {
                return snapFromFling(layoutManager, velocityX, velocityY);
            }
        }
    }

    private boolean snapFromFling(@NonNull LayoutManager layoutManager, int velocityX, int velocityY) {
        int minFlingVelocity = getMinFlingVelocity() * VELOCITY_RATIO;
        HorizontalPageLayoutManager horizontalPageLayoutManager = (HorizontalPageLayoutManager) layoutManager;
        int[] out = calculateScrollDistance(velocityX, velocityY);
        Log.e(TAG, "snapFromFling: " + out[0] + ", " + out[1]);
        int position;
        int page = -1;
        if (velocityX > 0 && (Math.abs(out[0]) > layoutManager.getWidth() * SLIDE_RATIO_OF_SCREEN || Math.abs(velocityX) > minFlingVelocity)) {
            page = horizontalPageLayoutManager.getPageIndex() + 1;
        } else if (velocityX < 0 && (Math.abs(out[0]) > layoutManager.getWidth() * SLIDE_RATIO_OF_SCREEN || Math.abs(velocityX) > minFlingVelocity)) {
            page = horizontalPageLayoutManager.getPageIndex() - 1;
        }
        if (velocityY > 0 && (Math.abs(out[1]) > layoutManager.getHeight() * SLIDE_RATIO_OF_SCREEN || Math.abs(velocityY) > minFlingVelocity)) {
            page = horizontalPageLayoutManager.getPageIndex() + 1;
        } else if (velocityY < 0 && (Math.abs(out[1]) > layoutManager.getHeight() * SLIDE_RATIO_OF_SCREEN || Math.abs(velocityY) > minFlingVelocity)) {
            page = horizontalPageLayoutManager.getPageIndex() - 1;

        }
        if (page <= -1) {
            return false;
        }
        if (getLayoutManager().canScrollVertically()) {
            position = getHeight() * page;
        } else {
            position = getWidth() * page;
        }
        scrollToPosition(position);
        return true;
    }

    private int[] calculateScrollDistance(int velocityX, int velocityY) {
        int[] outDist = new int[2];
        this.mGravityScroller.fling(0, 0, velocityX, velocityY, -2147483648, 2147483647, -2147483648, 2147483647);
        outDist[0] = this.mGravityScroller.getFinalX();
        outDist[1] = this.mGravityScroller.getFinalY();
        Log.e(TAG, "calculateScrollDistance x: " + outDist[0] + ", y: " + outDist[1]);
        return outDist;
    }

    private int getStartPageIndex() {
        int page = 0;
        if (getHeight() == 0 || getWidth() == 0) {
            // No width or height to handle
            return page;
        }
        if (getLayoutManager().canScrollVertically()) {
            page = startY / getHeight();
        } else {
            page = startX / getWidth();
        }
        return page;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Log.e(TAG, "onTouchEvent: " + e.getAction());
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            startY = offsetY;
            startX = offsetX;
        }
        if (e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_CANCEL) {
            int i;
            int offset;
            if (getLayoutManager().canScrollVertically()) {
                offset = offsetY;
                i = (((HorizontalPageLayoutManager) getLayoutManager()).computePageSize() - 1) * getHeight();
            } else {
                offset = offsetX;
                i = (((HorizontalPageLayoutManager) getLayoutManager()).computePageSize() - 1) * getWidth();
            }
            if (offset != 0 && offset != i) {
                // Use animation for scrolling
                if (mAnimator == null) {
                    mAnimator = ValueAnimator.ofInt(startPoint, endPoint);
                    mAnimator.setDuration(200);
                    mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int nowPoint = (int) animation.getAnimatedValue();

                            if (getLayoutManager().canScrollVertically()) {
                                int dy = nowPoint - offsetY;
                                // RecyclerView scrollBy Method to implement scrolling
                                scrollBy(0, dy);
                            } else {
                                int dx = nowPoint - offsetX;
                                scrollBy(dx, 0);
                            }
                        }
                    });
                    mAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            stopScroll();
                            startY = offsetY;
                            startX = offsetX;
                        }
                    });
                } else {
                    mAnimator.cancel();
                    mAnimator.setIntValues(startPoint, endPoint);
                }
                mAnimator.start();
            }
        }
        return super.onTouchEvent(e);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        offsetY += dy;
        offsetX += dx;

        //获取开始滚动时所在页面的index
        int pageIndex = getStartPageIndex();
        //记录滚动开始和结束的位置
        endPoint = 0;
        startPoint = 0;

        //如果是垂直方向
        if (getLayoutManager().canScrollVertically()) {
            startPoint = offsetY;
            if (offsetY - startY < 0 && Math.abs(offsetY - startY) > getHeight() * SLIDE_RATIO_OF_SCREEN) {
                Log.i(TAG, "onScrolled : " + getStartPageIndex() + ", startPoint: " + startPoint);
                pageIndex--;
            } else if (offsetY - startY > 0 && Math.abs(offsetY - startY) > getHeight() * SLIDE_RATIO_OF_SCREEN) {
                Log.i(TAG, "onScrolled : " + getStartPageIndex() + ", startPoint: " + startPoint);
                pageIndex++;
            }
            endPoint = pageIndex * getHeight();
        } else {
            startPoint = offsetX;
            if (offsetX - startX < 0 && Math.abs(offsetX - startX) > getWidth() * SLIDE_RATIO_OF_SCREEN) {
                Log.i(TAG, "onScrolled : " + getStartPageIndex() + ", startPoint: " + startPoint);
                pageIndex--;
            } else if (offsetX - startX > 0 && Math.abs(offsetX - startX) > getWidth() * SLIDE_RATIO_OF_SCREEN) {
                Log.i(TAG, "onScrolled : " + getStartPageIndex() + ", startPoint: " + startPoint);
                pageIndex++;
            }
            endPoint = pageIndex * getWidth();
        }
        if (endPoint < 0) {
            endPoint = 0;
        }
    }
}
