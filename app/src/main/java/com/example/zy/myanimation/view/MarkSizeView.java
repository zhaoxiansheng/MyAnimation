package com.example.zy.myanimation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;
import com.example.zy.myanimation.R;

import java.util.ArrayList;
import java.util.List;

public class MarkSizeView extends View {

    private static final int DEFAULT_MARKED_COLOR = Color.parseColor("#00000000");
    private static final int DEFAULT_UNMARKED_COLOR = Color.parseColor("#7f000000");
    private static final int DEFAULT_STROKE_COLOR = Color.parseColor("#009688");
    private static final int DEFAULT_STROKE_WIDTH = 2;//dp
    private static final int DEFAULT_VERTEX_COLOR = Color.parseColor("#009688");
    private static final int DEFAULT_CONFIRM_BUTTON_RES = R.drawable.ic_done_white_36dp;
    private static final int DEFAULT_CANCEL_BUTTON_RES = R.drawable.ic_close_capture;

    private static final int BUTTON_EXTRA_WIDTH = ConvertUtils.dp2px(8);

    private static final int DEFAULT_VERTEX_WIDTH = 20;//dp


    private int markedColor = DEFAULT_MARKED_COLOR;
    private int unmarkedColor = DEFAULT_UNMARKED_COLOR;
    private int strokeColor = DEFAULT_STROKE_COLOR;
    private int strokeWidth = ConvertUtils.dp2px(DEFAULT_STROKE_WIDTH);//dp
    private int vertexColor = DEFAULT_VERTEX_COLOR;
    private int confirmButtonRes = DEFAULT_CONFIRM_BUTTON_RES;
    private int cancelButtonRes = DEFAULT_CANCEL_BUTTON_RES;
    private int vertexWidth = (int) ConvertUtils.dp2px(DEFAULT_VERTEX_WIDTH);
    private int mActionGap;


    private Paint unMarkPaint, markPaint, vertexPaint, mBitPaint;

    private int downX, downY;
    private int startX, startY;
    private int endX, endY;

    private Rect markedArea;
    private Rect confirmArea, cancelArea;
    private RectF ltVer, rtVer, lbVer, rbVer;
    private boolean isValid = false;
    private boolean isUp = false;
    private boolean isMoveMode = false;
    /**
     * 是否是大小调整
     */
    private boolean isAdjustMode = false;
    private boolean isButtonClicked = false;
    private int adjustNum = 0;

    private Bitmap confirmBitmap, cancelBitmap;

    private onClickListener mOnClickListener;

    private boolean isMarkRect = true;
    private GraphicPath mGraphicPath;

    private Bitmap mBitmap;

    private boolean ltr, ttb;

    private boolean isConfirmOrientation;

    public MarkSizeView(Context context) {
        super(context);
        init(context, null);
    }

