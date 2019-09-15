package com.songoda.core.nms;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

public class NmsManager {

    private final static String serverPackagePath = Bukkit.getServer().getClass().getPackage().getName();
    private final static String serverPackageVersion = serverPackagePath.substring(serverPackagePath.lastIndexOf('.') + 1);
    private final static CoreNMS nms = _getNMS();

    private static CoreNMS _getNMS() {
//        try {
//            return (CoreNMS) Class.forName("com.songoda.core.nms." + serverPackageVersion + ".NMS").newInstance();
//        } catch (Exception ex) {
//            Logger.getLogger(NmsManager.class.getName()).log(Level.SEVERE, "Failed to load NMS for this server version", ex);
//        }
        // this block was only added to keep minimizeJar happy
        switch (serverPackageVersion) {
            case "v1_8_R1":
                return new com.songoda.core.nms.v1_8_R1.NMS();
            case "v1_8_R2":
                return new com.songoda.core.nms.v1_8_R2.NMS();
            case "v1_8_R3":
                return new com.songoda.core.nms.v1_8_R3.NMS();
            case "v1_9_R1":
                return new com.songoda.core.nms.v1_9_R1.NMS();
            case "v1_9_R2":
                return new com.songoda.core.nms.v1_9_R2.NMS();
            case "v1_10_R1":
                return new com.songoda.core.nms.v1_10_R1.NMS();
            case "v1_11_R1":
                return new com.songoda.core.nms.v1_11_R1.NMS();
            case "v1_12_R1":
                return new com.songoda.core.nms.v1_12_R1.NMS();
            case "v1_13_R1":
                return new com.songoda.core.nms.v1_13_R1.NMS();
            case "v1_13_R2":
                return new com.songoda.core.nms.v1_13_R2.NMS();
            case "v1_14_R1":
                return new com.songoda.core.nms.v1_14_R1.NMS();
        }
        Logger.getLogger(NmsManager.class.getName()).log(Level.SEVERE, "Failed to load NMS for this server version: version {0} not found", serverPackageVersion);
        return null;
    }

    public static CoreNMS getNMS() {
        return nms;
    }

    public static boolean hasNMS() {
        return nms != null;
    }
}
