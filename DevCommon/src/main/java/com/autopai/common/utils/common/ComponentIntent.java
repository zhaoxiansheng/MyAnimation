package com.autopai.common.utils.common;

import android.content.ComponentName;
import android.content.Intent;

public class ComponentIntent extends Intent {

    private String mPackageName;

    private String mClassName;

    public ComponentIntent(String packageName, String className) {
        this.mPackageName = packageName;
        this.mClassName = className;
        setComponent(new ComponentName(mPackageName, mClassName));
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
        setComponent(new ComponentName(mPackageName, mClassName));
    }

    public String getClassName() {
        return mClassName;
    }

    public void setClassName(String className) {
        this.mClassName = className;
        setComponent(new ComponentName(mPackageName, mClassName));
    }
}
