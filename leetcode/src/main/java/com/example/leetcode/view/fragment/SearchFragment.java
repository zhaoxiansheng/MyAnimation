package com.example.leetcode.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leetcode.R;
import com.example.leetcode.arithmetic.BinarySearch;
import com.example.leetcode.arithmetic.Sort;

import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {

    private static final String TAG = SearchFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final int[] mData = new int[]{8, 4, 5, 10, 9, 6, 12, 7, 3, 11};

    public SearchFragment() {
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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

        int key = BinarySearch.commonBinarySearch(mData, 7);
        Log.d(TAG, "onCreate: key = " + key);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}