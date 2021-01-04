package com.example.pipeline_annotations.pipeline.annotation.pipeline.factory;

import com.example.pipeline_annotations.pipeline.annotation.pipeline.base.BasePipelineManager;
import com.example.pipeline_annotations.pipeline.annotation.pipeline.base.PipelineState;
import com.example.pipeline_annotations.pipeline.annotation.pipeline.manager.PipelineHeapManager;
import com.example.pipeline_annotations.pipeline.annotation.pipeline.manager.PipelineLinkManager;

public class PipelineFactory<T> {

    private static BasePipelineManager mManager;

    private static PipelineState mState = PipelineState.LINK;

    private PipelineFactory() {
    }

    public static BasePipelineManager getInstance() {
        if (mManager == null) {
            mManager = newInstancePipeline(mState);
        }
        return mManager;
    }

    public static BasePipelineManager getInstance(PipelineState state) {
        if (mManager == null) {
            if (state != null) {
                mManager = newInstancePipeline(state);
                mState = state;
            } else {
                mManager = newInstancePipeline(mState);
            }
        } else {
            if (mState != state && state != null) {
                mManager = newInstancePipeline(state);
                mState = state;
            }
        }
        return mManager;
    }

    public static <T> BasePipelineManager newInstancePipeline(PipelineState state) {
        BasePipelineManager pipelineManager;
        if (state.equals(PipelineState.LINK)) {
            pipelineManager = new PipelineLinkManager<T>(state);
        } else {
            pipelineManager = new PipelineHeapManager<T>(state);
        }
        return pipelineManager;
    }
}
