package com.example.pipeline_annotations.pipeline.annotation.pipeline.base;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class PipelineNode<T> {

    private BasePipelineTask<T> task;
    public PipelineNode next;

    public PipelineNode(BasePipelineTask<T> task) {
        this.task = task;
    }

    public PipelineNode(String className, int sceneState, int pipelineType) {
        try {
            Class<?>  cls = Class.forName(className);
            Class<?> clazz = cls.getClassLoader().loadClass(className);
            Constructor constructor = clazz.getConstructor(int.class, int.class);
            this.task = (BasePipelineTask<T>) constructor.newInstance(sceneState, pipelineType);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public abstract void perSubmit(T msg);

    public void submit(T msg) {
        perSubmit(msg);
    }

    public BasePipelineTask<T> getTask() {
        return task;
    }
}

