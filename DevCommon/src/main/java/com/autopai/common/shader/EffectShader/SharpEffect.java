package com.autopai.common.shader.EffectShader;

import android.graphics.ColorMatrix;
import android.view.View;

public class SharpEffect extends ColorFilterEffect {
    private float mSharp;
    public SharpEffect(float sharp) {
        super(sharp);
    }

    @Override
    protected void initMatrix(float sharp) {
        float[] shrpMatrix  = {
                4.8f * sharp, -1.0f * sharp, -0.1f * sharp, 0, -388.4f * sharp,
                -0.5f * sharp, 4.4f * sharp, -0.1f * sharp, 0, -388.4f * sharp,
                -0.5f * sharp, -1.0f * sharp, 5.2f * sharp, 0, -388.4f * sharp,
                0, 0, 0, 1.0f, 0};

        if(mMatrix == null) {
            mMatrix = new MultiColorMatrix(shrpMatrix);
        }

        mSharp = sharp;
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
