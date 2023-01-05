package com.example.leetcode.arithmetic;

import com.example.leetcode.basic.Tree;

import java.util.ArrayList;

//树
public class TreeProblem {

    private static int index = 0;

    //树的构建
    public static <T> void growth(Tree<T> root, ArrayList<T> data, int deep) {
        if (data == null || deep == 0) {
            return;
        }

        Tree<T> left = new Tree<>(data.get(index++));
        Tree<T> right = new Tree<>(data.get(index++));

        System.out.println("root: " + root.getData() + ", left :" + left.getData() + ", right: " + right.getData());

        root.setLeftChild(left);
        root.setRightChild(right);

        growth(left, data, deep - 1);
        growth(right, data, deep - 1);
    }

    //前序遍历
    public static <T> void preOrderTraverse(Tree<T> tree) {
        if (tree == null) {
            return;
        }

        System.out.println("preOrderTraverse： " + tree.getData());
        preOrderTraverse(tree.getLeftChild());
        preOrderTraverse(tree.getRightChild());
    }

    //中序遍历
    public static <T> void midOrderTraverse(Tree<T> tree) {
        if (tree == null) {
            return;
        }

        midOrderTraverse(tree.getLeftChild());
        System.out.println("midOrderTraverse： " + tree.getData());
        midOrderTraverse(tree.getRightChild());
    }

    //后序遍历
    public static <T> void postOrderTraverse(Tree<T> tree) {
        if (tree == null) {
            return;
        }

        postOrderTraverse(tree.getLeftChild());
        postOrderTraverse(tree.getRightChild());
        System.out.println("postOrderTraverse： " + tree.getData());
    }
}
