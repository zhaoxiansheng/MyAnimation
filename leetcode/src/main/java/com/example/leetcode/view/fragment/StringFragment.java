package com.example.leetcode.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leetcode.R;
import com.example.leetcode.arithmetic.StringProblem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StringFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StringFragment extends Fragment {

    private static final String TAG = StringFragment.class.getSimpleName();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private final char[] mTarget = new char[]{'a', 'b', 'c', 'b', 'a', 'a', 'b', 'a', 'b', 'a', 'f', 'g', 'a', 'b', 'c', 'b', 'a', 'x'};
    private final char[] mMatch = new char[]{'a', 'b', 'c', 'b', 'a', 'x'};
    private final char[] mMatch1 = new char[]{'a', 'a'};
    private final char[] mMatch2 = new char[]{'a', 'b', 'a', 'b', 'a'};

    public StringFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StringFragment.
     */
    public static StringFragment newInstance(String param1, String param2) {
        StringFragment fragment = new StringFragment();
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

        int index = StringProblem.KMPSolution(mTarget, mMatch2);
        System.out.println("KMP of index is: " + index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_string, container, false);
    }
}