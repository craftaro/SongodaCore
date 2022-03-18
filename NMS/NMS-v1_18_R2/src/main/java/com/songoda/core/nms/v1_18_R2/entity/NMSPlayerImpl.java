package com.songoda.core.nms.v1_18_R2.entity;

import com.songoda.core.nms.entity.NMSPlayer;
import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSPlayerImpl implements NMSPlayer {
    @Override
    public void sendPacket(Player p, Object packet) {
        ((CraftPlayer) p).getHandle().connection.send((Packet<?>) packet);
    }
}
