package com.example.zy.myanimation.utils.manager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zy.myanimation.R;

/**
 * Created on 2017/11/9.
 *
 * @author zhaoy
 */
public class TestViewHolder extends RecyclerView.ViewHolder {
    public TextView textName;
    public TextView textSales;
    public TextView textTotalSales;
    public TestViewHolder(View itemView) {
        super(itemView);
        textName = itemView.findViewById(R.id.text_name);
        textSales = itemView.findViewById(R.id.text_sales);
        textTotalSales = itemView.findViewById(R.id.text_total_sales);
    }
}
