package com.songoda.core.nms;

import com.songoda.core.nms.anvil.AnvilCore;
import com.songoda.core.nms.nbt.NBTCore;
import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NmsManager {

    private final static String serverPackagePath = Bukkit.getServer().getClass().getPackage().getName();
    private final static String serverPackageVersion = serverPackagePath.substring(serverPackagePath.lastIndexOf('.') + 1);
    private final static AnvilCore anvil;
    private final static NBTCore nbt;

    static {
        switch (serverPackageVersion) {
            case "v1_8_R1":
                anvil = new com.songoda.core.nms.v1_8_R1.anvil.AnvilCore();
                nbt = new com.songoda.core.nms.v1_8_R1.nbt.NBTCoreImpl();
                break;
            case "v1_8_R2":
                anvil = new com.songoda.core.nms.v1_8_R2.anvil.AnvilCore();
                nbt = new com.songoda.core.nms.v1_8_R2.nbt.NBTCoreImpl();
                break;
            case "v1_8_R3":
                anvil = new com.songoda.core.nms.v1_8_R3.anvil.AnvilCore();
                nbt = new com.songoda.core.nms.v1_8_R3.nbt.NBTCoreImpl();
                break;
            case "v1_9_R1":
                anvil = new com.songoda.core.nms.v1_9_R1.anvil.AnvilCore();
                nbt = new com.songoda.core.nms.v1_9_R1.nbt.NBTCoreImpl();
                break;
            case "v1_9_R2":
                anvil = new com.songoda.core.nms.v1_9_R2.anvil.AnvilCore();
                nbt = new com.songoda.core.nms.v1_9_R2.nbt.NBTCoreImpl();
                break;
            case "v1_10_R1":
                anvil = new com.songoda.core.nms.v1_10_R1.anvil.AnvilCore();
                nbt = new com.songoda.core.nms.v1_10_R1.nbt.NBTCoreImpl();
                break;
            case "v1_11_R1":
                anvil = new com.songoda.core.nms.v1_11_R1.anvil.AnvilCore();
                nbt = new com.songoda.core.nms.v1_11_R1.nbt.NBTCoreImpl();
                break;
            case "v1_12_R1":
                anvil = new com.songoda.core.nms.v1_12_R1.anvil.AnvilCore();
                nbt = new com.songoda.core.nms.v1_12_R1.nbt.NBTCoreImpl();
                break;
            case "v1_13_R1":
                anvil = new com.songoda.core.nms.v1_13_R1.anvil.AnvilCore();
                nbt = new com.songoda.core.nms.v1_13_R1.nbt.NBTCoreImpl();
                break;
            case "v1_13_R2":
                anvil = new com.songoda.core.nms.v1_13_R2.anvil.AnvilCore();
                nbt = new com.songoda.core.nms.v1_13_R2.nbt.NBTCoreImpl();
                break;
            case "v1_14_R1":
                anvil = new com.songoda.core.nms.v1_14_R1.anvil.AnvilCore();
                nbt = new com.songoda.core.nms.v1_14_R1.nbt.NBTCoreImpl();
                break;
            case "v1_15_R1":
                anvil = new com.songoda.core.nms.v1_15_R1.anvil.AnvilCore();
                nbt = new com.songoda.core.nms.v1_15_R1.nbt.NBTCoreImpl();
                break;
            case "v1_16_R1":
                anvil = new com.songoda.core.nms.v1_16_R1.anvil.AnvilCore();
                nbt = new com.songoda.core.nms.v1_16_R1.nbt.NBTCoreImpl();
                break;
            default:
                Logger.getLogger(NmsManager.class.getName()).log(Level.SEVERE, "Failed to load NMS for this server version: version {0} not found", serverPackageVersion);
                anvil = null;
                nbt = null;
                break;
        }
    }

    public static AnvilCore getAnvil() {
        return anvil;
    }

    public static boolean hasAnvil() {
        return anvil != null;
    }

    public static NBTCore getNbt() {
        return nbt;
    }

    public static boolean hasNbt() {
        return nbt != null;
    }
}
