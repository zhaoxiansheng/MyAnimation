package com.autopai.common.shader.EffectShader.Shader;

import android.view.View;

public interface IShader {
    static final int RED_FILTER = 0x0001;
    static final int GREEN_FILTER = 0x0010;
    static final int BLUE_FILTER = 0x0100;
    //static final int DARKEN = 120;

    static final int GREY = 121;
    static final int CLASSICAL = 122;
    static final int GONTHIC = 123;
    static final int ELEGANT = 124;
    static final int BLUES = 125;
    static final int HALO = 126;
    static final int FANTASY = 127;
    static final int CLARET = 128;
    static final int FILM = 129;
    static final int FILM2 = 130;
    static final int RIPPLES_SPARKLES = 131;
    static final int BROWN = 132;
    static final int ANCIENT = 133;
    static final int YELLOWING = 134;
    static final int TRADITION = 135;
    static final int SHARP = 136;
    static final int PEACE = 137;
    static final int ROMANTIC = 138;
    static final int DIM = 139;



    void apply(View target);
    void setupShader(View target, float[] colorMatrix);
    void setValue(float value);
    void unset();
}
