package com.autopai.common.utils.reflect;

public class AttachInfoHook {
    static Class<?> mAttachInfoCla;

    static {
        mAttachInfoCla = ReflectUtil.getClazz("android.view.View$AttachInfo");
    }
}
