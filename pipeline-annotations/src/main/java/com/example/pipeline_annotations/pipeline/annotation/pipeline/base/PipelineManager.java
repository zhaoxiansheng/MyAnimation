package com.example.pipeline_annotations.pipeline.annotation.pipeline.base;

public interface PipelineManager<T> {

    void add(PipelineNode<T> node);

    void remove(PipelineNode<T> node);

    void submitWork(T work);
}
