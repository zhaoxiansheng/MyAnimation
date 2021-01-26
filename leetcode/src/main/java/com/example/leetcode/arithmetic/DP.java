package com.example.leetcode.arithmetic;

import java.util.ArrayList;

//动态规划 Dynamic Programming，DP
public class DP {

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
     * @param values    保存每一种硬币的币值的数组  顺序排序
     * @param money     需要找零的面值
     * @param useCount 保存面值为i的纸币找零所需的最小硬币数
     */
    public static int makeChange(ArrayList<Integer> values, int money, int useCount) {
        for (int i = 0; i < values.size(); i++) {
            if (money - values.get(i) > 0) {
                useCount++;
                makeChange(values, money - values.get(i), useCount);
            } else if (money - values.get(i) != 0){
                values.remove(i);
            } else {
                useCount++;
            }
        }
        return useCount;
    }

    //最长回文子序列
    public static void BasicDP() {
    }
}
