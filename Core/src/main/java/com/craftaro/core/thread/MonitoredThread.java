package com.craftaro.core.thread;


import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MonitoredThread {

    private final String name;
    private final int timeout;
    private final TimeUnit timeUnit;
    private ScheduledExecutorService executor;
    private Instant started = null;
    private StackTraceElement[] trace = null;
    private boolean nonDisruptable = false;

    public MonitoredThread(String name, int timeout, TimeUnit timeUnit) {
        this.name = name;
        this.timeout = timeout;
        this.timeUnit = timeUnit;

        System.out.println("Thread '" + name + "' was started...");
        start();
    }

    public void execute(Runnable runnable, boolean nonDisruptable) {
        this.nonDisruptable = nonDisruptable;
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        executor.execute(() -> {
            started = Instant.now();
            this.trace = trace;
            try {
                runnable.run();
            } catch (Exception e) {
                StackTraceElement[] newTrace = new StackTraceElement[e.getStackTrace().length + trace.length];
                System.arraycopy(e.getStackTrace(), 0, newTrace, 0, e.getStackTrace().length);
                System.arraycopy(trace, 0, newTrace, e.getStackTrace().length, trace.length);
                e.setStackTrace(newTrace);
                System.out.println("Thread '" + name + "' failed with exception: " + e.getMessage());
                e.printStackTrace();
            }
            started = null;
        });
    }

    public MonitoredThread start() {
        if (executor != null) {
            executor.shutdown();
            System.out.println("Thread '" + name + "' was restarted due to a stall. Stack trace:");
            for (StackTraceElement element : this.trace)
                System.out.println("    " + element.toString());
        }
        executor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, name));
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isStalled() {
        return !nonDisruptable && started != null && started.plusMillis(timeUnit.toMillis(timeout)).isBefore(Instant.now())
                || started != null && started.plusMillis(TimeUnit.HOURS.toMillis(1)).isBefore(Instant.now());
    }

    public boolean isRunning() {
        return started != null;
    }

    public Instant getStarted() {
        return started;
    }

    public ScheduledFuture<?> schedule(Runnable runnable, long delay, TimeUnit timeUnit) {
        return executor.schedule(runnable, delay, timeUnit);
    }

    public void destroy() {
        executor.shutdownNow();
    }
}
