package com.songoda.core.nms;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

public class NmsManager {

    private final static CoreNMS nms = _getNMS();
    private final static String serverPackagePath = Bukkit.getServer().getClass().getPackage().getName();
    private final static String serverPackageVersion = serverPackagePath.substring(serverPackagePath.lastIndexOf('.') + 1);

    private static CoreNMS _getNMS() {
        CoreNMS result = null;
        try {
            result = (CoreNMS) Class.forName("com.songoda.core.nms" + serverPackageVersion + "NMS").newInstance();
        } catch (Exception ex) {
            Logger.getLogger(NmsManager.class.getName()).log(Level.SEVERE, "Failed to load NMS for this server version", ex);
        }
        return result;
    }

    public static CoreNMS getNMS() {
        return nms;
    }

    public static boolean hasNMS() {
        return nms != null;
    }
}
