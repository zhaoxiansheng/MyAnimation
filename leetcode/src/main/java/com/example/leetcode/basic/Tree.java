package com.example.leetcode.basic;

public class Tree<T> {

    private Tree<T> mLeftChild;
    private Tree<T> mRightChild;
    private T mData;

    public Tree(T data) {
        this.mData = data;
    }

    public Tree<T> getLeftChild() {
        return mLeftChild;
    }

    public void setLeftChild(Tree<T> mLeftChild) {
        this.mLeftChild = mLeftChild;
    }

    public Tree<T> getRightChild() {
        return mRightChild;
    }

    public void setRightChild(Tree<T> mRightChild) {
        this.mRightChild = mRightChild;
    }

    public T getData() {
        return mData;
    }

    public void setData(T mData) {
        this.mData = mData;
    }
}
