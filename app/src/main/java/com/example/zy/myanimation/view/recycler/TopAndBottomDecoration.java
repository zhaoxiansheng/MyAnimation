package com.example.zy.myanimation.view.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.example.zy.myanimation.R;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created on 2018/4/8.
 * recyclerView的分割线
 *
 * @author zhaoy
 */
public class TopAndBottomDecoration extends RecyclerView.ItemDecoration {

    private Paint bottomPaint;
    private int dividerHeight;

    public TopAndBottomDecoration(Context context) {
        bottomPaint = new Paint();
        bottomPaint.setColor(ContextCompat.getColor(context, R.color.title_color));
        dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.divider_height);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(child);
            int x = pos / 5;
            switch (x) {
                case 0:
                    float right2 = child.getRight();
                    float left2 = child.getLeft();
                    float top2 = child.getTop();
                    float bottom2 = top2 + dividerHeight;
                    c.drawRect(left2, top2, right2, bottom2, bottomPaint);

                    float right1 = child.getRight();
                    float left1 = child.getLeft();
                    float bottom1 = child.getBottom();
                    float top1 = bottom1 - dividerHeight;
                    c.drawRect(left1, top1, right1, bottom1, bottomPaint);
                    break;
                default:
                    float right = child.getRight();
                    float left = child.getLeft();
                    float bottom = child.getBottom();
                    float top = bottom - dividerHeight;
                    c.drawRect(left, top, right, bottom, bottomPaint);
                    break;
            }

        }
    }
}
