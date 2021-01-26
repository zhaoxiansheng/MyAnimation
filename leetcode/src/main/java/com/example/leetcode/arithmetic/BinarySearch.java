package com.example.leetcode.arithmetic;

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
}
