package com.example.zy.myanimation.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.view.lifecycle.CustomImageView;
import com.example.zy.myanimation.view.lifecycle.CustomLinearLayout;
import com.example.zy.myanimation.view.lifecycle.scene.CustomChoreographer;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;

import static android.transition.TransitionSet.ORDERING_TOGETHER;

public class TransitionManagerActivity extends Activity {

    CustomLinearLayout viewContainer;
    ImageView img;
    CustomImageView img1;
    ImageView img2;

    private boolean mTemp;
    private WindowManager.LayoutParams winParams;
    private TransitionSet set;
    private Transition transition;

    private boolean isFirst;

    private Scene scene1;
    private Scene scene2;

    private int width;
    private int height;
    private ArrayList<View> views;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        winParams = getWindow().getAttributes();
        winParams.width = 600;
        width = winParams.width;
        height = winParams.height;
        winParams.gravity = Gravity.LEFT;
        getWindow().setAttributes(winParams);
        setContentView(R.layout.activity_transition_manager);

        viewContainer = findViewById(R.id.view_container);
        img = findViewById(R.id.img);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);

        init();

        setOnClick();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        views = new ArrayList<>();
        views.add(img);
        views.add(img2);

        mChoreographer = new CustomChoreographer();
        mChoreographer.setWindowParams(winParams);
        if (viewContainer != null) {
            mChoreographer.setLayout(viewContainer);
        }
        if (img != null) {
            viewContainer.setUnUpdateView(img1);
        }
        if (views != null && views.size() > 0) {
            viewContainer.setUpdateView(views);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            scene1 = Scene.getSceneForLayout(viewContainer, R.layout.scene1_layout, this);
//            scene2 = Scene.getSceneForLayout(viewContainer, R.layout.scene2_layout, this);

//            View view1 = LayoutInflater.from(this).inflate(R.layout.scene1_layout, null);
//            View view2 = LayoutInflater.from(this).inflate(R.layout.scene2_layout, null);
//            scene1 = new Scene(viewContainer, view1);
//            scene2 = new Scene(viewContainer, view2);
//            TransitionManager.go(scene2);

//            scene1 = new Scene(viewContainer, img1);
        }

        set = new TransitionSet();
        Fade fade = new Fade();
        fade.setDuration(1000);
//        FadeTransition fade = new FadeTransition(new LinearInterpolator(), 1, 0.2f, 1);

        transition = set.addTransition(fade)
                .addTransition(new Slide())
                .setDuration(1000)
                .setOrdering(ORDERING_TOGETHER);
//        transition = fade;

//        viewContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Log.e("zhaoy", "onGlobalLayout: 123123123123");
//                if (mCurrentWidth == -1 && mCurrentHeight == -1) {
//                    mCurrentWidth = mLastWidth = winParams.width;
//                    mCurrentHeight = mLastHeight = winParams.height;
//                } else {
//                    mCurrentWidth = winParams.width;
//                    mCurrentHeight = winParams.height;
//                    if (mCurrentWidth != mLastWidth || mCurrentHeight != mLastHeight) {
//                        mLastWidth = mCurrentWidth;
//                        mLastHeight = mCurrentHeight;
//                        mChoreographer.play();
//                    }
//                }
//            }
//        });
    }

    private void setOnClick() {
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

//                    TransitionManager.beginDelayedTransition(viewContainer, transition);
                    if (mTemp) {
                        winParams.width = 500;
//                        winParams.gravity = Gravity.LEFT;
//                        TransitionManager.go(scene2, transition);
                    } else {
                        winParams.width = 1500;
//                        winParams.gravity = Gravity.LEFT;
//                        TransitionManager.go(scene1, transition);
                    }
                    mTemp = !mTemp;
                    getWindow().setAttributes(winParams);
//                    changeScene(img1);
                }
            }
        });
    }

    private CustomChoreographer mChoreographer;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);
        Log.e("zhaoy", "onWindowAttributesChanged: " + width + ", " + params.width);
        if (width != params.width | height != params.height) {
            width = params.width;
            height = params.height;
            if (width == 500) {
                mChoreographer.changeScene(true);
            } else if (width == 1500) {
                mChoreographer.changeScene(false);
            }
        }
    }
}
