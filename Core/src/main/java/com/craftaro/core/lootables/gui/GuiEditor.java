package com.craftaro.core.lootables.gui;

import com.craftaro.core.lootables.loot.LootManager;
import com.craftaro.core.lootables.loot.Lootable;
import com.cryptomorin.xseries.XMaterial;
import com.craftaro.core.compatibility.ServerVersion;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiEditor {
    private final LootManager lootManager;
    private final Player player;
    private final PaginatedGui gui;

    public GuiEditor(LootManager lootManager, Player player) {
        this.lootManager = lootManager;
        this.player = player;

        this.gui = Gui.paginated()
                .rows(6)
                .pageSize(36)
                .title(Component.text("Lootables Overview"))
                .disableAllInteractions()
                .create();

        paint();
    }

    private void paint() {
        List<Lootable> lootables = new ArrayList<>(lootManager.getRegisteredLootables().values());

        if (gui.getCurrentPageNum() != 1) {
            gui.setItem(5, 2, ItemBuilder.from(XMaterial.ARROW.parseItem()).name(Component.text("Back")).asGuiItem(event -> {
                gui.previous();
            }));
        }

        if (gui.getCurrentPageNum() != gui.getPagesNum()) {
            gui.setItem(5, 6, ItemBuilder.from(XMaterial.ARROW.parseItem()).name(Component.text("Next")).asGuiItem(event -> {
                gui.next();
            }));
        }

        for (Lootable lootable : lootables) {
            gui.addItem(ItemBuilder.from(getIcon(lootable.getKey())).asGuiItem(event -> {
                new GuiLootableEditor(lootManager, lootable, player, gui);
            }));
        }
    }

    public ItemStack getIcon(String key) {
        ItemStack stack = null;
        EntityType type = EntityType.fromName(key);

        if (type != null) {
            XMaterial material = getSpawnEgg(type);

            if (material != null) {
                stack = material.parseItem();
            }
        }

        if (stack == null) {
            stack = XMaterial.GHAST_SPAWN_EGG.parseItem();
        }

        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(key);
        stack.setItemMeta(meta);

        return stack;
    }

    public XMaterial getSpawnEgg(EntityType type) {
        if (type == EntityType.MUSHROOM_COW) {
            return XMaterial.MOOSHROOM_SPAWN_EGG;
        }

        if (ServerVersion.isServerVersionBelow(ServerVersion.V1_16) && type == EntityType.valueOf("PIG_ZOMBIE")) {
            return XMaterial.ZOMBIFIED_PIGLIN_SPAWN_EGG;
        }

        return XMaterial.matchXMaterial(type.name() + "_SPAWN_EGG").get();
    }
}
