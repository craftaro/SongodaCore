package com.craftaro.core.nms.entity;

import org.bukkit.entity.Player;

public interface NMSPlayer {
    void sendPacket(Player p, Object packet);
}
