package com.autopai.common.utils.utils.pipeline.manager;

import android.view.View;

public class SceneManager<T extends View> {

    private static SceneManager mSceneManager = null;

    private T mCurrentView;

    private T mFollowView;

    public static SceneManager getInstance() {
        if (mSceneManager == null) {
            synchronized (SceneManager.class) {
                if (mSceneManager == null) {
                    mSceneManager = new SceneManager();
                }
            }
        }
        return mSceneManager;
    }

    private SceneManager() {
    }

    /**
     * clear view
     */
    public void clear() {
        mCurrentView = null;
    }

    public void setCurrentView(T currentView) {
        mCurrentView = currentView;
    }

    public T getCurrentView() {
        return mCurrentView;
    }

    public void setFollowView(T followView) {
        mFollowView = followView;
    }

    public View getFollowView() {
        return mFollowView;
    }

    public View getMatchView(View view) {
        if (mFollowView == null) {
            throw new NullPointerException("mFollowView is null");
        }
        if (mCurrentView == null) {
            throw new NullPointerException("mCurrentView is null");
        }
        if (view != null) {
            if (view.equals(mCurrentView)) {
                return mFollowView;
            } else if (view.equals(mFollowView)) {
                return mCurrentView;
            }
        }
        return null;
    }
}
