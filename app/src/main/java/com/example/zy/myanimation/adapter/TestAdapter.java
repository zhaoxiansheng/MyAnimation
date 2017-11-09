package com.example.zy.myanimation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zy.myanimation.R;
import com.example.zy.myanimation.bean.Test;
import com.example.zy.myanimation.utils.manager.TestViewHolder;

import java.util.List;

/**
 * Created on 2017/11/8.
 *
 * @author zhaoy
 */
public class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {

    private List<Test> list;

    public TestAdapter(List<Test> list) {
        this.list = list;
    }

    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_test, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TestViewHolder holder, int position) {
        holder.textName.setText(list.get(position).getName());
        holder.textSales.setText(list.get(position).getCurrentSales());
        holder.textTotalSales.setText(list.get(position).getTotalSales());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
