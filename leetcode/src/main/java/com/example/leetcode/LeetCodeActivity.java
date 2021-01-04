package com.example.leetcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class LeetCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leet_code);

        quickSort(mData, 0, mData.length - 1);
        for (int i : mData) {
            Log.d(TAG, "onCreate: value = " + i);
        }
        int key = commonBinarySearch(mData, 7);
        Log.d(TAG, "onCreate: key = " + key);
    }

    //快排
    public void quickSort(int[] arr, int low, int high) {
        int i, j, temp, t;
        if (low > high) {
            return;
        }
        i = low;
        j = high;
        //temp就是基准位
        temp = arr[low];

        while (i < j) {
            //先看右边，依次往左递减
            while (temp <= arr[j] && i < j) {
                j--;
            }
            //再看左边，依次往右递增
            while (temp >= arr[i] && i < j) {
                i++;
            }
            //如果满足条件则交换
            if (i < j) {
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }
        }
        //最后将基准为与i和j相等位置的数字交换
        arr[low] = arr[i];
        arr[i] = temp;
        System.out.println(" sort: ");
        for (int a = 0; a < arr.length; a++) {
            System.out.print(arr[a] + ", ");
        }
        //递归调用左半数组
        quickSort(arr, low, j - 1);
        //递归调用右半数组
        quickSort(arr, j + 1, high);
    }

    //二分查找
    private int commonBinarySearch(int[] array, int key) {
        if (array.length < 0) {
            return -1;
        }

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