package com.autopai.common.shader;

import android.content.Context;

public class MaskShaderFactory {

    public static final int CIRCLE_GRADIENT = 0;
    public static final int OVAL_GRADIENT = 1;
    public static final int LINER_GRADIENT = 2;
    public static final int SWEEP_GRADIENT = 3;
    public static final int BRIGHTNESS_MASK = 4;
    public static final int BLUR_MASK = 5;
    public static final int IRREGULAR_MASK = 6;
    public static final int CIRCLE_MASK = 7;

    private MaskShaderFactory() {

    }

    public static IMask getShader(int shader_type, final int start, final int end) {
        switch (shader_type) {
            case CIRCLE_GRADIENT: {
                return new CircleGradientMask(start, end);
            }
            case OVAL_GRADIENT: {
                return new OvalGradientMask(start, end);
            }
            case LINER_GRADIENT: {
                return new LinerGardientMask(start, end);
            }
            case SWEEP_GRADIENT: {
                return new SweepGradientMask(start, end);
            }
            case BRIGHTNESS_MASK: {
                return new BrightnessMask(start, end);
            }
            case BLUR_MASK: {
                return new BlurMask();
            }
        }
        return null;
    }

    public static IMask getDrawableMask(int shader_type, Context context, int maskId) {
        switch (shader_type) {
            case IRREGULAR_MASK:
                return new IrregularMask(context, maskId);
            default:
                return null;
        }
    }

    public static IMask getCircleMask(int shader_type, boolean mode) {
        switch (shader_type) {
            case CIRCLE_MASK:
                return new CircleMask(mode);
            default:
                return null;
        }
    }

    public static IMask getRoundCornerMask(int radius, int margin, RoundedCornersMask.CornerType cornerType) {
        return new RoundedCornersMask(radius, margin, cornerType);
    }
}
