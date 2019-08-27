package com.songoda.core.gui;

import com.songoda.core.compatibility.LegacyMaterials;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @since 2019-08-25
 * @author jascotty2
 */
public class GuiUtils {

    public static ItemStack getBorderGlassItem() {
        ItemStack glass = LegacyMaterials.LIGHT_BLUE_STAINED_GLASS_PANE.getItem();
        ItemMeta glassmeta = glass.getItemMeta();
        glassmeta.setDisplayName(ChatColor.BLACK.toString());
        glass.setItemMeta(glassmeta);
        return glass;
    }

    public static ItemStack getBorderItem(ItemStack item) {
        ItemMeta glassmeta = item.getItemMeta();
        glassmeta.setDisplayName(ChatColor.BLACK.toString());
        item.setItemMeta(glassmeta);
        return item;
    }

    public static ItemStack getBorderItem(Material mat) {
        ItemStack item = new ItemStack(mat);
        ItemMeta glassmeta = item.getItemMeta();
        glassmeta.setDisplayName(ChatColor.BLACK.toString());
        item.setItemMeta(glassmeta);
        return item;
    }

    public static ItemStack getBorderItem(LegacyMaterials mat) {
        ItemStack item = mat.getItem();
        ItemMeta glassmeta = item.getItemMeta();
        glassmeta.setDisplayName(ChatColor.BLACK.toString());
        item.setItemMeta(glassmeta);
        return item;
    }

    public static ItemStack createButtonItem(Material mat, String title, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        if (lore != null) {
            meta.setLore(Arrays.asList(lore.length == 1 ? lore[0].split("\n") : lore));
        } else {
            meta.setLore(Collections.EMPTY_LIST);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createButtonItem(LegacyMaterials mat, String title, String... lore) {
        ItemStack item = mat.getItem();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        if (lore != null) {
            meta.setLore(Arrays.asList(lore.length == 1 ? lore[0].split("\n") : lore));
        } else {
            meta.setLore(Collections.EMPTY_LIST);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createButtonItem(ItemStack from, String title, String... lore) {
        ItemStack item = from.clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        if (lore != null) {
            meta.setLore(Arrays.asList(lore.length == 1 ? lore[0].split("\n") : lore));
        } else {
            meta.setLore(Collections.EMPTY_LIST);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createButtonItem(Material mat, String title, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        if (lore != null) {
            meta.setLore(lore.size() == 1 ? Arrays.asList(lore.get(0).split("\n")) : lore);
        } else {
            meta.setLore(Collections.EMPTY_LIST);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createButtonItem(LegacyMaterials mat, String title, List<String> lore) {
        ItemStack item = mat.getItem();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        if (lore != null) {
            meta.setLore(lore.size() == 1 ? Arrays.asList(lore.get(0).split("\n")) : lore);
        } else {
            meta.setLore(Collections.EMPTY_LIST);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createButtonItem(ItemStack from, String title, List<String> lore) {
        ItemStack item = from.clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        if (lore != null) {
            meta.setLore(lore.size() == 1 ? Arrays.asList(lore.get(0).split("\n")) : lore);
        } else {
            meta.setLore(Collections.EMPTY_LIST);
        }
        item.setItemMeta(meta);
        return item;
    }
}
