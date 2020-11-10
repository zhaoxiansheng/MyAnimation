package com.example.zy.myanimation.utils.shitu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zy.myanimation.R;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Bean.ResultBean> mData;  //设置数据

    public ItemAdapter(Context context, ArrayList<Bean.ResultBean> data) {
        this.context = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Bean.ResultBean getItem(int position) {
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
            view = LayoutInflater.from(context).inflate(R.layout.popup_item_normal, null);
            holder = new ViewHolder();
            holder.score = view.findViewById(R.id.score);
            holder.root = view.findViewById(R.id.root);
            holder.keyword = view.findViewById(R.id.keyword);

            view.setTag(holder); //打一个标记
        } else {
            holder = (ViewHolder) view.getTag();  //取出标记
        }
        Bean.ResultBean resultBean = getItem(position);
        holder.score.setText(String.valueOf(resultBean.getScore()));
        holder.root.setText(resultBean.getRoot());
        holder.keyword.setText(resultBean.getKeyword());

        return view;
    }

    static class ViewHolder {
        TextView score;
        TextView root;
        TextView keyword;
    }
}
