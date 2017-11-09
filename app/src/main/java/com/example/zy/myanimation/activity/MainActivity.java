package com.example.zy.myanimation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.adapter.TestAdapter;
import com.example.zy.myanimation.bean.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Create on 17/10/31
 *
 * @author zhaoy
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Test> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initView() {
        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        //创建默认的线性LayoutManager
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        //创建并设置Adapter
        TestAdapter mAdapter = new TestAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
        Button scrollBtn = (Button) findViewById(R.id.scroll_anim_btn);
        Button calendarBtn = (Button) findViewById(R.id.calendar_view);
        scrollBtn.setOnClickListener(this);
        calendarBtn.setOnClickListener(this);
    }

    private void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new Test("双色球", "32355465", "54654464"));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scroll_anim_btn:
                ScrollAnimationActivity.startActivity(this);
                break;
            case R.id.calendar_view:
                CalendarActivity.startActivity(this);
                break;
            default:
                break;
        }
    }
}
