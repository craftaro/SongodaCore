package com.songoda.core.gui.methods;

import com.songoda.core.gui.GUI;
import org.bukkit.entity.Player;

public interface Closable {

    void onClose(Player player, GUI gui);
}
