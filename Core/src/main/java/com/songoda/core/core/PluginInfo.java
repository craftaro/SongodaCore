package com.songoda.core.core;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.verification.ProductVerificationStatus;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PluginInfo {
    protected final JavaPlugin javaPlugin;
    protected final int songodaId;
    protected final String coreIcon;
    protected final CompatibleMaterial icon;
    protected final String coreLibraryVersion;
    public final ProductVerificationStatus verificationStatus;

    private final List<PluginInfoModule> modules = new ArrayList<>();
    private boolean hasUpdate = false;
    private String latestVersion;
    private String notification;
    private String changeLog;
    private String marketplaceLink;
    private JSONObject json;

    public PluginInfo(JavaPlugin javaPlugin, int songodaId, String icon, String coreLibraryVersion, ProductVerificationStatus verificationStatus) {
        this.javaPlugin = javaPlugin;
        this.songodaId = songodaId;
        this.coreIcon = icon;
        this.icon = CompatibleMaterial.getMaterial(icon);
        this.coreLibraryVersion = coreLibraryVersion;
        this.verificationStatus = verificationStatus;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;

        hasUpdate = latestVersion != null && !latestVersion.isEmpty() && !javaPlugin.getDescription().getVersion().equalsIgnoreCase(latestVersion);
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public boolean hasUpdate() {
        return hasUpdate;
    }

    public void setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
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

    public String getCoreIcon() {
        return coreIcon;
    }

    public String getCoreLibraryVersion() {
        return coreLibraryVersion;
    }
}
