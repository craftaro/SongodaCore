package com.craftaro.core.placeholder;

import org.bukkit.entity.Player;

public class NoPluginResolver implements IPlaceholderResolver{

    @Override
    public String setPlaceholders(Player player, String args) {
        return args.replace("%player%", player.getName());
    }
}
