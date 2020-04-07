package com.example.zy.myanimation.view.lifecycle.scene;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class SceneManager {

    private static final String TAG = SceneManager.class.toString();

    private ArrayList<CustomScene> mSceneList = new ArrayList<>();

    /**
     * 按场景区分的map
     */
    private ConcurrentHashMap<Integer, ArrayList<CustomScene>> mSceneMap = new ConcurrentHashMap<>();
    /**
     * 按照页码区分的map
     */
    private ConcurrentHashMap<Integer, ArrayList<CustomScene>> mPageMap = new ConcurrentHashMap<>();

    private SceneManager() {
    }

    /**
     * @return this
     */
    public static SceneManager getInstance() {
        return SingleTonBuilder.sceneManager;
    }

    private static class SingleTonBuilder {
        private static SceneManager sceneManager = new SceneManager();
    }

    public ConcurrentHashMap<Integer, ArrayList<CustomScene>> getSceneMap() {
        return mSceneMap;
    }

    public ConcurrentHashMap<Integer, ArrayList<CustomScene>> getPageMap() {
        return mPageMap;
    }

    public void setSceneData(ArrayList<CustomScene> data) {
        if (data != null) {
            mSceneList.addAll(data);
            setSceneTypeMap(data);
            setPageMap(data);
        } else {
            Log.e(TAG, "data is null");
        }
    }

    public void addScene(CustomScene scene) {
        if (scene != null) {
            mSceneList.add(scene);
            addPageScene(scene);
            addTypeScene(scene);
        }
    }

    public void removeScene(CustomScene scene) {
        if (scene != null) {
            mSceneList.remove(scene);
            removePageScene(scene);
            removeTypeScene(scene);
        }
    }

    /**
     * 按场景类型分，
     *
     * @param data
     */
    private void setSceneTypeMap(ArrayList<CustomScene> data) {
        for (CustomScene scene : data) {
            if (mSceneMap.containsKey(scene.getSceneType())) {
                ArrayList<CustomScene> scenes = mSceneMap.get(scene.getSceneType());
                if (scenes != null) {
                    scenes.add(scene);
                } else {
                    Log.e(TAG, "setTypeMap sceneType: " + scene.getSceneType() + " ArrayList is null");
                }
            } else if (scene.getSceneType() != -1) {
                ArrayList<CustomScene> scenes = new ArrayList<>();
                scenes.add(scene);
                mSceneMap.put(scene.getSceneType(), scenes);
            }
        }
    }

    /**
     * 按照页码分
     *
     * @param data
     */
    private void setPageMap(ArrayList<CustomScene> data) {
        for (CustomScene scene : data) {
            if (mPageMap.containsKey(scene.getPage())) {
                ArrayList<CustomScene> scenes = mPageMap.get(scene.getPage());
                if (scenes != null) {
                    scenes.add(scene);
                } else {
                    Log.e(TAG, "setPageMap page: " + scene.getPage() + " ArrayList is null");
                }
            } else if (scene.getSceneType() != -1) {
                ArrayList<CustomScene> scenes = new ArrayList<>();
                scenes.add(scene);
                mPageMap.put(scene.getPage(), scenes);
            }
        }
    }

    private void addTypeScene(CustomScene scene) {
        int sceneType = scene.getSceneType();
        if (sceneType != -1) {
            ArrayList<CustomScene> scenes;
            if (mSceneMap.containsKey(sceneType)) {
                scenes = mSceneMap.get(sceneType);
                if (scenes != null) {
                    scenes.add(scene);
                } else {
                    Log.e(TAG, "addTypeScene sceneType: " + scene.getSceneType() + " ArrayList is null");
                }
            } else {
                scenes = new ArrayList<>();
                scenes.add(scene);
                mSceneMap.put(sceneType, scenes);
            }
        }
    }

    private void addPageScene(CustomScene scene) {
        int page = scene.getPage();
        if (page != -1) {
            ArrayList<CustomScene> scenes;
            if (mPageMap.containsKey(page)) {
                scenes = mPageMap.get(page);
                if (scenes != null) {
                    scenes.add(scene);
                } else {
                    Log.e(TAG, "addPageScene page: " + scene.getPage() + " ArrayList is null");
                }
            } else {
                scenes = new ArrayList<>();
                scenes.add(scene);
                mPageMap.put(page, scenes);
            }
        }
    }

    private void removeTypeScene(CustomScene scene) {
        int sceneType = scene.getSceneType();
        if (sceneType != -1) {
            ArrayList<CustomScene> scenes;
            if (mSceneMap.containsKey(sceneType)) {
                scenes = mSceneMap.get(sceneType);
                if (scenes != null) {
                    scenes.remove(scene);
                } else {
                    Log.e(TAG, "removeTypeScene sceneType: " + scene.getSceneType() + " ArrayList is null");
                }
            } else {
                Log.e(TAG, "removeTypeScene:  sceneType is error: " + sceneType);
            }
        }
    }

    private void removePageScene(CustomScene scene) {
        int page = scene.getPage();
        if (page != -1) {
            ArrayList<CustomScene> scenes;
            if (mPageMap.containsKey(page)) {
                scenes = mPageMap.get(page);
                if (scenes != null) {
                    scenes.remove(scene);
                } else {
                    Log.e(TAG, "removePageScene page: " + scene.getPage() + " ArrayList is null");
                }
            } else {
                Log.e(TAG, "removePageScene:  page is error: " + page);
            }
        }
    }
}
