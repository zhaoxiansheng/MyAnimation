package com.example.extension;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.alion.mycanvas.RemoteBase.TargetDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TargetSceneDialog extends TargetDialog {

    private SceneDelegate mSceneDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(View view) {
        getSceneDelegate().setContentView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        getSceneDelegate().setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getSceneDelegate().setContentView(view, params);
    }

    @Override
    public void addContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        getSceneDelegate().addContentView(view, params);
    }

    public SceneDelegate getSceneDelegate() {
        if (this.mSceneDelegate == null) {
            this.mSceneDelegate = SceneDelegate.create(this);
        }
        return mSceneDelegate;
    }
}
