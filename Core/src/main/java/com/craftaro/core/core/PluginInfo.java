package com.craftaro.core.core;

import com.craftaro.core.compatibility.CompatibleMaterial;
import com.craftaro.core.dependency.DependencyLoader;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PluginInfo {
    private final JavaPlugin javaPlugin;
    private final int songodaId;
    private final String coreIcon;
    private final XMaterial icon;
    private final String coreLibraryVersion;

    private final List<PluginInfoModule> modules = new ArrayList<>();
    private boolean hasUpdate = false;
    private String latestVersion;
    private String notification;
    private String changeLog;
    private String marketplaceLink;
    private JSONObject json;

    public PluginInfo(JavaPlugin javaPlugin, int songodaId, String icon, String coreLibraryVersion) {
        this.javaPlugin = javaPlugin;
        this.songodaId = songodaId;
        this.coreIcon = icon;
        this.icon = CompatibleMaterial.getMaterial(icon).orElse(XMaterial.STONE);
        this.coreLibraryVersion = coreLibraryVersion;
    }

    public String getLatestVersion() {
        return this.latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;

        this.hasUpdate = latestVersion != null && !latestVersion.isEmpty() && !this.javaPlugin.getDescription().getVersion().equalsIgnoreCase(latestVersion);
    }

    public String getNotification() {
        return this.notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public boolean hasUpdate() {
        return this.hasUpdate;
    }

    public void setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public String getChangeLog() {
        return this.changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public String getMarketplaceLink() {
        return this.marketplaceLink;
    }

    public void setMarketplaceLink(String marketplaceLink) {
        this.marketplaceLink = marketplaceLink;
    }

    public JSONObject getJson() {
        return this.json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public PluginInfoModule addModule(PluginInfoModule module) {
        this.modules.add(module);

        return module;
    }

    public List<PluginInfoModule> getModules() {
        return Collections.unmodifiableList(this.modules);
    }

    public JavaPlugin getJavaPlugin() {
        return this.javaPlugin;
    }

    public int getSongodaId() {
        return this.songodaId;
    }

    public String getCoreIcon() {
        return this.coreIcon;
    }

    public XMaterial getIcon() {
        return this.icon;
    }

    public String getCoreLibraryVersion() {
        return this.coreLibraryVersion;
    }

    public int getDependencyVersion() {
        return DependencyLoader.getDependencyVersion();
    }
}
