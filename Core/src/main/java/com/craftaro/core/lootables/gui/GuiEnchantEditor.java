package com.craftaro.core.lootables.gui;

import com.craftaro.core.lootables.loot.Loot;
import com.craftaro.core.compatibility.CompatibleMaterial;
import com.craftaro.core.gui.AnvilGui;
import com.craftaro.core.gui.Gui;
import com.craftaro.core.gui.GuiUtils;
import com.craftaro.core.utils.TextUtils;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiEnchantEditor extends Gui {
    private final Gui returnGui;
    private final Loot loot;

    public GuiEnchantEditor(Loot loot, Gui returnGui) {
        super(1, returnGui);

        this.returnGui = returnGui;
        this.loot = loot;

        setDefaultItem(null);
        setTitle("Enchantment Editor");

        paint();
    }

    public void paint() {
        Map<String, Integer> lore = loot.getEnchants() == null ? new HashMap<>() : new HashMap<>(loot.getEnchants());

        setButton(2, GuiUtils.createButtonItem(CompatibleMaterial.OAK_FENCE_GATE,
                        TextUtils.formatText("&cBack")),
                (event) -> {
                    guiManager.showGUI(event.player, returnGui);
                    ((GuiLootEditor) returnGui).paint();
                });
        setButton(6, GuiUtils.createButtonItem(CompatibleMaterial.OAK_FENCE_GATE,
                        TextUtils.formatText("&cBack")),
                (event) -> {
                    guiManager.showGUI(event.player, returnGui);
                    ((GuiLootEditor) returnGui).paint();
                });
        setButton(3, GuiUtils.createButtonItem(CompatibleMaterial.ARROW,
                        TextUtils.formatText("&aAdd new line")),
                (event -> {
                    AnvilGui gui = new AnvilGui(event.player, this);
                    gui.setAction((e -> {
                        if (Enchantment.getByName(gui.getInputText().toUpperCase().trim()) == null) {
                            e.player.sendMessage("That is not a valid enchantment.");
                            e.player.closeInventory();
                            return;
                        }

                        AnvilGui gui1 = new AnvilGui(event.player, this);
                        gui1.setAction((ee -> {
                            lore.put(gui.getInputText().toUpperCase().trim(), Integer.parseInt(gui1.getInputText().trim()));
                            loot.setEnchants(lore);
                            ee.player.closeInventory();
                            paint();
                        }));
                        gui1.setTitle("Enter a level");
                        guiManager.showGUI(event.player, gui1);
                    }));

                    gui.setTitle("Enter an enchant");
                    guiManager.showGUI(event.player, gui);
                }));

        List<String> enchantments = new ArrayList<>();

        String last = null;

        if (!lore.isEmpty()) {
            for (Map.Entry<String, Integer> entry : lore.entrySet()) {
                last = entry.getKey();
                enchantments.add("&6" + entry.getKey() + " " + entry.getValue());
            }
        }

        setItem(4, GuiUtils.createButtonItem(CompatibleMaterial.WRITABLE_BOOK,
                TextUtils.formatText("&7Enchant Override:"),
                lore.isEmpty()
                        ? TextUtils.formatText(Collections.singletonList("&cNo enchantments set..."))
                        : TextUtils.formatText(enchantments)));

        String lastFinal = last;
        setButton(5, GuiUtils.createButtonItem(CompatibleMaterial.ARROW,
                        TextUtils.formatText("&cRemove the last line")),
                (event -> {
                    lore.remove(lastFinal);
                    loot.setEnchants(lore);
                    paint();
                }));
    }
}
