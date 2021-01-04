package com.example.pipeline_compiler;

import com.example.pipeline_annotations.pipeline.annotation.PipeAnnotation;
import com.example.pipeline_annotations.pipeline.annotation.PipelineNodeType;
import com.example.pipeline_annotations.pipeline.annotation.SceneState;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import static com.example.pipeline_compiler.Utils.generateSourceFile;
import static com.example.pipeline_compiler.Utils.getPackageName;

@AutoService(Processor.class)
public class StartAnimationProcessor extends AbstractProcessor {

    private static final String CLASS_TAG = "$$Pipeline";
    private static final String METHOD_TAG = "Node$$Pipeline";

    private Filer mFiler;

    private Logger mLogger;

    private HashMap<String, AnnotationTranslator> mRegisterAnnotation = new HashMap<>();

    private HashMap<ExecutableElement, ArrayList<Element>> mMethodField = new HashMap<>();

    private HashMap<TypeElement, ArrayList<Element>> mClass = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        registerAnnotation();
        //Filer可以创建文件
        mFiler = processingEnvironment.getFiler();

        mLogger = new Logger(processingEnvironment.getMessager());
        mLogger.i("<<<<<<<<<<<<<init>>>>>>>>>>>>");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        HashMap<TypeElement, ArrayList<Element>> classes = findAndParseTargets(set, roundEnvironment);

