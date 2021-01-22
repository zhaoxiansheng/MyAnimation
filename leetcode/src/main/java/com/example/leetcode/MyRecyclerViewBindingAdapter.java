package com.example.leetcode;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewBindingAdapter {

    @BindingAdapter(value = {"adapter"}, requireAll = false)
    public static void setAdapter(RecyclerView recyclerView, ListAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter(value = {"submitList"}, requireAll = false)
    public static void submitList(RecyclerView recyclerView, List list) {
        if (recyclerView.getAdapter() != null) {
            ListAdapter adapter = (ListAdapter) recyclerView.getAdapter();
            adapter.submitList(list);
        }
    }
}
