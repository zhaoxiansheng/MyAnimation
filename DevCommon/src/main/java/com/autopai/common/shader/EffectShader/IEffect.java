package com.autopai.common.shader.EffectShader;

import com.autopai.common.shader.EffectShader.Shader.IShader;

public interface IEffect extends IShader {
    IEffect combine(IEffect shader);
    void divide(IEffect shader);
}
