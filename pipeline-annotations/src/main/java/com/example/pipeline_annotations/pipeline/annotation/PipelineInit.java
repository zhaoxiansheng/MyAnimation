package com.example.pipeline_annotations.pipeline.annotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class PipelineInit {

    private final static Map<Class<?>, Constructor> BINDINGS = new LinkedHashMap<>();

    private static Logger log;
    static {
        log = Logger.getLogger("PipelineInit");
        log.setLevel(Level.ALL);
    }


    private PipelineInit() {
        throw new AssertionError("No instances");
    }

    private static final String TAG = "AddAnimation";
    private static boolean DEBUG = true;

    public static void setDebug(boolean debug) {
        PipelineInit.DEBUG = debug;
    }

    public static void mount(Object target) {
        Class<?> targetClass = target.getClass();
        if (DEBUG) {
            log.info("Looking up binding for : " + targetClass.getName());
        }
        if (BINDINGS.containsKey(targetClass)) {
            if (DEBUG) log.info("HIT: Cached in binding map.");
            return;
        }

        Constructor constructor = findBindingConstructorForClass(targetClass);

        if (constructor == null) {
            return;
        }

        try {
            constructor.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new RuntimeException("Unable to create binding instance,", cause);
        }
    }

    private static Constructor findBindingConstructorForClass(Class<?> cls) {
        Constructor bindingCtor = BINDINGS.get(cls);
        if (bindingCtor != null || BINDINGS.containsKey(cls)) {
            if (DEBUG) log.info("HIT: Cached in binding map.");
            return bindingCtor;
        }
        String clsName = cls.getName();
        if (clsName.startsWith("android.") || clsName.startsWith("java.")
                || clsName.startsWith("androidx.")) {
            if (DEBUG) log.info("Miss : Reached framework class, Abandoning search: ");
            return null;
        }
        try {
            Class<?> bindClass = cls.getClassLoader().loadClass(clsName + "$$Pipeline");
            bindingCtor = bindClass.getConstructor();
        } catch (ClassNotFoundException e) {
            bindingCtor = findBindingConstructorForClass(cls.getSuperclass());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        BINDINGS.put(cls, bindingCtor);
        return bindingCtor;
    }
}
