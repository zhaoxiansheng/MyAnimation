package com.example.leetcode.arithmetic;

import java.util.HashMap;

// 1、请你设计一个词典类，支持添加新单词和查找字符串。
// 实现类WordDictionary ：
// void addWord(word) 将 word 添加到数据结构中，之后可以对它进行匹配
// bool search(word) 如果数据结构中存在字符串与 word 匹配，则返回 true ；否则，返回  false。'.' 作为模糊匹配，能且仅能匹配一个字符，比如'a..'可以匹配'abc'
// addWord("bad");
// addWord("dad");
// addWord("mad");
// search("pad"); // return False
// search("bad"); // return True
// search(".ad"); // return True
// search("b.."); // return True


class WordDictionary {

    private class Node {
        private boolean isWord;
        private HashMap<Character, Node> next;

        public Node() {
            isWord = false;
            next = new HashMap<>();
        }
    }

    private Node root;

    public WordDictionary() {
        root = new Node();
    }

    public void addWord(String word) {
        Node cur = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (cur.next.get(c) == null) {
                cur.next.put(c, new Node());
            }
            cur = cur.next.get(c);
        }
        if (!cur.isWord) {
            cur.isWord = true;
        }
    }

    public boolean search(String word) {
        return match(root, word, 0);
    }

    private boolean match(Node node, String word, int i) {
        if (i == word.length()) {
            return node.isWord;        //不应该直接返回true，这是不是一个单词要看这个节点的isWord属性
        }
        char c = word.charAt(i);
        if (c != '.') {
            if (node.next.get(c) == null) {
                return false;
            }
            return match(node.next.get(c), word, i + 1);
        } else {
            for (char nextChar : node.next.keySet()) {
                if (match(node.next.get(nextChar), word, i + 1)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 给定一个整数数组nums，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
     * 示例:
     * 输入: [-2,1,-3,4,-1,2,1,-5,4],
     * 输出: 6
     * 解释: 连续子数组 [4,-1,2,1] 的和最大为 6。
     *
     * @param a
     * @return
     */
    private int nums(int[] a) throws Exception {
        if (a == null || a.length == 0) {
            throw new Exception("。。。");
        }

        int maxNums = a[0];
        for (int i = 0; i < a.length; i++) {
            int temp = 0;
            temp += a[i];
            for (int j = i + 1; j < a.length; j++) {
                temp += a[j];
                if (temp > maxNums) {
                    maxNums = temp;
                }
            }

        }
        return maxNums;
    }

    private int numss(int[] a) throws Exception {
        if (a == null || a.length == 0) {
            throw new Exception("。。。");
        }

        int maxNums = -1;
        int temp = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > 0 && maxNums == -1) {
                maxNums = a[i];
                temp = maxNums;
            } else {
                if (maxNums != -1) {
                    temp += a[i];
                    if (temp > maxNums) {
                        maxNums = temp;
                    }
                }
            }
        }
        return maxNums;
    }
}

