package com.craftaro.core.lootables.gui;

import com.craftaro.core.lootables.loot.Loot;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public class GuiLoreEditor extends AbstractGuiListEditor {
    public GuiLoreEditor(Loot loot, Player player, Gui returnGui) {
        super(loot, player, Component.text("Lore Editor"), returnGui);
    }

    @Override
    protected List<String> getData() {
        return loot.getRawLore();
    }

    @Override
    protected void updateData(List<String> list) {
        loot.setLore(list);
    }

    @Override
    protected String validate(String line) {
        return line.trim();
    }
}
