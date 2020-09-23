package com.example.extension;

import android.view.View;

import com.alion.mycanvas.server.SceneParam;

import java.util.ArrayList;

public interface ViewChangeInterface {

    int getAnimationDuration();

    int getAnimationTypes();

    View getUnUpdateView();

    ArrayList<View> getUpdateView();

    SceneParam getSceneParam();
}
