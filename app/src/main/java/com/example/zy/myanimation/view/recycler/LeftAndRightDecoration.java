package com.example.zy.myanimation.view.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.zy.myanimation.R;

/**
 * Created on 2018/4/8.
 * RecyclerView的左右item
 *
 * @author zhaoy
 */
public class LeftAndRightDecoration extends RecyclerView.ItemDecoration {

    private int tagWidth;
    private Paint rightPaint;

    public LeftAndRightDecoration(Context context) {
        rightPaint = new Paint();
        rightPaint.setColor(ContextCompat.getColor(context, R.color.title_color));
        tagWidth = context.getResources().getDimensionPixelSize(R.dimen.divider_width);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(child);
            int x = pos % 5;
            switch (x) {
                case 0:
                    float left1 = child.getLeft();
                    float right1 = left1 + tagWidth;
                    float top1 = child.getTop();
                    float bottom1 = child.getBottom();
                    c.drawRect(left1, top1, right1, bottom1, rightPaint);

                    float right2 = child.getRight();
                    float left2 = right2 - tagWidth;
                    float top2 = child.getTop();
                    float bottom2 = child.getBottom();
                    c.drawRect(left2, top2, right2, bottom2, rightPaint);
                    break;
                default:
                    float right = child.getRight();
                    float left = right - tagWidth;
                    float top = child.getTop();
                    float bottom = child.getBottom();
                    c.drawRect(left, top, right, bottom, rightPaint);
                    break;
            }

        }
    }
}
