package com.example.zy.myanimation.view.lifecycle;

import android.view.View;

import com.example.zy.myanimation.view.lifecycle.scene.SceneParam;

import java.util.ArrayList;

public interface ViewChangeInterface {

    void update();
    void recover();

    int getAnimationDuration();
    int getAnimationTypes();

    View getUnUpdateView();
    ArrayList<View> getUpdateView();

    SceneParam getSceneParam();
}
