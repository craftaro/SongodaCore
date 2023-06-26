package com.craftaro.core.lootables.gui;

import com.craftaro.core.CraftaroCore;
import com.craftaro.core.lootables.loot.Loot;
import com.craftaro.core.lootables.loot.LootBuilder;
import com.craftaro.core.lootables.loot.LootManager;
import com.craftaro.core.lootables.loot.Lootable;
import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Optional;

public class GuiLootableEditor {
    private final LootManager lootManager;
    private final Lootable lootable;
    private final Player player;
    private final PaginatedGui returnGui;
    private final Gui gui;

    public GuiLootableEditor(LootManager lootManager, Lootable lootable, Player player, PaginatedGui returnGui) {
        this.lootManager = lootManager;
        this.lootable = lootable;
        this.returnGui = returnGui;
        this.player = player;
        this.gui = Gui.gui()
                .rows(6)
                .title(Component.text("Lootables Editor"))
                .disableAllInteractions()
                .create();

        open();
    }

    private void open() {
        gui.setCloseGuiAction(event -> lootManager.saveLootables(false));
        gui.setItem(0, ItemBuilder.from(XMaterial.LIME_DYE.parseItem()).name(Component.text("Create new Loot", NamedTextColor.GREEN)).asGuiItem(event -> {
            new AnvilGUI.Builder()
                    .title("Enter a material")
                    .itemLeft(XMaterial.PAPER.parseItem())
                    .plugin(CraftaroCore.getInstance())
                    .onComplete((player, text) -> {
                        Optional<XMaterial> material = XMaterial.matchXMaterial(text.trim().toUpperCase());
                        if (material.isPresent()) {
                            lootable.registerLoot(new LootBuilder().setMaterial(material.get()).build());
                        } else {
                            return AnvilGUI.Response.text("That is not a valid material.");
                        }

                        return AnvilGUI.Response.close();
                    }).onClose(player -> new GuiLootableEditor(lootManager, lootable, player, returnGui)).open(player);
        }));

        gui.setItem(8, ItemBuilder.from(XMaterial.OAK_DOOR.parseItem()).name(Component.text("Back", NamedTextColor.RED)).asGuiItem(event -> {
            returnGui.open(player);
        }));

        int i = 9;
        for (Loot loot : lootable.getRegisteredLoot()) {
            gui.setItem(i, (loot.getMaterial() == null ? ItemBuilder.from(XMaterial.BARRIER.parseItem()) : ItemBuilder.from(loot.getMaterial().parseItem())
                    .lore(Component.text("Left click", NamedTextColor.GOLD).append(Component.text(" to edit", NamedTextColor.GRAY)),
                            Component.text("Right click", NamedTextColor.GOLD).append(Component.text(" to destroy", NamedTextColor.GRAY)))).asGuiItem(event -> {
                           if (event.getClick() == ClickType.LEFT) {
                               // TODO gui loot editor
                               return;
                           }

                           if (event.getClick() == ClickType.RIGHT) {
                               lootable.removeLoot(loot);
                               new GuiLootableEditor(lootManager, lootable, player, returnGui);
                           }
            }));

            i++;
        }
    }
}
