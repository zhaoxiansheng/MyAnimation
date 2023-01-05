package com.example.leetcode.arithmetic;

public class StringProblem {

    // KMP
    public static int KMPSolution(char[] target, char[] match) {
        int i = -1;
        int j = -1;
        int[] next = new int[match.length + 1];
        getNext(match, match.length, next);
        while (i < target.length && j < match.length) {
            if (j == -1 || target[i] == match[j]) {
                ++i;
                ++j;
            } else {
                j = next[j];
            }
        }
        if (j >= match.length) {
            return i - match.length;
        } else {
            return 0;
        }
    }

    private static void getNext(char[] match, int matchLen, int[] next) {
        next[0] = -1;
        int i = 0, j = -1;
        while (i < matchLen) {
            if (j == -1 || match[i] == match[j]) {
                next[++i] = ++j;
            } else {
                j = next[j];
            }
        }
    }

    /**
     * 回文数 如果x为偶数 或者x为奇数
     * @param x
     * @return
     */
    public static boolean isPalindrome(int x) {
        //思考：这里大家可以思考一下，为什么末尾为 0 就可以直接返回 false
        if (x < 0 || (x % 10 == 0 && x != 0)) return false;
        int revertedNumber = 0;
        while (x > revertedNumber) {
            revertedNumber = revertedNumber * 10 + x % 10;
            x /= 10;
        }
        return x == revertedNumber || x == revertedNumber / 10;
    }

    /**
     * 相同概率问题
     * 给一个函数，返回 0 和 1，概率为 p 和 1-p，请你实现一个函数，使得返回 0 1 概率一样
     * 定义函数，返回0概率为p;返回1概率为1-p
     */
    public int getZeroOrOne() {
        if (true) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 生成0的概率为p，生成1的概率为1-p。
     * 生成0 1 概率为p(1-p)
     * 生成1 0概率为(1-p)p
     * 则相等的
     */
    public int getZeroOrOneSameProbability() {
        while (true) {
            int i = getZeroOrOne();
            int j = getZeroOrOne();

            if (i == 0 && j == 1) {
                return 1;
            }

            if (i == 1 && j == 0) {
                return 0;
            }
        }
    }
}
