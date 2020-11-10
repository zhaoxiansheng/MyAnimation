package com.example.zy.myanimation.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.utils.ReflectUtil;
import com.example.zy.myanimation.utils.ScreenShotUtils;
import com.example.zy.myanimation.utils.shitu.Bean;
import com.example.zy.myanimation.utils.shitu.GsonUtils;
import com.example.zy.myanimation.utils.shitu.PopupUtils;
import com.example.zy.myanimation.view.MarkSizeView;
import com.example.zy.myanimation.view.recycler.utils.Utils;

import java.lang.reflect.Field;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScreenShotActivity extends AppCompatActivity {

    private final int REQUEST_MEDIA_PROJECTION = 1;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.screen_shot)
    Button screenShot;
    @BindView(R.id.screen_get)
    Button screenGet;
    @BindView(R.id.mark_size)
    MarkSizeView markSizeView;

    private boolean isMarkRect = true;
    private Rect markedArea;
    private MarkSizeView.GraphicPath mGraphicPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot);
        ButterKnife.bind(this);

        ScreenShotUtils.requestMediaProjection(this, REQUEST_MEDIA_PROJECTION);

        markSizeView.setUnmarkedColor(getResources().getColor(R.color.transparent));

        markSizeView.setOnClickListener(new MarkSizeView.onClickListener() {
            @Override
            public void onConfirm(Rect markedArea) {
                ScreenShotActivity.this.markedArea = new Rect(markedArea);
                markSizeView.reset();
                markSizeView.setUnmarkedColor(getResources().getColor(R.color.transparent));
            }

            @Override
            public void onConfirm(MarkSizeView.GraphicPath path) {
                mGraphicPath = path;
                markSizeView.reset();
                markSizeView.setUnmarkedColor(getResources().getColor(R.color.transparent));
            }

            @Override
            public void onCancel() {
                markSizeView.reset();
                markSizeView.setUnmarkedColor(getResources().getColor(R.color.transparent));
            }

            @Override
            public void onTouch() {
                markSizeView.setUnmarkedColor(getResources().getColor(R.color.quarter_transparent));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            ScreenShotUtils.mResultCode = resultCode;
            ScreenShotUtils.mResultData = data;
            ScreenShotUtils.setUpMediaProjection();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.screen_shot, R.id.screen_get})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.screen_shot:
                start();
                break;
            case R.id.screen_get:
                break;
        }
    }

    private void start() {
        View root = screenShot.getRootView();
        if (root == null) {
            return;
        }
        ViewParent viewRoot = root.getParent();

        Field f = ReflectUtil.getClassFiled(viewRoot.getClass(), "mSurface");

        Surface o = (Surface) ReflectUtil.getObjectByFiled(viewRoot, f);

        ScreenShotUtils.playback(new ScreenShotUtils.ShituCallBack() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    Bean bean = GsonUtils.resultToJson(result);
                    PopupUtils.createPopup(Utils.getApp(), screenShot, bean);
                } else {
                    Log.e("zhaoy", "onSuccess but result is null");
                }
            }
        }, image.getWidth(), image.getHeight(), o);
    }
}
