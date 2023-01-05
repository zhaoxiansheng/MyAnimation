package com.example.leetcode.arithmetic;

import com.example.leetcode.basic.Tree;

import java.util.ArrayList;
import java.util.HashMap;

//查找
public class BinarySearch {

    //二分查找
    public static int commonBinarySearch(int[] array, int key) {
        int i = 0;
        int j = array.length - 1;
        int mid;

        while (i <= j) {
            mid = (i + j) / 2;
            if (array[mid] == key) {
                return mid;
            }
            if (array[mid] < key) {
                i = mid + 1;
            }
            if (array[mid] > key) {
                j = mid - 1;
            }
        }
        return -1;
    }


//    public static boolean matchString(Tree[][] board, Tree[] match, int x, int y) {
//        if (x == 0 && y == 0) {
//            return false;
//        }
//
//        if (board == null) {
//            return false;
//        }
//
//        if (match == null || match.length == 0) {
//            return false;
//        }
//
//        for (int i = 0; i < x; i++) {
//            for (int j = 0; j < y; j++) {
//                if (match[k].equals(board[i][j].data)) {
//                    board[i][j].isUse = true;
//                }
//            }
//        }
//
//        return true;
//    }

    static Tree[] matchString;
    private static int k = 0;
}
