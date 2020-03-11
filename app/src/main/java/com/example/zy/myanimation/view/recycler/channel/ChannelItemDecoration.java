package com.example.zy.myanimation.view.recycler.channel;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.example.zy.myanimation.view.recycler.manager.PagingItemDecoration;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelItemDecoration extends PagingItemDecoration {

    private static final String TAG = "ChannelItemDecoration";

    public ChannelItemDecoration(Context context) {
        super(context);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int pageCount;
        if (layoutManager != null) {
            pageCount = layoutManager.getPageSize();
            if (pageCount <= 1) {
                return;
            }
        } else {
            Log.e(TAG, "HorizontalPageLayoutManager is null");
            return;
        }

        float totalLength = mLongIndicatorItemLength + mShortIndicatorItemLength * (pageCount - 1);
        float paddingBetweenItems = Math.max(0, pageCount - 1) * mIndicatorItemPadding;
        float indicatorTotalWidth = totalLength + paddingBetweenItems;
        float indicatorStartX;
        float indicatorPosY;
        if (layoutManager.canScrollHorizontally()) {
            indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2F;
            indicatorPosY = parent.getHeight() - mIndicatorHeight;
        } else {
            indicatorStartX = ChannelConfig.MARKER_LEFT_MARGIN;
            indicatorPosY = (parent.getHeight() - indicatorTotalWidth) / 2F;
        }

        int mCurrentPage = layoutManager.getPageIndex();
        int nextPage;
        float progress;
        float f;
        int offset;
        if (layoutManager.canScrollHorizontally()) {
            offset = layoutManager.getOffsetX();
        } else {
            offset = layoutManager.getOffsetY();
        }
        if (offset - layoutManager.getScrollForPage(mCurrentPage) > 0) {
            nextPage = mCurrentPage + 1;
        } else if (offset - layoutManager.getScrollForPage(mCurrentPage) < 0) {
            nextPage = mCurrentPage - 1;
        } else {
            nextPage = mCurrentPage;
        }
        if (mCurrentPage == 0) {
            if (layoutManager.canScrollHorizontally()) {
                f = layoutManager.getOffsetX() / (float) (parent.getWidth());
            } else {
                f = layoutManager.getOffsetY() / (float) (parent.getHeight());
            }
        } else {
            if (layoutManager.canScrollHorizontally()) {
                f = (layoutManager.getOffsetX() - mCurrentPage * parent.getWidth()) / (float) (parent.getWidth());
            } else {
                f = (layoutManager.getOffsetY() - mCurrentPage * parent.getHeight()) / (float) (parent.getHeight());
            }
        }
        progress = mInterpolator.getInterpolation(f);
        drawInactiveIndicators(c, indicatorStartX, indicatorPosY, mCurrentPage, progress, pageCount, nextPage);
    }

    private void drawInactiveIndicators(Canvas c, float indicatorStartX, float indicatorPosY,
                                        int highlightPosition, float progress, int pageCount, int nextPage) {
        int blendColor = ColorUtils.blendARGB(colorActive, colorInactive, progress);
        int blendColor1 = ColorUtils.blendARGB(colorActive, colorInactive, 1 - progress);

        final float itemWidth = mLongIndicatorItemLength + mIndicatorItemPadding;
        float start;
        if (layoutManager.canScrollHorizontally()) {
            start = indicatorStartX;
        } else {
            start = indicatorPosY;
        }
        if (progress == 0F) {
            for (int i = 0; i < pageCount; i++) {
                if (i == highlightPosition) {
                    mPaint.setColor(colorActive);
                    if (layoutManager.canScrollHorizontally()) {
                        c.drawLine(start, indicatorPosY, start + mLongIndicatorItemLength, indicatorPosY, mPaint);
                        start += itemWidth;
                    } else {
                        c.drawLine(indicatorStartX, start, indicatorStartX, start + mLongIndicatorItemLength, mPaint);
                        start += itemWidth;
                    }
                } else {
                    mPaint.setColor(colorInactive);
                    if (layoutManager.canScrollHorizontally()) {
                        c.drawLine(start, indicatorPosY, start + mShortIndicatorItemLength, indicatorPosY, mPaint);
                        start += mShortIndicatorItemLength + mIndicatorItemPadding;
                    } else {
                        c.drawLine(indicatorStartX, start, indicatorStartX, start + mShortIndicatorItemLength, mPaint);
                        start += mShortIndicatorItemLength + mIndicatorItemPadding;
                    }
                }
            }
        } else {
            float partialLength = mShortIndicatorItemLength * progress;
            for (int i = 0; i < pageCount; i++) {
                if (nextPage > highlightPosition) {
                    if (i == highlightPosition) {
                        mPaint.setColor(blendColor);
                        if (layoutManager.canScrollHorizontally()) {
                            c.drawLine(start, indicatorPosY, start + mLongIndicatorItemLength - partialLength, indicatorPosY, mPaint);
                            start += itemWidth;
                        } else {
                            c.drawLine(indicatorStartX, start, indicatorStartX, start + mLongIndicatorItemLength - partialLength, mPaint);
                            start += itemWidth;
                        }

                    } else if (i == nextPage) {
                        mPaint.setColor(blendColor1);
                        if (layoutManager.canScrollHorizontally()) {
                            c.drawLine(start - partialLength, indicatorPosY, start + mShortIndicatorItemLength, indicatorPosY, mPaint);
                            start += mShortIndicatorItemLength + mIndicatorItemPadding;
                        } else {
                            c.drawLine(indicatorStartX, start - partialLength, indicatorStartX, start + mShortIndicatorItemLength, mPaint);
                            start += mShortIndicatorItemLength + mIndicatorItemPadding;
                        }
                    } else {
                        mPaint.setColor(colorInactive);
                        if (layoutManager.canScrollHorizontally()) {
                            c.drawLine(start, indicatorPosY, start + mShortIndicatorItemLength, indicatorPosY, mPaint);
                            start += mShortIndicatorItemLength + mIndicatorItemPadding;
                        } else {
                            c.drawLine(indicatorStartX, start, indicatorStartX, start + mShortIndicatorItemLength, mPaint);
                            start += mShortIndicatorItemLength + mIndicatorItemPadding;
                        }
                    }
                } else {
                    if (i == highlightPosition) {
                        mPaint.setColor(blendColor);
                        if (layoutManager.canScrollHorizontally()) {
                            c.drawLine(start + partialLength, indicatorPosY, start + mLongIndicatorItemLength, indicatorPosY, mPaint);
                            start += itemWidth;
                        } else {
                            c.drawLine(indicatorStartX, start + partialLength, indicatorStartX, start + mLongIndicatorItemLength, mPaint);
                            start += itemWidth;
                        }
                    } else if (i == nextPage) {
                        mPaint.setColor(blendColor1);
                        if (layoutManager.canScrollHorizontally()) {
                            c.drawLine(start, indicatorPosY, start + mShortIndicatorItemLength + partialLength, indicatorPosY, mPaint);
                            start += mShortIndicatorItemLength + mIndicatorItemPadding;
                        } else {
                            c.drawLine(indicatorStartX, start, indicatorStartX, start + mShortIndicatorItemLength + partialLength, mPaint);
                            start += mShortIndicatorItemLength + mIndicatorItemPadding;
                        }
                    } else {
                        mPaint.setColor(colorInactive);
                        if (layoutManager.canScrollHorizontally()) {
                            c.drawLine(start, indicatorPosY, start + mShortIndicatorItemLength, indicatorPosY, mPaint);
                            start += mShortIndicatorItemLength + mIndicatorItemPadding;
                        } else {
                            c.drawLine(indicatorStartX, start, indicatorStartX, start + mShortIndicatorItemLength, mPaint);
                            start += mShortIndicatorItemLength + mIndicatorItemPadding;
                        }
                    }
                }
            }
        }
    }
    /* zhaoy end */

    /* zhaoy: 2019/4/9 indicator  */
/*    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int pageCount;
        if (layoutManager != null) {
            pageCount = layoutManager.getPageSize();
        } else {
            Log.e(TAG, "HorizontalPageLayoutManager is null");
            return;
        }

        float totalLength = mLongIndicatorItemLength * pageCount;
        float paddingBetweenItems = Math.max(0, pageCount - 1) * mIndicatorItemPadding;
        float indicatorTotalWidth = totalLength + paddingBetweenItems;

        float indicatorStartX;
        float indicatorPosY;
        if (layoutManager.canScrollHorizontally()) {
            indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2F;
            indicatorPosY = parent.getHeight() - mIndicatorHeight / 2F;
        } else {
            indicatorStartX = AppsConfig.MARKER_LEFT_MARGIN;
            indicatorPosY = AppsConfig.MARKER_TOP_MARGIN;
        }

        drawInactiveIndicators(c, indicatorStartX, indicatorPosY, pageCount);

        int mCurrentPage = layoutManager.getPageIndex();
        int nextPage;
        float progress;
        float f;
        int offset;
        if (layoutManager.canScrollHorizontally()) {
            offset = layoutManager.getOffsetX();
        } else {
            offset = layoutManager.getOffsetY();
        }
        if (offset - layoutManager.getScrollForPage(mCurrentPage) > 0) {
            nextPage = mCurrentPage + 1;
        } else {
            nextPage = mCurrentPage - 1;
        }
        if (mCurrentPage == 0) {
            if (layoutManager.canScrollHorizontally()) {
                f = layoutManager.getOffsetX() / (float) (parent.getWidth());
            } else {
                f = layoutManager.getOffsetY() / (float) (parent.getHeight());
            }
        } else {
            if (layoutManager.canScrollHorizontally()) {
                f = (layoutManager.getOffsetX() - mCurrentPage * parent.getWidth()) / (float) (parent.getWidth());
            } else {
                f = (layoutManager.getOffsetY() - mCurrentPage * parent.getHeight()) / (float) (parent.getHeight());
            }
        }

        progress = mInterpolator.getInterpolation(f);

        drawHighlights(c, indicatorStartX, indicatorPosY, mCurrentPage, progress, pageCount, nextPage);
    }

    private void drawInactiveIndicators(Canvas c, float indicatorStartX, float indicatorPosY, int pageCount) {
        mPaint.setColor(colorInactive);

        final float itemWidth = mLongIndicatorItemLength + mIndicatorItemPadding;

        float start;
        if (layoutManager.canScrollHorizontally()) {
            start = indicatorStartX;
//            for (int i = 0; i < pageCount; i++) {
//                c.drawLine(start, indicatorPosY, start + mLongIndicatorItemLength, indicatorPosY, mPaint);
//                start += itemWidth;
//            }
            c.drawLine(start, indicatorPosY, start + itemWidth * pageCount, indicatorPosY, mPaint);
        } else {
            start = indicatorPosY;
            c.drawLine(indicatorStartX, start, indicatorStartX, start + itemWidth * pageCount, mPaint);
        }
//        else {
//            start = indicatorPosY;
//            for (int i = 0; i < pageCount; i++) {
//                c.drawLine(indicatorStartX, start, indicatorStartX, start + mLongIndicatorItemLength, mPaint);
//                start += itemWidth;
//            }
//        }
    }

    private void drawHighlights(Canvas c, float indicatorStartX, float indicatorPosY,
                                int highlightPosition, float progress, int pageCount, int nextPage) {
        mPaint.setColor(colorActive);

        final float itemWidth = mLongIndicatorItemLength + mIndicatorItemPadding;

        if (progress == 0F) {
            if (layoutManager.canScrollHorizontally()) {
                float highlightStart = indicatorStartX + itemWidth * highlightPosition;
                c.drawLine(highlightStart, indicatorPosY,
                        highlightStart + mLongIndicatorItemLength, indicatorPosY, mPaint);
            } else {
                float highlightStart = indicatorPosY + itemWidth * highlightPosition;
                c.drawLine(indicatorStartX, highlightStart,
                        indicatorStartX, highlightStart + mLongIndicatorItemLength, mPaint);
            }
        } else if (progress != 1) {
            if (layoutManager.canScrollHorizontally()) {
                if (nextPage > highlightPosition) {
                    if (progress <= 0.2) {
                        float highlightStart = indicatorStartX + itemWidth * highlightPosition;
                        float partialLength = mLongIndicatorItemLength * progress;

                        c.drawLine(highlightStart, indicatorPosY,
                                highlightStart + mLongIndicatorItemLength, indicatorPosY, mPaint);

                        Log.e(TAG, "progress: " + progress + ", highlightPosition: " + highlightPosition + ", highlightStart: " + highlightStart + ", partialLength: " + partialLength);

                        if (highlightPosition <= pageCount - 1) {
                            highlightStart += itemWidth;
                            c.drawLine(highlightStart, indicatorPosY,
                                    highlightStart + partialLength, indicatorPosY, mPaint);
                            Log.e(TAG, "highlightStart: " + highlightStart);
                        }
                    } else {
                        float highlightStart = indicatorStartX + itemWidth * highlightPosition;
                        float partialLength = (float) (mLongIndicatorItemLength * (progress - 0.2) * 1.25);
                        float partialLength1 = mLongIndicatorItemLength * progress;

                        c.drawLine(highlightStart + partialLength, indicatorPosY,
                                highlightStart + mLongIndicatorItemLength, indicatorPosY, mPaint);

                        Log.e(TAG, "progress: " + progress + ", highlightPosition: " + highlightPosition + ", highlightStart: " + highlightStart + ", partialLength: " + partialLength);

                        if (highlightPosition <= pageCount - 1) {
                            highlightStart += itemWidth;
                            c.drawLine(highlightStart, indicatorPosY,
                                    highlightStart + partialLength1, indicatorPosY, mPaint);
                            Log.e(TAG, "highlightStart: " + highlightStart);
                        }
                    }
                } else {
                    if (progress <= 0.2) {
                        float highlightStart = indicatorStartX + itemWidth * highlightPosition;
                        float partialLength = mLongIndicatorItemLength * progress;

                        c.drawLine(highlightStart, indicatorPosY,
                                highlightStart + mLongIndicatorItemLength, indicatorPosY, mPaint);

                        Log.e(TAG, "progress: " + progress + ", highlightPosition: " + highlightPosition + ", highlightStart: " + highlightStart + ", partialLength " + partialLength);

                        if (highlightPosition <= pageCount - 1) {
                            highlightStart -= mIndicatorItemPadding;
                            c.drawLine(highlightStart - partialLength, indicatorPosY,
                                    highlightStart, indicatorPosY, mPaint);
                            Log.e(TAG, "highlightStart: " + highlightStart);
                        }
                    } else {
                        float highlightStart = indicatorStartX + itemWidth * highlightPosition;
                        float partialLength = (float) (mLongIndicatorItemLength * (progress - 0.2) * 1.25);
                        float partialLength1 = mLongIndicatorItemLength * progress;

                        c.drawLine(highlightStart, indicatorPosY,
                                highlightStart + mLongIndicatorItemLength - partialLength, indicatorPosY, mPaint);

                        Log.e(TAG, "progress: " + progress + ", highlightPosition: " + highlightPosition + ", highlightStart: " + highlightStart + ", partialLength: " + partialLength);

                        if (highlightPosition <= pageCount - 1) {
                            highlightStart -= mIndicatorItemPadding;
                            c.drawLine(highlightStart - partialLength1, indicatorPosY,
                                    highlightStart, indicatorPosY, mPaint);
                            Log.e(TAG, "highlightStart: " + highlightStart);
                        }
                    }
                }
            } else {
                if (nextPage > highlightPosition) {
                    if (progress <= 0.2) {
                        float highlightStart = indicatorPosY + itemWidth * highlightPosition;
                        float partialLength = mLongIndicatorItemLength * progress;

                        c.drawLine(indicatorStartX, highlightStart,
                                indicatorStartX, highlightStart + mLongIndicatorItemLength, mPaint);

                        Log.e(TAG, "progress: " + progress + ", highlightPosition: " + highlightPosition + ", highlightStart: " + highlightStart + ", partialLength: " + partialLength);

                        if (highlightPosition <= pageCount - 1) {
                            highlightStart += itemWidth;
                            c.drawLine(indicatorStartX, highlightStart,
                                    indicatorStartX, highlightStart + partialLength, mPaint);
                            Log.e(TAG, "highlightStart: " + highlightStart);
                        }
                    } else {
                        float highlightStart = indicatorPosY + itemWidth * highlightPosition;
                        float partialLength = (float) (mLongIndicatorItemLength * (progress - 0.2) * 1.25);
                        float partialLength1 = mLongIndicatorItemLength * progress;

                        c.drawLine(indicatorStartX, highlightStart + partialLength,
                                indicatorStartX, highlightStart + mLongIndicatorItemLength, mPaint);

                        Log.e(TAG, "progress: " + progress + ", highlightPosition: " + highlightPosition + ", highlightStart: " + highlightStart + ", partialLength: " + partialLength);

                        if (highlightPosition <= pageCount - 1) {
                            highlightStart += itemWidth;
                            c.drawLine(indicatorStartX, highlightStart,
                                    indicatorStartX, highlightStart + partialLength1, mPaint);
                            Log.e(TAG, "highlightStart: " + highlightStart);
                        }
                    }
                } else {
                    if (progress <= 0.2) {
                        float highlightStart = indicatorPosY + itemWidth * highlightPosition;
                        float partialLength = mLongIndicatorItemLength * progress;

                        c.drawLine(indicatorStartX, highlightStart,
                                indicatorStartX, highlightStart + mLongIndicatorItemLength, mPaint);

                        Log.e(TAG, "progress: " + progress + ", highlightPosition: " + highlightPosition + ", highlightStart: " + highlightStart + ", partialLength: " + partialLength);

                        if (highlightPosition <= pageCount - 1) {
                            highlightStart -= mIndicatorItemPadding;
                            c.drawLine(indicatorStartX, highlightStart - partialLength,
                                    indicatorStartX, highlightStart, mPaint);
                            Log.e(TAG, "highlightStart: " + highlightStart);
                        }
                    } else {
                        float highlightStart = indicatorPosY + itemWidth * highlightPosition;
                        float partialLength = (float) (mLongIndicatorItemLength * (progress - 0.2) * 1.25);
                        float partialLength1 = mLongIndicatorItemLength * progress;

                        c.drawLine(indicatorStartX, highlightStart,
                                indicatorStartX, highlightStart + mLongIndicatorItemLength - partialLength, mPaint);

                        Log.e(TAG, "progress: " + progress + ", highlightPosition: " + highlightPosition + ", highlightStart: " + highlightStart + ", partialLength: + partialLength);

                        if (highlightPosition <= pageCount - 1) {
                            highlightStart -= mIndicatorItemPadding;
                            c.drawLine(indicatorStartX, highlightStart - partialLength1,
                                    indicatorStartX, highlightStart, mPaint);
                            Log.e(TAG, "highlightStart: " + highlightStart);
                        }
                    }
                }
            }
        }
    }*/
    /* zhaoy end */
}
