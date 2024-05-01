package com.craftaro.core.compatibility;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public enum ServerVersion {
    UNKNOWN, V1_7, V1_8, V1_9, V1_10, V1_11, V1_12, V1_13, V1_14, V1_15, V1_16, V1_17, V1_18, V1_19, V1_20, V1_21, V1_22, V1_23;

    private static final String serverPackageVersion;
    private static final String serverReleaseVersion;
    private static final ServerVersion serverVersion;
    private static final boolean isMocked;

    private static final Map<String, String> VERSION_TO_REVISION;

    static {
        VERSION_TO_REVISION = new HashMap<>();
        VERSION_TO_REVISION.put("1.20", "v1_20_R1");
        VERSION_TO_REVISION.put("1.20.1", "v1_20_R1");
        VERSION_TO_REVISION.put("1.20.2", "v1_20_R2");
        VERSION_TO_REVISION.put("1.20.3", "v1_20_R3");
        VERSION_TO_REVISION.put("1.20.4", "v1_20_R3");
        VERSION_TO_REVISION.put("1.20.5", "v1_20_R4");
        VERSION_TO_REVISION.put("1.20.6", "v1_20_R4");

        if (Bukkit.getServer() != null) {
            String srvPackage = Bukkit.getServer().getClass().getPackage().getName();
            isMocked = srvPackage.equals("be.seeseemelk.mockbukkit");

            if (isMocked) {
                serverPackageVersion = "v" + Bukkit.getServer().getBukkitVersion().replace('.', '_') + "_R0";
                serverReleaseVersion = "0";
            } else {
                String nmsVersion;
                try {
                    nmsVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
                } catch (Exception ex) {
                    String minecraftVersion = Bukkit.getServer().getBukkitVersion().split("-")[0];
                    nmsVersion = VERSION_TO_REVISION.getOrDefault(minecraftVersion, "");

                    if (nmsVersion.isEmpty()) {
                        new RuntimeException("Cannot detect NMS version for server version: " + minecraftVersion).printStackTrace();
                    }
                }

                serverPackageVersion = nmsVersion;
                serverReleaseVersion = serverPackageVersion.indexOf('R') != -1 ? serverPackageVersion.substring(serverPackageVersion.indexOf('R') + 1) : "";
            }
        } else {
            serverPackageVersion = "Bukkit-not-initialized";
            serverReleaseVersion = "Bukkit-not-initialized";
            isMocked = false;
        }

        serverVersion = getVersion();
    }

    private static ServerVersion getVersion() {
        for (ServerVersion version : values()) {
            if (serverPackageVersion.toUpperCase().startsWith(version.name())) {
                return version;
            }
        }

        return UNKNOWN;
    }

    public boolean isLessThan(ServerVersion other) {
        if (serverVersion == UNKNOWN) {
            return false;
        }

        return this.ordinal() < other.ordinal();
    }

    public boolean isAtOrBelow(ServerVersion other) {
        if (serverVersion == UNKNOWN && other != UNKNOWN) {
            return false;
        }

        return this.ordinal() <= other.ordinal();
    }

    public boolean isGreaterThan(ServerVersion other) {
        if (serverVersion == UNKNOWN) {
            return false;
        }

        return this.ordinal() > other.ordinal();
    }

    public boolean isAtLeast(ServerVersion other) {
        if (serverVersion == UNKNOWN && other != UNKNOWN) {
            return false;
        }

        return this.ordinal() >= other.ordinal();
    }

    public static String getServerVersionString() {
        return serverPackageVersion;
    }

    public static String getVersionReleaseNumber() {
        return serverReleaseVersion;
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

    public static boolean isServerVersionAbove(ServerVersion version) {
        if (serverVersion == UNKNOWN) {
            return false;
        }

        return serverVersion.ordinal() > version.ordinal();
    }

    public static boolean isServerVersionAtLeast(ServerVersion version) {
        if (serverVersion == UNKNOWN && version != UNKNOWN) {
            return false;
        }

        return serverVersion.ordinal() >= version.ordinal();
    }

    public static boolean isServerVersionAtOrBelow(ServerVersion version) {
        if (serverVersion == UNKNOWN && version != UNKNOWN) {
            return false;
        }

        return serverVersion.ordinal() <= version.ordinal();
    }

    public static boolean isServerVersionBelow(ServerVersion version) {
        if (serverVersion == UNKNOWN) {
            return false;
        }

        return serverVersion.ordinal() < version.ordinal();
    }
}
