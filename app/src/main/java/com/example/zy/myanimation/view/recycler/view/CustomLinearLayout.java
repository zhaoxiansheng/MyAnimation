package com.example.zy.myanimation.view.recycler.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.view.recycler.channel.ChannelConfig;
import com.example.zy.myanimation.view.recycler.manager.BeanUtils;
import com.example.zy.myanimation.view.recycler.utils.ScreenUtils;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class CustomLinearLayout extends LinearLayout implements View.OnClickListener, CustomImageView.onDrawableStateChangedListener {

    private CustomImageView mImageView;
    private TextView mTextView;
    private ProgressBar mProgress;

    public CustomLinearLayout(Context context) {
        this(context, null, 0);
    }

    public CustomLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        initIcon();
        initText();
        initProgress();
    }

    private void initIcon() {
        LayoutParams imgParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imgParams.gravity = Gravity.CENTER;
        mImageView = new CustomImageView(getContext());
        imgParams.width = ScreenUtils.getDimens(R.dimen.app_icon_size);
        imgParams.height = ScreenUtils.getDimens(R.dimen.app_icon_size);
        mImageView.setLayoutParams(imgParams);
        addView(mImageView);

        mImageView.setOnClickListener(this);
        mImageView.setOnDrawableStateChangedListener(this);
    }

    private void initText() {
        LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER_HORIZONTAL;

        mTextView = new TextView(getContext());
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.getDimens(R.dimen.icon_text_size));
        mTextView.setTextColor(Color.WHITE);
        mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        mTextView.setMaxLines(1);
        mTextView.setMaxWidth(ScreenUtils.getDimens(R.dimen.text_max_length));

        textParams.topMargin = ScreenUtils.getDimens(R.dimen.icon_text_gap);
        mTextView.setLayoutParams(textParams);

        addView(mTextView);
    }

    private void initProgress() {
        LayoutParams progressParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressParams.gravity = Gravity.CENTER_HORIZONTAL;
        mProgress = new ProgressBar(getContext());
        BeanUtils.setFieldValue(mProgress, "mOnlyIndeterminate", Boolean.FALSE);
        mProgress.setIndeterminate(false);
        mProgress.setProgressDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_progress));
        mProgress.setIndeterminateDrawable((ContextCompat.getDrawable(getContext(), android.R.drawable.progress_indeterminate_horizontal)));
        mProgress.setMinimumHeight(ChannelConfig.PROGRESS_HEIGHT);
        mProgress.setProgress(0);
        progressParams.topMargin = ChannelConfig.PROGRESS_MARGIN_TOP;
        progressParams.width = ChannelConfig.PROGRESS_WIDTH;
        progressParams.height = ChannelConfig.PROGRESS_HEIGHT;
        mProgress.setLayoutParams(progressParams);

        addView(mProgress);
        mProgress.setVisibility(GONE);
    }

    public void setProgress(int progress) {
        mProgress.setProgress(progress);
    }

    public void setProgressShow(boolean showProgress) {
        if (showProgress) {
            mTextView.setVisibility(GONE);
            mProgress.setVisibility(VISIBLE);
        } else {
            mTextView.setVisibility(VISIBLE);
            mProgress.setVisibility(GONE);
        }
    }

    public void setShowBadge(boolean showBadge) {
        if (showBadge) {
            mImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.launcher_icon_soft_manager_red));
        } else {
            mImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.launcher_icon_soft_manager));
        }
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    @Override
    public void onClick(View v) {
        if (mOnIconClickListener != null) {
            mOnIconClickListener.onIconClick(this);
        }
    }

    public onIconClickListener mOnIconClickListener;

    public void setOnIconClickListener(onIconClickListener onIconClickListener) {
        this.mOnIconClickListener = onIconClickListener;
    }

    public interface onIconClickListener {
        void onIconClick(View v);
    }

    @Override
    public void onDrawableStateChanged(float alpha) {
        setAlpha(alpha);
    }
}
