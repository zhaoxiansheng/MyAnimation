package com.autopai.common.utils.reflect;

import android.graphics.SurfaceTexture;
import android.view.Surface;

import java.lang.reflect.Field;


public class ViewRootProxySurface extends ProxySurfaceBase {
    private static final String TAG = "ViewRootProxySurface";
    private boolean mPassThrough = false;
    private Object mBaseLock;
    private ProxyCallBack mCallBack;
    static Field mLockField;
    static Field mGenerationIdField;

    static {
        mLockField = ReflectUtil.getClassFiled(Surface.class, "mLock");
        mGenerationIdField = ReflectUtil.getClassFiled(Surface.class, "mGenerationId");
    }

    public interface ProxyCallBack
    {
        void onGetGenerationId();
        boolean onRelease();
    }

    public ViewRootProxySurface()
    {
        this(new SurfaceTexture(0));
    }

    public ViewRootProxySurface(SurfaceTexture surfaceTexture) {
        super(surfaceTexture);
        //release();
        mBaseLock = ReflectUtil.getObjectByFiled(this, mLockField);
    }

    public ViewRootProxySurface(boolean bPassThrough)
    {
        this(new SurfaceTexture(0));
        mPassThrough = bPassThrough;
    }

    public void setProxyCallBack(ProxyCallBack callBack)
    {
        mCallBack = callBack;
    }

    public void uninit()
    {

    }

    @Override
    public void release() {
        if(mCallBack != null) {
            if( mCallBack.onRelease() ){
                super.release();
            }
        }else {
            super.release();
        }
    }

    /*@Override
    public boolean isValid() {
        boolean ret = super.isValid();
        Log.e("RootProxySurface", "MNMN isValid: " + ret);
        Log.e(TAG, "MNMN" + Log.getStackTraceString(new Throwable()));
        return ret;
    }*/

    public boolean isValidSuper() {
        boolean ret = super.isValid();
        return ret;
    }

    public int getGenerationId() {
        if(mCallBack != null)
            mCallBack.onGetGenerationId();

        int currentGenId = getGenerationIdSuper();
        /*Log.e(TAG, "MNMN ???? curentGenid: " + currentGenId + ", prevId: " + mPervGenerationId);
        if(mPervGenerationId > -1 && mPervGenerationId != currentGenId)
        {
            enableUpdateSurface();
        }
        mPervGenerationId = currentGenId;*/
        return currentGenId;
    }

    public int getGenerationIdSuper()
    {
        int currentGenId;
        synchronized (mBaseLock) {
            currentGenId = (int)ReflectUtil.getObjectByFiled(this, mGenerationIdField);
        }
        return currentGenId;
    }
}
