package com.example.pipeline_annotations.pipeline.annotation.pipeline.base;


public abstract class BasePipelineManager<T> implements PipelineManager<T> {

    private PipelineState mPipelineState;

    private PipelineNode<T> mCurrentNode;

    private int mFence = 0;

    private Object msg;

    private String mName = "base";

    private int[] currentEndSize;
    private int[] followEndSize;

    @Override
    public abstract void add(PipelineNode<T> node);

    @Override
    public abstract void remove(PipelineNode<T> node);

    @Override
    public abstract void submitWork(T work);

    public BasePipelineManager(PipelineState pipelineState) {
        mPipelineState = pipelineState;
    }

    public BasePipelineManager(PipelineState pipelineState, String name) {
        mPipelineState = pipelineState;
        this.mName = name;
    }

    public PipelineState getPipelineState() {
        return mPipelineState;
    }

    public String getName() {
        return mName;
    }

    public void setCurrentNode(PipelineNode<T> currentNode) {
        mCurrentNode = currentNode;
    }

    public PipelineNode<T> getCurrentNode() {
        return mCurrentNode;
    }

    public void addFence() {
        mFence++;
    }

    public void subtractFence() {
        mFence--;
    }

    public int currentFence() {
        return mFence;
    }

    public void setFence(int mFence) {
        this.mFence = mFence;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public Object getMsg() {
        return msg;
    }

    public int[] getCurrentEndSize() {
        return currentEndSize;
    }

    public void setCurrentEndSize(int[] currentEndSize) {
        this.currentEndSize = currentEndSize;
    }

    public int[] getFollowEndSize() {
        return followEndSize;
    }

    public void setFollowEndSize(int[] followEndSize) {
        this.followEndSize = followEndSize;
    }
}
