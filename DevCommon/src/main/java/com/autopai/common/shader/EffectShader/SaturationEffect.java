package com.autopai.common.shader.EffectShader;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;

public class SaturationEffect extends ColorFilterEffect {
    private float mSaturation;
    public SaturationEffect(float saturation) {
        super(saturation);
    }

    @Override
    protected void initMatrix(float saturation) {
        if(mMatrix == null) {
            mMatrix = new MultiColorMatrix();
        }

        mSaturation = saturation;
        mMatrix.setSaturation(mSaturation);
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
