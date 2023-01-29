package com.songoda.core.lootables.gui;

import com.cryptomorin.xseries.XMaterial;
import com.songoda.core.SongodaCore;
import com.songoda.core.compatibility.CompatibleHand;
import com.songoda.core.lootables.loot.Loot;
import com.songoda.core.lootables.loot.LootBuilder;
import com.songoda.core.lootables.loot.LootManager;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GuiLootEditor {
    private final LootManager lootManager;
    private final Loot loot;
    private final Player player;
    private final Gui returnGui;
    private final Gui gui;

    public GuiLootEditor(LootManager lootManager, Loot loot, Player player, Gui returnGui) {
        this.lootManager = lootManager;
        this.loot = loot;
        this.returnGui = returnGui;
        this.player = player;
        this.gui = Gui.gui()
                .rows(6)
                .title(Component.text("Loot Editor"))
                .disableAllInteractions()
                .create();

        paint();
    }

    public void paint() {
        gui.setItem(8, ItemBuilder.from(XMaterial.OAK_DOOR.parseItem()).name(Component.text("Back", NamedTextColor.RED)).asGuiItem(event -> {
            returnGui.open(player);
        }));

        gui.setItem(9, ItemBuilder.from((loot.getMaterial() == null ? XMaterial.BARRIER : loot.getMaterial()).parseItem())
                .name(Component.text("Current Material: ", NamedTextColor.GRAY).append(Component.text(loot.getMaterial() != null ? loot.getMaterial().name() : "None", NamedTextColor.GOLD)))
                .lore(Component.empty(),
                        Component.text("Click to set the material to", NamedTextColor.DARK_GRAY),
                        Component.text("the material in your hand.", NamedTextColor.DARK_GRAY))
                .asGuiItem(event -> {
                    ItemStack itemStack = CompatibleHand.MAIN_HAND.getItem(player);
                    loot.setMaterial(XMaterial.matchXMaterial(itemStack));

                    paint();
                }));

        gui.setItem(10, ItemBuilder.from(XMaterial.PAPER.parseItem())
                .name(Component.text("Name Override:", NamedTextColor.GRAY).append(loot.getName() == null ? Component.text("None set", NamedTextColor.GOLD) : loot.getName()))
                .asGuiItem(event -> {
                    new AnvilGUI.Builder()
                            .title("Enter a name")
                            .itemLeft(XMaterial.PAPER.parseItem())
                            .plugin(SongodaCore.getInstance())
                            .onComplete((player, text) -> {
                                loot.setName(text);
                                return AnvilGUI.Response.close();
                            }).onClose(player -> paint()).open(player);
                }));

        gui.setItem(11, ItemBuilder.from(XMaterial.WRITABLE_BOOK.parseItem()).name(Component.text("Lore Override:", NamedTextColor.GRAY))
                .lore(loot.getLore() == null ? Collections.singletonList(Component.text("None set", NamedTextColor.GOLD)) : loot.getLore()).asGuiItem(event -> {
                    new GuiLoreEditor(loot, player, gui);
                }));

        List<Component> enchantments = new ArrayList<>();

        if (loot.getEnchants() != null) {
            for (Map.Entry<String, Integer> entry : loot.getEnchants().entrySet()) {
                enchantments.add(Component.text(entry.getKey() + " " + entry.getValue(), NamedTextColor.GOLD));
            }
        }

        gui.setItem(12, ItemBuilder.from(XMaterial.ENCHANTED_BOOK.parseItem()).name(Component.text("Enchantments:", NamedTextColor.GRAY))
                .lore(enchantments.isEmpty() ? Collections.singletonList(Component.text("None set", NamedTextColor.GOLD)) : enchantments).asGuiItem(event -> {
                    new GuiEnchantEditor(loot, player, gui);
                }));

        gui.setItem(13, ItemBuilder.from((loot.getBurnedMaterial() == null ? XMaterial.FIRE_CHARGE : loot.getBurnedMaterial()).parseItem())
                .name(Component.text("Current Burned Material: ", NamedTextColor.GRAY).append(Component.text(loot.getBurnedMaterial() != null ? loot.getBurnedMaterial().name() : "None", NamedTextColor.GOLD)))
                .lore(Component.empty(),
                        Component.text("Click to set the burned material to", NamedTextColor.DARK_GRAY),
                        Component.text("the material in your hand.", NamedTextColor.DARK_GRAY))
                .asGuiItem(event -> {
                    ItemStack itemStack = CompatibleHand.MAIN_HAND.getItem(player);
                    loot.setBurnedMaterial(XMaterial.matchXMaterial(itemStack));

                    paint();
                }));

        gui.setItem(14, ItemBuilder.from(XMaterial.CLOCK.parseItem())
                .name(Component.text("Chance: ", NamedTextColor.GRAY).append(Component.text(loot.getChance(), NamedTextColor.GOLD)))
                .lore(Component.text("Click to edit this loot's", NamedTextColor.DARK_GRAY),
                        Component.text("drop chance.", NamedTextColor.DARK_GRAY))
                .asGuiItem(event -> {
                    new AnvilGUI.Builder()
                            .title("Enter a chance")
                            .itemLeft(XMaterial.PAPER.parseItem())
                            .plugin(SongodaCore.getInstance())
                            .onComplete((player, text) -> {
                                loot.setChance(Double.parseDouble(text));
                                return AnvilGUI.Response.close();
                            }).onClose(player -> paint()).open(player);
                }));


        gui.setItem(15, ItemBuilder.from(XMaterial.REDSTONE.parseItem())
                .name(Component.text("Min Drop Amount: ", NamedTextColor.GRAY).append(Component.text(loot.getMin(), NamedTextColor.GOLD)))
                .asGuiItem(event -> {
                    new AnvilGUI.Builder()
                            .title("Enter a number")
                            .itemLeft(XMaterial.PAPER.parseItem())
                            .plugin(SongodaCore.getInstance())
                            .onComplete((player, text) -> {
                                loot.setMin(Integer.parseInt(text));
                                return AnvilGUI.Response.close();
                            }).onClose(player -> paint()).open(player);
                }));

        gui.setItem(16, ItemBuilder.from(XMaterial.GLOWSTONE_DUST.parseItem())
                .name(Component.text("Max Drop Amount: ", NamedTextColor.GRAY).append(Component.text(loot.getMax(), NamedTextColor.GOLD)))
                .asGuiItem(event -> {
                    new AnvilGUI.Builder()
                            .title("Enter a number")
                            .itemLeft(XMaterial.PAPER.parseItem())
                            .plugin(SongodaCore.getInstance())
                            .onComplete((player, text) -> {
                                loot.setMax(Integer.parseInt(text));
                                return AnvilGUI.Response.close();
                            }).onClose(player -> paint()).open(player);
                }));

        gui.setItem(17, ItemBuilder.from(XMaterial.REDSTONE.parseItem())
                .name(Component.text("Min Item Damage: ", NamedTextColor.GRAY).append(Component.text(loot.getDamageMin(), NamedTextColor.GOLD)))
                .asGuiItem(event -> {
                    new AnvilGUI.Builder()
                            .title("Enter a number")
                            .itemLeft(XMaterial.PAPER.parseItem())
                            .plugin(SongodaCore.getInstance())
                            .onComplete((player, text) -> {
                                loot.setDamageMin(Integer.parseInt(text));
                                return AnvilGUI.Response.close();
                            }).onClose(player -> paint()).open(player);
                }));

        gui.setItem(18, ItemBuilder.from(XMaterial.GLOWSTONE_DUST.parseItem())
                .name(Component.text("Max Item Damage: ", NamedTextColor.GRAY).append(Component.text(loot.getDamageMax(), NamedTextColor.GOLD)))
                .asGuiItem(event -> {
                    new AnvilGUI.Builder()
                            .title("Enter a number")
                            .itemLeft(XMaterial.PAPER.parseItem())
                            .plugin(SongodaCore.getInstance())
                            .onComplete((player, text) -> {
                                loot.setDamageMax(Integer.parseInt(text));
                                return AnvilGUI.Response.close();
                            }).onClose(player -> paint()).open(player);
                }));

        gui.setItem(19, ItemBuilder.from(XMaterial.CHEST.parseItem())
                .name(Component.text("Allow Looting Enchantment?: ", NamedTextColor.GRAY).append(Component.text(loot.isAllowLootingEnchant(), NamedTextColor.GOLD)))
                .asGuiItem(event -> {
                    loot.setAllowLootingEnchant(!loot.isAllowLootingEnchant());
                    paint();
                }));

        gui.setItem(20, ItemBuilder.from(XMaterial.REDSTONE.parseItem())
                .name(Component.text("Min Child Loot Drops: ", NamedTextColor.GRAY).append(Component.text(loot.getChildDropCountMin(), NamedTextColor.GOLD)))
                .asGuiItem(event -> {
                    new AnvilGUI.Builder()
                            .title("Enter a number")
                            .itemLeft(XMaterial.PAPER.parseItem())
                            .plugin(SongodaCore.getInstance())
                            .onComplete((player, text) -> {
                                loot.setChildDropCountMin(Integer.parseInt(text));
                                return AnvilGUI.Response.close();
                            }).onClose(player -> paint()).open(player);
                }));

        gui.setItem(21, ItemBuilder.from(XMaterial.GLOWSTONE_DUST.parseItem())
                .name(Component.text("Max Child Loot Drops: ", NamedTextColor.GRAY).append(Component.text(loot.getChildDropCountMax(), NamedTextColor.GOLD)))
                .asGuiItem(event -> {
                    new AnvilGUI.Builder()
                            .title("Enter a number")
                            .itemLeft(XMaterial.PAPER.parseItem())
                            .plugin(SongodaCore.getInstance())
                            .onComplete((player, text) -> {
                                loot.setChildDropCountMax(Integer.parseInt(text));
                                return AnvilGUI.Response.close();
                            }).onClose(player -> paint()).open(player);
                }));

        List<Component> entities = new ArrayList<>();

        if (loot.getOnlyDropFor() != null) {
            for (EntityType entity : loot.getOnlyDropFor()) {
                entities.add(Component.text(entity.name(), NamedTextColor.GOLD));
            }
        }

        gui.setItem(22, ItemBuilder.from(XMaterial.SPAWNER.parseItem()).name(Component.text("Only Drop For:", NamedTextColor.GRAY))
                .lore(entities.isEmpty() ? Collections.singletonList(Component.text("None set", NamedTextColor.GOLD)) : entities).asGuiItem(event -> {
                    new GuiEntityEditor(loot, player, gui);
                }));

        gui.setItem(4, 0, ItemBuilder.from(XMaterial.LIME_DYE.parseItem())
                .name(Component.text("Create new Child Loot", NamedTextColor.GREEN))
                .asGuiItem(event -> {
                    new AnvilGUI.Builder()
                            .title("Enter a material")
                            .itemLeft(XMaterial.PAPER.parseItem())
                            .plugin(SongodaCore.getInstance())
                            .onComplete((player, text) -> {
                                Optional<XMaterial> material = XMaterial.matchXMaterial(text.trim().toUpperCase());
                                if (material.isPresent()) {
                                    loot.addChildLoots(new LootBuilder().setMaterial(material.get()).build());
                                } else {
                                    return AnvilGUI.Response.text("That is not a valid material.");
                                }

                                return AnvilGUI.Response.close();
                            }).onClose(player -> paint()).open(player);
                }));

        int i = 9 * 5;
        for (Loot loot : loot.getChildLoot()) {
            gui.setItem(i, (loot.getMaterial() == null
                    ? ItemBuilder.from(XMaterial.BARRIER.parseItem())
                    : ItemBuilder.from(loot.getMaterial().parseItem())
                    .lore(Component.text("Left click", NamedTextColor.GOLD).append(Component.text(" to edit", NamedTextColor.GRAY)),
                            Component.text("Right click", NamedTextColor.GOLD).append(Component.text(" to destroy", NamedTextColor.GRAY)))).asGuiItem(event -> {
                if (event.getClick() == ClickType.LEFT) {
                    new GuiLootEditor(lootManager, loot, player, gui);
                }else if (event.getClick() == ClickType.RIGHT) {
                    this.loot.removeChildLoot(loot);
                    paint();
                }
            }));

            ++i;
        }
    }
}
