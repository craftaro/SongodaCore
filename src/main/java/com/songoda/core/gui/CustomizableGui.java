package com.songoda.core.gui;

import com.songoda.core.compatibility.LegacyMaterials;
import com.songoda.core.configuration.DataStoreObject;
import com.songoda.core.configuration.SimpleDataStore;
import com.songoda.core.gui.methods.Clickable;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a GUI screen that can be user-configured
 *
 * @since 2019-09-06
 * @author jascotty2
 */
public class CustomizableGui extends Gui {

    final Map<String, CustomButton> buttons;

    public CustomizableGui(SimpleDataStore<CustomButton> buttons) {
        this((Map<String, CustomButton>) (Map) buttons.getData());
    }

    public CustomizableGui(SimpleDataStore<CustomButton> buttons, Gui parent) {
        this((Map<String, CustomButton>) (Map) buttons.getData(), parent);
    }

    public CustomizableGui(@NotNull Map<String, CustomButton> buttons) {
        this(buttons, null);
    }

    public CustomizableGui(@NotNull Map<String, CustomButton> buttons, @Nullable Gui parent) {
        super(parent);
        this.buttons = buttons;
        if (buttons.containsKey("__DEFAULT__")) {
            blankItem = GuiUtils.getBorderItem(buttons.get("__DEFAULT__").icon);
        }
    }

    @NotNull
    public CustomButton[] getButtons() {
        return buttons.values().toArray(new CustomButton[buttons.size()]);
    }

    @NotNull
    @Override
    public CustomizableGui setDefaultItem(ItemStack item) {
        if ((blankItem = item) != null) {
            buttons.put("__DEFAULT__", (new CustomButton("__DEFAULT__")).setIcon(LegacyMaterials.getMaterial(item)));
        }
        return this;
    }

    @Nullable
    public CustomButton getButton(@NotNull String key) {
        return key == null ? null : buttons.get(key.toLowerCase());
    }

    @NotNull
    public CustomizableGui setItem(int defaultRow, int defaultCol, @NotNull String key, @NotNull ItemStack item) {
        final int cell = defaultCol + defaultRow * 9;
        return setItem(cell, key, item);
    }

    @NotNull
    public CustomizableGui setItem(int defaultCell, @NotNull String key, @NotNull ItemStack item) {
        CustomButton btn = key == null ? null : buttons.get(key = key.toLowerCase());
        if (btn == null) {
            buttons.put(key, btn = (new CustomButton(key, defaultCell)).setIcon(LegacyMaterials.getMaterial(item)));
        } else {
            ItemStack btnItem = btn.icon.getItem();
            ItemMeta itemMeta = item.getItemMeta();
            ItemMeta btnItemMeta = btnItem.getItemMeta();
            if (itemMeta != null && btnItemMeta != null) {
                btnItemMeta.setDisplayName(itemMeta.getDisplayName());
                btnItemMeta.setLore(itemMeta.getLore());
                btnItem.setItemMeta(itemMeta);
            }
            item = btnItem;
        }
        cellItems.put(btn.position, item);
        if (inventory != null && btn.position >= 0 && btn.position < inventory.getSize()) {
            inventory.setItem(btn.position, item);
        }
        return this;
    }

    @NotNull
    public CustomizableGui setItem(int defaultRow, int defaultCol, @NotNull String key, @NotNull LegacyMaterials defaultItem, @NotNull String title, @NotNull String... lore) {
        final int cell = defaultCol + defaultRow * 9;
        return setItem(cell, key, defaultItem, title, lore);
    }

    @NotNull
    public CustomizableGui setItem(int defaultCell, @NotNull String key, @NotNull LegacyMaterials defaultItem, @NotNull String title, @NotNull String... lore) {
        CustomButton btn = key == null ? null : buttons.get(key = key.toLowerCase());
        if (btn == null) {
            buttons.put(key, btn = (new CustomButton(key, defaultCell)).setIcon(defaultItem));
        }
        ItemStack item = GuiUtils.createButtonItem(btn.icon, title, lore);
        cellItems.put(btn.position, item);
        if (inventory != null && btn.position >= 0 && btn.position < inventory.getSize()) {
            inventory.setItem(btn.position, item);
        }
        return this;
    }

    @NotNull
    public CustomizableGui highlightItem(@NotNull String key) {
        CustomButton btn = key == null ? null : buttons.get(key.toLowerCase());
        if (btn != null) {
            this.highlightItem(btn.position);
        }
        return this;
    }

    @NotNull
    public CustomizableGui removeHighlight(@NotNull String key) {
        CustomButton btn = key == null ? null : buttons.get(key.toLowerCase());
        if (btn != null) {
            this.removeHighlight(btn.position);
        }
        return this;
    }

    @NotNull
    public CustomizableGui updateItem(@NotNull String key, @Nullable String title, @NotNull String... lore) {
        CustomButton btn = key == null ? null : buttons.get(key.toLowerCase());
        if (btn != null) {
            this.updateItem(btn.position, title, lore);
        }
        return this;
    }

    @NotNull
    public CustomizableGui updateItem(@NotNull String key, @Nullable String title, @Nullable List<String> lore) {
        CustomButton btn = key == null ? null : buttons.get(key.toLowerCase());
        if (btn != null) {
            this.updateItem(btn.position, title, lore);
        }
        return this;
    }

    @NotNull
    public CustomizableGui updateItem(@NotNull String key, @NotNull LegacyMaterials itemTo, @NotNull String title, @NotNull String... lore) {
        CustomButton btn = key == null ? null : buttons.get(key.toLowerCase());
        if (btn != null) {
            this.updateItem(btn.position, itemTo, title, lore);
        }
        return this;
    }

