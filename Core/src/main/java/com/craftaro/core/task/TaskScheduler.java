package com.craftaro.ultimatestacker.tasks;

import com.craftaro.core.SongodaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TaskScheduler {
    private final SongodaPlugin plugin;
    private final Map<Runnable, Long> tasks = new ConcurrentHashMap<>();
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
        Iterator<Map.Entry<Runnable, Long>> iterator = tasks.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Runnable, Long> entry = iterator.next();
            if (entry.getValue() <= currentTime) {
                entry.getKey().run();
                iterator.remove();
            }
        }
    }

    public synchronized void addTask(Runnable task, long delay) {
        tasks.put(task, System.currentTimeMillis() + delay);
        startScheduler();
    }
}
