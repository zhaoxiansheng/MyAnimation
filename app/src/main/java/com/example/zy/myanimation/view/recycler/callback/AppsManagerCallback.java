package com.example.zy.myanimation.view.recycler.callback;


import com.example.zy.myanimation.view.recycler.model.infos.AppInfo;

import java.util.List;

public interface AppsManagerCallback {

    void changeTheme();

    void onAppDataChange(List<AppInfo> data);

    void localeChanged();

    void setAppListAdapter(List<AppInfo> data);

}
