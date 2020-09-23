package com.example.scene;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alion.mycanvas.RemoteBase.TargetDialog;
import com.alion.mycanvas.server.SceneParam;
import com.example.extension.animation.AnimationType;
import com.example.extension.annotation.AddAnimation;
import com.example.extension.scene.CustomLinearLayout;
import com.example.extension_annotations.SceneState;
import com.example.extension_annotations.StartAnimation;
import com.example.extension_annotations.UnUpdateView;
import com.example.extension_annotations.UpdateView;

import androidx.annotation.RequiresApi;


public class TransitionManagerDialog extends TargetDialog {

    @StartAnimation(animationType = AnimationType.FADE, sceneState = SceneState.EXPAND, resId = R.id.view_container)
    CustomLinearLayout layout;

    @UpdateView(R.id.img)
    ImageView img;
    @UnUpdateView(R.id.img1)
    ImageView img1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_transition_manager);
        AddAnimation.bind(this);
    }

    protected void onSceneChange(SceneParam param) {
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // TODO: 2020/4/28 需要通知 是什么状态
    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);
    }
}
