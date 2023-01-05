package com.example.leetcode.arithmetic;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

//动态规划 Dynamic Programming，DP
public class DP {

    private static final String TAG = DP.class.getSimpleName();

    //斐波那契数列 暴力递归解法 其中大部分数都会被计算多次
    public static long fibonacci(long number) {
        if ((number == 0) || (number == 1))
            return number;
        else
            return fibonacci(number - 1) + fibonacci(number - 2);
    }

    //斐波那契数列 带备忘录解法
    public static int fibonacciDP(int N) {
        if (N < 1) return 0;
        // 备忘录全初始化为 0
        int[] memo = new int[N + 1];
        // 初始化最简情况
        memo[1] = memo[2] = 1;
        return helper(memo, N);
    }

    //DP table
    private static int helper(int[] memo, int n) {
        // 未被计算过
        if (n > 0 && memo[n] == 0) {
            memo[n] = helper(memo, n - 1) + helper(memo, n - 2);
        }
        return memo[n];
    }

    //斐波那契数列 自底向下
    public static int fibonacciB(int N) {
        int[] memo = new int[N + 1];
        memo[1] = memo[2] = 1;
        for (int i = 3; i <= N; i++) {
            memo[i] = memo[i - 1] + memo[i - 2];
        }
        return memo[N];
    }

    //斐波那契数列优化
    public static int fibonacciBest(int n) {
        if (n < 2) return n;
        int prev = 0, curr = 1;
        for (int i = 0; i < n - 1; i++) {
            int sum = prev + curr;
            prev = curr;
            curr = sum;
        }
        return curr;
    }

    /**
     * @param values   保存每一种硬币的币值的数组  顺序排序
     * @param money    需要找零的面值
     * @param useCount 使用的数量
     */
    public static int makeChange(ArrayList<Integer> values, int money, int useCount) {
        for (int i = 0; i < values.size(); i++) {
            if (money - values.get(i) > 0) {
                useCount++;
                makeChange(values, money - values.get(i), useCount);
            } else if (money - values.get(i) == 0) {
                useCount++;
            } else {
                Log.d(TAG, "makeChange: 最终剩下" + money);
                return useCount;
            }
        }
        return useCount;
    }

    //最长回文子序列
    public static void BasicDP() {
    }

    public static void main(String[] args) {
        int[] nums1 = new int[]{1, 2, 3, 0, 0, 0};
        int[] nums2 = new int[]{2, 5, 6};

        int[] nums3 = new int[]{1};
        int[] nums4 = new int[]{};

        int[] nums5 = new int[]{6, 7, 8, 0, 0, 0};
        int[] nums6 = new int[]{1, 3, 5};
        System.out.println(Arrays.toString(merge(nums1, 3, nums2, 3)));
        System.out.println(Arrays.toString(merge(nums3, 1, nums4, 0)));
        System.out.println(Arrays.toString(merge(nums5, 3, nums6, 3)));
    }

    //题目2：
    //给你两个有序整数数组 nums1 和 nums2，请你将 nums2 合并到 nums1 中，使 nums1 成为一个有序数组。
    //初始化 nums1 和 nums2 的元素数量分别为 m 和 n 。你可以假设 nums1 的空间大小等于 m + n，这样它就有足够的空间保存来自 nums2 的元素。
    //示例 1：
    //    输入：nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
    //    输出：[1,2,2,3,5,6]
    //示例 2：
    //    输入：nums1 = [1], m = 1, nums2 = [], n = 0
    //    输出：[1]
    public static int[] merge(int[] nums1, int m, int[] nums2, int n) {
        int hostNum = m - 1;
        int mergeNum = n - 1;
        int length = m + n - 1;
        while (hostNum >= 0 && mergeNum >= 0) {
            if (nums1[hostNum] >= nums2[mergeNum]) {
                nums1[length--] = nums1[hostNum--];
            } else {
                nums1[length--] = nums2[mergeNum--];
            }
        }

        while (mergeNum >= 0) {
            nums1[length--] = nums2[mergeNum--];
        }

        return nums1;
    }

}
