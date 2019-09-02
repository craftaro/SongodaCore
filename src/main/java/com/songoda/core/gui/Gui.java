package com.songoda.core.gui;

import com.songoda.core.compatibility.LegacyMaterials;
import com.songoda.core.gui.events.GuiClickEvent;
import com.songoda.core.gui.events.GuiCloseEvent;
import com.songoda.core.gui.events.GuiDropItemEvent;
import com.songoda.core.gui.events.GuiOpenEvent;
import com.songoda.core.gui.methods.Pagable;
import com.songoda.core.gui.methods.Clickable;
import com.songoda.core.gui.methods.Droppable;
import com.songoda.core.gui.methods.Closable;
import com.songoda.core.gui.methods.Openable;
import com.songoda.core.utils.ItemUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * TODO: animated buttons
 *
 * @since 2019-08-25
 * @author jascotty2
 */
public class Gui {

    protected Inventory inventory;
    protected String title;
    protected GuiType inventoryType = GuiType.STANDARD;
    protected int rows, page, pages;
    protected boolean acceptsItems = false;
    protected boolean allowDropItems = true;
    protected boolean allowClose = true;
    protected final Map<Integer, Boolean> unlockedCells = new HashMap<>();
    protected final Map<Integer, ItemStack> cellItems = new HashMap<>();
    protected final Map<Integer, Map<ClickType, Clickable>> conditionalButtons = new HashMap<>();
    protected ItemStack blankItem = GuiUtils.getBorderGlassItem();
    protected int nextPageIndex, prevPageIndex;
    protected ItemStack nextPage, prevPage;
    protected Gui parent = null;
    protected static ItemStack AIR = new ItemStack(Material.AIR);

    protected boolean open = false;
    protected Openable opener = null;
    protected Closable closer = null;
    protected Droppable dropper = null;
    protected Pagable pager = null;

    public Gui() {
        this.rows = 3;
    }

    public Gui(GuiType type) {
        this.inventoryType = type;
        switch (type) {
            case HOPPER:
            case DISPENSER:
                this.rows = 1;
                break;
            default:
                this.rows = 3;
        }
    }

    public Gui(Gui parent) {
        this.parent = parent;
    }

    public Gui(int rows) {
        this.rows = Math.max(1, Math.min(6, rows));
    }

    public Gui(int rows, Gui parent) {
        this.parent = parent;
        this.rows = Math.max(1, Math.min(6, rows));
    }

    public List<Player> getPlayers() {
        return inventory == null ? Collections.EMPTY_LIST
                : inventory.getViewers().stream()
                        .filter(e -> e instanceof Player)
                        .map(e -> (Player) e)
                        .collect(Collectors.toList());
    }

    public boolean isOpen() {
        // double check
        if (inventory != null && inventory.getViewers().isEmpty()) {
            open = false;
        }
        return open;
    }

    public boolean getAcceptsItems() {
        return acceptsItems;
    }

    public Gui setAcceptsItems(boolean acceptsItems) {
        this.acceptsItems = acceptsItems;
        return this;
    }

    /**
     * If this is true, then items in the player's cursor when the GUI is closed
     * will be cleared
     */
    public boolean getAllowDrops() {
        return allowDropItems;
    }

    /**
     * Set if items in the player's cursor will be cleared when the GUI is
     * closed
     */
    public Gui setAllowDrops(boolean allow) {
        this.allowDropItems = allow;
        return this;
    }

    public boolean getAllowClose() {
        return allowClose;
    }

    public Gui setAllowClose(boolean allow) {
        this.allowClose = allow;
        return this;
    }

    /**
     * Close the GUI without calling onClose() and without opening any parent GUIs
     */
    public void exit() {
        allowClose = true;
        open = false;
        inventory.getViewers().stream()
                .filter(e -> e instanceof Player)
                .map(e -> (Player) e)
                .collect(Collectors.toList())
                .forEach(Player::closeInventory);
    }

