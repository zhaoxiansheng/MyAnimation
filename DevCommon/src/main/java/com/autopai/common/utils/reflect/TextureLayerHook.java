package com.autopai.common.utils.reflect;

import android.graphics.SurfaceTexture;

import com.autopai.common.utils.common.Utilities;

import java.lang.reflect.Method;

public class TextureLayerHook {
    private static String clzName = Utilities.ATLEAST_P ? "android.view.TextureLayer" : "android.view.HardwareLayer";
    private static Method setSurfaceTextureMethod;
    private static Method detachSurfaceTextureMethod;
    private static Method updateSurfaceTextureMethod;

    static{
        Class<?> textureLayerClz = ReflectUtil.getClazz(clzName);
        if(textureLayerClz != null) {
            setSurfaceTextureMethod = ReflectUtil.getMethod(textureLayerClz, "setSurfaceTexture", new Class<?>[]{SurfaceTexture.class});
            detachSurfaceTextureMethod = ReflectUtil.getMethod(textureLayerClz, "detachSurfaceTexture", new Class<?>[]{});
            updateSurfaceTextureMethod = ReflectUtil.getMethod(textureLayerClz, "updateSurfaceTexture", new Class<?>[]{});
        }
    }

    public static boolean setSurfaceTexture(Object layer, SurfaceTexture surfTex){
        boolean ret = false;
        ret = ReflectUtil.callMethod(setSurfaceTextureMethod, layer, new Object[]{surfTex}, null);
        return ret;
    }

    public static boolean detachSurfaceTexture(Object layer){
        boolean ret = false;
        ret = ReflectUtil.callMethod(detachSurfaceTextureMethod, layer, null, null);
        return ret;
    }

    public static boolean updateSurfaceTexture(Object layer){
        boolean ret = false;
        ret = ReflectUtil.callMethod(updateSurfaceTextureMethod, layer, null, null);
        return ret;
    }
}
