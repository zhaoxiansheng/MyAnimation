package com.example.extension;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.extension.scene.CustomChoreographer;
import com.example.extension.scene.CustomLinearLayout;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

public abstract class SceneDelegate {

    public SceneDelegate() {
    }

    public static SceneDelegate create(Dialog dialog) {
        return new SceneDelegateImpl(dialog.getContext(), dialog.getWindow());
    }

    public abstract void onCreate(Bundle saveInstanceState);

    public abstract void onPostCreate(Bundle var1);

    public abstract void onConfigurationChanged(Configuration var1);

    public abstract void onStart();

    public abstract void onStop();

    public abstract void onPostResume();

    @Nullable
    public abstract <T extends View> T findViewById(@IdRes int var1);

    public abstract void setContentView(View var1);

    public abstract void setContentView(@LayoutRes int var1);

    public abstract void setContentView(View var1, ViewGroup.LayoutParams var2);

    public abstract void addContentView(View var1, ViewGroup.LayoutParams var2);

    public abstract void onDestroy();

    public abstract void onSaveInstanceState(Bundle var1);

    public abstract CustomLinearLayout getSubDecor();

    public abstract CustomChoreographer getChoreographer();
}
