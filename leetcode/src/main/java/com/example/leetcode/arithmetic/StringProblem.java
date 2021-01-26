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
}
