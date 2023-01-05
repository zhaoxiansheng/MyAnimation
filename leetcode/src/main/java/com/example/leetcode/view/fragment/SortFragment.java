package com.example.leetcode.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leetcode.R;
import com.example.leetcode.arithmetic.Sort;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class SortFragment extends Fragment {

    private static final String TAG = SortFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private final int[] mData = new int[]{8, 4, 5, 10, 9, 6, 12, 7, 3, 11};

    public SortFragment() {
    }

    public static SortFragment newInstance(String param1, String param2) {
        SortFragment fragment = new SortFragment();
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

        Sort.quickSort(mData, 0, mData.length - 1);

        ArrayList<Integer> arrayListA = new ArrayList<>();
        arrayListA.add(1);
        arrayListA.add(2);
        arrayListA.add(3);
        arrayListA.add(4);

        ArrayList<Integer> arrayListB = new ArrayList<>();

        arrayListB.addAll(arrayListA);

        arrayListB.remove(0);

        for (int i : arrayListA) {
            System.out.println(i);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sort, container, false);
    }
}