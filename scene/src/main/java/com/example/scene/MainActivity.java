package com.example.scene;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.alion.mycanvas.RemoteBase.RemoteAttributeParser;
import com.alion.mycanvas.client.BnTargetControl;
import com.alion.mycanvas.debugMode.DevManager;
import com.alion.mycanvas.service.BnClientService;
import com.alion.mycanvas.service.TargetService;

import java.lang.reflect.Field;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private DevManager mManager;
    private BnClientService mService;
    private boolean mIsBinded = false;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = (BnClientService) service;
            mManager.setDevService(mService);
            Spinner spinner = findViewById(com.alion.mycanvas.texturecanvas.R.id.spinner);
            List<String> dialogNames = DevManager.getInstance(mContext).getDialogNames();
            spinner.setAdapter(new ArrayAdapter<>(mContext, com.alion.mycanvas.texturecanvas.R.layout.support_simple_spinner_dropdown_item, dialogNames.toArray(new String[dialogNames.size()])));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String className = mManager.getDialogs().get(mManager.getDialogNames().get(position));
                    startTarget(className);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private BnTargetControl mTarget;

    private void startTarget(String target) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(getPackageName(), target));
        Bundle bundle = new Bundle();
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        mParams.x = 0;
        mParams.y = 0;
        RemoteAttributeParser.putTargetDescription(bundle, target);
        RemoteAttributeParser.putWindowAttribute(bundle, mParams);
        mManager.startTarget(intent, bundle, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                mTarget = (BnTargetControl) msg.obj;
                return false;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (TargetService.getOSVersion() >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                requestPermission(this);
            }
        }

        mManager = DevManager.getInstance(this);
        Context context = mManager.getCurrentContext();
        if (context instanceof Service) {
            Log.e("TAG", "Target running... disable debug");
            Toast.makeText(this, "Target running... disable debug", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        mManager.setDevMode(true);
        mManager.loadAllDialogs(this);
        mContext = this;
        Intent intent = new Intent();
        intent.setAction("com.alion.mycanvas.Service");
        intent.setPackage(getPackageName());
        mIsBinded = bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        setContentView(com.alion.mycanvas.texturecanvas.R.layout.activity_spinner);
    }

    @Override
    protected void onDestroy() {
        if (mIsBinded) {
            unbindService(mServiceConnection);
        }
        DevManager.getInstance(this).setDevMode(false);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (mTarget != null) {
            DevManager.getInstance(this).finishTarget(mTarget);
        }
        super.onStop();
    }

    /**
     * 请求悬浮窗权限
     *
     * @param context 上下文
     */
    private void requestPermission(Context context) {
        if (TargetService.getOSVersion() >= 19 && TargetService.getOSVersion() < 23) {
            jump2DetailActivity(context);
        } else if (TargetService.getOSVersion() >= 23) {
            highVersionJump2PermissionActivity(context);
        }
    }

    /**
     * 跳转应用详情界面
     *
     * @param context
     */
    private void jump2DetailActivity(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }

    /**
     * Android 6.0 版本及之后的跳转权限申请界面
     *
     * @param context 上下文
     */
    private void highVersionJump2PermissionActivity(Context context) {
        try {
            Class clazz = Settings.class;
            Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
            Intent intent = new Intent(field.get(null).toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("zhaoy", Log.getStackTraceString(e));
        }
    }
}
