package com.example.zy.myanimation.view.recycler.utils;

import android.graphics.Rect;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;

public class ClickAreaUtils {

    private static final String TAG = "ClickAreaUtils";

    public static void modifyClickArea(final View view, final int widthDimenID, final int heightDimenID) {
        if (view != null) {
            view.post(new Runnable() {
                @Override
                public void run() {

                    Rect bounds = new Rect();
                    view.setEnabled(true);
                    view.getHitRect(bounds);

                    int heightDimen = ScreenUtils.getDimens(heightDimenID);
                    int widthDimen = ScreenUtils.getDimens(widthDimenID);
                    int viewHeight = view.getHeight();
                    int viewWidth = view.getWidth();
                    int verticalModifyArea, horizontalModifyArea;
                    if (viewHeight >= heightDimen) {
                        verticalModifyArea = viewHeight - heightDimen;
                        bounds.bottom -= verticalModifyArea;
                    } else {
                        verticalModifyArea = heightDimen - viewHeight;
                        bounds.bottom += verticalModifyArea;
                    }
                    if (viewWidth >= widthDimen) {
                        horizontalModifyArea = (viewWidth - widthDimen) / 2;
                        bounds.left += horizontalModifyArea;
                        bounds.right -= horizontalModifyArea;
                    } else {
                        horizontalModifyArea = (widthDimen - viewWidth) / 2;
                        bounds.left -= horizontalModifyArea;
                        bounds.right += horizontalModifyArea;
                    }

                    TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                    if (View.class.isInstance(view.getParent())) {
                        ((View) view.getParent()).setTouchDelegate(touchDelegate);
                    }
                }
            });
        } else {
            Log.e(TAG, "view is null");
        }
    }

    public static void modifyClickArea(final View view, final int top, final int bottom, final int left, final int right) {
        if (view != null) {
            view.post(new Runnable() {
                @Override
                public void run() {

                    Rect bounds = new Rect();
                    view.setEnabled(true);
                    view.getHitRect(bounds);

                    bounds.top += top;
                    bounds.bottom -= bottom;
                    bounds.left += left;
                    bounds.right -= right;

                    TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                    if (View.class.isInstance(view.getParent())) {
                        ((View) view.getParent()).setTouchDelegate(touchDelegate);
                    }
                }
            });
        } else {
            Log.e(TAG, "view is null");
        }
    }
}
