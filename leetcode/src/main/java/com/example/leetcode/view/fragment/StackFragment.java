package com.example.leetcode.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leetcode.R;
import com.example.leetcode.arithmetic.StackSolution;

import java.util.ArrayList;

public class StackFragment extends Fragment {

    private static final String TAG = StackFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private final String[] mTokens = new String[]{"9", "+", "(", "3", "-", "1", ")", "*", "3", "+", "10", "/", "2"};

    public StackFragment() {
    }

    public static StackFragment newInstance(String param1, String param2) {
        StackFragment fragment = new StackFragment();
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

        ArrayList<String> stackRnp = StackSolution.midToSuffix(mTokens);

        int num = StackSolution.evalRPN(stackRnp.toArray(new String[0]));
        System.out.println("RPN of num is: " + num);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stack, container, false);
    }
}