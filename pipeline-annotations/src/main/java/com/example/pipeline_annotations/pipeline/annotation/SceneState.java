package com.example.pipeline_annotations.pipeline.annotation;

public class SceneState {

    public static final int NONE = -1;

    public static final int START = 0;

    public static final int TRANSFORM_TARGET = 1;

    public static final int POST_TEXTURE_CHANGED = 2;

    public static final int PRE_OTHER_TRANSFORM_TARGET = 3;

    public static final int FOLLOW_PRETREATMENT = 4;

    public static final int OTHER_TRANSFORM_TARGET = 5;

    public static final int RESIZE_CURRENT_ITEM = 6;

    public static final int POST_OTHER_TEXTURE_CHANGED = 7;

    public static final int RESIZE_FOLLOW_ITEM = 8;

    public static final int END = 9;

    public static final int RESET = 10;
}
