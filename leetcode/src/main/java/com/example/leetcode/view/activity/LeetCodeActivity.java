package com.example.leetcode.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.leetcode.R;
import com.example.leetcode.arithmetic.BinarySearch;
import com.example.leetcode.arithmetic.DP;
import com.example.leetcode.arithmetic.Sort;
import com.example.leetcode.arithmetic.StackSolution;
import com.example.leetcode.arithmetic.StringProblem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

public class LeetCodeActivity extends AppCompatActivity {

    private static final String TAG = LeetCodeActivity.class.getSimpleName();
    private final int[] mData = new int[]{8, 4, 5, 10, 9, 6, 12, 7, 3, 11};

    private final String[] mTokens = new String[]{"9", "+", "(", "3", "-", "1", ")", "*", "3", "+", "10", "/", "2"};

    private final char[] mTarget = new char[]{'a', 'b', 'c', 'b', 'a', 'f', 'g', 'a', 'b', 'c', 'b', 'a', 'x'};
    private final char[] mMatch = new char[]{'a', 'b', 'c', 'b', 'a', 'x'};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leet_code);

        initView();

        init();
    }

    private void initView() {
    }

    private void init() {
        Sort.quickSort(mData, 0, mData.length - 1);
        for (int i : mData) {
            Log.d(TAG, "onCreate: value = " + i);
        }
        int key = BinarySearch.commonBinarySearch(mData, 7);
        Log.d(TAG, "onCreate: key = " + key);

        ArrayList<String> stackRNP = StackSolution.midToSuffix(mTokens);

        int num = StackSolution.evalRPN(stackRNP.toArray(new String[0]));
        System.out.println("RPN of num is: " + num);

        int index = StringProblem.KMPSolution(mTarget, mMatch);
        System.out.println("KMP of index is: " + index);

        int sum = DP.fibonacciBest(10);
        System.out.println("fibonacci of sum is : " + sum);

        ArrayList<Integer> values = new ArrayList<>();
        values.add(5);
        values.add(2);
        values.add(1);
        int s = 0;
        int coinCount = DP.makeChange(values, 11, s);
        System.out.println("makeChange of coinCount is : " + coinCount);
    }
}