package com.autopai.common.utils.reflect;

import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class WindowHook {
    private static Field privateFlagsField;
    public static final int PRIVATE_FLAG_HIDE_NON_SYSTEM_OVERLAY_WINDOWS = 0x00080000;

    static {
        privateFlagsField = ReflectUtil.getClassFiled(WindowManager.LayoutParams.class, "privateFlags");
    }

    public static boolean setPrivateFlags(WindowManager.LayoutParams params, int flags){
        if(params != null){
            int privateFlags = getPrivateFlags(params);
            privateFlags = (privateFlags & ~flags) | (flags & flags);
            return ReflectUtil.setObjectByFiled(params, privateFlags, privateFlagsField);
        }
        return false;
    }

    public static int getPrivateFlags(WindowManager.LayoutParams params){
        if(params != null){
            return (int)ReflectUtil.getObjectByFiled(params, privateFlagsField);
        }
        return 0;
    }
}
