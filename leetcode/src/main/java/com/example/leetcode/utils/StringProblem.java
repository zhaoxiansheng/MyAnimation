package com.example.leetcode.utils;

public class StringProblem {

    // KMP
    public static int KMPSolution(char[] target, char[] match) {
        int i = 0;
        int j = 1;
        int[] next = new int[255];
        getNext(match, match.length, next);
        while (i < target.length && j < match.length) {
            if (j == 0 || target[i] == match[j]) {
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
        next[1] = 0;
        int i = 1, j = 0;
        while (i < matchLen) {
            if (j == 0 || match[i] == match[j]) {
                next[++i] = ++j;
            } else {
                j = next[j];
            }
        }
    }
}
