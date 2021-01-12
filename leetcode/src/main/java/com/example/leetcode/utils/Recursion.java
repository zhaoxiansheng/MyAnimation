package com.example.leetcode.utils;

//递归问题
public class Recursion {

    //斐波那契数列 典型的递归问题
    public static long fibonacci(long number) {
        if ((number == 0) || (number == 1))
            return number;
        else
            return fibonacci(number - 1) + fibonacci(number - 2);
    }
}
