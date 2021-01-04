package com.autopai.common.shader.EffectShader.Shader;

import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.autopai.common.utils.reflect.ViewHook;

import java.lang.ref.WeakReference;

public class ColorPassShader implements IShader {
    private static final String TAG = "ColorPassShader";

    private LightingColorFilter mColorFilter;
    private Paint mPaint;
    protected WeakReference<View> mTarget;
    private int mOrgType;
    private Paint mOrgPaint;
    private View.OnAttachStateChangeListener mAttachRunnable;

    @Override
    public void apply(final View target) {
        if(target != null) {
            if(!target.isAttachedToWindow() && mTarget == null) {
                if(mAttachRunnable == null) {

                    mAttachRunnable = new View.OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View v) {
                            apply(target);
                            target.removeOnAttachStateChangeListener(this);
                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {

                        }
                    };

                    //throw new IllegalStateException("view: " + target + " not attatched, setupShader failed");
                    target.addOnAttachStateChangeListener(mAttachRunnable);
                }
                return;
            }

            if(mTarget == null) {
                saveLayer(target);
            }

            if(target == mTarget.get()) {
                setPaint(target, mColorFilter);
            }
        }
    }

    private void saveLayer(View target){
        if(target != null) {
            mTarget = new WeakReference<>(target);
            mOrgType = target.getLayerType();
            mOrgPaint = ViewHook.getLayerPaint(target);
        }
    }

    private void restoreLayer(){
        View target = null;
        if( mTarget != null &&  (target = mTarget.get()) != null) {
            target.setLayerType(mOrgType, mOrgPaint);
        }
    }

    protected void setPaint(View target, ColorFilter colorFilter){
        if(target != null) {
            mPaint = new Paint();
            mPaint.setColorFilter(colorFilter);
            int layerType;
            if(mOrgType == View.LAYER_TYPE_NONE) {
                boolean isHwEnable = target.isHardwareAccelerated();
                layerType = isHwEnable ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_SOFTWARE;
            }else {
                layerType = mOrgType;
            }
            target.setLayerType(layerType, mPaint);
        }
    }

    @Override
    public void setupShader(View target, float[] colorMatrix) {
        if(target != null && (mTarget == null || mTarget.get() == target)) {
            mColorFilter = new LightingColorFilter((int)colorMatrix[0], (int)colorMatrix[1]);
            apply(target);
        }else{
            Log.e(TAG, "setupShader failed: currentTarget: " + mTarget + " target: " + target);
        }
    }

    @Override
    public void setValue(float value) {

    }

    @Override
    public void unset() {
        if(mTarget != null && mTarget.get() != null) {
            if(mAttachRunnable != null) {
                mTarget.get().removeOnAttachStateChangeListener(mAttachRunnable);
                mAttachRunnable = null;
            }
        }
        restoreLayer();
        mTarget = null;
        mPaint = null;
    }
}
