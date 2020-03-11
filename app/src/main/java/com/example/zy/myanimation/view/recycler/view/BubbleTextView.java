/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.zy.myanimation.view.recycler.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.example.zy.myanimation.view.recycler.channel.ChannelConfig;

/**
 * TextView that draws a bubble behind the text. We cannot use a LineBackgroundSpan
 * because we want to make the bubble taller than the text and TextView's clip is
 * too aggressive.
 */
@SuppressLint("AppCompatCustomView")
public class BubbleTextView extends TextView {

    private Drawable mIcon;
    private final int mIconSize;


    public BubbleTextView(Context context) {
        this(context, null, 0);
    }

    public BubbleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setGravity(Gravity.CENTER_HORIZONTAL);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, ChannelConfig.APP_TEXT_SIZE);
        setTextColor(Color.parseColor("#FFFFFFFF"));
        mIconSize = ChannelConfig.APP_ICON_SIZE;
        setLines(1);
    }


    /**
     * Returns the icon for this view.
     */
    public Drawable getIcon() {
        return mIcon;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setPadding(0, ChannelConfig.BUBBLE_TEXTVIEW_PADDING_TOP, 0, 0);
    }

    /**
     * Sets the icon for this view based on the layout direction.
     */
    public Drawable setIcon(Drawable icon) {
        mIcon = icon;
        if (mIconSize != -1) {
            mIcon.setBounds(0, 0, mIconSize, mIconSize);
        }
        setCompoundDrawables(null, mIcon, null, null);
        setCompoundDrawablePadding(ChannelConfig.APP_DRAWABLE_PADDING);
        return icon;
    }


}
