package com.autopai.common.utils.resolution;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

/**
 * child size match to ui design
 */
public class BasePixelAdapter {

    private WeakReference<ViewGroup> mContainer;
    private boolean mMeasureFinished = false;

    public BasePixelAdapter(ViewGroup container){
        mContainer = new WeakReference<>(container);
    }

    /**
     * call before onMeasure
     */
    protected void preMeasure() {
        if (!mMeasureFinished) {
            mMeasureFinished = true;
            ViewGroup container = mContainer.get();
            if(container != null) {
                Utils.getInstance(container.getContext()).initBaseLine(1080, 1920);
                float scaleX = Utils.getInstance(container.getContext()).getHorizontalScale();
                float scaleY = Utils.getInstance(container.getContext()).getVerticalScale();

                int childCount = container.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = container.getChildAt(i);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) child.getLayoutParams();
                    //compute size with scale
                    params.width = (int) (params.width * scaleX);
                    params.height = (int) (params.height * scaleY);
                    //compute margin with scale
                    params.leftMargin = (int) (params.leftMargin * scaleX);
                    params.rightMargin = (int) (params.rightMargin * scaleX);
                    params.topMargin = (int) (params.topMargin * scaleY);
                    params.bottomMargin = (int) (params.bottomMargin * scaleY);
//            child.setPadding();
                }
            }
        }
    }
}
