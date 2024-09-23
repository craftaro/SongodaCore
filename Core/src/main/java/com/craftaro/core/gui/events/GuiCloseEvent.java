package com.craftaro.core.gui.events;

import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiManager;
import org.bukkit.entity.Player;

public class GuiCloseEvent extends GuiEvent {
    public GuiCloseEvent(GuiManager manager, Gui gui, Player player) {
        super(manager, gui, player);
    }
}
