package com.example.zy.myanimation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.zy.myanimation.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> mData;  //设置数据

    public ListViewAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            // 获取布局文件中的控件
            view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.text = view.findViewById(R.id.btn);

            view.setTag(holder); //打一个标记
        } else {
            holder = (ViewHolder) view.getTag();  //取出标记
        }

        // 为控件绑定数据
        String text = getItem(position);
        holder.text.setText(text);
        holder.text.setOnClickListener(view1 -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(position);
            }
        });

        return view;
    }

    private ItemClickListener mOnItemClickListener;

    public void setItemClickListener(ItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    static class ViewHolder {
        Button text;
    }

}
