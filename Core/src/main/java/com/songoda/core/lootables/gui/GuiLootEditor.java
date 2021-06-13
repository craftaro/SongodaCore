package com.songoda.core.lootables.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.AnvilGui;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.utils.TextUtils;
import com.songoda.core.lootables.loot.Loot;
import com.songoda.core.lootables.loot.LootBuilder;
import com.songoda.core.lootables.loot.LootManager;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GuiLootEditor extends Gui {

    private final LootManager lootManager;
    private final Loot loot;
    private final Gui returnGui;

    public GuiLootEditor(LootManager lootManager, Loot loot, Gui returnGui) {
        super(6, returnGui);
        this.lootManager = lootManager;
        this.loot = loot;
        this.returnGui = returnGui;
        setDefaultItem(null);
        setTitle("Loot Editor");
        paint();
        setOnClose((event) ->
                lootManager.saveLootables(false));
    }

    public void paint() {
        if (inventory != null)
            inventory.clear();
        setActionForRange(0, 0, 5, 9, null);

        setButton(8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR,
                TextUtils.formatText("&cBack")),
                (event) -> {
                    guiManager.showGUI(event.player, returnGui);
                });

        setButton(9, GuiUtils.createButtonItem(loot.getMaterial() == null ? CompatibleMaterial.BARRIER : loot.getMaterial(),
                TextUtils.formatText("&7Current Material: &6" + (loot.getMaterial() != null
                        ? loot.getMaterial().name() : "None")), TextUtils.formatText(
                        Arrays.asList("",
                                "&8Click to set the material to",
                                "&8the material in your hand.")
                )), (event) -> {
            ItemStack stack = event.player.getInventory().getItemInMainHand();
            loot.setMaterial(CompatibleMaterial.getMaterial(stack));
            paint();
        });

        setButton(10, GuiUtils.createButtonItem(CompatibleMaterial.PAPER,
                TextUtils.formatText("&7Name Override: &6" + (loot.getName() == null ? "None set" : loot.getName()))),
                (event) -> {
                    AnvilGui gui = new AnvilGui(event.player, this);
                    gui.setAction((e -> {
                        loot.setName(gui.getInputText().trim());
                        paint();
                        e.player.closeInventory();
                    }));
                    guiManager.showGUI(event.player, gui);
                    gui.setInput(GuiUtils.createButtonItem(CompatibleMaterial.PAPER, loot.getName()));
                });

        setButton(11, GuiUtils.createButtonItem(CompatibleMaterial.WRITABLE_BOOK,
                TextUtils.formatText("&7Lore Override:"),
                TextUtils.formatText(loot.getLore() == null ? Collections.singletonList("&6None set") : loot.getLore())),
                (event) -> guiManager.showGUI(event.player, new GuiLoreEditor(loot, this)));

        List<String> enchantments = new ArrayList<>();

        if (loot.getEnchants() != null)
            for (Map.Entry<String, Integer> entry : loot.getEnchants().entrySet())
                enchantments.add("&6" + entry.getKey() + " " + entry.getValue());

        setButton(12, GuiUtils.createButtonItem(CompatibleMaterial.ENCHANTED_BOOK,
                TextUtils.formatText("&7Enchantments:"),
                TextUtils.formatText(enchantments.isEmpty() ? Collections.singletonList("&6None set") : enchantments)),
                (event) -> guiManager.showGUI(event.player, new GuiEnchantEditor(loot, this)));

        setButton(13, GuiUtils.createButtonItem(
                loot.getBurnedMaterial() == null
                        ? CompatibleMaterial.FIRE_CHARGE
                        : loot.getBurnedMaterial(),
                TextUtils.formatText("&7Current Burned Material: &6"
                        + (loot.getBurnedMaterial() == null
                        ? "None"
                        : loot.getBurnedMaterial().name())), TextUtils.formatText(
                        Arrays.asList("",
                                "&8Click to set the burned material to",
                                "&8the material in your hand.")
                )),
                (event) -> {
                    ItemStack stack = event.player.getInventory().getItemInMainHand();
                    loot.setBurnedMaterial(CompatibleMaterial.getMaterial(stack));
                    paint();
                });

        setButton(14, GuiUtils.createButtonItem(CompatibleMaterial.CLOCK,
                TextUtils.formatText("&7Chance: &6" + loot.getChance()),
                TextUtils.formatText(
                        Arrays.asList("",
                                "&8Click to edit this loots",
                                "&8drop chance.")
                )),
                (event) -> {
                    AnvilGui gui = new AnvilGui(event.player, this);
                    gui.setAction((e) -> {
                        loot.setChance(Double.parseDouble(gui.getInputText()));
                        paint();
                        e.player.closeInventory();
                    });
                    gui.setInput(GuiUtils.createButtonItem(CompatibleMaterial.PAPER,
                            String.valueOf(loot.getChance())));
                    guiManager.showGUI(event.player, gui);
                });

        setButton(15, GuiUtils.createButtonItem(CompatibleMaterial.REDSTONE,
                TextUtils.formatText("&7Min Drop Amount: &6" + loot.getMin())),
                (event) -> {
                    AnvilGui gui = new AnvilGui(event.player, this);
                    gui.setAction((e) -> {
                        loot.setMin(Integer.parseInt(gui.getInputText()));
                        paint();
                        e.player.closeInventory();
                    });
                    gui.setInput(GuiUtils.createButtonItem(CompatibleMaterial.PAPER,
                            String.valueOf(loot.getMin())));
                    guiManager.showGUI(event.player, gui);
                });

        setButton(16, GuiUtils.createButtonItem(CompatibleMaterial.GLOWSTONE_DUST,
                TextUtils.formatText("&7Max Drop Amount: &6" + loot.getMax())),
                (event) -> {
                    AnvilGui gui = new AnvilGui(event.player, this);
                    gui.setAction((e) -> {
                        loot.setMax(Integer.parseInt(gui.getInputText()));
                        paint();
                        e.player.closeInventory();
                    });
                    gui.setInput(GuiUtils.createButtonItem(CompatibleMaterial.PAPER,
                            String.valueOf(loot.getMax())));
                    guiManager.showGUI(event.player, gui);
                });

        setButton(17, GuiUtils.createButtonItem(CompatibleMaterial.REDSTONE,
                TextUtils.formatText("&7Min Item Damage: &6" + loot.getDamageMin())),
                (event) -> {
                    AnvilGui gui = new AnvilGui(event.player, this);
                    gui.setAction((e) -> {
                        loot.setDamageMin(Integer.parseInt(gui.getInputText()));
                        paint();
                        e.player.closeInventory();
                    });
                    gui.setInput(GuiUtils.createButtonItem(CompatibleMaterial.PAPER,
                            String.valueOf(loot.getDamageMin())));
                    guiManager.showGUI(event.player, gui);
                });

        setButton(18, GuiUtils.createButtonItem(CompatibleMaterial.GLOWSTONE_DUST,
                TextUtils.formatText("&7Max Item Damage: &6" + loot.getDamageMax())),
                (event) -> {
                    AnvilGui gui = new AnvilGui(event.player, this);
                    gui.setAction((e) -> {
                        loot.setDamageMax(Integer.parseInt(gui.getInputText()));
                        paint();
                        e.player.closeInventory();
                    });
                    gui.setInput(GuiUtils.createButtonItem(CompatibleMaterial.PAPER,
                            String.valueOf(loot.getDamageMax())));
                    guiManager.showGUI(event.player, gui);
                });

        setButton(19, GuiUtils.createButtonItem(CompatibleMaterial.CHEST,
                TextUtils.formatText("&7Allow Looting Enchantment?: &6" + loot.isAllowLootingEnchant())),
                (event) -> {
                    loot.setAllowLootingEnchant(!loot.isAllowLootingEnchant());
                    paint();
                    event.player.closeInventory();
                });

        setButton(20, GuiUtils.createButtonItem(CompatibleMaterial.REDSTONE,
                TextUtils.formatText("&7Min Child Loot Min: &6" + loot.getChildDropCountMin())),
                (event) -> {
                    AnvilGui gui = new AnvilGui(event.player, this);
                    gui.setAction((e) -> {
                        loot.setChildDropCountMin(Integer.parseInt(gui.getInputText()));
                        paint();
                        e.player.closeInventory();
                    });
                    gui.setInput(GuiUtils.createButtonItem(CompatibleMaterial.PAPER,
                            String.valueOf(loot.getChildDropCountMin())));
                    guiManager.showGUI(event.player, gui);
                });

        setButton(21, GuiUtils.createButtonItem(CompatibleMaterial.GLOWSTONE_DUST,
                TextUtils.formatText("&7Min Child Loot Max: &6" + loot.getChildDropCountMax())),
                (event) -> {
                    AnvilGui gui = new AnvilGui(event.player, this);
                    gui.setAction((e) -> {
                        loot.setChildDropCountMax(Integer.parseInt(gui.getInputText()));
                        paint();
                        e.player.closeInventory();
                    });
                    gui.setInput(GuiUtils.createButtonItem(CompatibleMaterial.PAPER,
                            String.valueOf(loot.getChildDropCountMax())));
                    guiManager.showGUI(event.player, gui);
                });

        List<String> entities = new ArrayList<>();

        if (loot.getOnlyDropFor() != null)
            for (EntityType entity : loot.getOnlyDropFor())
                entities.add("&6" + entity.name());

        setButton(22, GuiUtils.createButtonItem(CompatibleMaterial.ENCHANTED_BOOK,
                TextUtils.formatText("&7Only Drop For:"),
                TextUtils.formatText(entities)),
                (event) -> guiManager.showGUI(event.player, new GuiEntityEditor(loot, this)));

        setButton(4, 0, GuiUtils.createButtonItem(CompatibleMaterial.LIME_DYE, TextUtils.formatText("&aCreate new Child Loot")),
                (event -> {
                    AnvilGui gui = new AnvilGui(event.player, this);
                    gui.setAction((event1 -> {
                        try {
                            loot.addChildLoots(new LootBuilder().setMaterial(CompatibleMaterial
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

        int i = 9 * 5;
        for (Loot loot : loot.getChildLoot()) {
            ItemStack item = loot.getMaterial() == null
                    ? CompatibleMaterial.BARRIER.getItem()
                    : GuiUtils.createButtonItem(loot.getMaterial(), null,
                    TextUtils.formatText("&6Left click &7to edit"),
                    TextUtils.formatText("&6Right click &7to destroy"));

            setButton(i, item,
                    (event) -> {
                        if (event.clickType == ClickType.RIGHT) {
                            this.loot.removeChildLoot(loot);
                            paint();
                        } else if (event.clickType == ClickType.LEFT) {
                            guiManager.showGUI(event.player, new GuiLootEditor(lootManager, loot, this));
                        }
                    });
            i++;
        }
    }
}
