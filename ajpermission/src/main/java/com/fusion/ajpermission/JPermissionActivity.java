package com.fusion.ajpermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.view.WindowManager;

import com.fusion.ajpermission.callback.IPermission;
import com.fusion.ajpermission.utils.JPermissionHelper;
import com.fusion.ajpermission.utils.ResultDispatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/18
 * @description
 */

public class JPermissionActivity extends Activity {

    private static final String PARAM_PERMISSION = "param_permission";
    private static final String PARAM_REQUEST_CODE = "param_request_code";
    private static final String PARAM_DISPATCH_CODE = "dispatch_code";

    private String[] mPermissions;
    private int mRequestCode;
    private long mDispatchToken;
    private static final ResultDispatcher mDispatcher = new ResultDispatcher();

    public static synchronized void permissionRequest(Context context,
                                         boolean forceRequest,
                                         String[] permissions,
                                         int requestCode,
                                         IPermission iPermission) {

        if(!forceRequest) {
            JPermissionHelper.PermissionResult result = JPermissionHelper.needRequest(context, permissions);
            if (result.mDeniedList.isEmpty()) {
                mDispatcher.dispatchResule(mDispatcher.enqueueRequest(iPermission), requestCode, result);
                return;
            }
        }

        Intent intent = new Intent(context, JPermissionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle bundle = new Bundle();
        bundle.putStringArray(PARAM_PERMISSION, permissions);
        bundle.putInt(PARAM_REQUEST_CODE, requestCode);
        bundle.putLong(PARAM_DISPATCH_CODE, mDispatcher.enqueueRequest(iPermission));

        intent.putExtras(bundle);

        context.startActivity(intent);
        //屏蔽进入动画
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.j_activity_permission);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = 1;
        params.height = 1;
        getWindow().setAttributes(params);
        this.mPermissions = getIntent().getStringArrayExtra(PARAM_PERMISSION);
        this.mRequestCode = getIntent().getIntExtra(PARAM_REQUEST_CODE, 0);
        mDispatchToken = getIntent().getLongExtra(PARAM_DISPATCH_CODE, -1);

        if (this.mPermissions == null || this.mPermissions.length <= 0) {
            finish();
            return;
        }

        //申请授权
        //该方法是异步的。
        // 第一个参数是Context；
        // 第二个参数是需要申请的权限的字符串数组；
        // 第三个参数为requestCode，主要用于回调的时候检测。
        //可以从方法名requestPermissions以及第二个参数看出，是支持一次性申请多个权限的，系统会通过对话框逐一询问用户是否授权。
        ActivityCompat.requestPermissions(this, this.mPermissions, this.mRequestCode);
    }

    /**
     * grantResults对应于申请的结果，这里的数组对应于申请时的第二个权限字符串数组。
     * 如果你同时申请两个权限，那么grantResults的length就为2，分别记录你两个权限的申请结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == this.mRequestCode) {
            JPermissionHelper.PermissionResult result = JPermissionHelper.getResult(this, permissions, grantResults);
            mDispatcher.dispatchResule(mDispatchToken, requestCode, result);
        }

        finish();
    }

    @Override
    public void finish() {
        mDispatcher.cancelRequest(mDispatchToken);
        super.finish();
        overridePendingTransition(0, 0);
    }

}
