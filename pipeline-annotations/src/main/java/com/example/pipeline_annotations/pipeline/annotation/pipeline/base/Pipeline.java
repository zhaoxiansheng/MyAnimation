package com.example.pipeline_annotations.pipeline.annotation.pipeline.base;

/**
 * 执行接口
 * @param <T>
 */
public interface Pipeline<T> {
    void process(PipelineNode ctx, T t);
}
