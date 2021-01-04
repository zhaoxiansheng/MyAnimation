package com.example.pipeline_annotations.pipeline.annotation.pipeline.base;

public abstract class BasePipelineTask<T> implements Pipeline<T> {

    public int sceneStates;

    public int pipelineType;

    @Override
    public abstract void process(PipelineNode nextNode, T o);

    public BasePipelineTask(int state, int pipelineType) {
        this.sceneStates = state;
        this.pipelineType  = pipelineType;
    }
}
