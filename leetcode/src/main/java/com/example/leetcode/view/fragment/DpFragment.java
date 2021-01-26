package com.example.leetcode.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leetcode.R;
import com.example.leetcode.arithmetic.DP;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class DpFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DpFragment() {
    }

    public static DpFragment newInstance(String param1, String param2) {
        DpFragment fragment = new DpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        int sum = DP.fibonacciBest(10);
        System.out.println("fibonacci of sum is : " + sum);

        //面值分别是5，2，1
        ArrayList<Integer> values = new ArrayList<>();
        values.add(5);
        values.add(2);
        values.add(1);

        int s = 0;
        int coinCount = DP.makeChange(values, 11, s);
        System.out.println("makeChange of coinCount is : " + coinCount);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dp, container, false);
    }
}