package com.autopai.common.utils.utils.shitu;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.autopai.common.R;

import razerdp.basepopup.BasePopupWindow;

public class DemoPopup extends BasePopupWindow {

    private Bean mData;

    private boolean flag;
    private View view;

    public DemoPopup(Context context, Bean data) {
        super(context);
        this.mData = data;
        ListView listView = view.findViewById(R.id.tv_desc);
        listView.setAdapter(new ItemAdapter(getContext(), mData.getResult()));
    }

    @Override
    public View onCreateContentView() {
        view = createPopupById(R.layout.popup_normal);
        return view;
    }
}