    public MarkSizeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarkSizeView);
            markedColor = typedArray.getColor(R.styleable.MarkSizeView_markedColor, DEFAULT_MARKED_COLOR);
            unmarkedColor = typedArray.getColor(R.styleable.MarkSizeView_unMarkedColor, DEFAULT_UNMARKED_COLOR);
            strokeColor = typedArray.getColor(R.styleable.MarkSizeView_strokeColor, DEFAULT_STROKE_COLOR);
            strokeWidth = typedArray.getDimensionPixelSize(R.styleable.MarkSizeView_strokeWidth, ConvertUtils.dp2px(DEFAULT_STROKE_WIDTH));
            vertexColor = typedArray.getColor(R.styleable.MarkSizeView_vertexColor, DEFAULT_VERTEX_COLOR);
            vertexWidth = typedArray.getDimensionPixelSize(R.styleable.MarkSizeView_vertexWidth, ConvertUtils.dp2px(DEFAULT_VERTEX_WIDTH));
            confirmButtonRes = typedArray.getResourceId(R.styleable.MarkSizeView_confirmButtonRes, DEFAULT_CONFIRM_BUTTON_RES);
            cancelButtonRes = typedArray.getResourceId(R.styleable.MarkSizeView_cancleButtonRes, DEFAULT_CANCEL_BUTTON_RES);

            typedArray.recycle();
        }

        unMarkPaint = new Paint();
        unMarkPaint.setColor(unmarkedColor);
        unMarkPaint.setAntiAlias(true);

        markPaint = new Paint();
        markPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        markPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        markPaint.setColor(markedColor);
        markPaint.setStrokeWidth(strokeWidth);
        markPaint.setAntiAlias(true);

        vertexPaint = new Paint();
        vertexPaint.setColor(vertexColor);
        vertexPaint.setAntiAlias(true);

        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);

        markedArea = new Rect();
        confirmArea = new Rect();
        cancelArea = new Rect();

        ltVer = new RectF();
        rtVer = new RectF();
        lbVer = new RectF();
        rbVer = new RectF();

        confirmBitmap = BitmapFactory.decodeResource(getResources(), confirmButtonRes);
        cancelBitmap = BitmapFactory.decodeResource(getResources(), cancelButtonRes);

        mActionGap = ConvertUtils.dp2px(15);

        mGraphicPath = new GraphicPath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, mBitPaint);
        }

        canvas.drawRect(0, 0, width, height, unMarkPaint);

        //draw marked
        if (isMarkRect) {
            if (isValid || !isEnabled()) {
                if (mBitmap != null) {
                    canvas.drawBitmap(mBitmap, markedArea, markedArea, mBitPaint);
                }
            }
            if (!isEnabled()) {
                return;
            }
            //draw vertex
            if (isValid && isUp) {
                canvas.drawOval(ltVer, vertexPaint);
                canvas.drawOval(rtVer, vertexPaint);
                canvas.drawOval(lbVer, vertexPaint);
                canvas.drawOval(rbVer, vertexPaint);
            }

            //draw button
            if (isValid && isUp) {
                canvas.drawBitmap(confirmBitmap, null, confirmArea, mBitPaint);
                canvas.drawBitmap(cancelBitmap, null, cancelArea, mBitPaint);
            }
        } else {
            if (!isEnabled()) {
                return;
            }

            if (isUp) {
                if (isValid) {
                    Path path = new Path();
                    if (mGraphicPath.size() > 1) {
                        path.moveTo(mGraphicPath.pathX.get(0), mGraphicPath.pathY.get(0));
                        for (int i = 1; i < mGraphicPath.size(); i++) {
                            path.lineTo(mGraphicPath.pathX.get(i), mGraphicPath.pathY.get(i));
                        }
                    } else {
                        return;
                    }
                    canvas.drawPath(path, markPaint);
                }
            } else {
                if (mGraphicPath.size() > 1) {
                    for (int i = 1; i < mGraphicPath.size(); i++) {
                        canvas.drawLine(mGraphicPath.pathX.get(i - 1), mGraphicPath.pathY.get(i - 1), mGraphicPath.pathX.get(i), mGraphicPath.pathY.get(i), markPaint);
                    }
                }
            }

            //draw button
            if (isValid && isUp) {
                canvas.drawBitmap(confirmBitmap, null, confirmArea, mBitPaint);
                canvas.drawBitmap(cancelBitmap, null, cancelArea, mBitPaint);
            }
        }


        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        if (isMarkRect) {
            isMarkEvent(event);
        } else {
            notMarkEvent(event);
        }
        postInvalidate();
        return true;
    }

    private void isMarkEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isUp = false;
                isAdjustMode = false;
                isMoveMode = false;
                isButtonClicked = false;
                adjustNum = 0;
                downX = x;
                downY = y;
                if (mOnClickListener != null) {
                    mOnClickListener.onTouch();
                }
                if (isAreaContainPoint(confirmArea, x, y)) {
                    isButtonClicked = true;
                    isValid = true;
                    if (mOnClickListener != null) {
                        mOnClickListener.onConfirm(markedArea);
                    }
                } else if (isAreaContainPoint(cancelArea, x, y)) {
                    isButtonClicked = true;
                    isValid = true;
                    if (mOnClickListener != null) {
                        mOnClickListener.onCancel();
                        isValid = false;
                        startX = startY = endX = endY = 0;
                        adjustMark(0, 0);
                    }
                } else if (isAreaContainPoint(ltVer, x, y)) {
                    isAdjustMode = true;
                    adjustNum = 1;
                } else if (isAreaContainPoint(rtVer, x, y)) {
                    isAdjustMode = true;
                    adjustNum = 2;
                } else if (isAreaContainPoint(lbVer, x, y)) {
                    isAdjustMode = true;
                    adjustNum = 3;
                } else if (isAreaContainPoint(rbVer, x, y)) {
                    isAdjustMode = true;
                    adjustNum = 4;
                } else if (markedArea.contains(x, y)) {
                    isMoveMode = true;
                } else {
                    isMoveMode = false;
                    startX = (int) event.getX();
                    startY = (int) event.getY();
                    endX = startX;
                    endY = startY;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isButtonClicked) {
                    break;
                }
                adjustMark(x, y);
                break;
            case MotionEvent.ACTION_UP:
                isUp = true;
                if (isButtonClicked) {
                    break;
                }
                adjustMark(x, y);

                confirmOrientation();

                startX = markedArea.left;
                startY = markedArea.top;
                endX = markedArea.right;
                endY = markedArea.bottom;

                confirmButtonArea();

                if (!isValid) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onCancel();
                    }
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                isUp = true;
                break;
        }
    }

    private void notMarkEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isUp = false;
                isAdjustMode = false;
                isMoveMode = false;
                isButtonClicked = false;
                adjustNum = 0;
                downX = x;
                downY = y;
                if (mOnClickListener != null) {
                    mOnClickListener.onTouch();
                }
                if (isAreaContainPoint(confirmArea, x, y)) {
                    isButtonClicked = true;
                    isValid = true;
                    if (mOnClickListener != null) {
                        mOnClickListener.onConfirm(mGraphicPath);
                    }
                } else if (isAreaContainPoint(cancelArea, x, y)) {
                    isButtonClicked = true;
                    isValid = true;
                    if (mOnClickListener != null) {
                        mOnClickListener.onCancel();
                        isValid = false;
                        startX = startY = endX = endY = 0;
                        adjustMark(0, 0);
                    }
                    mGraphicPath.clear();
                } else {
                    isMoveMode = false;
                    startX = (int) event.getX();
                    startY = (int) event.getY();
                    endX = startX;
                    endY = startY;
                    mGraphicPath.clear();
                    mGraphicPath.addPath(x, y);

                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isButtonClicked) {
                    break;
                }
                mGraphicPath.addPath(x, y);
                break;
            case MotionEvent.ACTION_UP:
                isUp = true;
                if (isButtonClicked) {
                    break;
                }
                mGraphicPath.addPath(x, y);

                startX = mGraphicPath.getLeft();
                startY = mGraphicPath.getTop();
                endX = mGraphicPath.getRight();
                endY = mGraphicPath.getBottom();

                if ((endX - startX) * (endY - startY) > 200) {
                    isValid = true;
                }
                markedArea.set(startX, startY, endX, endY);
                if (endY < getHeight() - confirmBitmap.getHeight() * 3) {
                    //显示在选区的下面
                    confirmArea.set(endX - confirmBitmap.getWidth() - mActionGap, endY + mActionGap, endX - mActionGap, endY + confirmBitmap.getHeight() + mActionGap);
                    cancelArea.set(endX - 2 * confirmBitmap.getWidth() - mActionGap * 2, endY + mActionGap, endX - confirmBitmap.getWidth() - mActionGap * 2, endY + confirmBitmap.getHeight() + mActionGap);
                } else if (startY > confirmBitmap.getHeight() * 3) {
                    //显示在选区的上面
                    confirmArea.set(endX - confirmBitmap.getWidth() - mActionGap, startY - confirmBitmap.getHeight() - mActionGap, endX - mActionGap, startY - mActionGap);
                    cancelArea.set(endX - 2 * confirmBitmap.getWidth() - mActionGap * 2, startY - confirmBitmap.getHeight() - mActionGap, endX - confirmBitmap.getWidth() - mActionGap * 2, startY - mActionGap);
                } else
