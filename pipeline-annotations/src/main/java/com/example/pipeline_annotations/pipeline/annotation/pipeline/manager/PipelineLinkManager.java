package com.example.pipeline_annotations.pipeline.annotation.pipeline.manager;

import com.example.pipeline_annotations.pipeline.annotation.PipelineNodeType;
import com.example.pipeline_annotations.pipeline.annotation.SceneState;
import com.example.pipeline_annotations.pipeline.annotation.pipeline.base.BasePipelineManager;
import com.example.pipeline_annotations.pipeline.annotation.pipeline.base.PipelineNode;
import com.example.pipeline_annotations.pipeline.annotation.pipeline.base.PipelineState;
import com.example.pipeline_annotations.pipeline.annotation.pipeline.node.LastNode;
import com.example.pipeline_annotations.pipeline.annotation.pipeline.node.LastTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @param <T>
 */
public class PipelineLinkManager<T> extends BasePipelineManager<T> {

    private static final String TAG = PipelineLinkManager.class.getSimpleName();

    private PipelineNode<T> head;
    private PipelineNode<T> tail;

    private ArrayList<PipelineNode<T>> mNodes;

    private boolean isLinked = false;

    @Override
    public void add(PipelineNode<T> node) {
        mNodes.add(node);
    }

    @Override
    public void remove(PipelineNode<T> node) {
        mNodes.remove(node);
    }

    @Override
    public void submitWork(T work) {
        if (!isLinked) {
            makeLinked();
        }
        head.submit(work);
    }

    private void makeLinked() {
        sortTask();
        for (int i = 0; i < mNodes.size(); i++) {
            addFirst(mNodes.get(i));
        }
        isLinked = true;
    }

    private void sortTask() {
        Collections.sort(mNodes, new Comparator<PipelineNode>() {
            @Override
            public int compare(PipelineNode o1, PipelineNode o2) {
                return o2.getTask().sceneStates - o1.getTask().sceneStates;
            }
        });
    }

    /**
     *
     * @param node
     */
    private void addFirst(PipelineNode<T> node) {
        if (head != null) {
            PipelineNode tmp = head;
            head = node;
            head.next = tmp;
        } else {
            head = tail = node;
        }
    }

    public PipelineLinkManager(PipelineState pipelineState) {
        super(pipelineState);
        head = tail = new LastNode<T>(new LastTask<T>(SceneState.RESET, PipelineNodeType.NORMAl_NODE));
        mNodes = new ArrayList<>();
    }

    public PipelineLinkManager(PipelineState mPipelineState, String name) {
        super(mPipelineState, name);
        head = tail = new LastNode<T>(new LastTask<T>(SceneState.RESET, PipelineNodeType.NORMAl_NODE));
        mNodes = new ArrayList<>();
    }
}
