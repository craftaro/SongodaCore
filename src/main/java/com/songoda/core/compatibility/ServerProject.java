package com.songoda.core.compatibility;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;

public enum ServerProject {

    UNKNOWN, CRAFTBUKKIT, SPIGOT, PAPER, GLOWSTONE;
    private static ServerProject serverProject = checkProject();

    private static ServerProject checkProject() {
        String serverPath = Bukkit.getServer().getClass().getName();
        if (serverPath.contains("glowstone")) {
            return GLOWSTONE;
        }
        // paper used to be called "paperclip"
        try {
            Class.forName("com.destroystokyo.paperclip.Paperclip");
            return PAPER;
        } catch (ClassNotFoundException ex) {
        }
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return PAPER;
        } catch (ClassNotFoundException ex) {
        }
        try {
            Class.forName("org.spigotmc.SpigotConfig");
            return SPIGOT;
        } catch (ClassNotFoundException ex) {
        }
        return serverPath.contains("craftbukkit") ? CRAFTBUKKIT : UNKNOWN;
    }

    public static ServerProject getServerVersion() {
        return serverProject;
    }

    public static boolean isServer(ServerProject version) {
        return serverProject == version;
    }

    public static boolean isServer(ServerVersion... versions) {
        return ArrayUtils.contains(versions, serverProject);
    }

}
