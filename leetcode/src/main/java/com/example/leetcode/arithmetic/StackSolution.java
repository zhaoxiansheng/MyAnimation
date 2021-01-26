package com.example.leetcode.arithmetic;

import java.util.ArrayList;
import java.util.Stack;

public class StackSolution {

    //逆波兰表达式
    //遇到数字就进栈，遇到符号就把栈顶的两个数字出栈，然后和符号进行运算之后在将结果进栈，最终得到结果
    public static int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();
        for (String token : tokens) {
            try {
                int num = Integer.parseInt(token);
                stack.add(num);
            } catch (Exception e) {
                int b = stack.pop();
                int a = stack.pop();
                stack.add(match(a, b, token));
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
    //从左到右，如果是数字直接输出，如果是符号，则判断和栈顶的优先级，是右括号或优先级低于栈顶，则依次输出，并将当前符号进栈。
    public static ArrayList<String> midToSuffix(String[] tokens) {
        Stack<String> stack = new Stack<>();
        ArrayList<String> stackRpn = new ArrayList<>();
        for (String token : tokens) {
            try {
                int num = Integer.parseInt(token);
                stackRpn.add(token);
                System.out.println(num);
            } catch (Exception e) {
                match(stack, stackRpn, token);
            }
        }
        while (!stack.empty()) {
            String top = stack.pop();
            stackRpn.add(top);
            System.out.println(top);
        }
        return stackRpn;
    }

    private static void match(Stack<String> stack, ArrayList<String> targetStack, String operator) {
        if (stack.empty()) {
            stack.push(operator);
            return;
        }
        switch (operator) {
            case "+":
            case "-":
                if ("*".equals(stack.peek()) || "/".equals(stack.peek())) {
                    while (!stack.empty() && !"(".equals(stack.peek())) {
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
                while (!stack.empty() && !"(".equals(stack.peek())) {
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
