package com.autopai.common.shader;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;

public class MaskSet {
    private ArrayList<IMask> mMasks = new ArrayList();

    public void addMask(IMask mask){
        if(mask != null) {
            mMasks.add(mask);
        }
    }

    public void removeMask(IMask mask){
        if(mask != null) {
            mMasks.remove(mask);
        }
    }

    public void setBound(final Rect bound){
        for(IMask mask : mMasks) {
            mask.setBound(bound);
        }
    }

    public void drawMask(Canvas canvas) {
        for(IMask mask : mMasks) {
            mask.drawMask(canvas);
        }
    }

    public void reset(){
        mMasks.clear();
    }
}
