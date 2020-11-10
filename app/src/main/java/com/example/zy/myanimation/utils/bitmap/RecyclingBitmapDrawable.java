package com.example.zy.myanimation.utils.bitmap;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class RecyclingBitmapDrawable extends BitmapDrawable {

    static final String TAG = "RecyclingBitmapDrawable";

    private int mDisplayRefCount = 0;

    private boolean mHasBeenDisplayed;

    public RecyclingBitmapDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
    }

    void setIsDisplayed(boolean isDisplayed) {
        if (isDisplayed) {
            mDisplayRefCount++;
            mHasBeenDisplayed = true;
        } else {
            mDisplayRefCount--;
        }
        checkState();
    }

    private void checkState() {
        BitmapUtils bitmapUtils = BitmapUtils.getInstance();
        if (mDisplayRefCount <= 0 && mHasBeenDisplayed && hasValidBitmap()) {
            bitmapUtils.removeFormUsable(getBitmap());
        }
    }

    private boolean hasValidBitmap() {
        Bitmap bitmap = getBitmap();
        return bitmap != null && !bitmap.isRecycled();
    }

}
