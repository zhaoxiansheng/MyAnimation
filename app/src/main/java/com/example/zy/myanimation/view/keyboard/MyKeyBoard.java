package com.example.zy.myanimation.view.keyboard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;


public abstract class MyKeyBoard extends Keyboard implements KeyboardView.OnKeyboardActionListener {

    protected EditText currentEditText;
    protected View nextFocusView;
    protected MyKeyStyle myKeyStyle;

    public MyKeyBoard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public MyKeyBoard(Context context, int xmlLayoutResId, int modeId, int width, int height) {
        super(context, xmlLayoutResId, modeId, width, height);
        initKeyBoard(context, xmlLayoutResId, modeId, width, height);
    }

    public MyKeyBoard(Context context, int xmlLayoutResId, int modeId) {
        super(context, xmlLayoutResId, modeId);
    }

    public MyKeyBoard(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }

    private void initKeyBoard(Context context, int xmlLayoutResId, int modeId, int width, int height) {

    }

    protected int getKeyCode(int resId) {
        if (null != currentEditText) {
            return currentEditText.getContext().getResources().getInteger(resId);
        } else {
            return Integer.MIN_VALUE;
        }
    }

    public EditText getCurrentEditText() {
        return currentEditText;
    }

    public void setCurrentEditText(EditText currentEditText) {
        this.currentEditText = currentEditText;
    }

    public void setNextFocusView(View nextFocusView) {
        this.nextFocusView = nextFocusView;
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int i, int[] ints) {
        if (null != currentEditText && currentEditText.hasFocus() && handleSpecialKey(currentEditText, i)) {
            Editable editable = currentEditText.getText();

            int start = currentEditText.getSelectionStart();

            if (i == Keyboard.KEYCODE_DELETE) {
                if (!TextUtils.isEmpty(editable)) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            }
        }
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    public void hideKeyboard(){
        if (null != nextFocusView) {
            nextFocusView.requestFocus();
        }
    }

    /**
     * 处理自定义软键盘的特殊键
     * @param etCurrent 当前操作的editText
     * @param primaryCode 选择的key
     * @return 是否处理过 true已经处理过 false未处理
     */
    public abstract boolean handleSpecialKey(EditText etCurrent, int primaryCode);

    public interface MyKeyStyle {
        Drawable getKeyBackground(Key key, EditText currentEditText);

        Float getKeyTextSize(Key key, EditText currentEditText);

        Integer getKeyTextColor(Key key, EditText currentEditText);

        CharSequence getKeyLabnel(Key key, EditText currentEditText);
    }

    public static class SimpleMyKeyStyle implements MyKeyStyle {
        @Override
        public Drawable getKeyBackground(Key key, EditText currentEditText) {
            return key.iconPreview;
        }

        @Override
        public Float getKeyTextSize(Key key, EditText currentEditText) {
            return null;
        }

        @Override
        public Integer getKeyTextColor(Key key, EditText currentEditText) {
            return null;
        }

        @Override
        public CharSequence getKeyLabnel(Key key, EditText currentEditText) {
            return key.label;
        }

        protected int getKeyCode(Context context, int resId) {
            if (null != context) {
                return context.getResources().getInteger(resId);
            } else {
                return Integer.MIN_VALUE;
            }
        }

        protected Drawable getDrawable(Context context, int resId) {
            if (null != context) {
                return context.getResources().getDrawable(resId);
            } else {
                return null;
            }
        }
    }
}
