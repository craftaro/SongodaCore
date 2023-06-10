package com.craftaro.core.gui.events;

import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiManager;
import org.bukkit.entity.Player;

public class GuiOpenEvent extends GuiEvent {
    public GuiOpenEvent(GuiManager manager, Gui gui, Player player) {
        super(manager, gui, player);
    }
}
