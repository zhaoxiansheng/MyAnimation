package com.example.pipeline_annotations.pipeline.annotation.pipeline.manager;

import com.example.pipeline_annotations.pipeline.annotation.pipeline.base.BasePipelineManager;
import com.example.pipeline_annotations.pipeline.annotation.pipeline.base.PipelineNode;
import com.example.pipeline_annotations.pipeline.annotation.pipeline.base.PipelineState;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @param <T>
 */
// TODO: 2020/9/7 使用PriorityQueue
public class PipelineHeapManager<T> extends BasePipelineManager<T> {

    private static final String TAG = PipelineLinkManager.class.getSimpleName();

    private PriorityQueue<PipelineNode<T>> mQueue;
    private static final int SIZE = 20;

    private Comparator<PipelineNode> sceneComparator;

    @Override
    public void add(PipelineNode<T> node) {
        mQueue.add(node);
    }

    @Override
    public void remove(PipelineNode<T> node) {
        mQueue.remove(node);
    }

    @Override
    public void submitWork(T work) {
        mQueue.poll();
    }

    private void initComparator() {
        sceneComparator = new Comparator<PipelineNode>() {
            @Override
            public int compare(PipelineNode o1, PipelineNode o2) {
                return o2.getTask().sceneStates - o1.getTask().sceneStates;

            }
        };
    }

    public PipelineHeapManager(PipelineState pipelineState) {
        super(pipelineState);
        initComparator();
        mQueue = new PriorityQueue<>(SIZE, sceneComparator);
    }

    public PipelineHeapManager(PipelineState mPipelineState, String name) {
        super(mPipelineState, name);
        initComparator();
        mQueue = new PriorityQueue<>(SIZE, sceneComparator);
    }
}
