package com.craftaro.core.dependency;

import org.jetbrains.annotations.Nullable;

public class Dependency {
    private final String repositoryUrl;
    private final String groupId;
    private final String artifactId;
    private final String version;
    private boolean relocate;
    private final Relocation relocation;

    public Dependency(String repositoryUrl, String groupId, String artifactId, String version) {
        this(repositoryUrl, groupId, artifactId, version, true);
    }

    public Dependency(String repositoryUrl, String groupId, String artifactId, String version, boolean relocate) {
        this(repositoryUrl, groupId, artifactId, version, null);
        this.relocate = relocate;
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
        return this.repositoryUrl;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public String getArtifactId() {
        return this.artifactId;
    }

    public String getVersion() {
        return this.version;
    }

    public Relocation getRelocation() {
        return this.relocation;
    }

    public boolean relocate() {
        return this.relocate;
    }
}
