package com.autopai.common.utils.reflect;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.view.Surface;

class ProxySurfaceBase extends Surface {
    private static final String TAG = "ProxySurfaceBase";
    protected boolean mLocked = false;

    public ProxySurfaceBase(SurfaceTexture surfaceTexture) {
        super(surfaceTexture);
        destroyInit(surfaceTexture);
    }

    public boolean clear(){
        super.release();
        mLocked = false;
        boolean ret = SurfaceHook.clearGenId(this);
        ret &= SurfaceHook.clearBufMode(this);
        ret &= SurfaceHook.clearName(this);
        return ret;
    }

    protected boolean destroyInit(SurfaceTexture surfaceTexture){
        boolean ret = false;
        ret = clear();
        surfaceTexture.detachFromGLContext();
        surfaceTexture.releaseTexImage();
        surfaceTexture.release();
        return ret;
    }

    @Override
    public void release() {
        super.release();
        mLocked = false;
    }

    @Override
    public Canvas lockCanvas(Rect inOutDirty) throws IllegalArgumentException, OutOfResourcesException {
        mLocked = true;
        return super.lockCanvas(inOutDirty);
    }

    @Override
    public Canvas lockHardwareCanvas() {
        mLocked = true;
        return super.lockHardwareCanvas();
    }

    @Override
    public void unlockCanvasAndPost(Canvas canvas) {
        if(mLocked) {
            super.unlockCanvasAndPost(canvas);
            mLocked = false;
        }
    }

    @Override
    public void unlockCanvas(Canvas canvas) {
        if(mLocked) {
            super.unlockCanvas(canvas);
            mLocked = false;
        }
    }
}
