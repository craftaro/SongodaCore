package com.craftaro.core.nms.world;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface NMSWorldBorder {
    void send(Player player, BorderColor color, double size, Location center);

    enum BorderColor {
        BLUE, GREEN, RED
    }
}
