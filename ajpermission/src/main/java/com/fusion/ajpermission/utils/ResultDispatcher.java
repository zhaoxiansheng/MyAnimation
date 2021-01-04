package com.fusion.ajpermission.utils;

import com.fusion.ajpermission.callback.AbstractDispatcher;
import com.fusion.ajpermission.callback.IPermission;

public final class ResultDispatcher extends AbstractDispatcher {

    public ResultDispatcher(){
    }

    @Override
    public void dispatchResule(long token, int requestCode, JPermissionHelper.PermissionResult result) {
        IPermission permissionListener = mListeners.remove(token);
        if(permissionListener != null){
            if (!result.mDeniedForverList.isEmpty()) {
                //有永久拒绝权限
                permissionListener.deniedForver(requestCode, result.mDeniedForverList);
            } else if (!result.mDeniedList.isEmpty()) {
                //是否有临时被拒绝的授权
                permissionListener.denied(requestCode, result.mDeniedList);
            } else {
                //授权全部授予
                permissionListener.ganted();
            }
        }
    }
}
