package com.example.leetcode.arithmetic;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//排序
public class Sort {

    //快排
    public static void quickSort(int[] arr, int low, int high) {
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


    /**
     * 两个有序数组排序成一个有序数组
     *
     * @param a
     * @param b
     * @return
     */
    public static int[] sort(int[] a, int[] b) {
        if (a.length == 0 || a == null) {
            return a;
        }

        if (b.length == 0 || b == null) {
            return b;
        }

        int[] c = new int[a.length + b.length];
        int index = -1;
        int aIndex = 0;
        int bIndex = 0;


        while (aIndex < a.length && bIndex < b.length) {
            if (a[aIndex] <= b[bIndex]) {
                c[index++] = a[aIndex];
                aIndex++;
            } else {
                c[index++] = b[bIndex];
                bIndex++;
            }
        }

        if (aIndex == a.length - 1 && bIndex < b.length) {
            while (bIndex < b.length) {
                c[index++] = b[bIndex];
                bIndex++;
            }
        }

        if (bIndex == b.length - 1 && aIndex < a.length) {
            while (aIndex < a.length) {
                c[index++] = a[aIndex];
                aIndex++;
            }
        }
        return c;
    }


//    public static void main(String[] args) throws InterruptedException {
//        int[] x = new int[]{1, 2, 3, 4, 5};
//        int[] y = start(x);
//        for (Integer i : y) {
//            System.out.println(i);
//        }
//    }

    public static int[] start(int[] nums) throws InterruptedException {
        if (nums == null || nums.length == 0) {
            return nums;
        }

        ExecutorService UTIL_POOL = new ThreadPoolExecutor(3, 5,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());

        int i = 0;
        int j = nums.length - 1;

        CountDownLatch countDownLatch = new CountDownLatch(nums.length / 2);

        for (int k = 0; k < nums.length / 2; k++) {
            int finalI = i;
            int finalJ = j;

            UTIL_POOL.submit(new Runnable() {
                @Override
                public void run() {
                    reserve(nums, finalI, finalJ);
                    countDownLatch.countDown();
                }
            });
            i++;
            j--;
        }

        countDownLatch.await();
        return nums;
    }


    public static void reserve(int[] nums, int i, int j) {
        int temp;

        temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public static void main(String[] args) {
        NumberThread number = new NumberThread();
        Thread t1 = new Thread(number, "Printer1");
        Thread t2 = new Thread(number, "Printer2");
        t1.start();
        t2.start();
    }

    static class NumberThread implements Runnable {
        private int i = 0;

        @Override
        public void run() {
            while (true) {
                synchronized (this) {
                    notify();
                    if (i < 100) {
                        i++;
                        System.out.println(Thread.currentThread().getName() + "---" + i);
                    } else {
                        break;
                    }
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
