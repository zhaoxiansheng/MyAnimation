package com.autopai.common.utils.utils.bean;

public class TaskResult<T> {
    public T result;
    public String successMsg;
    public String errorMsg;

    public TaskResult(final T result, final String successMsg, final String errorMsg) {
        this.result = result;
        this.successMsg = successMsg;
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "result: " + result + "\n" +
                "errorMsg: " + errorMsg;
    }
}
