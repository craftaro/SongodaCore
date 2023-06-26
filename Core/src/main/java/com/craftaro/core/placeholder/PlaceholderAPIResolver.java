package com.craftaro.core.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlaceholderAPIResolver implements IPlaceholderResolver {

    public String setPlaceholders(Player player, String args) {
        return PlaceholderAPI.setPlaceholders(player, args);
    }
}
