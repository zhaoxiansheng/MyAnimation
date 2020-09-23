package com.example.extension.scene;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

import com.alion.mycanvas.server.SceneParam;
import com.example.extension.ViewOnLayoutListener;
import com.example.extension.animation.AnimationFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import androidx.annotation.RequiresApi;

public class CustomChoreographer<T extends ViewGroup> implements ViewOnLayoutListener {

    private static final String TAG = CustomChoreographer.class.toString();

    private Context mContext;

    private AnimationFactory mAnimationFactory;

    private T mLayout;

    private SceneParam mSceneParam;
    private SceneParam mLastSceneParam;

    private View mUnUpdateView;
    private ArrayList<View> mUpdateViews;

    private boolean isFolded;

    public CustomChoreographer(Context context) {
        this.mContext = context;
        mAnimationFactory = new AnimationFactory();
        getSceneParams();
    }

    private void getUpdateView() {
        if (mLayout != null) {
            if (mLayout instanceof CustomLinearLayout) {
                mUpdateViews = ((CustomLinearLayout) mLayout).getUpdateView();
            }
        }
    }

    private void getUnUpdateView() {
        if (mLayout != null) {
            if (mLayout instanceof CustomLinearLayout) {
                mUnUpdateView = ((CustomLinearLayout) mLayout).getUnUpdateView();
            }
        }
    }

    private void getSceneParams() {
        if (mLayout != null) {
            if (mLayout instanceof CustomLinearLayout) {
                mSceneParam = ((CustomLinearLayout) mLayout).getSceneParam();
                if (mAnimationFactory != null) {
                    mAnimationFactory.setIsFold(mSceneParam.getSceneType() == SceneParam.ACTION_EXPAND);
                }
            }
        }
    }

    private void getAnimationType() {
        if (mLayout != null) {
            if (mLayout instanceof CustomLinearLayout) {
                mAnimationFactory.setAnimationType(((CustomLinearLayout) mLayout).getAnimationTypes());
            }
        }
    }

    private void getAnimationDuration() {
        if (mLayout != null) {
            if (mLayout instanceof CustomLinearLayout) {
                mAnimationFactory.setDuration(((CustomLinearLayout) mLayout).getAnimationDuration());
            }
        }
    }

    public void setSceneParam(SceneParam mSceneParam) {
        this.mSceneParam = mSceneParam;
    }

    public void setLayout(T mLayout) {
        if (mLayout != null) {
            this.mLayout = mLayout;
            getAnimationDuration();
            getAnimationType();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onGlobalLayoutListener() {
//        play();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void changeScene(final boolean isFold) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                isFolded = isFold;
                if (mUnUpdateView == null) {
                    getUnUpdateView();
                }
                if (mUpdateViews == null || mUpdateViews.size() == 0) {
                    getUpdateView();
                }
                if (!isFold) {
                    TransitionManager.beginDelayedTransition(mLayout, mAnimationFactory.getCurrentAnimation());
                }
                mAnimationFactory.changeScene(mUnUpdateView, mUpdateViews);
            }
        });
    }

    public void completeScene() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TransitionManager.endTransitions(mLayout);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void goScene(boolean isFold, ViewGroup viewGroup) {
        isFolded = isFold;
        clearData();
        Scene scene = new Scene(viewGroup);
        TransitionManager.go(scene, mAnimationFactory.getCurrentAnimation());
        if (!isFold) {
            TransitionManager.endTransitions(mLayout);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void goScene(boolean isFold, int layoutId) {
        isFolded = isFold;
        clearData();
        Scene scene = Scene.getSceneForLayout(mLayout, layoutId, this.mContext);
        TransitionManager.go(scene, mAnimationFactory.getCurrentAnimation());
        if (!isFold) {
            TransitionManager.endTransitions(mLayout);
        }
    }

    private void clearData() {
        mUnUpdateView = null;
        mUpdateViews = null;
    }

    public Object createHandler(Looper looper, Handler.Callback callback, boolean sync) {
        Object[] params = new Object[3];
        params[0] = looper;
        params[1] = callback;
        params[2] = sync;

        Class[] args = new Class[3];
        args[0] = Looper.class;
        args[1] = Handler.Callback.class;
        args[2] = boolean.class;
        try {
            Class ownerClass = Class.forName("android.os.Handler");
            Constructor method = ownerClass.getConstructor(args);
            return method.newInstance(looper, callback, sync);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
