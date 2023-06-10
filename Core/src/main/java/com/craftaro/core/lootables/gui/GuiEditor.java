package com.craftaro.core.lootables.gui;

import com.craftaro.core.lootables.loot.LootManager;
import com.craftaro.core.lootables.loot.Lootable;
import com.craftaro.core.compatibility.CompatibleMaterial;
import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiEditor extends Gui {
    private final LootManager lootManager;

    public GuiEditor(LootManager lootManager) {
        super(6);

        this.lootManager = lootManager;

        setDefaultItem(null);
        setTitle("Lootables Overview");

        paint();
    }

    private void paint() {
        if (inventory != null) {
            inventory.clear();
        }

        setActionForRange(0, 0, 5, 9, null);

        List<Lootable> lootables = new ArrayList<>(lootManager.getRegisteredLootables().values());

        double itemCount = lootables.size();
        this.pages = (int) Math.max(1, Math.ceil(itemCount / 36));

        if (page != 1) {
            setButton(5, 2, GuiUtils.createButtonItem(CompatibleMaterial.ARROW, "Back"),
                    (event) -> {
                        page--;
                        paint();
                    });
        }

        if (page != pages) {
            setButton(5, 6, GuiUtils.createButtonItem(CompatibleMaterial.ARROW, "Next"),
                    (event) -> {
                        page++;
                        paint();
                    });
        }

        for (int i = 9; i < 45; i++) {
            int current = ((page - 1) * 36) - 9;
            if (current + i >= lootables.size()) {
                setItem(i, null);
                continue;
            }

            Lootable lootable = lootables.get(current + i);
            if (lootable == null) {
                continue;
            }

            setButton(i, getIcon(lootable.getKey()),
                    (event) -> guiManager.showGUI(event.player, new GuiLootableEditor(lootManager, lootable, this)));
        }
    }

    public ItemStack getIcon(String key) {
        ItemStack stack = null;
        EntityType type = EntityType.fromName(key);

        if (type != null) {
            CompatibleMaterial material = CompatibleMaterial.getSpawnEgg(type);

            if (material != null) {
                stack = material.getItem();
            }
        }

        if (stack == null) {
            stack = CompatibleMaterial.GHAST_SPAWN_EGG.getItem();
        }

        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(key);
        stack.setItemMeta(meta);

        return stack;
    }
}
