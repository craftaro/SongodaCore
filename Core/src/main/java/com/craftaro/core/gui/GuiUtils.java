package com.craftaro.core.gui;

import com.craftaro.core.compatibility.CompatibleMaterial;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuiUtils {
    public static ItemStack getBorderGlassItem() {
        ItemStack glass = CompatibleMaterial.LIGHT_BLUE_STAINED_GLASS_PANE.getItem();
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

    public static ItemStack getBorderItem(CompatibleMaterial mat) {
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
     *
     * @return newline and length-corrected item lore
     */
    public static List<String> getSafeLore(List<String> lines) {
        // fix newlines
        ArrayList<String> newLore = new ArrayList<>();

        for (String l : lines) {
            for (String l2 : l.split("\n")) {
                if (l2.length() < 54) {
                    newLore.add(l2);
                    continue;
                }

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

        return newLore;
    }

    public static ItemStack createButtonItem(CompatibleMaterial mat, String title, String... lore) {
        ItemStack item = mat.getItem();
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(title);

            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createButtonItem(CompatibleMaterial mat, int amount, String title, String... lore) {
        ItemStack item = mat.getItem();
        item.setAmount(amount);

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(title);

            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.emptyList());
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
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createButtonItem(CompatibleMaterial mat, String title, List<String> lore) {
        ItemStack item = mat.getItem();
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(title);

            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createButtonItem(CompatibleMaterial mat, int amount, String title, List<String> lore) {
        ItemStack item = mat.getItem();
        item.setAmount(amount);

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(title);

            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

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
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createButtonItem(CompatibleMaterial mat, String[] lore) {
        ItemStack item = mat.getItem();
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (lore != null && lore.length != 0) {
                List<String> safe = getSafeLore(lore);

                meta.setDisplayName(safe.get(0));
                meta.setLore(safe.subList(1, safe.size()));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createButtonItem(CompatibleMaterial mat, int amount, String[] lore) {
        ItemStack item = mat.getItem();
        item.setAmount(amount);

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (lore != null && lore.length != 0) {
                List<String> safe = getSafeLore(lore);

                meta.setDisplayName(safe.get(0));
                meta.setLore(safe.subList(1, safe.size()));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createButtonItem(ItemStack from, String[] lore) {
        ItemStack item = from.clone();
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (lore != null && lore.length != 0) {
                List<String> safe = getSafeLore(lore);

                meta.setDisplayName(safe.get(0));
                meta.setLore(safe.subList(1, safe.size()));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createButtonItem(CompatibleMaterial mat, List<String> lore) {
        ItemStack item = mat.getItem();
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (lore != null && !lore.isEmpty()) {
                List<String> safe = getSafeLore(lore);

                meta.setDisplayName(safe.get(0));
                meta.setLore(safe.subList(1, safe.size()));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createButtonItem(CompatibleMaterial mat, int amount, List<String> lore) {
        ItemStack item = mat.getItem();
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (lore != null && !lore.isEmpty()) {
                List<String> safe = getSafeLore(lore);

                meta.setDisplayName(safe.get(0));
                meta.setLore(safe.subList(1, safe.size()));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack createButtonItem(ItemStack from, List<String> lore) {
        ItemStack item = from.clone();
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (lore != null && !lore.isEmpty()) {
                List<String> safe = getSafeLore(lore);

                meta.setDisplayName(safe.get(0));
                meta.setLore(safe.subList(1, safe.size()));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack updateItem(ItemStack item, String title, String... lore) {
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(title);

            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack updateItemName(ItemStack item, String title) {
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(title);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack updateItemLore(ItemStack item, String... lore) {
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (lore != null && lore.length != 0) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack updateItemLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack updateItem(ItemStack item, CompatibleMaterial matTo, String title, String... lore) {
        if (!matTo.matches(item)) {
            item = matTo.getItem();
        }

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(title);

            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack updateItem(ItemStack item, ItemStack to, String title, String... lore) {
        if (!CompatibleMaterial.getMaterial(item).matches(to)) {
            item = to.clone();
        }

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(title);

            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack updateItem(ItemStack item, String title, List<String> lore) {
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(title);

            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack updateItem(ItemStack item, CompatibleMaterial matTo, String title, List<String> lore) {
        if (!matTo.matches(item)) {
            item = matTo.getItem();
        }

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(title);

            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack updateItem(ItemStack item, ItemStack to, String title, List<String> lore) {
        if (!CompatibleMaterial.getMaterial(item).matches(to)) {
            item = to.clone();
        }

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(title);

            if (lore != null) {
                meta.setLore(getSafeLore(lore));
            } else {
                meta.setLore(Collections.emptyList());
            }

            item.setItemMeta(meta);
        }

        return item;
    }
}
