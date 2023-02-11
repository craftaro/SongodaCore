package com.songoda.core.placeholder;

import org.bukkit.entity.Player;

public interface IPlaceholderResolver {

    String setPlaceholders(Player player, String args);
}
