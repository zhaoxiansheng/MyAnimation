package com.example.extension;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.extension.scene.CustomChoreographer;
import com.example.extension.scene.CustomLinearLayout;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

public class SceneDelegateImpl extends SceneDelegate {

    private Context mContext;
    private Window mWindow;
    private boolean mSubDecorInstalled;
    private CustomLinearLayout mSubDecor;
    private CustomChoreographer mChoreographer;

    public SceneDelegateImpl(Context context, Window window) {
        mContext = context;
        mWindow = window;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {

    }

    @Override
    public void onPostCreate(Bundle var1) {
        ensureSubDecor();
    }

    @Override
    public void onConfigurationChanged(Configuration var1) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPostResume() {

    }

    @Nullable
    @Override
    public <T extends View> T findViewById(@IdRes int id) {
        ensureSubDecor();
        return (T) mWindow.findViewById(id);
    }

    @Override
    public void setContentView(View v) {
        ensureSubDecor();
        mSubDecor.addView(v);
    }

    @Override
    public void setContentView(int redId) {
        ensureSubDecor();
        LayoutInflater.from(mContext).inflate(redId, mSubDecor);
    }

    @Override
    public void setContentView(View v, ViewGroup.LayoutParams lp) {
        ensureSubDecor();
        mSubDecor.addView(v, lp);
    }

    @Override
    public void addContentView(View v, ViewGroup.LayoutParams lp) {
        ensureSubDecor();
        mSubDecor.addView(v, lp);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onSaveInstanceState(Bundle var1) {

    }

    private void ensureSubDecor() {
        if (!mSubDecorInstalled) {
            mSubDecor = createSubDecor();
            mSubDecorInstalled = true;
        }
    }

    private CustomLinearLayout createSubDecor() {
        mSubDecor = new CustomLinearLayout(mContext);
        mWindow.setContentView(mSubDecor);

        mChoreographer = new CustomChoreographer(mContext);

        mSubDecor.setWinParams(mWindow.getAttributes());

        mChoreographer.setLayout(mSubDecor);

        // TODO: 2020/7/27 替换id
        return mSubDecor;
    }

    public CustomLinearLayout getSubDecor() {
        return mSubDecor;
    }

    public CustomChoreographer getChoreographer() {
        return mChoreographer;
    }
}
