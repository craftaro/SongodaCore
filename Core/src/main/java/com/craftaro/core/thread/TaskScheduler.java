package com.craftaro.core.thread;

import com.craftaro.core.SongodaPlugin;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TaskScheduler {

    private final SongodaPlugin plugin;
    private final Map<TaskWrapper, Long> tasks = new ConcurrentHashMap<>();
    private BukkitRunnable runnable;

    public TaskScheduler(SongodaPlugin plugin) {
        this.plugin = plugin;
    }

    private void startScheduler() {
        if (runnable == null || runnable.isCancelled()) {
            runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    executeTasks();
                }
            };
            runnable.runTaskTimerAsynchronously(plugin, 20L, 20L);
        }
    }

    private void stopScheduler() {
        if (runnable != null && !runnable.isCancelled()) {
            runnable.cancel();
        }
    }

    private void executeTasks() {
        if (tasks.isEmpty()) {
            stopScheduler();
            return;
        }

        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<TaskWrapper, Long>> iterator = tasks.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<TaskWrapper, Long> entry = iterator.next();
            if (entry.getValue() <= currentTime) {
                TaskWrapper taskWrapper = entry.getKey();
                if (taskWrapper.isAsync()) {
                    // Run the task asynchronously
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            taskWrapper.getTask().run();
                        }
                    }.runTaskAsynchronously(plugin);
                } else {
                    // Run the task synchronously
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            taskWrapper.getTask().run();
                        }
                    }.runTask(plugin);
                }
                iterator.remove();
            }
        }
    }

    public synchronized void addTask(Runnable task, long delay, boolean async) {
        tasks.put(new TaskWrapper(task, async), System.currentTimeMillis() + delay);
        startScheduler();
    }

    public synchronized void addTask(Runnable task, long delay) {
        addTask(task, delay, false);
    }

    private static class TaskWrapper {
        private final Runnable task;
        private final boolean async;

        public TaskWrapper(Runnable task, boolean async) {
            this.task = task;
            this.async = async;
        }

        public Runnable getTask() {
            return task;
        }

        public boolean isAsync() {
            return async;
        }
    }
}
