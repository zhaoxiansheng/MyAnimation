package com.example.extension.annotation;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public final class AddAnimation {

    private final static Map<Class<?>, Constructor> BINDINGS = new LinkedHashMap<>();

    private AddAnimation() {
        throw new AssertionError("No instances");
    }

    private static final String TAG = "AddAnimation";
    private static boolean DEBUG = true;

    public static void setDebug(boolean debug) {
        AddAnimation.DEBUG = debug;
    }

    public static void bind(@NonNull Activity target) {
        View sourceView = target.getWindow().getDecorView();
        bind(target, sourceView);
    }

    public static void bind(@NonNull Dialog target) {
        View sourceView = target.getWindow().getDecorView();
        bind(target, sourceView);
    }

    public static void bind(@NonNull Object target, @NonNull View source) {
        Class<?> targetClass = target.getClass();
        if (DEBUG) {
            Log.d(TAG, "Looking up binding for : " + targetClass.getName());
        }
        Constructor constructor = findBindingConstructorForClass(targetClass);

        if (constructor == null) {
            return;
        }

        try {
            constructor.newInstance(target, source);
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
            if (DEBUG) Log.d(TAG, "HIT: Cached in binding map.");
            return bindingCtor;
        }
        String clsName = cls.getName();
        if (clsName.startsWith("android.") || clsName.startsWith("java.")
                || clsName.startsWith("androidx.")) {
            if (DEBUG) Log.d(TAG, "Miss : Reached framework class, Abandoning search: ");
            return null;
        }
        try {
            Class<?> bindClass = cls.getClassLoader().loadClass(clsName + "$$SceneBinder");
            bindingCtor = bindClass.getConstructor(Object.class, Object.class);
        } catch (ClassNotFoundException e) {
            bindingCtor = findBindingConstructorForClass(cls.getSuperclass());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        BINDINGS.put(cls, bindingCtor);
        return bindingCtor;
    }
}
