package com.songoda.core.plugins;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginInfo {
    protected final JavaPlugin javaPlugin;
    protected final int songodaId;
    protected final XMaterial iconMaterial;

    private boolean hasUpdate = false;
    private String latestVersion;
    private String notification;
    private String changeLog;
    private String marketplaceLink;

    public PluginInfo(JavaPlugin javaPlugin, int songodaId, String icon) {
        this.javaPlugin = javaPlugin;
        this.songodaId = songodaId;
        this.iconMaterial = XMaterial.matchXMaterial(icon).orElse(XMaterial.PAPER);
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

    public JavaPlugin getJavaPlugin() {
        return javaPlugin;
    }

    public int getSongodaId() {
        return songodaId;
    }
}