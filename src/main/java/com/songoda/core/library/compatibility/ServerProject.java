package com.songoda.core.library.compatibility;

import org.bukkit.Bukkit;

/**
 * TODO
 */
public enum ServerProject {
    
    CRAFTBUKKIT, SPIGOT, PAPER;
    private final static String serverPackage = Bukkit.getServer().getClass().getPackage().getName();
    
}
