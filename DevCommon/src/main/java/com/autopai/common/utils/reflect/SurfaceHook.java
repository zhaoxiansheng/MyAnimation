package com.autopai.common.utils.reflect;

import android.view.Surface;

import com.autopai.common.utils.common.Utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SurfaceHook {
    private static final String TAG = "SurfaceHook";

    private static Method transferMethod;
    private static Method checkNotReleasedMethod;
    private static Method setNativeObjectLockedMethod;
    private static Method nForceScopedDisconnectMethod;
    private static Method nReleaseMethod;
    private static Method forceScopedDisconnectMethod;
    private static Method createFromMethod;
    private static Method copyFromMethod;
    private static Method nativeGetFromSurfaceControlMethod;
    private static Method nativeCreateFromSurfaceControlMethod;
    private static Method getNativeFromSurfaceControl;

    private static Field mLockField;
    private static Field mNativeObjectField;
    private static Field mNameField;
    private static Field mIsSingleBufField;
    static Field mGenerationIdField;

    static {
        init();
    }


    private static void init()
    {
        transferMethod = ReflectUtil.getMethod(Surface.class, "transferFrom",  new Class<?>[]{Surface.class});
        checkNotReleasedMethod = ReflectUtil.getMethod(Surface.class, "checkNotReleasedLocked", null);

        mLockField = ReflectUtil.getClassFiled(Surface.class, "mLock");
        mNativeObjectField = ReflectUtil.getClassFiled(Surface.class, "mNativeObject");
        mNameField = ReflectUtil.getClassFiled(Surface.class, "mName");
        mGenerationIdField = ReflectUtil.getClassFiled(Surface.class, "mGenerationId");

        if (!Utilities.ATLEAST_LOLLIPOP)
        {
            //5.0之前int
            setNativeObjectLockedMethod = ReflectUtil.getMethod(Surface.class, "setNativeObjectLocked", new Class<?>[]{int.class});
            nReleaseMethod = ReflectUtil.getMethod(Surface.class, "nativeRelease", new Class<?>[]{int.class});
            nativeCreateFromSurfaceControlMethod = ReflectUtil.getMethod(Surface.class, "nativeCreateFromSurfaceControl", new Class<?>[]{int.class});
        }else
        {
            setNativeObjectLockedMethod = ReflectUtil.getMethod(Surface.class, "setNativeObjectLocked", new Class<?>[]{long.class});
            nReleaseMethod = ReflectUtil.getMethod(Surface.class, "nativeRelease", new Class<?>[]{long.class});
            nativeCreateFromSurfaceControlMethod = ReflectUtil.getMethod(Surface.class, "nativeCreateFromSurfaceControl", new Class<?>[]{long.class});
        }

        if (Utilities.ATLEAST_N_MR1)
        {
            //7.1.1才有这些方法
            nForceScopedDisconnectMethod = ReflectUtil.getMethod(Surface.class, "nativeForceScopedDisconnect", new Class<?>[]{long.class});
            forceScopedDisconnectMethod = ReflectUtil.getMethod(Surface.class, "forceScopedDisconnect", null);
            mIsSingleBufField = ReflectUtil.getClassFiled(Surface.class, "mIsSingleBuffered");
        }

        if(Utilities.ATLEAST_O) {
            nativeGetFromSurfaceControlMethod = ReflectUtil.getMethod(Surface.class, "nativeGetFromSurfaceControl", new Class<?>[]{long.class});
            getNativeFromSurfaceControl = nativeGetFromSurfaceControlMethod;
        }else{
            getNativeFromSurfaceControl = nativeCreateFromSurfaceControlMethod;
        }
    }

    public static int getGenerationId(Surface surface){
        int currentGenId = 0;
        Object mLock = ReflectUtil.getObjectByFiled(surface, mLockField);
        synchronized (mLock) {
            currentGenId = (int)ReflectUtil.getObjectByFiled(surface, mGenerationIdField);
        }
        return currentGenId;
    }

    public static Object getSurfaceLock(Surface surface){
        Object mLock = ReflectUtil.getObjectByFiled(surface, mLockField);
        return  mLock;
    }

    public static Number getNativeObject(final Surface surface)
    {
        Object mLock = ReflectUtil.getObjectByFiled(surface, mLockField);
        synchronized (mLock) {
            return (Number)ReflectUtil.getObjectByFiled(surface, mNativeObjectField);
        }
    }

    public static boolean updateSurface(Surface dstSuf, Surface srcSuf)
    {
        Object dstLock = ReflectUtil.getObjectByFiled(dstSuf, mLockField);
        Object srcLock = ReflectUtil.getObjectByFiled(dstSuf, mLockField);
        boolean ret = false;

        synchronized (srcLock) {
            String srcName = (String) ReflectUtil.getObjectByFiled(srcSuf, mNameField);
            boolean srcSingleBuf = false;
            if(mIsSingleBufField != null)
                srcSingleBuf = (boolean) ReflectUtil.getObjectByFiled(srcSuf, mIsSingleBufField);

            synchronized (dstLock) {

                //set mName
                ret = ReflectUtil.setObjectByFiled(dstSuf, srcName, mNameField);

                if (mIsSingleBufField != null) {
                    //set mIsSingleBuffered
                    ret &= ReflectUtil.setObjectByFiled(dstSuf, srcSingleBuf, mIsSingleBufField);
                }

                ret &= ReflectUtil.callMethod(transferMethod, dstSuf, new Object[]{srcSuf}, null);//transferFrom will release srcSurf native Surface

            }
        }
        return ret;
    }

    /**
     * //copySurface will not invalid srcSurf native Surface
     * @param dstSuf
     * @param srcSuf
     * @return
     */
    public static boolean copySurface(Surface dstSuf, Surface srcSuf)
    {
        Object dstLock = ReflectUtil.getObjectByFiled(dstSuf, mLockField);
        Object srcLock = ReflectUtil.getObjectByFiled(dstSuf, mLockField);
        boolean ret = false;

        synchronized (srcLock) {
            String srcName = (String) ReflectUtil.getObjectByFiled(srcSuf, mNameField);

            boolean srcSingleBuf = false;
            if(mIsSingleBufField != null)
                srcSingleBuf = (boolean) ReflectUtil.getObjectByFiled(srcSuf, mIsSingleBufField);

            Object srcNativePtr = ReflectUtil.getObjectByFiled(srcSuf, mNativeObjectField);

            synchronized (dstLock) {

                //set mName
                ret = ReflectUtil.setObjectByFiled(dstSuf, srcName, mNameField);

                if(mIsSingleBufField != null) {
                    //set mIsSingleBuffered
                    ret &= ReflectUtil.setObjectByFiled(dstSuf, srcSingleBuf, mIsSingleBufField);
                }

                //set mNativeObject
                Number dstNativePtr = (Number)ReflectUtil.getObjectByFiled(dstSuf, mNativeObjectField);
                if(dstNativePtr.longValue() != 0)
                {
                    //release old native Surface
                    ret &= ReflectUtil.callMethod(nReleaseMethod, dstSuf, new Object[]{dstNativePtr}, null);
                }

                //set dest srcNativePtr as mNativeObject
                ret &= ReflectUtil.callMethod(setNativeObjectLockedMethod, dstSuf, new Object[]{srcNativePtr}, null);
            }
        }
        return ret;
    }

    /**
     * implement transferFrom, srcSurface nativeObject will invalided
     * @param dstSuf
     * @param srcSuf
     * @return
     */
    public static boolean transferSurface(Surface dstSuf, Surface srcSuf)
    {
        Object dstLock = ReflectUtil.getObjectByFiled(dstSuf, mLockField);
        Object srcLock = ReflectUtil.getObjectByFiled(dstSuf, mLockField);
        boolean ret = false;

        synchronized (srcLock) {

            Object srcNativePtr = ReflectUtil.getObjectByFiled(srcSuf, mNativeObjectField);
            ret = ReflectUtil.callMethod(setNativeObjectLockedMethod, srcSuf, new Object[]{(Number)0}, null);
            synchronized (dstLock) {
                //set mNativeObject
                Number dstNativePtr = (Number)ReflectUtil.getObjectByFiled(dstSuf, mNativeObjectField);
                if(dstNativePtr.longValue() != 0)
                {
                    //release old native Surface
                    ret &= ReflectUtil.callMethod(nReleaseMethod, dstSuf, new Object[]{dstNativePtr}, null);//transferFrom will release srcSurf native Surface
                }

                //set dest srcNativePtr as mNativeObject
                ret &= ReflectUtil.callMethod(setNativeObjectLockedMethod, dstSuf, new Object[]{srcNativePtr}, null);
            }
        }
        return ret;
    }

    public static boolean copyFrom(Surface surface, Object surfaceControl)
    {
        boolean ret = false;
       if(copyFromMethod != null){
           ret = ReflectUtil.callMethod(copyFromMethod, surface, new Object[]{surfaceControl}, null);
       }
       return ret;
    }

    public static boolean createFrom(Surface surface, Object surfaceControl)
    {
        boolean ret = false;
        if(createFromMethod != null){
            ret = ReflectUtil.callMethod(createFromMethod, surface, new Object[]{surfaceControl}, null);
        }
        return ret;
    }

    public static boolean disconnect(Surface surface)
    {
        boolean ret = false;
        if(forceScopedDisconnectMethod != null) {
            ret = ReflectUtil.callMethod(forceScopedDisconnectMethod, surface, null, null);
        }
        return ret;
    }

    public static boolean clearName(Surface surface){
        Object mLock = ReflectUtil.getObjectByFiled(surface, mLockField);
        synchronized (mLock) {
            if(ReflectUtil.setObjectByFiled(surface, null, mNameField)) {
                return true;
            }
        }
        return false;
    }

    public static boolean clearBufMode(Surface surface){
        Object mLock = ReflectUtil.getObjectByFiled(surface, mLockField);
        synchronized (mLock) {
            if(ReflectUtil.setObjectByFiled(surface, false, mIsSingleBufField)) {
                return true;
            }
        }
        return false;
    }

    public static boolean clearGenId(Surface surface){
        Object mLock = ReflectUtil.getObjectByFiled(surface, mLockField);
        synchronized (mLock) {
            if(ReflectUtil.setObjectByFiled(surface, 0, mGenerationIdField)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkNotReleasedLocked(Surface surface)
    {
        boolean ret = ReflectUtil.callMethod(checkNotReleasedMethod, surface, null, null);
        return ret;
    }
}
