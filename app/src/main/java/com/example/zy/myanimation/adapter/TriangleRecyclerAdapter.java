package com.example.zy.myanimation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zy.myanimation.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 三角形点击事件弹窗适配器
 * Created on 2018/5/10.
 *
 * @author zhaoy
 */
public class TriangleRecyclerAdapter extends RecyclerView.Adapter<TriangleRecyclerAdapter.MyViewHolder> {
    private ArrayList<Integer> data;
    private Context context;

    private OnItemClickListener mOnItemClickListener;

    public TriangleRecyclerAdapter(Context context, ArrayList<Integer> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.triagle_recycler_item, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tvNum.setText(String.valueOf(data.get(position)));

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(view -> {
                mOnItemClickListener.onClickListener(data.get(holder.getAdapterPosition()));
            });
        }
    }

    @Override
    public int getItemCount() {
        return null != data ? data.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvNum;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvNum = itemView.findViewById(R.id.tv_num);
        }
    }

    public interface OnItemClickListener {

        void onClickListener(int num);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
