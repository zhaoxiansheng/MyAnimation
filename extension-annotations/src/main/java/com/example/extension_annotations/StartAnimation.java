package com.example.extension_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StartAnimation {
    //animation type
    int animationType();
    //scene state
    int sceneState();
    // res id
    int resId();
    //scene type
    int sceneType() default SceneType.PART;
}