    @NotNull
    public CustomizableGui updateItem(@NotNull String key, @NotNull LegacyMaterials itemTo, @NotNull String title, @Nullable List<String> lore) {
        CustomButton btn = key == null ? null : buttons.get(key.toLowerCase());
        if (btn != null) {
            this.updateItem(btn.position, itemTo, title, lore);
        }
        return this;
    }

    @NotNull
    public CustomizableGui setAction(@NotNull String key, Clickable action) {
        CustomButton btn = key == null ? null : buttons.get(key = key.toLowerCase());
        if (btn != null) {
            setConditional(btn.position, null, action);
        }
        return this;
    }

    @NotNull
    public CustomizableGui setAction(@NotNull String key, @Nullable ClickType type, @Nullable Clickable action) {
        CustomButton btn = key == null ? null : buttons.get(key = key.toLowerCase());
        if (btn != null) {
            setConditional(btn.position, type, action);
        }
        return this;
    }

    @NotNull
    public CustomizableGui setButton(int defaultCell, @NotNull String key, ItemStack item, @Nullable Clickable action) {
        setItem(defaultCell, key, item);
        setAction(key, null, action);
        return this;
    }

    @NotNull
    public CustomizableGui setButton(int defaultCell, @NotNull String key, ItemStack item, @Nullable ClickType type, @Nullable Clickable action) {
        setItem(defaultCell, key, item);
        setAction(key, type, action);
        return this;
    }

    @NotNull
    public CustomizableGui setButton(int defaultRow, int defaultCol, @NotNull String key, ItemStack item, @Nullable Clickable action) {
        final int defaultCell = defaultCol + defaultRow * 9;
        setItem(defaultCell, key, item);
        setAction(key, null, action);
        return this;
    }

    @NotNull
    public CustomizableGui setButton(int defaultRow, int defaultCol, @NotNull String key, ItemStack item, @Nullable ClickType type, @Nullable Clickable action) {
        final int defaultCell = defaultCol + defaultRow * 9;
        setItem(defaultCell, key, item);
        setAction(key, type, action);
        return this;
    }

    @NotNull
    @Override
    public CustomizableGui setNextPage(int row, int col, @NotNull ItemStack item) {
        return this.setNextPage(col + row * 9, item);
    }

    @NotNull
    @Override
    public CustomizableGui setNextPage(int cell, @NotNull ItemStack item) {
        CustomButton btn = buttons.get("__NEXT__");
        if (btn == null) {
            buttons.put("__NEXT__", btn = (new CustomButton("__NEXT__", cell)).setIcon(LegacyMaterials.getMaterial(item)));
        } else {
            ItemStack btnItem = btn.icon.getItem();
            ItemMeta itemMeta = item.getItemMeta();
            ItemMeta btnItemMeta = btnItem.getItemMeta();
            if (itemMeta != null && btnItemMeta != null) {
                btnItemMeta.setDisplayName(itemMeta.getDisplayName());
                btnItemMeta.setLore(itemMeta.getLore());
                btnItem.setItemMeta(itemMeta);
            }
            item = btnItem;
        }
        return (CustomizableGui) super.setNextPage(btn.position, item);
    }

    @NotNull
    @Override
    public CustomizableGui setPrevPage(int row, int col, @NotNull ItemStack item) {
        return this.setPrevPage(col + row * 9, item);
    }

    @NotNull
    @Override
    public CustomizableGui setPrevPage(int cell, @NotNull ItemStack item) {
        CustomButton btn = buttons.get("__PREV__");
        if (btn == null) {
            buttons.put("__PREV__", btn = (new CustomButton("__PREV__", cell)).setIcon(LegacyMaterials.getMaterial(item)));
        } else {
            ItemStack btnItem = btn.icon.getItem();
            ItemMeta itemMeta = item.getItemMeta();
            ItemMeta btnItemMeta = btnItem.getItemMeta();
            if (itemMeta != null && btnItemMeta != null) {
                btnItemMeta.setDisplayName(itemMeta.getDisplayName());
                btnItemMeta.setLore(itemMeta.getLore());
                btnItem.setItemMeta(itemMeta);
            }
            item = btnItem;
        }
        return (CustomizableGui) super.setPrevPage(btn.position, item);
    }

    public CustomizableGui clearActions(@NotNull String key) {
        CustomButton btn = key == null ? null : buttons.get(key = key.toLowerCase());
        if (btn != null) {
            this.clearActions(btn.position);
        }
        return this;
    }

    public static class CustomButton implements DataStoreObject<String> {

        boolean _changed = false;
        final String key;
        int position = -1;
        LegacyMaterials icon = LegacyMaterials.STONE;

        public CustomButton(String key) {
            this.key = key;
        }

        public CustomButton(String key, int position) {
            this.key = key;
            this.position = position;
        }

        public static CustomButton loadFromSection(ConfigurationSection sec) {
            CustomButton dat = new CustomButton(sec.getName());
            dat.icon = sec.contains("icon") ? LegacyMaterials.getMaterial(sec.getString("icon"), LegacyMaterials.STONE) : LegacyMaterials.STONE;
            dat.position = sec.getInt("position");
            return dat;
        }

        @Override
        public void saveToSection(ConfigurationSection sec) {
            sec.set("icon", icon.name());
            sec.set("position", position);
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getConfigKey() {
            return key;
        }

        @Override
        public boolean hasChanged() {
            return _changed;
        }

        @Override
        public void setChanged(boolean isChanged) {
            _changed = isChanged;
        }

        public LegacyMaterials getIcon() {
            return icon;
        }

        public CustomButton setIcon(LegacyMaterials icon) {
            this.icon = icon != null ? icon : LegacyMaterials.STONE;
            _changed = true;
            return this;
        }
    }
}
