package com.example.pipeline_annotations.pipeline.annotation.pipeline.node;

import com.example.pipeline_annotations.pipeline.annotation.pipeline.base.BasePipelineTask;
import com.example.pipeline_annotations.pipeline.annotation.pipeline.base.PipelineNode;

public final class LastNode<T> extends PipelineNode<T> {

    public LastNode(BasePipelineTask<T> handler) {
        super(handler);
    }

    @Override
    public void perSubmit(T msg) {
        getTask().process(next, msg);
    }
}
