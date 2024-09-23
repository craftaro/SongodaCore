package com.craftaro.core.gui.events;

import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiManager;
import org.bukkit.entity.Player;

public abstract class GuiEvent {
    public final GuiManager manager;
    public final Gui gui;
    public final Player player;

    public GuiEvent(GuiManager manager, Gui gui, Player player) {
        this.manager = manager;
        this.gui = gui;
        this.player = player;
    }
}
