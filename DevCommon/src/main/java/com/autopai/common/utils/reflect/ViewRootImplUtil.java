package com.autopai.common.utils.reflect;

import android.os.Build;
import android.view.Surface;
import android.view.View;

import com.autopai.common.utils.common.Utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ViewRootImplUtil {
    private static Method getViewRootImplMethod;
    private static Method scheduleTraversalsMethod;
    private static Field mAttachInfoField;
    private static Field mRecreateDisplayListField;
    private static Field mHwRenderField;
    private static Field mNewSurfaceNeededField;
    private static Field mSurfaceField;
    private static Method isEnabledMethod;
    private static final String HARDWARERENDER_NAME = (Utilities.ATLEAST_O) ? "mThreadedRenderer":"mHardwareRenderer";
    private static final String HARDWARERENDER_CLASS = (Utilities.ATLEAST_N) ? "android.view.ThreadedRenderer":"android.view.HardwareRenderer";


    static{
        Class<?> viewRootImplClz = ReflectUtil.getClazz("android.view.ViewRootImpl");
        if(viewRootImplClz != null) {
            mSurfaceField = ReflectUtil.getClassFiled(viewRootImplClz, "mSurface");
            scheduleTraversalsMethod = ReflectUtil.getMethod(viewRootImplClz, "scheduleTraversals", new Class<?>[]{});
            mNewSurfaceNeededField = ReflectUtil.getClassFiled(viewRootImplClz, "mNewSurfaceNeeded");
        }
        Class<?> renderCls = ReflectUtil.getClazz(HARDWARERENDER_CLASS);
        if(renderCls != null) {
            isEnabledMethod = ReflectUtil.getMethod(renderCls, "isEnabled", null);
        }
        mHwRenderField = ReflectUtil.getClassFiled(AttachInfoHook.mAttachInfoCla, HARDWARERENDER_NAME);

        mRecreateDisplayListField = ReflectUtil.getClassFiled(View.class, "mRecreateDisplayList");
    }

    public static Object getRootImpl(View root)
    {
        Object rootImpl = null;
        if(getViewRootImplMethod == null) {
            getViewRootImplMethod = ReflectUtil.getMethod(View.class, "getViewRootImpl", new Class<?>[]{});
        }

        Object[] ret = new Object[1];
        if( ReflectUtil.callMethod(getViewRootImplMethod, root, null, ret) )
        {
            rootImpl = ret[0];
        }
        return rootImpl;
    }

    public static Object getBaseAttachInfo(Object baseRootImpl)
    {
        if(mAttachInfoField == null)
        {
            mAttachInfoField = ReflectUtil.getClassFiled(baseRootImpl.getClass(), "mAttachInfo");
        }
        return ReflectUtil.getObjectByFiled(baseRootImpl, mAttachInfoField);
    }

    public static boolean setBaseAttachInfo(Object baseRootImpl, Object attachInfo)
    {
        if(mAttachInfoField == null)
        {
            mAttachInfoField = ReflectUtil.getClassFiled(baseRootImpl.getClass(), "mAttachInfo");
        }
        return ReflectUtil.setObjectByFiled(baseRootImpl, attachInfo, mAttachInfoField);
    }

    public static boolean getNeedNewSurface(Object baseRootImpl){
        return (boolean)ReflectUtil.getObjectByFiled(baseRootImpl, mNewSurfaceNeededField);
    }

    public static boolean setRootImplSurface(Object viewRootImpl, Surface remoteSurface)
    {
        if(viewRootImpl != null && mSurfaceField != null)
        {
            boolean ret = ReflectUtil.setObjectByFiled(viewRootImpl, remoteSurface, mSurfaceField);
            return ret;
        }
        return false;
    }

    public static boolean getRecreateDisplayList(View view)
    {
        boolean recreate = (boolean)ReflectUtil.getObjectByFiled(view, mRecreateDisplayListField);
        return recreate;
    }

    public static Object getAttachHwRender(Object attachInfo)
    {
        Object hwRender = ReflectUtil.getObjectByFiled(attachInfo, "mHardwareRenderer");
        return hwRender;
    }

    public static boolean doScheduleTraversals(Object viewRootImpl)
    {
        if(viewRootImpl != null && scheduleTraversalsMethod != null)
        {
            boolean ret = ReflectUtil.callMethod(scheduleTraversalsMethod, viewRootImpl, null, null);
            return ret;
        }
        return false;
    }

    public static Object getHardwareRender(Object rootImpl)
    {
        Object attatchInfo = getBaseAttachInfo(rootImpl);
        if(attatchInfo != null)
        {
            return ReflectUtil.getObjectByFiled(attatchInfo, mHwRenderField);
        }
        return null;
    }

    public static boolean isHardwareRenderEnable(Object viewRootImpl) {
        //mThreadedRender not null and mThreadedRender.isRequested true and mFakeSurface.isValidSuper true, we use hardware render
        Object hwRender = getHardwareRender(viewRootImpl);
        if (hwRender != null) {
            Object[] ret = new Object[1];
            if (ReflectUtil.callMethod(isEnabledMethod, hwRender, null, ret)) {
                return (boolean)ret[0];
            }
        }
        return false;
    }
}
