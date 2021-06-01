package com.songoda.core.world;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.utils.NMSUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WorldBorder {
    
    private static Class<?> packetPlayOutWorldBorderEnumClass;
    private static Class<?> worldBorderClass;
    private static Class<?> craftWorldClass;
    private static Constructor<?> packetPlayOutWorldBorderConstructor;

    static {
        try {
            Class<?> packetPlayOutWorldBorder = NMSUtils.getNMSClass("PacketPlayOutWorldBorder");

            if(packetPlayOutWorldBorder != null) {
                if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_11))
                    packetPlayOutWorldBorderEnumClass = packetPlayOutWorldBorder.getDeclaredClasses()[0];
                else
                    packetPlayOutWorldBorderEnumClass = packetPlayOutWorldBorder.getDeclaredClasses()[1];
                
                worldBorderClass = NMSUtils.getNMSClass("WorldBorder");
                craftWorldClass = NMSUtils.getCraftClass("CraftWorld");
    
                packetPlayOutWorldBorderConstructor = packetPlayOutWorldBorder.getConstructor(worldBorderClass,
                        packetPlayOutWorldBorderEnumClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void send(Player player, Color color, double size, Location centerLocation) {
        try {
            if (centerLocation == null || centerLocation.getWorld() == null)
                return;

            Object worldBorder = worldBorderClass.getConstructor().newInstance();

            if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_9)) {
                Object craftWorld = craftWorldClass.cast(centerLocation.getWorld());
                Method getHandleMethod = craftWorld.getClass().getMethod("getHandle");
                Object worldServer = getHandleMethod.invoke(craftWorld);
                NMSUtils.setField(worldBorder, "world", worldServer, false);
            }

            Method setCenter = worldBorder.getClass().getMethod("setCenter", double.class, double.class);
            setCenter.invoke(worldBorder, centerLocation.getX(), centerLocation.getZ());

            Method setSize = worldBorder.getClass().getMethod("setSize", double.class);
            setSize.invoke(worldBorder, size);

            Method setWarningTime = worldBorder.getClass().getMethod("setWarningTime", int.class);
            setWarningTime.invoke(worldBorder, 0);
            
            Method setWarningDistance = worldBorder.getClass().getMethod("setWarningDistance", int.class);
            setWarningDistance.invoke(worldBorder, 0);

            Method transitionSizeBetween = worldBorder.getClass().getMethod("transitionSizeBetween", double.class,
                    double.class, long.class);

            if (color == Color.Green) {
                transitionSizeBetween.invoke(worldBorder, size - 0.1D, size, 20000000L);
            } else if (color == Color.Red) {
                transitionSizeBetween.invoke(worldBorder, size, size - 1.0D, 20000000L);
            }

            @SuppressWarnings({"unchecked", "rawtypes"})
            Object packet = packetPlayOutWorldBorderConstructor.newInstance(worldBorder,
                    Enum.valueOf((Class<Enum>) packetPlayOutWorldBorderEnumClass, "INITIALIZE"));
            NMSUtils.sendPacket(player, packet);
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public enum Color {

        Blue, Green, Red

    }
}
