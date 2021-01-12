package com.example.leetcode.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class StackSolution {

    //逆波兰表达式
    public static int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < tokens.length; i++) {
            try {
                int num = Integer.parseInt(tokens[i]);
                stack.add(num);
            } catch (Exception e) {
                int b = stack.pop();
                int a = stack.pop();
                stack.add(match(a, b, tokens[i]));
            }
        }
        return stack.pop();
    }

    private static int match(int a, int b, String operator) {
        switch (operator) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return a / b;
            default:
                return 0;
        }
    }

    //中缀表达式转后缀表达式
    public static ArrayList<String> midToSuffix(String[] tokens) {
        Stack<String> stack = new Stack<>();
        ArrayList<String> stackRPN = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            try {
                int num = Integer.parseInt(tokens[i]);
                stackRPN.add(tokens[i]);
                System.out.println(num);
            } catch (Exception e) {
                match(stack, stackRPN, tokens[i]);
            }
        }
        while (!stack.empty()) {
            String top = stack.pop();
            stackRPN.add(top);
            System.out.println(top);
        }
        return stackRPN;
    }

    private static void match(Stack<String> stack, ArrayList<String> targetStack, String operator) {
        if (stack.empty()) {
            stack.push(operator);
            return;
        }
        switch (operator) {
            case "+":
                if (stack.peek().equals("*") || stack.peek().equals("/")) {
                    while (!stack.empty() && !stack.peek().equals("(")) {
                        String top = stack.pop();
                        targetStack.add(top);
                        System.out.println(top);
                    }
                }
                stack.push(operator);
                break;
            case "-":
                if (stack.peek().equals("*") || stack.peek().equals("/")) {
                    while (!stack.empty() && !stack.peek().equals("(")) {
                        String top = stack.pop();
                        targetStack.add(top);
                        System.out.println(top);
                    }
                }
                stack.push(operator);
                break;
            case "*":
                stack.push(operator);
                break;
            case "/":
                stack.push(operator);
                break;
            case "(":
                stack.push(operator);
                break;
            case ")":
                while (!stack.empty() && !stack.peek().equals("(")) {
                    String top = stack.pop();
                    targetStack.add(top);
                    System.out.println(top);
                }
                if (!stack.empty()) {
                    stack.pop();
                }
                break;
        }
    }
}
