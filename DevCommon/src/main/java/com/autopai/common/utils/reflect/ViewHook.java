package com.autopai.common.utils.reflect;

import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;

import com.autopai.common.utils.common.Utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ViewHook {
    private static Field mLayerPaintField;
    private static Field mOverlayField;
    private static Field mGhostViewField;
    private static Field mAttachInfoField;
    private static Method destroyHardwareResourcesMethod;
    private static Method invalidateMethod;
    private static Method invalidateParentCachesMethod;
    private static Method getOverlayViewMethod;
    private static Method resetDisplayListMethod;
    private static Method destroyLayerMethod;

    static {
        mLayerPaintField = ReflectUtil.getClassFiled(View.class, "mLayerPaint");
        mAttachInfoField = ReflectUtil.getClassFiled(View.class, "mAttachInfo");
        if(Utilities.ATLEAST_O) {
            mOverlayField = ReflectUtil.getClassFiled(View.class, "mOverlay");
            mGhostViewField = ReflectUtil.getClassFiled(View.class, "mGhostView");
            getOverlayViewMethod = ReflectUtil.getMethod(ViewOverlay.class, "getOverlayView", null);
        }else{
            if(!Utilities.ATLEAST_LOLLIPOP) {
                destroyLayerMethod = ReflectUtil.getMethod(View.class, "destroyLayer", new Class<?>[]{boolean.class});
            }
            resetDisplayListMethod = ReflectUtil.getMethod(View.class, "resetDisplayList", null);
        }

        destroyHardwareResourcesMethod = ReflectUtil.getMethod(View.class, "destroyHardwareResources", null);
        invalidateMethod = ReflectUtil.getMethod(View.class, "invalidate", new Class<?>[]{boolean.class});
        invalidateParentCachesMethod = ReflectUtil.getMethod(View.class, "invalidateParentCaches", null);
    }

    public static boolean destroyHardwareResources(View view){
        if(view != null) {
            boolean ret = false;
            if (Utilities.ATLEAST_O) {
                //mOverlay.getOverlayView().destroyHardwareResources();
                ViewOverlay overlay = (ViewOverlay)ReflectUtil.getObjectByFiled(view, mOverlayField);
                if(overlay != null) {
                    View overlayView = getOverlayView(overlay);
                    if(overlayView != null) {
                        ret = destroyHardwareResources(overlayView);
                    }
                }
                //mGhostView.destroyHardwareResources();
                View ghostView = (View)ReflectUtil.getObjectByFiled(view, mGhostViewField);
                if(ghostView != null) {
                    ret &= destroyHardwareResources(ghostView);
                }
            }else if(Utilities.ATLEAST_LOLLIPOP) {
                //resetDisplayList();
                ret = ReflectUtil.callMethod(resetDisplayListMethod, view, null, null);
            }else {
                //resetDisplayList();
                //destroyLayer(true);
                ret = ReflectUtil.callMethod(resetDisplayListMethod, view, null, null);
                ret &= ReflectUtil.callMethod(destroyLayerMethod, view, new Object[]{true}, null);
            }
            return ret;
        }
        return false;
    }

    public static ViewGroup getOverlayView(ViewOverlay overlay) {
        if(overlay != null) {
            Object[] ret = new Object[1];
            if( ReflectUtil.callMethod(getOverlayViewMethod, overlay, null, ret) ){
                return (ViewGroup)ret[0];
            }
        }
        return null;
    }
	
    public static Paint getLayerPaint(View view) {
        if(view != null) {
            if(mLayerPaintField != null) {
                return (Paint)ReflectUtil.getObjectByFiled(view, mLayerPaintField);
            }
        }
        return null;
    }

    public static Object getAttachInfo(View view){
        if(view != null) {
            return ReflectUtil.getObjectByFiled(view, mAttachInfoField);
        }
        return null;
    }

    public static boolean setAttachInfo(View view, Object attachInfo){
        return ReflectUtil.setObjectByFiled(view, attachInfo, mAttachInfoField);
    }

    public static boolean invalidate(View view, boolean diret){
        if(view != null) {
            return ReflectUtil.callMethod(invalidateMethod, view, new Object[]{diret}, null);
        }
        return false;
    }

    public static boolean invalidateParentCaches(View view){
        if(view != null) {
            return ReflectUtil.callMethod(invalidateParentCachesMethod, view, null, null);
        }
        return false;
    }
}
