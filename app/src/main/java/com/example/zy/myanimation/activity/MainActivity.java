package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.adapter.ListViewAdapter;
import com.example.zy.myanimation.utils.ToolUtils;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Create on 17/10/31
 *
 * @author zhaoy
 */
public class MainActivity extends AppCompatActivity implements ListViewAdapter.ItemClickListener {

    ListView listview;

    private int[] text = {
            R.string.scroll_anim,
            R.string.calendar, // TODO: 2020/3/11 未完成 
            R.string.messenger,
            R.string.aidl,
            R.string.run_text,
            R.string.droplet_bubble,
            R.string.stretchable,
            R.string.lottie,
            R.string.gpu_image,
            R.string.applist,
            R.string.reversal,
            R.string.transition_manager_test,
            R.string.animator_demo,
            R.string.toolbar,
            R.string.recycler_bitmap,
            R.string.screen_shot,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        listview = findViewById(R.id.listview);

        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < text.length; i++) {
            data.add(getString(text[i]));
        }
        ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, data);
        listview.setAdapter(adapter);
        adapter.setItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                ToolUtils.startActivity(this, ScrollAnimationActivity.class);
                break;
            case 1:
                ToolUtils.startActivity(this, CalendarActivity.class);
                break;
            case 2:
                ToolUtils.startActivity(this, MessengerActivity.class);
                break;
            case 3:
                ToolUtils.startActivity(this, BookManagerActivity.class);
                break;
            case 4:
                ToolUtils.startActivity(this, RunTextActivity.class);
                break;
            case 5:
                ToolUtils.startActivity(this, DropletBubbleActivity.class);
                break;
            case 6:
                ToolUtils.startActivity(this, StretchableActivity.class);
                break;
            case 7:
                ToolUtils.startActivity(this, LottieActivity.class);
                break;
            case 8:
                ToolUtils.startActivity(this, GpuImageActivity.class);
                break;
            case 9:
//                ToolUtils.startActivity(this, RecyclerViewActivity.class);
                break;
            case 10:
                ToolUtils.startActivity(this, ReversalActivity.class);
                break;
            case 11:
                ToolUtils.startActivity(this, TransitionManagerActivity.class);
                break;
            case 12:
                ToolUtils.startActivity(this, AnimationDemoActivity.class);
                break;
            case 13:
                ToolUtils.startActivity(this, ToolBarActivity.class);
                break;
            case 14:
                ToolUtils.startActivity(this, RecyclerBitmapActivity.class);
                break;
            case 15:
                ToolUtils.startActivity(this, ScreenShotActivity.class);
                break;
            default:
                break;
        }
    }
}
