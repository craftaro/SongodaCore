package com.craftaro.core.lootables.gui;

import com.craftaro.core.chat.AdventureUtils;
import com.craftaro.core.gui.AnvilGui;
import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.core.lootables.loot.Loot;
import com.craftaro.core.lootables.loot.LootBuilder;
import com.craftaro.core.lootables.loot.LootManager;
import com.craftaro.core.lootables.loot.Lootable;
import com.craftaro.core.utils.TextUtils;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GuiLootableEditor extends Gui {
    private final LootManager lootManager;
    private final Lootable lootable;
    private final Gui returnGui;

    public GuiLootableEditor(LootManager lootManager, Lootable lootable, Gui returnGui) {
        super(6);

        this.lootManager = lootManager;
        this.lootable = lootable;
        this.returnGui = returnGui;

        setOnClose((event) ->
                lootManager.saveLootables(false));
        setDefaultItem(null);
        setTitle("Lootables Editor");

        paint();
    }

    private void paint() {
        if (this.inventory != null) {
            this.inventory.clear();
        }

        setActionForRange(0, 0, 5, 9, null);

        setButton(0, GuiUtils.createButtonItem(XMaterial.LIME_DYE, TextUtils.formatText("&aCreate new Loot")),
                (event -> {
                    AnvilGui gui = new AnvilGui(event.player, this);
                    gui.setAction((event1 -> {
                        try {
                            this.lootable.registerLoot(new LootBuilder().setMaterial(XMaterial.valueOf(gui.getInputText().trim().toUpperCase())).build());
                        } catch (IllegalArgumentException ex) {
                            event.player.sendMessage("That is not a valid material.");
                        }

                        event.player.closeInventory();
                        paint();
                    }));

                    gui.setTitle("Enter a material");
                    this.guiManager.showGUI(event.player, gui);
                }));

        setButton(8, GuiUtils.createButtonItem(XMaterial.OAK_DOOR, TextUtils.formatText("&cBack")),
                (event -> this.guiManager.showGUI(event.player, this.returnGui)));

        int i = 9;
        for (Loot loot : this.lootable.getRegisteredLoot()) {
            ItemStack item = loot.getMaterial() == null
                    ? XMaterial.BARRIER.parseItem()
                    : GuiUtils.createButtonItem(loot.getMaterial(), null,
                    Arrays.asList(
                            AdventureUtils.formatComponent("&6Left click &7to edit"),
                            AdventureUtils.formatComponent("&6Right click &7to destroy")
                    )
            );

            setButton(i, item,
                    (event) -> {
                        if (event.clickType == ClickType.RIGHT) {
                            this.lootable.removeLoot(loot);
                            paint();

                            return;
                        }

                        if (event.clickType == ClickType.LEFT) {
                            this.guiManager.showGUI(event.player, new GuiLootEditor(this.lootManager, loot, this));
                        }
                    });

            i++;
        }
    }
}
