package com.songoda.core.library.settings;

import com.songoda.core.library.compatibility.ServerVersion;
import com.songoda.core.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Created by songoda on 6/4/2017.
 */
public class SettingsManagerOld implements Listener {

    private final JavaPlugin plugin;
    private final Config config;

    private Map<Player, String> cat = new HashMap<>();
    private Map<Player, String> current = new HashMap<>();

    public SettingsManagerOld(Config config) {
        this.plugin = config.getPlugin();
        this.config = config;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getType() != InventoryType.CHEST) return;
        ItemStack clickedItem = event.getCurrentItem();

        if (event.getInventory() != event.getWhoClicked().getOpenInventory().getTopInventory()
                || clickedItem == null || !clickedItem.hasItemMeta()
                || !clickedItem.getItemMeta().hasDisplayName()) {
            return;
        }

        if (event.getView().getTitle().equals(plugin.getName() + " Settings Manager")) {
            event.setCancelled(true);
            if (clickedItem.getType().name().contains("STAINED_GLASS")) return;

            String type = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            this.cat.put((Player) event.getWhoClicked(), type);
            this.openEditor((Player) event.getWhoClicked());
        } else if (event.getView().getTitle().equals(plugin.getName() + " Settings Editor")) {
            event.setCancelled(true);
            if (clickedItem.getType().name().contains("STAINED_GLASS")) return;

            Player player = (Player) event.getWhoClicked();

            String key = cat.get(player) + "." + ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

            if (config.getFileConfiguration().get(key).getClass().getName().equals("java.lang.Boolean")) {
                this.config.getFileConfiguration().set(key, !config.getFileConfiguration().getBoolean(key));
                this.finishEditing(player);
            } else {
                this.editObject(player, key);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!current.containsKey(player)) return;

        String value = current.get(player);
        FileConfiguration config = this.config.getFileConfiguration();
        if (config.isLong(value)) {
            config.set(value, Long.parseLong(event.getMessage()));
        } else if (config.isInt(value)) {
            config.set(value, Integer.parseInt(event.getMessage()));
        } else if (config.isDouble(value)) {
            config.set(value, Double.parseDouble(event.getMessage()));
        } else if (config.isString(value)) {
            config.set(value, event.getMessage());
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () ->
                this.finishEditing(player), 0L);

        event.setCancelled(true);
    }

    private void finishEditing(Player player) {
        this.current.remove(player);
        this.config.save();
        this.openEditor(player);
    }

    private void editObject(Player player, String current) {
        this.current.put(player, ChatColor.stripColor(current));

        player.closeInventory();
        player.sendMessage("");
        player.sendMessage(Methods.formatText("&7Please enter a value for &6" + current + "&7."));
        if (config.getFileConfiguration().isInt(current) || config.getFileConfiguration().isDouble(current)) {
            player.sendMessage(Methods.formatText("&cUse only numbers."));
        }
        player.sendMessage("");
    }

    public void openSettingsManager(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, plugin.getName() + " Settings Manager");

        int slot = 10;
        for (Category category : config.getCategories()) {
            ItemStack item = new ItemStack(ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13) ? Material.LEGACY_WOOL : Material.valueOf("WOOL"), 1, (byte) (slot - 9));
            ItemMeta meta = item.getItemMeta();
            meta.setLore(Collections.singletonList(Methods.formatText("&6Click To Edit This Category.")));
            meta.setDisplayName(Methods.formatText("&f&l" + category.getKey()));
            item.setItemMeta(meta);
            inventory.setItem(slot, item);
            slot++;
        }

        player.openInventory(inventory);
    }

    private void openEditor(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, plugin.getName() + " Settings Editor");
        FileConfiguration config = this.config.getFileConfiguration();

        int slot = 0;
        for (String key : config.getConfigurationSection(cat.get(player)).getKeys(true)) {
            String fKey = cat.get(player) + "." + key;
            ItemStack item = new ItemStack(Material.DIAMOND_HELMET);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Methods.formatText("&6" + key));

            List<String> lore = new ArrayList<>();
            if (config.isBoolean(fKey)) {
                item.setType(Material.LEVER);
                lore.add(Methods.formatText(config.getBoolean(fKey) ? "&atrue" : "&cfalse"));
            } else if (config.isString(fKey)) {
                item.setType(Material.PAPER);
                lore.add(Methods.formatText("&7" + config.getString(fKey)));
            } else if (config.isInt(fKey)) {
                item.setType(ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13) ? Material.CLOCK : Material.valueOf("WATCH"));
                lore.add(Methods.formatText("&7" + config.getInt(fKey)));
            } else if (config.isLong(fKey)) {
                item.setType(ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13) ? Material.CLOCK : Material.valueOf("WATCH"));
                lore.add(Methods.formatText("&7" + config.getLong(fKey)));
            } else if (config.isDouble(fKey)) {
                item.setType(ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13) ? Material.CLOCK : Material.valueOf("WATCH"));
                lore.add(Methods.formatText("&7" + config.getDouble(fKey)));
            }

            Setting setting = this.config.getSetting(fKey);

            if (setting instanceof FoundSetting && ((FoundSetting)setting).getComments() != null) {
                lore.add("");

                String comment = String.join(" ", ((FoundSetting)setting).getComments());

                int lastIndex = 0;
                for (int n = 0; n < comment.length(); n++) {
                    if (n - lastIndex < 30)
                        continue;

                    if (comment.charAt(n) == ' ') {
                        lore.add(Methods.formatText("&8" + comment.substring(lastIndex, n).trim()));
                        lastIndex = n;
                    }
                }

                if (lastIndex - comment.length() < 30)
                    lore.add(Methods.formatText("&8" + comment.substring(lastIndex).trim()));

            }

            meta.setLore(lore);
            item.setItemMeta(meta);

            inventory.setItem(slot, item);
            slot++;
        }

        player.openInventory(inventory);
    }
}