    public GuiType getType() {
        return inventoryType;
    }

    public Gui setUnlocked(int cell) {
        unlockedCells.put(cell, true);
        return this;
    }

    public Gui setUnlocked(int row, int col) {
        final int cell = col + row * 9;
        unlockedCells.put(cell, true);
        return this;
    }

    public Gui setUnlockedRange(int cellFirst, int cellLast) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            unlockedCells.put(cell, true);
        }
        return this;
    }

    public Gui setUnlockedRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast) {
        final int last = cellColLast + cellRowLast * 9;
        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            unlockedCells.put(cell, true);
        }
        return this;
    }

    public Gui setUnlocked(int cell, boolean open) {
        unlockedCells.put(cell, open);
        return this;
    }

    public Gui setUnlocked(int row, int col, boolean open) {
        final int cell = col + row * 9;
        unlockedCells.put(cell, open);
        return this;
    }

    public Gui setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getRows() {
        return rows;
    }

    public Gui setRows(int rows) {
        switch (inventoryType) {
            case HOPPER:
            case DISPENSER:
                break;
            default:
                this.rows = Math.max(1, Math.min(6, rows));
        }
        return this;
    }

    public Gui setDefaultItem(ItemStack item) {
        blankItem = item;
        return this;
    }

    public ItemStack getDefaultItem() {
        return blankItem;
    }

    public Gui setItem(int cell, ItemStack item) {
        cellItems.put(cell, item);
        if (open && cell >= 0 && cell < inventory.getSize()) {
            inventory.setItem(cell, item);
        }
        return this;
    }

    public Gui setItem(int row, int col, ItemStack item) {
        final int cell = col + row * 9;
        cellItems.put(cell, item);
        if (open && cell >= 0 && cell < inventory.getSize()) {
            inventory.setItem(cell, item);
        }
        return this;
    }

    public Gui highlightItem(int cell) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, ItemUtils.addGlow(item));
        }
        return this;
    }

    public Gui highlightItem(int row, int col) {
        final int cell = col + row * 9;
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, ItemUtils.addGlow(item));
        }
        return this;
    }

    public Gui removeHighlight(int cell) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, ItemUtils.removeGlow(item));
        }
        return this;
    }

    public Gui removeHighlight(int row, int col) {
        final int cell = col + row * 9;
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, ItemUtils.removeGlow(item));
        }
        return this;
    }

    public Gui updateItem(int row, int col, String name, String... lore) {
        return updateItem(col + row * 9, name, lore);
    }

    public Gui updateItem(int cell, String name, String... lore) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, GuiUtils.updateItem(item, title, lore));
        }
        return this;
    }

    public Gui updateItem(int row, int col, String name, List<String> lore) {
        return updateItem(col + row * 9, name, lore);
    }

    public Gui updateItem(int cell, String name, List<String> lore) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, GuiUtils.updateItem(item, title, lore));
        }
        return this;
    }

    public Gui updateItem(int row, int col, ItemStack itemTo, String title, String... lore) {
        return updateItem(col + row * 9, itemTo, title, lore);
    }

    public Gui updateItem(int cell, ItemStack itemTo, String title, String... lore) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, GuiUtils.updateItem(item, itemTo, title, lore));
        }
        return this;
    }

    public Gui updateItem(int row, int col, LegacyMaterials itemTo, String title, String... lore) {
        return updateItem(col + row * 9, itemTo, title, lore);
    }

    public Gui updateItem(int cell, LegacyMaterials itemTo, String title, String... lore) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, GuiUtils.updateItem(item, itemTo, title, lore));
        }
        return this;
    }

    public Gui updateItem(int row, int col, ItemStack itemTo, String title, List<String> lore) {
        return updateItem(col + row * 9, itemTo, title, lore);
    }

    public Gui updateItem(int cell, ItemStack itemTo, String title, List<String> lore) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, GuiUtils.updateItem(item, itemTo, title, lore));
        }
        return this;
    }

    public Gui updateItem(int row, int col, LegacyMaterials itemTo, String title, List<String> lore) {
        return updateItem(col + row * 9, itemTo, title, lore);
    }

    public Gui updateItem(int cell, LegacyMaterials itemTo, String title, List<String> lore) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, GuiUtils.updateItem(item, itemTo, title, lore));
        }
        return this;
    }

    public Gui setAction(int cell, Clickable action) {
        setConditional(cell, null, action);
        return this;
    }

    public Gui setAction(int row, int col, Clickable action) {
        setConditional(col + row * 9, null, action);
        return this;
    }

    public Gui setAction(int cell, ClickType type, Clickable action) {
        setConditional(cell, type, action);
        return this;
    }

    public Gui setAction(int row, int col, ClickType type, Clickable action) {
        setConditional(col + row * 9, type, action);
        return this;
    }

    public Gui setActionForRange(int cellFirst, int cellLast, Clickable action) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            setConditional(cell, null, action);
        }
        return this;
    }

    public Gui setActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, Clickable action) {
        final int last = cellColLast + cellRowLast * 9;
        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            setConditional(cell, null, action);
        }
        return this;
    }

    public Gui setActionForRange(int cellFirst, int cellLast, ClickType type, Clickable action) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            setConditional(cell, type, action);
        }
        return this;
    }

    public Gui setActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, ClickType type, Clickable action) {
        final int last = cellColLast + cellRowLast * 9;
        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            setConditional(cell, type, action);
        }
        return this;
    }

    public Gui clearActions(int cell) {
        conditionalButtons.remove(cell);
        return this;
    }

    public Gui clearActions(int row, int col) {
        final int cell = col + row * 9;
        conditionalButtons.remove(cell);
        return this;
    }

    public Gui setButton(int cell, ItemStack item, Clickable action) {
        setItem(cell, item);
        setConditional(cell, null, action);
        return this;
    }

    public Gui setButton(int row, int col, ItemStack item, Clickable action) {
        final int cell = col + row * 9;
        setItem(cell, item);
        setConditional(cell, null, action);
        return this;
    }

    public Gui setButton(int cell, ItemStack item, ClickType type, Clickable action) {
        setItem(cell, item);
        setConditional(cell, type, action);
        return this;
    }

    public Gui setButton(int row, int col, ItemStack item, ClickType type, Clickable action) {
        final int cell = col + row * 9;
        setItem(cell, item);
        setConditional(cell, type, action);
        return this;
    }

    protected void setConditional(int cell, ClickType type, Clickable action) {
        Map<ClickType, Clickable> conditionals = conditionalButtons.get(cell);
        if (action != null) {
            if (conditionals == null) {
                conditionalButtons.put(cell, conditionals = new HashMap());
            }
            conditionals.put(type, action);
        }
    }

    public Gui setOnOpen(Openable action) {
        opener = action;
        return this;
    }

    public Gui setOnClose(Closable action) {
        closer = action;
        return this;
    }

    public Gui setOnDrop(Droppable action) {
        dropper = action;
        return this;
    }

    public Gui setOnPage(Pagable action) {
        pager = action;
        return this;
    }

    public Gui setNextPage(int row, int col, ItemStack item) {
        nextPageIndex = col + row * 9;
        if (page < pages) {
            setButton(nextPageIndex, item, ClickType.LEFT, (event) -> this.nextPage());
        }
        return this;
    }

    public Gui setPrevPage(int row, int col, ItemStack item) {
        prevPageIndex = col + row * 9;
        if (page > 1) {
            setButton(prevPageIndex, item, ClickType.LEFT, (event) -> this.prevPage());
        }
        return this;
    }

    public void nextPage() {
        if (page < pages) {
            int lastPage = page;
            ++page;
            // page switch events
            if (pager != null) {
                pager.onPageChange(this, lastPage, page);

                // page markers
                updatePageNavigation();

                // push new inventory to the view inventory
                // shouldn't be needed since adding inventory update to setItem
                //update();
            }
        }
    }

    public void prevPage() {
        if (page > 1) {
            int lastPage = page;
            --page;
            if (pager != null) {
                pager.onPageChange(this, lastPage, page);

                // page markers
                updatePageNavigation();

                // push new inventory to the view inventory
                // shouldn't be needed since adding inventory update to setItem
                //update();
            }
        }
    }

    protected void updatePageNavigation() {
        if (page > 1) {
            this.setButton(prevPageIndex, prevPage, ClickType.LEFT, (event) -> this.prevPage());
        } else {
            this.setItem(prevPageIndex, null);
            this.clearActions(prevPageIndex);
        }
        if (pages > 1 && page != pages) {
            this.setButton(nextPageIndex, nextPage, ClickType.LEFT, (event) -> this.nextPage());
        } else {
            this.setItem(nextPageIndex, null);
            this.clearActions(nextPageIndex);
        }
    }

    protected Inventory getOrCreateInventory(GuiManager manager) {
        return inventory != null ? inventory : generateInventory(manager);
    }

    protected Inventory generateInventory(GuiManager manager) {
        final int cells = rows * 9;
        InventoryType t = inventoryType == null ? InventoryType.CHEST : inventoryType.type;
        switch (t) {
            case DISPENSER:
            case HOPPER:
                inventory = Bukkit.getServer().createInventory(new GuiHolder(manager, this), t,
                        title == null ? "" : trimTitle(ChatColor.translateAlternateColorCodes('&', title)));
                break;
            default:
                inventory = Bukkit.getServer().createInventory(new GuiHolder(manager, this), cells,
                        title == null ? "" : trimTitle(ChatColor.translateAlternateColorCodes('&', title)));
        }

        for (int i = 0; i < cells; ++i) {
            final ItemStack item = cellItems.get(i);
            inventory.setItem(i, item != null ? item : blankItem);
        }

        return inventory;
    }

    public Gui getParent() {
        return parent;
    }

    public void update() {
        if (inventory == null) {
            return;
        }
        final int cells = rows * 9;
        for (int i = 0; i < cells; ++i) {
            final ItemStack item = cellItems.get(i);
            inventory.setItem(i, item != null ? item : blankItem);
        }
    }

    protected static String trimTitle(String title) {
        if (title != null && title.length() > 32) {
            return title.substring(0, 31);
        }
        return title;
    }

    protected boolean onClickOutside(GuiManager manager, Player player, InventoryClickEvent event) {
        return dropper != null ? dropper.onDrop(new GuiDropItemEvent(manager, this, player, event)) : true;
    }

    protected boolean onClick(GuiManager manager, Player player, Inventory inventory, InventoryClickEvent event) {
        final int cell = event.getSlot();
        Map<ClickType, Clickable> conditionals = conditionalButtons.get(cell);
        Clickable button;
        if (conditionals != null
                && ((button = conditionals.get(event.getClick())) != null || (button = conditionals.get(null)) != null)) {
            button.onClick(new GuiClickEvent(manager, this, player, event, cell, true));
        } else {
            // no event for this button
            return false;
        }
        return true;
    }

    protected boolean onClickPlayerInventory(GuiManager manager, Player player, Inventory openInv, InventoryClickEvent event) {
        // no events for this yet
        return false;
    }

    public void onOpen(GuiManager manager, Player player) {
        open = true;
        if (opener != null) {
            opener.onOpen(new GuiOpenEvent(manager, this, player));
        }
    }

    public void onClose(GuiManager manager, Player player) {
        if (!allowClose) {
            manager.showGUI(player, this);
            return;
        }
        if (open && closer != null) {
            open = inventory.getViewers().isEmpty();
            closer.onClose(new GuiCloseEvent(manager, this, player));
        }
        if (parent != null) {
            manager.showGUI(player, parent);
        }
    }

}
