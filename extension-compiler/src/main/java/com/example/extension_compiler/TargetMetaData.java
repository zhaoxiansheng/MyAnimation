package com.example.extension_compiler;

import java.util.HashMap;
import java.util.HashSet;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 *
 */
public class TargetMetaData {

    /*private List<VariableElement> mFieldElements;

    private List<ExecutableElement> mMethodElements;

    private List<TypeParameterElement> mParamElements;*/
    //a target may has more than one annotation
    private HashMap<Element, HashSet<TypeElement>> mTarget_Annotations;

    public HashMap<Element, HashSet<TypeElement>> getAnnotations() {
        return mTarget_Annotations;
    }

    public void addAnnotations(Element target, TypeElement annotationElement) {
        if (mTarget_Annotations == null) {
            mTarget_Annotations = new HashMap<>();
        }
        HashSet<TypeElement> targetAnnotations = mTarget_Annotations.get(target);
        if (targetAnnotations == null) {
            targetAnnotations = new HashSet<>();
            mTarget_Annotations.put(target, targetAnnotations);
        }
        targetAnnotations.add(annotationElement);
    }

    public static class DataEntry<T> {
        public DataEntry(T fieldElement, TypeElement annotation) {
            mFieldElement = fieldElement;
            mAnnotation = annotation;
        }

        T mFieldElement;
        TypeElement mAnnotation;
    }
}
