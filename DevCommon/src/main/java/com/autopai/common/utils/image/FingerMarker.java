package com.autopai.common.utils.image;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;

//reference ImageUtil.getBitmapWinthinPath() use marker crop bitmap
public class FingerMarker{
    private static final String TAG = "FingerMarker";
    private Path mFingerPath = new Path();
    private Paint mFingerPaint;

    public FingerMarker(){
        init();
    }

    private void init(){
        mFingerPaint = new Paint();
        mFingerPaint.setAntiAlias(true);
        mFingerPaint.setStyle(Paint.Style.STROKE);
        mFingerPaint.setColor(Color.CYAN);
        mFingerPaint.setStrokeWidth(6);
    }

    public void setMarkerColor(int color){
        mFingerPaint.setColor(color);
    }

    public void setMarkerPaint(Paint paint){
        mFingerPaint = paint;
    }

    //build marker path used at dispatchTouchEvent
    public boolean onTouchEvent(final MotionEvent ev) {
        boolean consume = false;
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                Log.e(TAG, "UUIM moveTo: " + ev.getX() + "," + ev.getY());

                mFingerPath.reset();
                mFingerPath.moveTo(ev.getX(), ev.getY());
                //invalidate();
                consume = true;
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                Log.e(TAG, "UUIM lineTo: " + ev.getX() + "," + ev.getY());
                mFingerPath.lineTo(ev.getX(), ev.getY());
                //invalidate();
                consume = true;
            }
            break;
            case MotionEvent.ACTION_UP: {
                Log.e(TAG, "UUIM close: " + ev.getX() + "," + ev.getY());
                mFingerPath.lineTo(ev.getX(), ev.getY());
                mFingerPath.close();
                //clipBp = ImageUtil.getBitmapWinthinPath(bitmap, mFingerPath);
                //invalidate();
                consume = true;
            }
            break;
            case MotionEvent.ACTION_CANCEL: {
                mFingerPath.reset();
                //invalidate();
                consume = true;
            }
            break;
        }
        return consume;
    }

    //draw marker use canvas
    public void drawMarker(Canvas canvas){
        if(!mFingerPath.isEmpty()) {
            canvas.drawPath(mFingerPath, mFingerPaint);
        }
    }
}
