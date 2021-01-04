package com.autopai.common.shader.EffectShader;

import android.graphics.ColorMatrix;
import android.view.View;

public class GreyEffect extends ColorFilterEffect {
    private float mRatio;
    public GreyEffect(float ratio) {
        super(ratio);
    }

    @Override
    protected void initMatrix(float ratio) {
        mRatio = ratio;
        float[] shrpMatrix  = {
                0.33f * mRatio,0.59f * mRatio,0.11f * mRatio,0,0,   //red
                0.33f * mRatio,0.59f * mRatio,0.11f * mRatio,0,0,   //green
                0.33f * mRatio,0.59f * mRatio,0.11f * mRatio,0,0,   //blue
                0            ,0            ,0            ,1,0    //alpha
        };

        if(mMatrix == null) {
            mMatrix = new MultiColorMatrix(shrpMatrix);
        }
    }

    @Override
    public void setupShader(View target, float[] colorMatrix) {
        //do nothing
    }

    @Override
    public void setValue(float value) {
        initMatrix(value);
    }
}
