package com.craftaro.core.nms.v1_11_R1.entity;

import com.craftaro.core.nms.entity.NMSPlayer;
import net.minecraft.server.v1_11_R1.Packet;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSPlayerImpl implements NMSPlayer {
    @Override
    public void sendPacket(Player p, Object packet) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket((Packet<?>) packet);
    }
}
