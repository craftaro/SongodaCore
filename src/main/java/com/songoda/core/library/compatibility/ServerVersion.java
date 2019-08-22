package com.songoda.core.library.compatibility;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;

public enum ServerVersion {

    UNKNOWN, V1_7, V1_8, V1_9, V1_10, V1_11, V1_12, V1_13, V1_14, V1_15, V1_16, V1_17, V1_18, V1_19, V1_20;

    private final static String serverPackagePath = Bukkit.getServer().getClass().getPackage().getName().toUpperCase();
    private final static String serverPackageVersion = serverPackagePath.substring(serverPackagePath.lastIndexOf('.') + 1);
    private static ServerVersion serverVersion = UNKNOWN;
    static {
        for (ServerVersion version : values())
            if (serverPackageVersion.startsWith(version.name()))
                serverVersion = version;
    }

    public static String getServerVersionString() {
        return serverPackageVersion;
    }

    public static ServerVersion getServerVersion() {
        return serverVersion;
    }

    public static boolean isServerVersion(ServerVersion version) {
        return serverVersion == version;
    }

    public static boolean isServerVersion(ServerVersion... versions) {
        return ArrayUtils.contains(versions, serverVersion);
    }

    public static boolean isServerVersionAtLeast(ServerVersion version) {
        return serverVersion.ordinal() >= version.ordinal();
    }

    public static boolean isServerVersionBelow(ServerVersion version) {
        return serverVersion.ordinal() < version.ordinal();
    }
}