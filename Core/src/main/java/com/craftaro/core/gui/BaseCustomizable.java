package com.craftaro.core.gui;

import com.craftaro.core.CraftaroPlugin;
import com.craftaro.core.configuration.Config;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import me.clip.placeholderapi.PlaceholderAPI;
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

public abstract class BaseCustomizable {

    protected final CraftaroPlugin plugin;
    protected final Player player;
    protected final String key;
    protected final Config config;

    public BaseCustomizable(CraftaroPlugin plugin, Player player, String key) {
        this.plugin = plugin;
        this.player = player;
        this.key = key;

        this.config = plugin.createUpdatingConfig(new File(plugin.getDataFolder() + File.separator + "inventories", key + ".yml"));
    }

    public abstract void open();

    protected abstract void fill(Player player);

    protected void fillWithCustom(Player player) {
        if (!config.isConfigurationSection("customItems")) {
            return;
        }

        for (String key : config.getConfigurationSection("customItems").getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection("customItems." + key);
            List<String> actions = section.getStringList("actions");
            int slot = section.getInt("slot");

            setCustomItem(slot, getItem(section, Collections.emptyMap(), event -> plugin.getActionManager().executeActions(player, actions)));
        }
    }

    public List<Integer> getSlot(String route) {
        ConfigurationSection section = config.getConfigurationSection(route);
        List<Integer> slots = section.getIntegerList("slots");
        if (!slots.isEmpty()) {
            return slots;
        }

        int slot = section.getInt("slot");
        return Collections.singletonList(slot);
    }

    public GuiItem getItem(ConfigurationSection section, Map<String, String> placeholders, Consumer<InventoryClickEvent> consumer) {
        Material material = Material.getMaterial(section.getString("material"));

        String nameString = section.getString("name");
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            nameString = nameString.replace(entry.getKey(), entry.getValue());
        }

        Component name = MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(player, nameString))
                .applyFallbackStyle(TextDecoration.ITALIC.withState(false));


        List<String> loreString = section.getStringList("lore");
        loreString.replaceAll(text -> {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                text = text.replace(entry.getKey(), entry.getValue());
            }
            return text;
        });
        List<Component> lore = loreString.stream()
                .map(text -> MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(player, text))
                        .applyFallbackStyle(TextDecoration.ITALIC.withState(false)))
                .collect(Collectors.toList());

        String texture = section.getString("texture");
        boolean glow = section.getBoolean("glow");
        int modelData = section.getInt("modelData");

        return ItemBuilder.from(material)
                .name(name)
                .lore(lore)
                .setSkullTexture(texture)
                .glow(glow)
                .model(modelData)
                .asGuiItem(consumer::accept);
    }


    public void setItem(String itemKey, Map<String, String> placeholders) {
        setItem(itemKey, placeholders, event -> {});
    }

    public void setItem(String itemKey) {
        setItem(itemKey, event -> {});
    }

    public void setItem(String itemKey, Consumer<InventoryClickEvent> consumer) {
        setItem(itemKey, Collections.emptyMap(), consumer);
    }

    public abstract void setItem(String itemKey, Map<String, String> placeholders, Consumer<InventoryClickEvent> consumer);
    public abstract void setCustomItem(int slot, GuiItem item);

    public Player getPlayer() {
        return player;
    }

    public Config getConfig() {
        return config;
    }
}