//                    if (markedArea.width() > confirmBitmap.getWidth() * 3 + mActionGap * 3 && markedArea.height() > confirmBitmap.getHeight() * 5)
                {
                    //显示在选区的内底部
                    confirmArea.set(endX - confirmBitmap.getWidth() - mActionGap, endY - confirmBitmap.getHeight() - mActionGap, endX - mActionGap, endY - mActionGap);
                    cancelArea.set(endX - 2 * confirmBitmap.getWidth() - mActionGap * 2, endY - confirmBitmap.getHeight() - mActionGap, endX - confirmBitmap.getWidth() - mActionGap * 2, endY - mActionGap);
                }

                if (cancelArea.left < 0) {
                    int cancelAreaLeftMargin = Math.abs(cancelArea.left) + mActionGap;
                    cancelArea.left = cancelArea.left + cancelAreaLeftMargin;
                    cancelArea.right = cancelArea.right + cancelAreaLeftMargin;
                    confirmArea.left = confirmArea.left + cancelAreaLeftMargin;
                    confirmArea.right = confirmArea.right + cancelAreaLeftMargin;
                }
                if (!isValid) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onCancel();
                    }
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                isUp = true;
                break;
        }
    }

    private boolean isAreaContainPoint(Rect area, int x, int y) {
        Rect newArea = new Rect(area.left - BUTTON_EXTRA_WIDTH, area.top - BUTTON_EXTRA_WIDTH, area.right + BUTTON_EXTRA_WIDTH, area.bottom + BUTTON_EXTRA_WIDTH);
        return newArea.contains(x, y);
    }

    private boolean isAreaContainPoint(RectF area, int x, int y) {
        RectF newArea = new RectF(area.left - BUTTON_EXTRA_WIDTH, area.top - BUTTON_EXTRA_WIDTH, area.right + BUTTON_EXTRA_WIDTH, area.bottom + BUTTON_EXTRA_WIDTH);
        return newArea.contains(x, y);
    }

    private void adjustMark(int x, int y) {
        if (isAdjustMode) {
            int moveMentX = x - downX;
            int moveMentY = y - downY;

            switch (adjustNum) {
                case 1:
                    startX = startX + moveMentX;
                    startY = startY + moveMentY;
                    break;
                case 2:
                    endX = endX + moveMentX;
                    startY = startY + moveMentY;
                    break;
                case 3:
                    startX = startX + moveMentX;
                    endY = endY + moveMentY;
                    break;
                case 4:
                    endX = endX + moveMentX;
                    endY = endY + moveMentY;
                    break;
            }
            downX = x;
            downY = y;
        } else if (isMoveMode) {
            int moveMentX = x - downX;
            int moveMentY = y - downY;

            startX = startX + moveMentX;
            startY = startY + moveMentY;

            endX = endX + moveMentX;
            endY = endY + moveMentY;

            downX = x;
            downY = y;
        } else {
            endX = x;
            endY = y;
        }
        markedArea.set(Math.min(startX, endX), Math.min(startY, endY), Math.max(startX, endX), Math.max(startY, endY));

        markedArea.set(Math.max(markedArea.left, getLeft()), Math.max(markedArea.top, getTop())
                , Math.min(markedArea.right, getRight()), Math.min(markedArea.bottom, getBottom()));

        ltVer.set(markedArea.left - (vertexWidth >> 1), markedArea.top - (vertexWidth >> 1), markedArea.left + (vertexWidth >> 1), markedArea.top + (vertexWidth >> 1));
        rtVer.set(markedArea.right - (vertexWidth >> 1), markedArea.top - (vertexWidth >> 1), markedArea.right + (vertexWidth >> 1), markedArea.top + (vertexWidth >> 1));
        lbVer.set(markedArea.left - (vertexWidth >> 1), markedArea.bottom - (vertexWidth >> 1), markedArea.left + (vertexWidth >> 1), markedArea.bottom + (vertexWidth >> 1));
        rbVer.set(markedArea.right - (vertexWidth >> 1), markedArea.bottom - (vertexWidth >> 1), markedArea.right + (vertexWidth >> 1), markedArea.bottom + (vertexWidth >> 1));
        isValid = markedArea.height() * markedArea.width() > 200;
    }

    public interface onClickListener {
        void onConfirm(Rect markedArea);

        void onConfirm(GraphicPath path);

        void onCancel();

        void onTouch();
    }

    public void setOnClickListener(onClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public void setUnmarkedColor(int unmarkedColor) {
        this.unmarkedColor = unmarkedColor;
        unMarkPaint.setColor(unmarkedColor);
        invalidate();
    }

    public void setMarkedColor(int markedColor) {
        this.markedColor = markedColor;
        markPaint.setColor(markedColor);
        invalidate();
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
        invalidate();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void reset() {
        isUp = false;
        isValid = false;
        startX = startY = endX = endY = 0;
        mGraphicPath = new GraphicPath();
        adjustMark(0, 0);
        mBitmap = null;
        isConfirmOrientation = false;
    }

    public void setIsMarkRect(boolean isMarkRect) {
        this.isMarkRect = isMarkRect;
    }

    private void confirmOrientation() {
        if (!isConfirmOrientation) {
            isConfirmOrientation = true;
            ltr = startX <= endX;
            ttb = startY <= endY;
        }
    }

    private void confirmButtonArea() {
        if (ltr && ttb) {
            if (getBottom() - endY < confirmBitmap.getHeight() + mActionGap) {
                //显示在选区的内底部
                confirmArea.set(endX - confirmBitmap.getWidth() - mActionGap, endY - confirmBitmap.getHeight() - mActionGap, endX - mActionGap, endY - mActionGap);
                cancelArea.set(endX - 2 * confirmBitmap.getWidth() - mActionGap * 2, endY - confirmBitmap.getHeight() - mActionGap, endX - confirmBitmap.getWidth() - mActionGap * 2, endY - mActionGap);
            } else {
                //显示在选区的下面
                confirmArea.set(endX - confirmBitmap.getWidth() - mActionGap, endY + mActionGap, endX - mActionGap, endY + confirmBitmap.getHeight() + mActionGap);
                cancelArea.set(endX - 2 * confirmBitmap.getWidth() - mActionGap * 2, endY + mActionGap, endX - confirmBitmap.getWidth() - mActionGap * 2, endY + confirmBitmap.getHeight() + mActionGap);
            }
        }
        if (ltr && !ttb) {
            if (startY - getTop() < confirmBitmap.getHeight() + mActionGap) {
                //显示在选区的内底部
                confirmArea.set(endX - confirmBitmap.getWidth() - mActionGap, endY - confirmBitmap.getHeight() - mActionGap, endX - mActionGap, endY - mActionGap);
                cancelArea.set(endX - 2 * confirmBitmap.getWidth() - mActionGap * 2, endY - confirmBitmap.getHeight() - mActionGap, endX - confirmBitmap.getWidth() - mActionGap * 2, endY - mActionGap);
            } else {
                //显示在选区的上面
                confirmArea.set(endX - confirmBitmap.getWidth() - mActionGap, startY - confirmBitmap.getHeight() - mActionGap, endX - mActionGap, startY - mActionGap);
                cancelArea.set(endX - 2 * confirmBitmap.getWidth() - mActionGap * 2, startY - confirmBitmap.getHeight() - mActionGap, endX - confirmBitmap.getWidth() - mActionGap * 2, startY - mActionGap);
            }
        }

        if (!ltr && ttb) {
            if (getBottom() - endY < confirmBitmap.getHeight() + mActionGap) {
                //显示在选区的内底部
                confirmArea.set(startX + mActionGap, endY - confirmBitmap.getHeight() - mActionGap, startX + confirmBitmap.getWidth() + mActionGap, endY - mActionGap);
                cancelArea.set(startX + confirmBitmap.getWidth() + mActionGap * 2, endY - confirmBitmap.getHeight() - mActionGap, startX + 2 * confirmBitmap.getWidth() + mActionGap * 2, endY - mActionGap);
            } else {
                //显示在选区的下面
                confirmArea.set(startX + mActionGap, endY + mActionGap, startX + confirmBitmap.getWidth() + mActionGap, endY + confirmBitmap.getHeight() + mActionGap);
                cancelArea.set(startX + confirmBitmap.getWidth() + mActionGap * 2, endY + mActionGap, startX + confirmBitmap.getWidth() * 2 + mActionGap * 2, endY + confirmBitmap.getHeight() + mActionGap);
            }
        }
        if (!ltr && !ttb) {
            if (startY - getTop() < confirmBitmap.getHeight() + mActionGap) {
                //显示在选区的内底部
                confirmArea.set(startX + mActionGap, startY + mActionGap, startX + confirmBitmap.getWidth() + mActionGap, startY + confirmBitmap.getHeight() + mActionGap);
                cancelArea.set(startX + confirmBitmap.getWidth() + mActionGap * 2, startY + mActionGap, startX + 2 * confirmBitmap.getWidth() + mActionGap * 2, startY + confirmBitmap.getHeight() + mActionGap);
            } else {
                //显示在选区的上面
                confirmArea.set(startX + mActionGap, startY - confirmBitmap.getHeight() - mActionGap, startX + confirmBitmap.getWidth() + mActionGap, startY - mActionGap);
                cancelArea.set(startX + confirmBitmap.getWidth() + mActionGap * 2, startY - confirmBitmap.getHeight() - mActionGap, startX + 2 * confirmBitmap.getWidth() + mActionGap * 2, startY - mActionGap);
            }
        }
    }

    public static class GraphicPath implements Parcelable {

        GraphicPath(Parcel in) {
            int size = in.readInt();
            int[] x = new int[size];
            int[] y = new int[size];
            in.readIntArray(x);
            in.readIntArray(y);
            pathX = new ArrayList<>();
            pathY = new ArrayList<>();

            for (int item : x) {
                pathX.add(item);
            }

            for (int value : y) {
                pathY.add(value);
            }
        }

        public static final Creator<GraphicPath> CREATOR = new Creator<GraphicPath>() {
            @Override
            public GraphicPath createFromParcel(Parcel in) {
                return new GraphicPath(in);
            }

            @Override
            public GraphicPath[] newArray(int size) {
                return new GraphicPath[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(pathX.size());
            dest.writeIntArray(getXArray());
            dest.writeIntArray(getYArray());
        }

        List<Integer> pathX;
        List<Integer> pathY;

        GraphicPath() {
            pathX = new ArrayList<>();
            pathY = new ArrayList<>();
        }

        private int[] getXArray() {
            int[] x = new int[pathX.size()];
            for (int i = 0; i < x.length; i++) {
                x[i] = pathX.get(i);
            }
            return x;
        }

        private int[] getYArray() {
            int[] y = new int[pathY.size()];
            for (int i = 0; i < y.length; i++) {
                y[i] = pathY.get(i);
            }
            return y;
        }

        void addPath(int x, int y) {
            pathX.add(x);
            pathY.add(y);
        }

        public void clear() {
            pathX.clear();
            pathY.clear();
        }

        public int getTop() {
            int min = pathY.size() > 0 ? pathY.get(0) : 0;
            for (int y : pathY) {
                if (y < min) {
                    min = y;
                }
            }
            return min;
        }

        public int getLeft() {
            int min = pathX.size() > 0 ? pathX.get(0) : 0;
            for (int x : pathX) {
                if (x < min) {
                    min = x;
                }
            }
            return min;
        }

        public int getBottom() {
            int max = pathY.size() > 0 ? pathY.get(0) : 0;
            for (int y : pathY) {
                if (y > max) {
                    max = y;
                }
            }
            return max;
        }

        public int getRight() {
            int max = pathX.size() > 0 ? pathX.get(0) : 0;
            for (int x : pathX) {
                if (x > max) {
                    max = x;
                }
            }
            return max;
        }

        public int size() {
            return pathY.size();
        }
    }
}