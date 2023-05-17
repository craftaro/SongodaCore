package com.songoda;

import com.songoda.core.compatibility.ServerVersion;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class SchedulerUtils {


    /**
     * Run a task on an entity or fallback for Bukkit#runTask
     */
    public static SchedulerTask runEntityTask(@NotNull Plugin plugin, @NotNull Entity entity, @NotNull Runnable runnable) {
        if (ServerVersion.isFolia()) {
            return new SchedulerTask(runEntityTask(plugin, entity, runnable, 1));
        }
        return new SchedulerTask(Bukkit.getScheduler().runTask(plugin, runnable));
    }

    public static SchedulerTask runEntityTask(@NotNull Plugin plugin, @NotNull Entity entity, @NotNull Runnable runnable, long delay) {
        if (ServerVersion.isFolia()) {
            return new SchedulerTask(entity.getScheduler().runDelayed(plugin, scheduledTask -> runnable.run(), null,  delay));
        }
        return new SchedulerTask(Bukkit.getScheduler().runTask(plugin, runnable));
    }

    //location task

    public static SchedulerTask runLocationTask(@NotNull Plugin plugin, @NotNull Location location, @NotNull Runnable runnable) {
        if (ServerVersion.isFolia()) {
            return new SchedulerTask(runLocationTask(plugin, location, runnable, 1));
        }
        return new SchedulerTask(Bukkit.getScheduler().runTask(plugin, runnable));
    }

    public static SchedulerTask runEntityTask(@NotNull Plugin plugin, @NotNull Entity entity, @NotNull Runnable runnable, long delay, long period) {
        if (ServerVersion.isFolia()) {
            return new SchedulerTask(entity.getScheduler().runAtFixedRate(plugin, scheduledTask -> runnable.run(), null, delay, period));
        }
        return new SchedulerTask(Bukkit.getScheduler().runTask(plugin, runnable));
    }

    /**
     * Run a task on a location or fallback for Bukkit#runTask
     * Delay is in ticks
     */
    public static SchedulerTask runLocationTask(@NotNull Plugin plugin, @NotNull Location location, @NotNull Runnable runnable, long delay) {
        if (ServerVersion.isFolia()) {
            return new SchedulerTask(plugin.getServer().getRegionScheduler().runDelayed(plugin, location, scheduledTask -> runnable.run(), delay));
        }
        return new SchedulerTask(Bukkit.getScheduler().runTask(plugin, runnable));
    }

    public static SchedulerTask runLocationTask(@NotNull Plugin plugin, @NotNull Location location, @NotNull Runnable runnable, long delay, long period) {
        if (ServerVersion.isFolia()) {
            return new SchedulerTask(plugin.getServer().getRegionScheduler().runAtFixedRate(plugin, location, scheduledTask -> runnable.run(), delay, period));
        }
        return new SchedulerTask(Bukkit.getScheduler().runTask(plugin, runnable));
    }

    public static SchedulerTask runTask(@NotNull Plugin plugin, @NotNull Runnable runnable) {
        if (ServerVersion.isFolia()) {
            return new SchedulerTask(plugin.getServer().getGlobalRegionScheduler().run(plugin, scheduledTask -> runnable.run()));
        }
        return new SchedulerTask(Bukkit.getScheduler().runTask(plugin, runnable));
    }

    public static SchedulerTask runTaskAsynchronously(@NotNull Plugin plugin, @NotNull Runnable runnable) {
        if (ServerVersion.isFolia()) {
            return new SchedulerTask(plugin.getServer().getAsyncScheduler().runNow(plugin, scheduledTask -> runnable.run()));
        }
        return new SchedulerTask(Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable));
    }

    public static SchedulerTask runTaskLater(@NotNull Plugin plugin, @NotNull Runnable runnable, long delay) {
        if (ServerVersion.isFolia()) {
            return new SchedulerTask(plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin, scheduledTask -> runnable.run(), delay));
        }
        return new SchedulerTask(Bukkit.getScheduler().runTaskLater(plugin, runnable, delay));
    }

    public static SchedulerTask runTaskLaterAsynchronously(@NotNull Plugin plugin, @NotNull Runnable runnable, long delay) {
        if (ServerVersion.isFolia()) {
            return new SchedulerTask(plugin.getServer().getAsyncScheduler().runDelayed(plugin, scheduledTask -> runnable.run(), delay, TimeUnit.MILLISECONDS));
        }
        return new SchedulerTask(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay));
    }

    public static SchedulerTask scheduleSyncDelayedTask(@NotNull Plugin plugin, @NotNull Runnable runnable, long delay) {
        if (ServerVersion.isFolia()) {
            return new SchedulerTask(plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin, scheduledTask -> runnable.run(), delay));
        }
        return new SchedulerTask(Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay));
    }

    public static SchedulerTask scheduleSyncRepeatingTask(@NotNull Plugin plugin, @NotNull Runnable runnable, long delay, long period) {
        if (ServerVersion.isFolia()) {
            return new SchedulerTask(plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTask -> runnable.run(), delay, period));
        }
        return new SchedulerTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, period));
    }

    public static SchedulerTask runTaskTimer(@NotNull Plugin plugin, @NotNull Runnable runnable, long delay, long period) {
        if (ServerVersion.isFolia()) {
            return new SchedulerTask(plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTask -> runnable.run(), delay, period));
        }
        return new SchedulerTask(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period));
    }

    public static SchedulerTask runTaskTimerAsynchronously(@NotNull Plugin plugin, @NotNull Runnable runnable, long delay, long period) {
        if (ServerVersion.isFolia()) {
            return new SchedulerTask(plugin.getServer().getAsyncScheduler().runAtFixedRate(plugin, scheduledTask -> runnable.run(), delay, period, TimeUnit.MILLISECONDS));
        }
        return new SchedulerTask(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period));
    }

    public static void cancelTask(SchedulerTask task) {
        if (ServerVersion.isFolia()) {
            task.getAsFoliaTask().cancel();
        } else {
            Bukkit.getScheduler().cancelTask(task.getAsBukkitTaskId());
        }
    }

    public static void cancelAllTasks(Plugin plugin) {
        if (ServerVersion.isFolia()) {
            Bukkit.getAsyncScheduler().cancelTasks(plugin);
            Bukkit.getGlobalRegionScheduler().cancelTasks(plugin);
        } else {
            Bukkit.getScheduler().cancelTasks(plugin);
        }
    }

    public static boolean isCurrentlyRunning(SchedulerTask task) {
        if (ServerVersion.isFolia()) {
            return task.getAsFoliaTask().getExecutionState() == ScheduledTask.ExecutionState.RUNNING;
        } else {
            return Bukkit.getScheduler().isCurrentlyRunning(task.getAsBukkitTaskId());
        }
    }

    public static boolean isQueued(SchedulerTask task) {
        if (ServerVersion.isFolia()) {
            return task.getAsFoliaTask().getExecutionState() == ScheduledTask.ExecutionState.RUNNING;
        } else {
            return Bukkit.getScheduler().isQueued(task.getAsBukkitTaskId());
        }
    }
}
