package com.craftaro.core.nms.v1_8_R2.entity;

import com.craftaro.core.nms.entity.NMSPlayer;
import net.minecraft.server.v1_8_R2.Packet;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSPlayerImpl implements NMSPlayer {
    @Override
    public void sendPacket(Player p, Object packet) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket((Packet) packet);
    }
}
