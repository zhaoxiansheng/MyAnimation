package com.example.zy.myanimation.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.view.longbitmap.CustomImageView;
import com.example.zy.myanimation.view.longbitmap.CustomLinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ToolBarActivity extends AppCompatActivity {

    private float mScale = 0.5f;

    @BindView(R.id.content)
    CustomLinearLayout content;

    private CustomImageView customImageView;
    private CustomImageView customImageView1;
    private CustomImageView customImageView2;
    private CustomImageView customImageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams windowParams = getWindow().getAttributes();
        windowParams.width = 800;
        getWindow().setAttributes(windowParams);


        setContentView(R.layout.activity_toolbar);
        ButterKnife.bind(this);

        content.setClipChildren(false);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);

        params.weight = 1;
        customImageView = new CustomImageView(this);
        customImageView.setOriginalImage(BitmapFactory.decodeResource(getResources(), R.drawable.book));
        customImageView.setLayoutParams(params);
        customImageView.setBackgroundColor(Color.BLUE);

        customImageView1 = new CustomImageView(this);
        customImageView1.setOriginalImage(BitmapFactory.decodeResource(getResources(), R.drawable.camera));
        customImageView1.setLayoutParams(params);
        customImageView1.setBackgroundColor(Color.YELLOW);

        customImageView2 = new CustomImageView(this);
        customImageView2.setOriginalImage(BitmapFactory.decodeResource(getResources(), R.drawable.car));
        customImageView2.setLayoutParams(params);
        customImageView2.setBackgroundColor(Color.RED);

        customImageView3 = new CustomImageView(this);
        customImageView3.setOriginalImage(BitmapFactory.decodeResource(getResources(), R.drawable.micphone));
        customImageView3.setLayoutParams(params);
        customImageView3.setBackgroundColor(Color.GREEN);

        customImageView.setName("1");
        customImageView1.setName("2");
        customImageView2.setName("3");
        customImageView3.setName("4");

        content.addView(customImageView);
        content.addView(customImageView1);
        content.addView(customImageView2);
        content.addView(customImageView3);

//        setFoldMode();
        setExpendMode();

        customImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setScaleX(mScale);
                content.setPivotX(800);

                customImageView.handlerScale(mScale, 1f);
                customImageView1.handlerScale(mScale, 1f);
                customImageView2.handlerScale(mScale, 1f);
                customImageView3.handlerScale(mScale, 1f);

                content.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        WindowManager.LayoutParams params1 = getWindow().getAttributes();
                        params1.width = (int) (params1.width * mScale);
                        getWindow().setAttributes(params1);

                        content.setScaleX(1f);
                        content.setPivotX(0);

                        customImageView.reset();
                        customImageView1.reset();
                        customImageView2.reset();
                        customImageView3.reset();

                        setFoldMode();
//                        setExpendMode();
                    }
                }, 2000);

                content.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        WindowManager.LayoutParams params1 = getWindow().getAttributes();
                        params1.width = (int) (params1.width / mScale);
                        getWindow().setAttributes(params1);

                        setExpendMode();
                    }
                }, 5000);
            }
        });
    }

    public void setExpendMode() {
        customImageView.setVisibility(VISIBLE);
        customImageView1.setVisibility(VISIBLE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Fade fade = new Fade();
            fade.addTarget(customImageView2);
            fade.addTarget(customImageView3);
            fade.setDuration(1000);
            TransitionManager.beginDelayedTransition(content, fade);
        }

        customImageView2.setVisibility(VISIBLE);
        customImageView3.setVisibility(VISIBLE);
    }

    public void setFoldMode() {
        customImageView.setVisibility(VISIBLE);
        customImageView1.setVisibility(VISIBLE);

        customImageView2.setVisibility(GONE);
        customImageView3.setVisibility(GONE);
    }
}
