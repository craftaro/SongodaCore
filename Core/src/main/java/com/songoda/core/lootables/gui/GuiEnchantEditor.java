package com.songoda.core.lootables.gui;

import com.cryptomorin.xseries.XMaterial;
import com.songoda.core.SongodaCore;
import com.songoda.core.lootables.loot.Loot;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GuiEnchantEditor {
    private final Loot loot;
    private final Gui returnGui;
    private final Player player;
    private final Gui gui;

    public GuiEnchantEditor(Loot loot, Player player, Gui returnGui) {
        this.returnGui = returnGui;
        this.loot = loot;
        this.player = player;
        this.gui = Gui.gui()
                .rows(1)
                .title(Component.text("Enchantment Editor"))
                .disableAllInteractions().create();

        paint();
    }

    public void paint() {
        Map<String, Integer> lore = loot.getEnchants() == null ? new HashMap<>() : new HashMap<>(loot.getEnchants());

        gui.setItem(Arrays.asList(2, 6), ItemBuilder.from(XMaterial.OAK_DOOR.parseItem()).name(Component.text("Back", NamedTextColor.RED)).asGuiItem(event -> {
            returnGui.open(player);
        }));

        gui.setItem(3, ItemBuilder.from(XMaterial.ARROW.parseItem()).name(Component.text("Add new enchantment", NamedTextColor.GREEN)).asGuiItem(event -> {
            new AnvilGUI.Builder()
                    .title("Enter an enchantment")
                    .itemLeft(XMaterial.PAPER.parseItem())
                    .plugin(SongodaCore.getInstance())
                    .onComplete((player, text) -> {
                        if (Enchantment.getByName(text.toUpperCase().trim()) == null) {
                            return AnvilGUI.Response.text("Invalid enchantment");
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                new AnvilGUI.Builder()
                                        .title("Enter a level")
                                        .itemLeft(XMaterial.PAPER.parseItem())
                                        .plugin(SongodaCore.getInstance())
                                        .onComplete((player, level) -> {
                                            lore.put(text.toUpperCase().trim(), Integer.parseInt(level.trim()));
                                            loot.setEnchants(lore);
                                            player.closeInventory();

                                            return AnvilGUI.Response.close();
                                        }).onClose(player -> paint());
                            }
                        }.runTaskLater(SongodaCore.getInstance(), 1l);

                        return AnvilGUI.Response.close();
                    }).onClose(player -> paint());
        }));

        List<Component> enchantments = new ArrayList<>();
        String last = null;

        if (!lore.isEmpty()) {
            for (Map.Entry<String, Integer> entry : lore.entrySet()) {
                last = entry.getKey();
                enchantments.add(Component.text("&6" + entry.getKey() + " " + entry.getValue(), NamedTextColor.GOLD));
            }
        }

        gui.setItem(4, ItemBuilder.from(XMaterial.WRITABLE_BOOK.parseItem()).name(Component.text("Enchant Override", NamedTextColor.GRAY))
                        .lore(lore.isEmpty()
                            ? Collections.singletonList(Component.text("No enchantments set...", NamedTextColor.RED))
                                : enchantments).asGuiItem());

        String lastFinal = last;
        gui.setItem(5, ItemBuilder.from(XMaterial.ARROW.parseItem()).name(Component.text("Remove the last line", NamedTextColor.RED)).asGuiItem(event -> {
                    lore.remove(lastFinal);
                    loot.setEnchants(lore);
                    paint();
                }));
    }
}
