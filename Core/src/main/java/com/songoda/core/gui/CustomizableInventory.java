package com.songoda.core.gui;

import com.songoda.core.SongodaPlugin;
import com.songoda.core.configuration.Config;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.simpleyaml.configuration.ConfigurationSection;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class CustomizableInventory {

    protected final SongodaPlugin plugin;
    protected final Player player;
    private final String key;
    private final Config config;
    protected final Gui gui;

    public CustomizableInventory(SongodaPlugin plugin, Player player, String key) {
        this.plugin = plugin;
        this.player = player;
        this.key = key;

        this.config = plugin.createUpdatingConfig(new File(plugin.getDataFolder() + File.separator + "inventories", key + ".yml"));

        this.gui = Gui.gui()
                .rows(config.getInt("settings.rows"))
                .disableAllInteractions()
                .title(MiniMessage.miniMessage().deserialize(config.getString("settings.title")))
                .create();
    }

    public void open() {
        gui.getGuiItems().clear();

        fill(player);
        fillWithCustom(player);

        gui.open(player);
    }

    protected abstract void fill(Player player);

    private void fillWithCustom(Player player) {
        if (!config.isConfigurationSection("customItems")) {
            return;
        }

        for (String key : config.getConfigurationSection("customItems").getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection("customItems." + key);
            List<String> actions = section.getStringList("actions");
            int slot = section.getInt("slot");

            gui.setItem(slot, getItem("customItems." + key, Collections.emptyMap(), event -> plugin.getActionManager().executeActions(player, actions)));
        }
    }

    protected int getSlot(String route) {
        ConfigurationSection section = config.getConfigurationSection(route);
        return section.getInt("slot");
    }

    protected GuiItem getItem(String route, Map<String, String> placeholders, Consumer<InventoryClickEvent> consumer) {
        ConfigurationSection section = config.getConfigurationSection(route);

        Material material = Material.getMaterial(section.getString("material"));
        Component name = MiniMessage.miniMessage().deserialize(plugin.getPlaceholderResolver().setPlaceholders(player, section.getString("name")))
                .applyFallbackStyle(TextDecoration.ITALIC.withState(false));
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            name = name.replaceText(text -> text.matchLiteral(entry.getKey()).replacement(entry.getValue()));
        }

        List<Component> lore = section.getStringList("lore")
                .stream()
                .map(text -> MiniMessage.miniMessage().deserialize(plugin.getPlaceholderResolver().setPlaceholders(player, text))
                        .applyFallbackStyle(TextDecoration.ITALIC.withState(false)))
                .collect(Collectors.toList());
        lore.replaceAll(component -> {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                component = component.replaceText(text -> text.matchLiteral(entry.getKey()).replacement(entry.getValue()));
            }
            return component;
        });

        String texture = section.getString("texture");

        return ItemBuilder.from(material).name(name).lore(lore).setSkullTexture(texture).asGuiItem(consumer::accept);
    }


    protected void setItem(String itemKey, Map<String, String> placeholders) {
        setItem(itemKey, placeholders, event -> {});
    }

    protected void setItem(String itemKey) {
        setItem(itemKey, event -> {});
    }

    protected void setItem(String itemKey, Consumer<InventoryClickEvent> consumer) {
        setItem(itemKey, Collections.emptyMap(), consumer);
    }

    protected void setItem(String itemKey, Map<String, String> placeholders, Consumer<InventoryClickEvent> consumer) {
        gui.setItem(getSlot("items." + itemKey), getItem("items." + itemKey, placeholders, consumer));
    }
}
