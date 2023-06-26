package com.craftaro.core.lootables.gui;

import com.craftaro.core.CraftaroCore;
import com.craftaro.core.lootables.loot.Loot;
import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractGuiListEditor {
    protected final Loot loot;
    protected final Player player;
    private final Gui gui;
    private final Gui returnGui;

    public AbstractGuiListEditor(Loot loot, Player player, Component title, Gui returnGui) {
        this.player = player;
        this.returnGui = returnGui;
        this.loot = loot;

        this.gui = Gui.gui()
                .title(title)
                .rows(1)
                .disableAllInteractions()
                .create();

        paint();
    }

    public void paint() {
        List<String> lore = (getData() == null ? new ArrayList<>() : getData());

        gui.setItem(Arrays.asList(2, 6), ItemBuilder.from(XMaterial.OAK_DOOR.parseItem()).name(Component.text("Back", NamedTextColor.RED)).asGuiItem(event -> {
            returnGui.open(player);
        }));

        gui.setItem(3, ItemBuilder.from(XMaterial.ARROW.parseItem()).name(Component.text("Add new line", NamedTextColor.GREEN)).asGuiItem(event -> {
                    new AnvilGUI.Builder()
                            .title("Enter a new line")
                            .itemLeft(XMaterial.PAPER.parseItem())
                            .plugin(CraftaroCore.getInstance())
                            .onComplete((player, text) -> {
                                String validated = validate(text);
                                if (validated != null) {
                                    lore.add(validated);
                                    updateData(lore);
                                }

                                return AnvilGUI.Response.close();
                            }).onClose(player -> paint());
                }));

        gui.setItem(4, ItemBuilder.from(XMaterial.WRITABLE_BOOK.parseItem()).name(Component.text("Lore:", NamedTextColor.BLUE))
                .lore(lore.isEmpty()
                        ? Collections.singletonList(Component.text("No lore set...", NamedTextColor.RED))
                        : lore.stream().map(text -> MiniMessage.miniMessage().deserialize(text)).collect(Collectors.toList())).asGuiItem());

        gui.setItem(5, ItemBuilder.from(XMaterial.ARROW.parseItem()).name(Component.text("Remove the last line", NamedTextColor.RED)).asGuiItem(event -> {
            lore.remove(lore.size() - 1);
            updateData(lore);
            paint();
        }));
    }

    protected abstract List<String> getData();

    protected abstract void updateData(List<String> list);

    protected abstract String validate(String line);
}
