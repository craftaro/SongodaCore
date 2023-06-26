package com.craftaro.core.gui;

import com.craftaro.core.CraftaroPlugin;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.simpleyaml.configuration.ConfigurationSection;

import java.util.Map;
import java.util.function.Consumer;

public abstract class CustomizablePagedInventory extends BaseCustomizable {

    protected final PaginatedGui gui;
    public CustomizablePagedInventory(CraftaroPlugin plugin, Player player, String key) {
        super(plugin, player, key);

        this.gui = Gui.paginated()
                .pageSize(config.getInt("settings.pageSize"))
                .rows(config.getInt("settings.rows"))
                .title(MiniMessage.miniMessage().deserialize(config.getString("settings.title")))
                .create();
    }

    public void open() {
        gui.getGuiItems().clear();

        fill(player);
        fillWithCustom(player);

        gui.open(player);
    }

    @Override
    public void setItem(String itemKey, Map<String, String> placeholders, Consumer<InventoryClickEvent> consumer) {
        ConfigurationSection section = config.getConfigurationSection("items." + itemKey);
        gui.setItem(getSlot("items." + itemKey), getItem(section, placeholders, consumer));
    }

    @Override
    public void setCustomItem(int slot, GuiItem item) {
        gui.setItem(slot, item);
    }
}
