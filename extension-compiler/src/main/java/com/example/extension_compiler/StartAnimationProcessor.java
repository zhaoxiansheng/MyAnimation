package com.example.extension_compiler;

import com.example.extension_annotations.SceneType;
import com.example.extension_annotations.StartAnimation;
import com.example.extension_annotations.UnUpdateView;
import com.example.extension_annotations.UpdateView;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.JavaFileObject;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;
import static javax.tools.Diagnostic.Kind.WARNING;

@AutoService(Processor.class)
public class StartAnimationProcessor extends AbstractProcessor {

    private Filer mFiler;

    private Logger mLogger;

    private HashMap<Element, ArrayList<Element>> mClassFiles = new HashMap<>();

    private HashMap<String, AnnotationTranslator> mRegistedAnnotation = new HashMap<>();

    private void registerAnnotation() {
        mRegistedAnnotation.put(StartAnimation.class.getCanonicalName(), new AnnotationTranslator(StartAnimation.class) {
            @Override
            boolean translate(final StringBuffer stringBuffer, final Element annotationTarget) {

                String className = annotationTarget.getEnclosingElement().getSimpleName().toString();

                createMethod(stringBuffer, annotationTarget, className);

                return true;
            }
        });
        mRegistedAnnotation.put(UnUpdateView.class.getCanonicalName(), new AnnotationTranslator(UnUpdateView.class) {
            @Override
            boolean translate(final StringBuffer stringBuffer, final Element annotationTarget) {

                int value = annotationTarget.getAnnotation(UnUpdateView.class).value();

                stringBuffer.append("private static View unUpdateView; \n");

                stringBuffer.append("private static void initUnUpdateView() { \n" +
                        "unUpdateView = mDecorView.findViewById(" + value + ");\n" +
                        "}\n");

                return true;
            }
        });
        mRegistedAnnotation.put(UpdateView.class.getCanonicalName(), new AnnotationTranslator(UpdateView.class) {
            @Override
            boolean translate(final StringBuffer stringBuffer, final Element annotationTarget) {

                int[] value = annotationTarget.getAnnotation(UpdateView.class).value();

                stringBuffer.append("private static ArrayList<View> updateViews; \n");
                stringBuffer.append("private static void initUpdateViews() { \n" +
                        "updateViews = new ArrayList<>();\n");
                for (int i = 0; i < value.length; i++) {
                    stringBuffer.append("updateViews.add(mDecorView.findViewById(" + value[i] + "));\n");
                }
                stringBuffer.append("}\n");
                return true;
            }
        });
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        registerAnnotation();
        //Filer可以创建文件
        mFiler = processingEnvironment.getFiler();

        mLogger = new Logger();
        mLogger.i("<<<<<<<<<<<<<init>>>>>>>>>>>>");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        HashMap<Element, TargetMetaData> annotations = findAndParseTargets(set, roundEnvironment);
        Iterator<Map.Entry<Element, TargetMetaData>> iterator = annotations.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Element, TargetMetaData> entry = iterator.next();
            TargetMetaData data = entry.getValue();
            if (entry.getKey() instanceof TypeElement) {
                TypeElement classElem = (TypeElement) entry.getKey();
                String packageName = getPackageName(classElem);
                String genClazzName = classElem.getSimpleName() + "$$SceneBinder";

                StringBuffer sourceBuf = genSourceBuffer(packageName, genClazzName, classElem, data);
                if (!generateSourceFile(sourceBuf, packageName + "." + genClazzName)) {
                    mLogger.e("generateSource for " + packageName + "." + genClazzName + " failed");
                }
            }
        }
        return false;
    }

    private HashMap<Element, TargetMetaData> findAndParseTargets(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        HashMap<Element, TargetMetaData> targetMap = new HashMap<>();
        for (TypeElement elem : set) {
            //get annotations target belong to elem type from all source file
            Set<? extends Element> targets = roundEnvironment.getElementsAnnotatedWith(elem);
            for (Element annotationTarget : targets) {
                if (annotationTarget instanceof TypeElement) {
                    TypeElement annotationClass = (TypeElement) annotationTarget;
                    TargetMetaData data = targetMap.get(annotationClass);

                    if (data == null) {
                        data = new TargetMetaData();
                        targetMap.put(annotationClass, data);
                    }

                    if (mClassFiles.containsKey(elem)) {
                        ArrayList<Element> list = mClassFiles.get(elem);
                        list.add(annotationClass);
                    } else {
                        ArrayList<Element> list = new ArrayList<>();
                        list.add(annotationClass);
                        mClassFiles.put(elem, list);
                    }

                    mLogger.i("addFieldElements<" + annotationClass + "," + elem.toString() + " with " + annotationTarget + ">");
                    data.addAnnotations(annotationTarget, elem);
                } else if (annotationTarget instanceof VariableElement) {
                    VariableElement annotationVariable = (VariableElement) annotationTarget;
                    TypeElement annotationClass = (TypeElement) annotationVariable.getEnclosingElement();
                    TargetMetaData data = targetMap.get(annotationClass);

                    if (mClassFiles.containsKey(elem)) {
                        ArrayList<Element> list = mClassFiles.get(elem);
                        list.add(annotationVariable);
                    } else {
                        ArrayList<Element> list = new ArrayList<>();
                        list.add(annotationVariable);
                        mClassFiles.put(elem, list);
                    }

                    if (data == null) {
                        data = new TargetMetaData();
                        targetMap.put(annotationClass, data);
                    }

                    mLogger.i("addFieldElements<" + annotationClass + "," + elem.toString() + " with " + annotationTarget + ">");
                    data.addAnnotations(annotationTarget, elem);
                }
            }
        }
        return targetMap;
    }

    /**
     * generate source string
     *
     * @param packageName
     * @param genClazzName
     * @param classElement
     * @param annotationData
     * @return
     */
    public StringBuffer genSourceBuffer(String packageName, String genClazzName,
                                        TypeElement classElement, TargetMetaData annotationData) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("package " + packageName + ";\n");

        impPackage(stringBuffer);

        stringBuffer.append("public class " + genClazzName + " {\n");

        String className = classElement.getQualifiedName().toString();

        createFiled(stringBuffer, className);


        if (annotationData != null) {
            HashMap<Element, HashSet<TypeElement>> classAnnotations = annotationData.getAnnotations();
            int size = (classAnnotations != null) ? classAnnotations.size() : 0;
            mLogger.i("genSourceBuffer...... classAnnotations: " + classAnnotations + " size:" + size);
            if (classAnnotations != null) {
                for (Map.Entry<Element, HashSet<TypeElement>> entry : classAnnotations.entrySet()) {
                    Element state = entry.getKey();
                    HashSet<TypeElement> targetAnnotations = entry.getValue();
                    mLogger.i("genSourceBuffer...... state: " + state);
                    for (TypeElement annotation : targetAnnotations) {
                        String annotationType = annotation.toString();
                        mLogger.i("gensource state: " + state + " 's " + annotationType);
                        AnnotationTranslator translator = mRegistedAnnotation.get(annotationType);
                        if (translator != null) {
                            translator.translate(stringBuffer, state);
                        }
                        mLogger.i("gensource start: " + state + " 's " + annotationType + " use translator " + translator);
                    }

                }
            }
        }

        sceneChange(stringBuffer);

        stringBuffer.append("@Aspect\n" +
                "    public class AspectClass {\n" +
                "        private final String exeOnWindowAttributesChanged = \"execution(* \" + TAG + \".onWindowAttributesChanged(..))\"; \n" +
                "         private final String exeOnSceneChange = \"execution(* \" + TAG + \".onSceneChange(..))\"; ");

        onWindowAttributesChanged(stringBuffer);

        onSceneChange(stringBuffer);

        stringBuffer.append("    }\n");
        stringBuffer.append("} \n");
        return stringBuffer;
    }

    private boolean generateSourceFile(StringBuffer source, String fileName) {
        Writer writer = null;
        try {
            JavaFileObject sourceFile = mFiler.createSourceFile(fileName);
            writer = sourceFile.openWriter();
            writer.write(source.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationSet = new HashSet<>();
        annotationSet.add(UnUpdateView.class.getCanonicalName());
        annotationSet.add(UpdateView.class.getCanonicalName());
        annotationSet.add(StartAnimation.class.getCanonicalName());
        return annotationSet;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void createMethod(final StringBuffer stringBuffer, Element annotationTarget, String className) {
        int sceneStates = annotationTarget.getAnnotation(StartAnimation.class).sceneState();
        int animationTypes = annotationTarget.getAnnotation(StartAnimation.class).animationType();
        int resID = annotationTarget.getAnnotation(StartAnimation.class).resId();
        int sceneType = annotationTarget.getAnnotation(StartAnimation.class).sceneType();

        String name = className + "$$SceneBinder";

        stringBuffer.append("public " + name + "(Object target, Object view) {\n");
        stringBuffer.append(" this.mDecorView = (FrameLayout) view;\n" +
                "           this.mContext = target;");

        stringBuffer.append("if (mContext instanceof Dialog) {\n" +
                "            Dialog d = (Dialog) mContext;\n" +
                "            mWindow = d.getWindow(); \n" +
                "            mWinParams = mWindow.getAttributes();\n" +
                "            mViewContainer = mDecorView.findViewById(" + resID + ");\n" +
                "            mChoreographer = new CustomChoreographer(d.getContext());\n" +
                "        } else if (mContext instanceof Activity) {\n" +
                "            Activity a = (Activity) mContext;\n" +
                "            mWindow = a.getWindow();" +
                "            mWinParams = mWindow.getAttributes();\n" +
                "            mViewContainer = mDecorView.findViewById(" + resID + ");\n" +
                "            mChoreographer = new CustomChoreographer(a);\n" +
                "        }\n");
        stringBuffer.append("mState = " + sceneStates + ";\n" +
                "        mAnimationType = " + animationTypes + ";\n" +
                "        mViewContainer.setAnimationTypes(mAnimationType);\n" +
                "mChoreographer.setLayout(mViewContainer);\n" +
                "\n");

        if (sceneType == SceneType.PART) {
            stringBuffer.append("initUpdateViews();\n" +
                    "initUnUpdateView();\n" +
                    "if (unUpdateView != null) {\n" +
                    "            mViewContainer.setUnUpdateView(unUpdateView);\n" +
                    "        }\n" +
                    "        if (updateViews != null && updateViews.size() > 0) {\n" +
                    "            mViewContainer.setUpdateView(updateViews);\n" +
                    "        }");
        }
        stringBuffer.append("}\n");
    }

    public String getPackageName(Element classElement) {
        PackageElement packageOf = processingEnv.getElementUtils().getPackageOf(classElement);
        Name qualifiedName = packageOf.getQualifiedName();
        return qualifiedName.toString();
    }

    private void sceneChange(StringBuffer stringBuffer) {
        stringBuffer.append("private static void sceneChange() {\n" +
                "Log.d(TAG, \"sceneChange asdasdasdasdr : \" + mSceneParam);" +
                "        if (mSceneParam.getSceneType() == SceneParam.ACTION_EXPAND) {\n" +
                "            mChoreographer.completeScene();\n" +
                "            mChoreographer.changeScene(false);\n" +
                "        } else {\n" +
                "            mChoreographer.changeScene(true);\n" +
                "        }\n" +
                "    }\n");
    }

    private void onWindowAttributesChanged(StringBuffer stringBuffer) {
        stringBuffer.append("        @Around(exeOnWindowAttributesChanged)\n" +
                "        public void aroundOnWindowAttributesChanged(ProceedingJoinPoint joinPoint) {\n" +
                "             Object[] args = joinPoint.getArgs();\n" +
                "            if (args != null && args.length > 0 && args[0].getClass() == WindowManager.LayoutParams.class) {\n" +
                "                WindowManager.LayoutParams params = (WindowManager.LayoutParams) args[0];\n" +
                "                Log.d(TAG, \"aroundOnWindowAttributesChanged asdasdasdasdr :\" + params );\n" +
                "                if (params.width > 0) {\n" +
                "                    mWinParams = params;\n" +
                "                    if (mViewContainer != null) {\n" +
                "                        mViewContainer.setWinParams(params);\n" +
                "                        if (mState != mLastState) {\n" +
                "                            mLastState = mState;\n" +
                "                            if (mSceneParam != null) {\n" +
                "                                sceneChange();\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "                try {\n" +
                "                    joinPoint.proceed(args);\n" +
                "                } catch (Throwable throwable) {\n" +
                "                    throwable.printStackTrace();\n" +
                "                }\n" +
                "            }" +
                "        }\n");
    }

    private void onSceneChange(StringBuffer stringBuffer) {
        stringBuffer.append(" @Around(exeOnSceneChange)\n" +
                "        public void aroundOnSceneChanged(ProceedingJoinPoint joinPoint) {\n" +
                "            Object[] args = joinPoint.getArgs();\n" +
                "            if (args != null && args.length > 0 && args[0].getClass() == SceneParam.class) {\n" +
                "                mSceneParam = (SceneParam) args[0];\n" +
                "                if (width != mSceneParam.getWinWidth() | height != mSceneParam.getWinHeight()) {\n" +
                "                    width = mSceneParam.getWinWidth();\n" +
                "                    height = mSceneParam.getWinHeight();\n" +
                "                    mViewContainer.setSceneParams(mSceneParam);\n" +
                "                    mState = mSceneParam.getSceneType();\n" +
                "                }\n" +
                "                try {\n" +
                "                    joinPoint.proceed(args);\n" +
                "                } catch (Throwable throwable) {\n" +
                "                    throwable.printStackTrace();\n" +
                "                }\n" +
                "            }\n" +
                "        }");
    }

    private void impPackage(StringBuffer stringBuffer) {
        stringBuffer.append("import android.app.Activity;\n" +
                "import android.app.Dialog;\n" +
                "import android.view.View;\n" +
                "import android.view.WindowManager;\n" +
                "import android.widget.FrameLayout;\n" +
                "import com.example.extension.scene.CustomChoreographer;\n" +
                "import com.example.extension.scene.CustomLinearLayout;\n" +
                "import java.util.ArrayList;\n" +
                "import com.alion.mycanvas.server.SceneParam;\n" +
                "import org.aspectj.lang.ProceedingJoinPoint;\n" +
                "import org.aspectj.lang.annotation.Around;\n" +
                "import org.aspectj.lang.annotation.Aspect;\n" +
                "import com.example.extension.utils.AspectUtil;\n" +
                "import android.util.Log;\n" +
                "import com.example.extension.animation.AnimationType;\n" +
                "import android.view.Window;\n" +
                "import com.example.extension.TargetSceneDialog;");
    }

    private void createFiled(StringBuffer stringBuffer, String className) {
        stringBuffer.append("private final static String TAG = \"" + className + "\";\n" +
                "private static FrameLayout mDecorView;\n" +
                "\n" +
                "    private Object mContext;\n" +
                "\n" +
                "    private static CustomLinearLayout mViewContainer;\n" +
                "\n" +
                "    private static CustomChoreographer mChoreographer;\n" +
                "\n" +
                " private static int mState;\n" +
                " private static int mLastState = -1;\n" +
                "    \n" +
                "    private static int mAnimationType;\n" +
                "private static WindowManager.LayoutParams mWinParams;\n" +
                "\n" +
                "private static SceneParam mSceneParam;\n" +
                "private static int width, height;\n" +
                "private static Window mWindow;");
    }

    private class Logger {
        private Messager mLogger = processingEnv.getMessager();

        void i(String str) {
            if (str != null) {
                mLogger.printMessage(NOTE, str);
            }
        }

        void w(String str) {
            if (str != null) {
                mLogger.printMessage(WARNING, str);
            }
        }


        void e(String str) {
            if (str != null) {
                mLogger.printMessage(ERROR, str);
            }
        }
    }
}
