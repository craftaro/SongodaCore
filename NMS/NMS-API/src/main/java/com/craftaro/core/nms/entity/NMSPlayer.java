package com.craftaro.core.nms.entity;

import com.craftaro.core.nms.entity.player.GameProfile;
import org.bukkit.entity.Player;

public interface NMSPlayer {
    void sendPacket(Player p, Object packet);

    GameProfile getProfile(Player p);
}
