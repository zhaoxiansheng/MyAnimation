package com.autopai.common.shader.EffectShader;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.autopai.common.utils.reflect.ViewHook;

import org.ujmp.core.Matrix;

import java.lang.ref.WeakReference;

/**
 *     after onAttachToWindow
 *      ColorFilterEffect effect = new ColorFilterEffect(IEffect.HALO);
 *         ColorFilterEffect effect2 = new ColorFilterEffect(IEffect.BLUES);
 *         ColorFilterEffect effect3 = new ColorFilterEffect(IEffect.ELEGANT);
 *         ColorFilterEffect effect4 = new ColorFilterEffect(IEffect.CLASSICAL);
 *         effect.combine(effect2);
 *         effect.combine(effect3);
 *         effect.combine(effect4);
 *         effect.setupShader(this);
 */
public class ColorFilterEffect implements IEffect {
    private static final String TAG = "ColorFilterEffect";
    private Paint mPaint;
    protected MultiColorMatrix mMatrix;
    protected WeakReference<View> mTarget;
    private int mOrgType;
    private Paint mOrgPaint;
    private static final SparseArray<float[]> mColorMatrices = new SparseArray(24);
    private View.OnAttachStateChangeListener mAttachRunnable;

    static {
        mColorMatrices.put(IEffect.RED_FILTER, ColorMatrixUtil.colormatrix_red);
        mColorMatrices.put(IEffect.GREEN_FILTER, ColorMatrixUtil.colormatrix_green);
        mColorMatrices.put(IEffect.BLUE_FILTER, ColorMatrixUtil.colormatrix_blue);
        mColorMatrices.put(IEffect.GREY, ColorMatrixUtil.colormatrix_grey);
        mColorMatrices.put(IEffect.CLASSICAL, ColorMatrixUtil.colormatrix_classical);
        mColorMatrices.put(IEffect.GONTHIC, ColorMatrixUtil.colormatrix_gonthic);
        mColorMatrices.put(IEffect.ELEGANT, ColorMatrixUtil.colormatrix_elegant);
        mColorMatrices.put(IEffect.BLUES, ColorMatrixUtil.colormatrix_blues);
        mColorMatrices.put(IEffect.HALO, ColorMatrixUtil.colormatrix_halo);
        mColorMatrices.put(IEffect.BLUES, ColorMatrixUtil.colormatrix_blues);
        mColorMatrices.put(IEffect.FANTASY, ColorMatrixUtil.colormatrix_fantasy);
        mColorMatrices.put(IEffect.CLARET, ColorMatrixUtil.colormatrix_claret);
        mColorMatrices.put(IEffect.FILM, ColorMatrixUtil.colormatrix_film);
        mColorMatrices.put(IEffect.FILM2, ColorMatrixUtil.colormatrix_film2);
        mColorMatrices.put(IEffect.RIPPLES_SPARKLES, ColorMatrixUtil.colormatrix_ripples);
        mColorMatrices.put(IEffect.BROWN, ColorMatrixUtil.colormatrix_brown);
        mColorMatrices.put(IEffect.ANCIENT, ColorMatrixUtil.colormatrix_ancient);
        mColorMatrices.put(IEffect.YELLOWING, ColorMatrixUtil.colormatrix_yellowing);
        mColorMatrices.put(IEffect.TRADITION, ColorMatrixUtil.colormatrix_tradition);
        mColorMatrices.put(IEffect.SHARP, ColorMatrixUtil.colormatrix_sharp);
        mColorMatrices.put(IEffect.PEACE, ColorMatrixUtil.colormatrix_peace);
        mColorMatrices.put(IEffect.ROMANTIC, ColorMatrixUtil.colormatrix_romantic);
        mColorMatrices.put(IEffect.DIM, ColorMatrixUtil.colormatrix_dim);
    }

    public ColorFilterEffect(int shaderType){
        initMatrix(shaderType);
    }

    protected ColorFilterEffect(float value){
        initMatrix(value);
    }

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
                setPaint(target, mMatrix);
            }
        }
    }

    @Override
    public void setupShader(final View target, final float[] colorMatrix) {
        if(target != null && (mTarget == null || mTarget.get() == target)) {
            mMatrix = new MultiColorMatrix(colorMatrix);
            apply(target);
        }else{
            Log.e(TAG, "setupShader failed: currentTarget: " + mTarget + " target: " + target);
        }
    }

    @Override
    public IEffect combine(IEffect shader) {
        if(shader instanceof ColorFilterEffect && mMatrix != null) {
            ColorFilterEffect other = (ColorFilterEffect)shader;
            mMatrix.preConcat(other.mMatrix);
        }
        return this;
    }

    @Override
    public void divide(IEffect shader) {
        if(shader instanceof ColorFilterEffect && mMatrix != null) {
            mMatrix.divide(((ColorFilterEffect) shader).mMatrix);
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
        mMatrix = null;
        mTarget = null;
        mPaint = null;
    }

    protected void initMatrix(float shaderType){
        float[] colorMatrix = mColorMatrices.get((int)shaderType);
        if(colorMatrix != null) {
            mMatrix = new MultiColorMatrix(colorMatrix);
        }else{
            Log.e(TAG, "initMatrix failed, no such shader: " + shaderType);
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

    protected void setPaint(View target, ColorMatrix colorMatrix){
        if(target != null) {
            mPaint = new Paint();
            ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
            mPaint.setColorFilter(colorMatrixColorFilter);
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

    private Matrix getMatrix(ColorMatrix color){
        if(color != null) {
            float[] mx = color.getArray();
            Matrix current = Matrix.Factory.zeros(4, 5);
            for (int i = 0; i < mx.length; ++i) {
                current.setAsFloat(mx[i], i / 5, i % 5);
            }
            return current;
        }
        return null;
    }
}