        for (Map.Entry<TypeElement, ArrayList<Element>> entry : classes.entrySet()) {
            TypeElement classElem = entry.getKey();
            ArrayList<Element> methodOrFieldElem = entry.getValue();

            String packageName = getPackageName(processingEnv, classElem);
            String genClazzName = classElem.getSimpleName() + CLASS_TAG;

            mLogger.i("generateSource package: " + genClazzName);
            StringBuffer sourceBuf = genSourceBuffer(packageName, genClazzName, methodOrFieldElem);

            if (!generateSourceFile(mFiler, sourceBuf, packageName + "." + genClazzName)) {
                mLogger.e("generateSource for " + packageName + "." + genClazzName + " failed");
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return mRegisterAnnotation.keySet();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private StringBuffer genSourceBuffer(String packageName, String className, ArrayList<Element> methodOrField) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("package ").append(packageName).append(";\n");

        impPackager(stringBuffer);

        stringBuffer.append("public class ").append(className).append("<T> {\n");

        for (Element entry : methodOrField) {
            List<? extends AnnotationMirror> annotations = entry.getAnnotationMirrors();
            String annotationType;
            for (AnnotationMirror annotationMirror : annotations) {
                if (mRegisterAnnotation.containsKey(annotationMirror.getAnnotationType().toString())) {
                    annotationType = annotationMirror.getAnnotationType().toString();
                    AnnotationTranslator translator = mRegisterAnnotation.get(annotationType);
                    if (translator != null) {
                        translator.translate(stringBuffer, entry);
                    }
                    mLogger.i("gensource's bsss: " + annotationType + " use translator " + translator);
                }
            }
        }

        createConstructor(stringBuffer, methodOrField, className);

        stringBuffer.append("}\n");
        return stringBuffer;
    }

    private HashMap<TypeElement, ArrayList<Element>> findAndParseTargets(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        HashMap<TypeElement, ArrayList<Element>> classes = new HashMap<>();
        for (TypeElement elem : set) {
            Set<? extends Element> targets = roundEnvironment.getElementsAnnotatedWith(elem);
            for (Element annotationTarget : targets) {
                if (annotationTarget.getEnclosingElement() instanceof TypeElement) {
                    TypeElement key = (TypeElement) annotationTarget.getEnclosingElement();
                    if (classes.containsKey(key)) {
                        classes.get(key).add(annotationTarget);
                    } else {
                        ArrayList<Element> arrayList = new ArrayList<>();
                        arrayList.add(annotationTarget);
                        classes.put(key, arrayList);
                    }
                    mLogger.i("findAndParseTargets TypeElement<" + key + ", " + annotationTarget + ">");
                } else if (annotationTarget.getEnclosingElement() instanceof ExecutableElement) {
                    ExecutableElement key = (ExecutableElement) annotationTarget.getEnclosingElement();
                    if (mMethodField.containsKey(key)) {
                        mMethodField.get(key).add(annotationTarget);
                    } else {
                        ArrayList<Element> arrayList = new ArrayList<>();
                        arrayList.add(annotationTarget);
                        mMethodField.put(key, arrayList);
                    }
                    mLogger.i("findAndParseTargets ExecutableElement<" + key + ", " + annotationTarget + ">");
                }
            }
        }
        mClass.putAll(classes);
        return classes;
    }

    private void registerAnnotation() {
        mRegisterAnnotation.put(PipeAnnotation.class.getCanonicalName(), new AnnotationTranslator(PipeAnnotation.class) {
            @Override
            boolean translate(final StringBuffer stringBuffer, final Element annotationTarget) {
                createMethod(stringBuffer, annotationTarget);
                return true;
            }
        });
    }

    private void createMethod(final StringBuffer stringBuffer, Element annotationTarget) {
        final Name method = annotationTarget.getSimpleName();

        int sceneState = annotationTarget.getAnnotation(PipeAnnotation.class).state();
        int pipelineNodeType = annotationTarget.getAnnotation(PipeAnnotation.class).type();
        String taskName = annotationTarget.getAnnotation(PipeAnnotation.class).taskName();

        String innerClassName = method + METHOD_TAG;
        String className = getPackageName(processingEnv, annotationTarget) + "." + annotationTarget.getEnclosingElement().getSimpleName();

        createCommonContent(stringBuffer, innerClassName, sceneState, method);

//        disposeMsg(stringBuffer, sceneState, className, method);

//        disposeNextMsg(stringBuffer, sceneState, className);

        stringBuffer.append("}\n");
        stringBuffer.append("}\n");
        stringBuffer.append("}\n");
    }

    private void createCommonContent(StringBuffer stringBuffer, String innerClassName, int sceneState, Name method) {
        stringBuffer.append("public class ").append(innerClassName).append("<T> extends PipelineNode<T> {\n");
        stringBuffer.append("private int pipelineType;\n");
        stringBuffer.append("public ").append(innerClassName).append("(String className, int sceneState, int pipelineType) {");
        stringBuffer.append("super(className, sceneState, pipelineType);\n");
        stringBuffer.append("this.pipelineType = pipelineType;\n");
        stringBuffer.append("PipelineFactory.getInstance().add(this);}\n");

        stringBuffer.append(" @Override\n");
        stringBuffer.append("public void perSubmit(T msg) { \n");
        stringBuffer.append("BasePipelineManager p =  PipelineFactory.getInstance(); \n");
        stringBuffer.append("Log.d(\"changedComplete\", \"process: \" + p.currentFence() + \", name: ").append(method.toString()).append("\");\n");
        stringBuffer.append("p.setCurrentNode(this);\n" +
                "p.setMsg(msg);\n");
        if (sceneState == SceneState.RESIZE_CURRENT_ITEM) {
            stringBuffer.append("if (p.currentFence() == 1) {\n");
        } else {
            stringBuffer.append("if (p.currentFence() == 0) {\n");
        }
        stringBuffer.append("if (pipelineType == " + PipelineNodeType.FENCE_NODE + ") {\n" +
                "p.addFence();\n" +
                "}\n");
        stringBuffer.append("getTask().process(next, msg);\n");
    }

    private void impPackager(StringBuffer stringBuffer) {
        stringBuffer.append("import android.util.Log;\n" +
                "import com.example.pipeline_annotations.pipeline.annotation.pipeline.base.BasePipelineManager;\n" +
                "import com.example.pipeline_annotations.pipeline.annotation.pipeline.base.PipelineNode;\n" +
                "import com.example.pipeline_annotations.pipeline.annotation.pipeline.factory.PipelineFactory;\n");
    }

    private void createConstructor(StringBuffer stringBuffer, ArrayList<Element> methodOrField, String className) {
        stringBuffer.append("public ").append(className).append("(){\n");
        for (Element element : methodOrField) {
            final Name method = element.getSimpleName();

            int sceneState = element.getAnnotation(PipeAnnotation.class).state();
            int pipelineNodeType = element.getAnnotation(PipeAnnotation.class).type();
            String taskName = element.getAnnotation(PipeAnnotation.class).taskName();

            stringBuffer.append("new ").append(element.getSimpleName().toString()).append(METHOD_TAG)
                    .append("(\"").append(taskName).append("\", ").append(sceneState).append(", ")
                    .append(pipelineNodeType).append(");\n");
        }
        stringBuffer.append("}\n");
    }
}
