package com.example.pipeline_annotations.pipeline.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PipeAnnotation  {
    int type() default PipelineNodeType.NORMAl_NODE;

    int state() default SceneState.NONE;

    String taskName();
}
