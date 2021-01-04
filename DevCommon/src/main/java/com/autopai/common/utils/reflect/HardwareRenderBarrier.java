package com.autopai.common.utils.reflect;

import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.autopai.common.utils.common.Utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HardwareRenderBarrier implements ViewRootProxySurface.ProxyCallBack {

    private static final String TAG = "HardwareRenderBarrier";
    private PreDrawListener mPredrawListener = new PreDrawListener();
    private View mBaseView;
    private Object mBaseHwRender;
    private ViewRootProxySurface mProxySurface;
    private boolean mHadBarrier = false;
    private boolean baseIsAvailable = true;
    private static Field mRenderField;
    private static Field sIsAvailableField;
    private static Method isAvailableMethod;
    private static final String HARDWARERENDER_NAME = Utilities.ATLEAST_O ? "mThreadedRenderer":"mHardwareRenderer";
    private static final String HARDWARERENDER_CLASS = Utilities.ATLEAST_N ? "android.view.ThreadedRenderer":"android.view.HardwareRenderer";

    static{
        Class<?> attachInfoCls = ReflectUtil.getClazz("android.view.View$AttachInfo");
        if(attachInfoCls != null) {
            mRenderField = ReflectUtil.getClassFiled(attachInfoCls, HARDWARERENDER_NAME);
        }

        Class<?> renderCls = ReflectUtil.getClazz(HARDWARERENDER_CLASS);
        if(attachInfoCls != null) {
            isAvailableMethod = ReflectUtil.getMethod(renderCls, "isAvailable", null);
        }

        if(Utilities.ATLEAST_O)
        {
            Class<?> canvasCls = ReflectUtil.getClazz("android.view.ThreadedRenderer");
            if(canvasCls != null) {
                sIsAvailableField = ReflectUtil.getClassFiled(canvasCls, "sSupportsOpenGL");
            }
        }else{
            Class<?> canvasCls = ReflectUtil.getClazz("android.view.GLES20Canvas");
            if(canvasCls != null) {
                sIsAvailableField = ReflectUtil.getClassFiled(canvasCls, "sIsAvailable");
            }
        }
    }

    @Override
    public void onGetGenerationId() {
        if(mHadBarrier) {
            removeHwRenderBarrier();
        }
    }

    @Override
    public boolean onRelease() {
        //Log.e(TAG, "root surface release at: " + Log.getStackTraceString(new Throwable()));
        return true;
    }

    private class PreDrawListener implements ViewTreeObserver.OnPreDrawListener
    {
        @Override
        public boolean onPreDraw() {
            if(mHadBarrier) {
                removeHwRenderBarrier();
            }
            return true;
            //return true;//if true candraw
        }
    }

    public HardwareRenderBarrier(View attatchView, boolean proxySurface)
    {
        mBaseView = attatchView;
        init(proxySurface);
    }

    private void init(boolean proxySurface)
    {
        if(proxySurface) {
            /**
             * final int surfaceGenerationId = mSurface.getGenerationId();
             * */
            mProxySurface = new ViewRootProxySurface();
            mProxySurface.setProxyCallBack(this);
        }

        baseIsAvailable = isHwAvaliable();
    }

    private boolean isHwAvaliable()
    {
        Object[] ret = new Object[1];
        if( ReflectUtil.callMethod(isAvailableMethod, null, null, ret) )
        {
            return (boolean)ret[0];
        }

        return false;
    }

    private boolean setHwIsAvaliable(boolean bAvaliable)
    {
        boolean ret = ReflectUtil.setObjectByFiled(null, bAvaliable, sIsAvailableField);
        return ret;
    }

    private boolean setHwRenderBarrier()
    {
        Log.e(TAG, "setHwRenderBarrier");
        Object baseRootImpl = ViewRootImplUtil.getRootImpl(mBaseView);
        Object attachInfo = ViewRootImplUtil.getBaseAttachInfo(baseRootImpl);
        if(attachInfo != null) {
            mBaseHwRender = ReflectUtil.getObjectByFiled(attachInfo, mRenderField);
            mHadBarrier = ReflectUtil.setObjectByFiled(attachInfo, null, mRenderField);
        }
        return mHadBarrier;
    }

    private boolean removeHwRenderBarrier()
    {
        Log.e(TAG, "removeHwRenderBarrier");
        if(mHadBarrier) {
            Object baseRootImpl = ViewRootImplUtil.getRootImpl(mBaseView);
            Object attachInfo = ViewRootImplUtil.getBaseAttachInfo(baseRootImpl);
            if (attachInfo != null) {
                mHadBarrier = !ReflectUtil.setObjectByFiled(attachInfo, mBaseHwRender, mRenderField);
                if (!mHadBarrier) {
                    mBaseHwRender = null;
                }
            }
        }
        return !mHadBarrier;
    }

    private boolean needBarrier(){
        Object baseRootImpl = ViewRootImplUtil.getRootImpl(mBaseView);
        boolean ret = !ViewRootImplUtil.getNeedNewSurface(baseRootImpl);
        return ret;
    }

    public void onWindowVisibilityChanged(int visibility)
    {
        if(visibility != View.VISIBLE && needBarrier())
        {
            setHwRenderBarrier();
        }
    }

    public void onAttachedToWindow() {
        Object baseRootImpl = ViewRootImplUtil.getRootImpl(mBaseView);
        boolean ret = ViewRootImplUtil.setRootImplSurface(baseRootImpl, mProxySurface);
        if(!ret) {
            Log.e(TAG, "setRootImplSurface failed, " + Log.getStackTraceString(new Throwable()));
        }

        ViewTreeObserver observer = mBaseView.getViewTreeObserver();
        observer.addOnPreDrawListener(mPredrawListener);
    }

    public void onDetachedFromWindow() {
        ViewTreeObserver observer = mBaseView.getViewTreeObserver();
        observer.removeOnPreDrawListener(mPredrawListener);
    }

    public void onStop() {
        setHwIsAvaliable(false);
        //boolean ret = isHwAvaliable();
    }

    public void onStart()
    {
        setHwIsAvaliable(baseIsAvailable);
        //boolean ret = isHwAvaliable();
    }

    public void onDestory()
    {
        //removeHwRenderBarrier();
        setHwIsAvaliable(baseIsAvailable);
    }
}
