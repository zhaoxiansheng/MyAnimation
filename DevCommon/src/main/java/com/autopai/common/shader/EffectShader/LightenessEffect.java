package com.autopai.common.shader.EffectShader;

import android.graphics.ColorMatrix;
import android.view.View;

public class LightenessEffect extends ColorFilterEffect {
    private float mLighteness;
    public LightenessEffect(float lighteness) {
        super(lighteness);
    }

    @Override
    protected void initMatrix(float lighteness) {
        if(mMatrix == null) {
            mMatrix = new MultiColorMatrix();
        }

        mLighteness = lighteness;
        mMatrix.setScale(mLighteness, mLighteness, mLighteness, 1.0f);
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
