package com.songoda.core.hooks.jobs;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;

import java.util.List;
import java.util.stream.Collectors;

public class JobsHandler {
    public static List<String> getJobs() {
        return Jobs.getJobs().stream().map(Job::getName).collect(Collectors.toList());
    }
}
