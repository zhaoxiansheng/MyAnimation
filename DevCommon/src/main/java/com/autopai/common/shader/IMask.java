package com.autopai.common.shader;

import android.graphics.Canvas;
import android.graphics.Rect;

public interface IMask {
    public void setBound(final Rect bound);
    public void setValue(float ratio);
    public void drawMask(final Canvas canvas);
}
