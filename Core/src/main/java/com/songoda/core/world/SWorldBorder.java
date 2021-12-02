package com.songoda.core.world;

import com.songoda.core.compatibility.ClassMapping;
import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.nms.NmsManager;
import com.songoda.core.utils.NMSUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SWorldBorder {
    private static Class<?> packetPlayOutWorldBorderEnumClass;
    private static Class<?> worldBorderClass;
    private static Class<?> craftWorldClass;
    private static Constructor<?> packetPlayOutWorldBorderConstructor;

    private static Constructor<?> clientboundInitializeBorderPacketConstructor;

    static {
        try {
            worldBorderClass = ClassMapping.WORLD_BORDER.getClazz();
            craftWorldClass = NMSUtils.getCraftClass("CraftWorld");

            if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_17)) {
                Class<?> clientboundInitializeBorderPacketClass = ClassMapping.CLIENTBOUND_INITIALIZE_BORDER_PACKET.getClazz();
                clientboundInitializeBorderPacketConstructor = clientboundInitializeBorderPacketClass.getConstructor(worldBorderClass);
            } else {
                Class<?> packetPlayOutWorldBorder = ClassMapping.PACKET_PLAY_OUT_WORLD_BORDER.getClazz();

                if (packetPlayOutWorldBorder != null) {
                    if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_11))
                        packetPlayOutWorldBorderEnumClass = packetPlayOutWorldBorder.getDeclaredClasses()[0];
                    else
                        packetPlayOutWorldBorderEnumClass = packetPlayOutWorldBorder.getDeclaredClasses()[1];

                    packetPlayOutWorldBorderConstructor = packetPlayOutWorldBorder.getConstructor(worldBorderClass,
                            packetPlayOutWorldBorderEnumClass);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void send(Player player, Color color, double size, Location centerLocation) {
        try {
            if (centerLocation == null || centerLocation.getWorld() == null) {
                return;
            }

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
                transitionSizeBetween.invoke(worldBorder, size - 0.1D, size, Long.MAX_VALUE);
            } else if (color == Color.Red) {
                transitionSizeBetween.invoke(worldBorder, size, size - 1.0D, Long.MAX_VALUE);
            }

            if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_17)) {
                Object packet = clientboundInitializeBorderPacketConstructor.newInstance(worldBorder);
                NmsManager.getPlayer().sendPacket(player, packet);
            } else {
                @SuppressWarnings({"unchecked", "rawtypes"})
                Object packet = packetPlayOutWorldBorderConstructor.newInstance(worldBorder,
                        Enum.valueOf((Class<Enum>) packetPlayOutWorldBorderEnumClass, "INITIALIZE"));
                NmsManager.getPlayer().sendPacket(player, packet);
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public enum Color {
        Blue, Green, Red
    }
}
