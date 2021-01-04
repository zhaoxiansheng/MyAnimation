package com.autopai.common.utils.thread.threadpool.utils;

import android.util.ArrayMap;
import android.util.Log;

import com.autopai.common.utils.thread.WorkThread;
import com.autopai.common.utils.thread.threadpool.utils.DispatchThreadPoolBase.ScheduleTask;
import com.autopai.common.utils.thread.threadpool.utils.IDispatchWorkQueue.Work;

public class Scheduler extends WorkThread {
    private static final String TAG = "Scheduler";

    public void schedule(ScheduleTask task) {
        Work work = task.getWork();
        synchronized (work) {
            this.postDelayed(task, work.mDelay);
        }
    }

    public void scheduleAtFront(ScheduleTask task) {
        Work work = task.getWork();
        synchronized (work) {
            this.postAtFrontOfQueue(task);
        }
    }

    public void unschedule(ScheduleTask task) {
        if(task != null) {
            Work work = task.getWork();
            synchronized (work) {
                work.cancel();
                super.remove(task);
            }
        }
    }

    protected void finishSchedule(ScheduleTask task) {

    }
}
