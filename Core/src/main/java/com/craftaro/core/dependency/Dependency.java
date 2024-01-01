package com.craftaro.core.dependency;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Dependency {

    private final String repositoryUrl;
    private final String groupId;
    private final String artifactId;
    private final String version;
    private boolean relocate;
    private Relocation relocation;

    public Dependency(String repositoryUrl, String groupId, String artifactId, String version) {
        this.repositoryUrl = repositoryUrl;
        this.groupId = groupId.replaceAll(";", ".");
        this.artifactId = artifactId;
        this.version = version;
        this.relocate = true;
    }

    public Dependency(String repositoryUrl, String groupId, String artifactId, String version, boolean relocate) {
        this.repositoryUrl = repositoryUrl;
        this.groupId = groupId.replaceAll(";", ".");
        this.artifactId = artifactId;
        this.version = version;
    }

    public Dependency(String repositoryUrl, String groupId, String artifactId, String version, @Nullable Relocation relocation) {
        this.repositoryUrl = repositoryUrl;
        this.groupId = groupId.replaceAll(";", ".");
        this.artifactId = artifactId;
        this.version = version;
        this.relocation = relocation;
        this.relocate = true;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public Relocation getRelocation() {
        return relocation;
    }

    public boolean relocate() {
        return relocate;
    }
}
