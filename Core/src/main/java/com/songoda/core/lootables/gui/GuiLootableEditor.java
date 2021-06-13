package com.songoda.core.lootables.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.AnvilGui;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.utils.TextUtils;
import com.songoda.core.lootables.loot.Loot;
import com.songoda.core.lootables.loot.LootBuilder;
import com.songoda.core.lootables.loot.LootManager;
import com.songoda.core.lootables.loot.Lootable;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

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
        if (inventory != null)
            inventory.clear();
        setActionForRange(0, 0, 5, 9, null);

        setButton(0, GuiUtils.createButtonItem(CompatibleMaterial.LIME_DYE, TextUtils.formatText("&aCreate new Loot")),
                (event -> {
                    AnvilGui gui = new AnvilGui(event.player, this);
                    gui.setAction((event1 -> {
                        try {
                            lootable.registerLoot(new LootBuilder().setMaterial(CompatibleMaterial
                                    .valueOf(gui.getInputText().trim())).build());
                        } catch (IllegalArgumentException e) {
                            event.player.sendMessage("That is not a valid material.");
                        }
                        event.player.closeInventory();
                        paint();
                    }));
                    gui.setTitle("Enter a material");
                    guiManager.showGUI(event.player, gui);
                }));

        setButton(8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, TextUtils.formatText("&cBack")),
                (event -> guiManager.showGUI(event.player, returnGui)));

        int i = 9;
        for (Loot loot : lootable.getRegisteredLoot()) {
            ItemStack item = loot.getMaterial() == null
                    ? CompatibleMaterial.BARRIER.getItem()
                    : GuiUtils.createButtonItem(loot.getMaterial(), null,
                    TextUtils.formatText("&6Left click &7to edit"),
                    TextUtils.formatText("&6Right click &7to destroy"));

            setButton(i, item,
                    (event) -> {
                        if (event.clickType == ClickType.RIGHT) {
                            lootable.removeLoot(loot);
                            paint();
                        } else if (event.clickType == ClickType.LEFT) {
                            guiManager.showGUI(event.player, new GuiLootEditor(lootManager, loot, this));
                        }
                    });
            i++;
        }
    }
}
