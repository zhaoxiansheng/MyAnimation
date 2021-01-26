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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leet_code);
    }
}