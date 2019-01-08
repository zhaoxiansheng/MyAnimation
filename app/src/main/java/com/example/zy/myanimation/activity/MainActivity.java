package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.utils.ToolUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Create on 17/10/31
 *
 * @author zhaoy
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.scroll_anim_btn)
    Button scrollAnimBtn;
    @BindView(R.id.calendar_view)
    Button calendarView;
    @BindView(R.id.messenger_btn)
    Button messengerBtn;
    @BindView(R.id.aidl_btn)
    Button aidlBtn;
    @BindView(R.id.run_btn)
    Button runText;
    @BindView(R.id.droplet_btn)
    Button dropletBtn;
    @BindView(R.id.stretch_btn)
    Button stretchBtn;
    @BindView(R.id.lottie_btn)
    Button lottieBtn;
    @BindView(R.id.gpu_image_btn)
    Button gpuImageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.scroll_anim_btn, R.id.calendar_view, R.id.messenger_btn, R.id.aidl_btn, R.id.run_btn
            , R.id.droplet_btn, R.id.stretch_btn, R.id.lottie_btn, R.id.gpu_image_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scroll_anim_btn:
                ToolUtils.startActivity(this, ScrollAnimationActivity.class);
                break;
            case R.id.calendar_view:
                ToolUtils.startActivity(this, CalendarActivity.class);
                break;
            case R.id.messenger_btn:
                ToolUtils.startActivity(this, MessengerActivity.class);
                break;
            case R.id.aidl_btn:
                ToolUtils.startActivity(this, BookManagerActivity.class);
                break;
            case R.id.run_btn:
                ToolUtils.startActivity(this, RunTextActivity.class);
                break;
            case R.id.droplet_btn:
                ToolUtils.startActivity(this, DropletBubbleActivity.class);
                break;
            case R.id.stretch_btn:
                ToolUtils.startActivity(this, StretchableActivity.class);
                break;
            case R.id.lottie_btn:
                ToolUtils.startActivity(this, LottieActivity.class);
                break;
            case R.id.gpu_image_btn:
                ToolUtils.startActivity(this, GpuImageActivity.class);
                break;
            default:
                break;
        }
    }
}
