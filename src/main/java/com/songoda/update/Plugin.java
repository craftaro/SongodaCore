package com.songoda.update;

import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Plugin {

    private JavaPlugin javaPlugin;
    private int songodaId;
    private List<Module> modules = new ArrayList<>();
    private String latestVersion;
    private String notification;
    private JSONObject json;

    public Plugin(JavaPlugin javaPlugin, int songodaId) {
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

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public Module addModule(Module module) {
        modules.add(module);
        return module;
    }

    public List<Module> getModules() {
        return new ArrayList<>(modules);
    }

    public JavaPlugin getJavaPlugin() {
        return javaPlugin;
    }

    public int getSongodaId() {
        return songodaId;
    }
}
