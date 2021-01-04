package com.example.pipeline_compiler;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;

public abstract class AnnotationTranslator {
    Class<? extends Annotation> mAnnotationCLz = null;

    AnnotationTranslator(Class<? extends Annotation> annoClz){
        mAnnotationCLz = annoClz;
    }

    abstract boolean translate(final StringBuffer stringBuffer, final Element annotationTarget);
}
