package com.autopai.common.utils.utils.shitu;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

public class PopupUtils {

    public static void createPopup(Context context, View view, Bean bean) {
        DemoPopup demoPopup = new DemoPopup(context, bean);
        demoPopup.setPopupGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        demoPopup.showPopupWindow(view);
    }
}
