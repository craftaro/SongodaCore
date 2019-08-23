package com.songoda.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PluginInfo {

    private final JavaPlugin javaPlugin;
    private final int songodaId;
    private final List<PluginInfoModule> modules = new ArrayList<>();
    private String latestVersion;
    private String notification;
    private String changeLog;
    private String marketplaceLink;
    private JSONObject json;

    protected PluginInfo(JavaPlugin javaPlugin, int songodaId) {
        this.javaPlugin = javaPlugin;
        this.songodaId = songodaId;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public String getMarketplaceLink() {
        return marketplaceLink;
    }

    public void setMarketplaceLink(String marketplaceLink) {
        this.marketplaceLink = marketplaceLink;
    }

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public PluginInfoModule addModule(PluginInfoModule module) {
        modules.add(module);
        return module;
    }

    public List<PluginInfoModule> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public JavaPlugin getJavaPlugin() {
        return javaPlugin;
    }

    public int getSongodaId() {
        return songodaId;
    }
}
