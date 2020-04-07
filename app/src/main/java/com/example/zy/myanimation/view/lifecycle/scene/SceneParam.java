package com.example.zy.myanimation.view.lifecycle.scene;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class SceneParam implements Parcelable {
    private static final String TAG = "SceneParam";

    private int mSceneType = 0;
    private Rect mWinRect;

    public SceneParam(int sceneType, Rect winRect) {
        mSceneType = sceneType;
        mWinRect = winRect;
    }

    private SceneParam(Parcel source) {
        this.mSceneType = source.readInt();
        Log.e(TAG, "CCXB mSceneType: " + mSceneType);
        this.mWinRect = Rect.CREATOR.createFromParcel(source);
        Log.e(TAG, "CCXB mWinRect: " + mWinRect);
    }

    public int getWinWidth() {
        if (mWinRect != null) {
            return mWinRect.width();
        }
        return -1;
    }

    public int getWinHeight() {
        if (mWinRect != null) {
            return mWinRect.height();
        }
        return -1;
    }

    public int getWinLeft() {
        if (mWinRect != null) {
            return mWinRect.left;
        }
        return -1;
    }

    public int getWinTop() {
        if (mWinRect != null) {
            return mWinRect.top;
        }
        return -1;
    }

    public int getSceneType() {
        return mSceneType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mSceneType);
        mWinRect.writeToParcel(dest, 0);
    }

    public static final Creator<SceneParam> CREATOR = new Creator<SceneParam>() {

        @Override
        public SceneParam createFromParcel(Parcel source) {
            try {
                return new SceneParam(source);
            } catch (Exception e) {
                Log.e(TAG, "CCXB Exception creating SceneParam from parcel " + e);
                return null;
            }
        }

        @Override
        public SceneParam[] newArray(int size) {
            return new SceneParam[size];
        }

    };

    @Override
    public String toString() {
        return super.toString() + " sceneType: " + mSceneType + " winRect " + mWinRect;
    }
}