package com.songoda.core.gui;

import com.songoda.core.compatibility.LegacyMaterials;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
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

    public static ItemStack getBorderItem(LegacyMaterials mat) {
        ItemStack item = mat.getItem();
        ItemMeta glassmeta = item.getItemMeta();
        glassmeta.setDisplayName(ChatColor.BLACK.toString());
        item.setItemMeta(glassmeta);
        return item;
    }

    public static List<String> getSafeLore(String... lines) {
        return getSafeLore(Arrays.asList(lines));
    }

    /**
     * Get a lore value that will display fine on clients using auto gui scaling
     *
     * @param lines lines to format
     * @return newline and length-corrected item lore
     */
    public static List<String> getSafeLore(List<String> lines) {
        // fix newlines
        ArrayList<String> newLore = new ArrayList();
        for (String l : lines) {
            for (String l2 : l.split("\n")) {
                if (l2.length() < 54) {
                    newLore.add(l2);
                } else {
                    // try to shorten the string
                    String shorterString = l2;
                    ChatColor lastColor = null; // todo? probably should also track formatting codes..
                    int line = 0;
                    while (shorterString.length() > 50) {
                        int breakingSpace = -1;
                        for (int i = 0; i < 50; ++i) {
                            if (shorterString.charAt(i) == ChatColor.COLOR_CHAR) {
                                lastColor = ChatColor.getByChar(shorterString.charAt(++i));
                            } else if (shorterString.charAt(i) == ' ' || shorterString.charAt(i) == '-') {
                                breakingSpace = i;
                            }
                        }
                        if (breakingSpace == -1) {
                            breakingSpace = Math.max(50, shorterString.length());
                            newLore.add((line != 0 && lastColor != null ? lastColor.toString() : "") + shorterString.substring(0, breakingSpace) + "-");
                            shorterString = breakingSpace == shorterString.length() ? "" : shorterString.substring(breakingSpace + 1);
                        } else {
                            newLore.add((line != 0 && lastColor != null ? lastColor.toString() : "") + shorterString.substring(0, breakingSpace));
                            shorterString = breakingSpace == shorterString.length() ? "" : shorterString.substring(breakingSpace + 1);
                        }
                        ++line;
                    }
                    if (!shorterString.isEmpty()) {
                        newLore.add((line != 0 && lastColor != null ? lastColor.toString() : "") + "   " + shorterString);
                    }
                }
            }
        }
        return newLore;
    }

    public static ItemStack createButtonItem(LegacyMaterials mat, String title, String... lore) {
        ItemStack item = mat.getItem();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.EMPTY_LIST);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createButtonItem(ItemStack from, String title, String... lore) {
        ItemStack item = from.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.EMPTY_LIST);
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createButtonItem(LegacyMaterials mat, String title, List<String> lore) {
        ItemStack item = mat.getItem();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.EMPTY_LIST);
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createButtonItem(ItemStack from, String title, List<String> lore) {
        ItemStack item = from.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.EMPTY_LIST);
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack updateItem(ItemStack item, String title, String... lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.EMPTY_LIST);
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack updateItem(ItemStack item, LegacyMaterials matTo, String title, String... lore) {
        if (!matTo.matches(item)) {
            item = matTo.getItem();
        }
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.EMPTY_LIST);
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack updateItem(ItemStack item, ItemStack to, String title, String... lore) {
        if (!LegacyMaterials.getMaterial(item).matches(to)) {
            item = to.clone();
        }
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.EMPTY_LIST);
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack updateItem(ItemStack item, String title, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.EMPTY_LIST);
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack updateItem(ItemStack item, LegacyMaterials matTo, String title, List<String> lore) {
        if (!matTo.matches(item)) {
            item = matTo.getItem();
        }
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.EMPTY_LIST);
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack updateItem(ItemStack item, ItemStack to, String title, List<String> lore) {
        if (!LegacyMaterials.getMaterial(item).matches(to)) {
            item = to.clone();
        }
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.EMPTY_LIST);
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    public static void mirrorFill(Gui gui, int row, int col, boolean mirrorRow, boolean mirrorCol, ItemStack item) {
        gui.setItem(row, col, item);
        if (mirrorRow) {
            gui.setItem(gui.rows - row - 1, col, item);
        }
        if (mirrorCol) {
            gui.setItem(row, 8 - col, item);
        }
        if (mirrorRow && mirrorCol) {
            gui.setItem(gui.rows - row - 1, 8 - col, item);
        }
    }
}
