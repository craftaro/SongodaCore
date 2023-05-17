package com.songoda;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

public class SchedulerTask {

    private final Object task;

    public SchedulerTask(Object task) {
        this.task = task;
    }

    public Object getTask() {
        return task;
    }

    public ScheduledTask getAsFoliaTask() {
        return (ScheduledTask) task;
    }

    public int getAsBukkitTaskId() {
        return (int) task;
    }
}
