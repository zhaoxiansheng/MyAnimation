package com.example.leetcode;

import android.os.Bundle;
import android.util.Log;

import com.example.leetcode.utils.BinarySearch;
import com.example.leetcode.utils.Recursion;
import com.example.leetcode.utils.Sort;
import com.example.leetcode.utils.StackSolution;
import com.example.leetcode.utils.StringProblem;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

public class LeetCodeActivity extends AppCompatActivity {

    private static final String TAG = LeetCodeActivity.class.getSimpleName();
    private int[] mData = new int[]{8, 4, 5, 10, 9, 6, 12, 7, 3, 11};

    private String[] mTokens = new String[]{"9", "+", "(", "3", "-", "1", ")", "*", "3", "+", "10", "/", "2"};

    private char[] mTarget = new char[]{'a', 'b', 'c', 'b', 'a', 'f', 'g', 'a', 'b', 'c', 'b', 'a', 'x'};
    private char[] mMatch = new char[]{'a', 'b', 'c', 'b', 'a', 'x'};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leet_code);

        Sort.quickSort(mData, 0, mData.length - 1);
        for (int i : mData) {
            Log.d(TAG, "onCreate: value = " + i);
        }
        int key = BinarySearch.commonBinarySearch(mData, 7);
        Log.d(TAG, "onCreate: key = " + key);


        for (int counter = 0; counter <= 10; counter++) {
            System.out.printf("Fibonacci of %d is: %d\n",
                    counter, Recursion.fibonacci(counter));
        }

        ArrayList<String> stackRNP = StackSolution.midToSuffix(mTokens);

        int num = StackSolution.evalRPN(stackRNP.toArray(new String[stackRNP.size()]));
        System.out.println("RPN of num is: " + num);

        int index = StringProblem.KMPSolution(mTarget, mMatch);
        System.out.println("KMP of index is: " + index);
    }
}