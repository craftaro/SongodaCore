package com.songoda.core.nms.v1_10_R1.entity;

import com.songoda.core.nms.entity.NMSPlayer;
import net.minecraft.server.v1_10_R1.Packet;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSPlayerImpl implements NMSPlayer {
    @Override
    public void sendPacket(Player p, Object packet) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket((Packet<?>) packet);
    }
}
