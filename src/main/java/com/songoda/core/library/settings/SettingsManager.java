package com.songoda.core.library.settings;

import com.songoda.core.library.compatibility.ServerVersion;
import com.songoda.core.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
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

import java.io.*;
import java.util.*;

/**
 * Created by songoda on 6/4/2017.
 */
public abstract class SettingsManager implements Listener {

    private final JavaPlugin plugin;
    private Map<Player, String> cat = new HashMap<>();
    private Map<Player, String> current = new HashMap<>();

    public SettingsManager(JavaPlugin plugin) {
        this.plugin = plugin;
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

            if (plugin.getConfig().get(key).getClass().getName().equals("java.lang.Boolean")) {
                this.plugin.getConfig().set(key, !plugin.getConfig().getBoolean(key));
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
        FileConfiguration config = plugin.getConfig();
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
        this.saveConfig();
        this.openEditor(player);
    }

    private void editObject(Player player, String current) {
        this.current.put(player, ChatColor.stripColor(current));

        player.closeInventory();
        player.sendMessage("");
        player.sendMessage(Methods.formatText("&7Please enter a value for &6" + current + "&7."));
        if (plugin.getConfig().isInt(current) || plugin.getConfig().isDouble(current)) {
            player.sendMessage(Methods.formatText("&cUse only numbers."));
        }
        player.sendMessage("");
    }

    public void openSettingsManager(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, plugin.getName() + " Settings Manager");

        int slot = 10;
        for (String key : plugin.getConfig().getDefaultSection().getKeys(false)) {
            ItemStack item = new ItemStack(ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13) ? Material.LEGACY_WOOL : Material.valueOf("WOOL"), 1, (byte) (slot - 9));
            ItemMeta meta = item.getItemMeta();
            meta.setLore(Collections.singletonList(Methods.formatText("&6Click To Edit This Category.")));
            meta.setDisplayName(Methods.formatText("&f&l" + key));
            item.setItemMeta(meta);
            inventory.setItem(slot, item);
            slot++;
        }

        player.openInventory(inventory);
    }

    private void openEditor(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, plugin.getName() + " Settings Editor");
        FileConfiguration config = plugin.getConfig();

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

            Setting setting = Config.getSetting(fKey);

            if (setting != null && setting.getComments() != null) {
                lore.add("");

                String comment = String.join(" ", setting.getComments());

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

    public void reloadConfig() {
        plugin.reloadConfig();
        this.setupConfig();
    }

    public void setupConfig() {
        FileConfiguration config = plugin.getConfig();

        for (Setting setting : Config.getSettings()) {
            config.addDefault(setting.getCompleteKey(), setting.getDefaultValue());
        }
        plugin.getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void saveConfig() {

        // Delete old config values.
        FileConfiguration configuration = plugin.getConfig();
        for (String line : configuration.getConfigurationSection("").getKeys(true)) {
            if (line.contains(".") && Config.getSetting(line) == null)
                configuration.set(line, null);
            else if (!line.contains(".")) {
                if (((MemorySection) configuration.get(line)).getKeys(true).size() == 0)
                    configuration.set(line, null);
            }
        }

        // Add comments.
        String dump = plugin.getConfig().saveToString();
        StringBuilder config = new StringBuilder();
        BufferedReader bufReader = new BufferedReader(new StringReader(dump));

        try {
            boolean first = true;

            String line;
            int currentTab = 0;
            String category = "";

            while ((line = bufReader.readLine()) != null) {
                if (line.trim().startsWith("#")) continue;

                int tabChange = line.length() - line.trim().length();
                if (currentTab != tabChange) {
                    category = category.contains(".") && tabChange != 0 ? category.substring(0, category.indexOf(".")) : "";
                    currentTab = tabChange;
                }

                if (line.endsWith(":")) {
                    bufReader.mark(1000);
                    String found = bufReader.readLine();
                    bufReader.reset();

                    if (!found.trim().startsWith("-")) {

                        String newCategory = line.substring(0, line.length() - 1).trim();

                        if (category.equals(""))
                            category = newCategory;
                        else
                            category += "." + newCategory;

                        currentTab = tabChange + 2;

                        if (!first) {
                            config.append("\n\n");
                        } else {
                            first = false;
                        }

                        if (!category.contains("."))
                            config.append("#").append("\n");
                        try {
                            Category categoryObj = Config.getCategory(category);

                            config.append(new String(new char[tabChange]).replace('\0', ' '));
                            for (String l : categoryObj.getComments())
                                config.append("# ").append(l).append("\n");
                        } catch (IllegalArgumentException e) {
                            config.append("# ").append(category).append("\n");
                        }
                        if (!category.contains("."))
                            config.append("#").append("\n");

                        config.append(line).append("\n");

                        continue;
                    }
                }

                if (line.trim().startsWith("-")) {
                    config.append(line).append("\n");
                    continue;
                }

                String key = category + "." + (line.split(":")[0].trim());
                for (Setting setting : Config.getSettings()) {
                    if (!setting.getCompleteKey().equals(key) || setting.getComments() == null) continue;
                    config.append("  ").append("\n");
                    for (String l : setting.getComments()) {
                        config.append(new String(new char[currentTab]).replace('\0', ' '));
                        config.append("# ").append(l).append("\n");
                    }
                }
                config.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (!plugin.getDataFolder().exists())
                plugin.getDataFolder().mkdir();
            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(new File(plugin.getDataFolder() + File.separator + "config.yml")));
            writer.write(config.toString());
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}