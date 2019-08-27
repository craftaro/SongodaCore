package com.songoda.core.gui.methods;

import com.songoda.core.gui.GUI;
import org.bukkit.entity.Player;

public interface Openable {
    
    void onOpen(Player player, GUI gui);
}
