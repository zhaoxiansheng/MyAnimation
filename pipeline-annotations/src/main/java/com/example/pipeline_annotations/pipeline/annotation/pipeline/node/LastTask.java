package com.example.pipeline_annotations.pipeline.annotation.pipeline.node;

import com.example.pipeline_annotations.pipeline.annotation.pipeline.base.BasePipelineTask;
import com.example.pipeline_annotations.pipeline.annotation.pipeline.base.PipelineNode;
import com.example.pipeline_annotations.pipeline.annotation.pipeline.factory.PipelineFactory;

public class LastTask<T> extends BasePipelineTask<T> {

    @Override
    public void process(PipelineNode ctx, T o) {
        PipelineFactory.getInstance().setCurrentNode(null);
        PipelineFactory.getInstance().setFence(0);
        PipelineFactory.getInstance().setMsg(null);
        System.out.println(LastTask.class.toString() + ",  " + sceneStates);
    }

    public LastTask(int state, int pipelineType) {
        super(state, pipelineType);
    }
}
