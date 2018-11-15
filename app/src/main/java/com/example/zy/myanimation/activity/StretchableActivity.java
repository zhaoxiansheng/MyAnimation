package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.view.stretchable.StretchableFloatingButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StretchableActivity extends AppCompatActivity {

    @BindView(R.id.stretch_float_btn)
    StretchableFloatingButton stretchFloatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stretchable);
        ButterKnife.bind(this);

        stretchFloatBtn.setFoldListener((isIncrease, sfb) -> {
            stretchFloatBtn.startScroll();
        });
    }
}
