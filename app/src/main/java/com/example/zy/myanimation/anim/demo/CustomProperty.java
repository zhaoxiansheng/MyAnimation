package com.example.zy.myanimation.anim.demo;

import android.util.Property;
import android.widget.TextView;

public class CustomProperty extends Property<TextView, String> {

    public CustomProperty(Class<String> type, String name) {
        super(type, name);
    }

    @Override
    public void set(TextView object, String value) {
        object.setText(value);
    }

    @Override
    public String get(TextView object) {
        return object.getText().toString();
    }
}
