package com.songoda.core.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.events.GuiClickEvent;
import com.songoda.core.gui.events.GuiCloseEvent;
import com.songoda.core.gui.events.GuiDropItemEvent;
import com.songoda.core.gui.events.GuiOpenEvent;
import com.songoda.core.gui.events.GuiPageEvent;
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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    protected int rows, page = 1, pages = 1;
    protected boolean acceptsItems = false;
    protected boolean allowDropItems = true;
    protected boolean allowClose = true;
    protected final Map<Integer, Boolean> unlockedCells = new HashMap<>();
    protected final Map<Integer, ItemStack> cellItems = new HashMap<>();
    protected final Map<Integer, Map<ClickType, Clickable>> conditionalButtons = new HashMap<>();
    protected ItemStack blankItem = GuiUtils.getBorderGlassItem();
    protected int nextPageIndex = -1, prevPageIndex = -1;
    protected ItemStack nextPage, prevPage;
    protected Gui parent = null;
    protected static ItemStack AIR = new ItemStack(Material.AIR);

    protected GuiManager guiManager;
    protected boolean open = false;
    protected Clickable defaultClicker = null;
    protected Openable opener = null;
    protected Closable closer = null;
    protected Droppable dropper = null;
    protected Pagable pager = null;

    public Gui() {
        this.rows = 3;
    }

    public Gui(@NotNull GuiType type) {
        this.inventoryType = type != null ? type : GuiType.STANDARD;
        switch (type) {
            case HOPPER:
            case DISPENSER:
                this.rows = 1;
                break;
            default:
                this.rows = 3;
        }
    }

    public Gui(@Nullable Gui parent) {
        this.parent = parent;
    }

    public Gui(int rows) {
        this.rows = Math.max(1, Math.min(6, rows));
    }

    public Gui(int rows, @Nullable Gui parent) {
        this.parent = parent;
        this.rows = Math.max(1, Math.min(6, rows));
    }

    @NotNull
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
     * Close the GUI without calling onClose() and without opening any parent
     * GUIs
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

    @NotNull
    public GuiType getType() {
        return inventoryType;
    }

    @NotNull
    public Gui setUnlocked(int cell) {
        unlockedCells.put(cell, true);
        return this;
    }

    @NotNull
    public Gui setUnlocked(int row, int col) {
        final int cell = col + row * 9;
        unlockedCells.put(cell, true);
        return this;
    }

    @NotNull
    public Gui setUnlockedRange(int cellFirst, int cellLast) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            unlockedCells.put(cell, true);
        }
        return this;
    }

    @NotNull
    public Gui setUnlockedRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast) {
        final int last = cellColLast + cellRowLast * 9;
        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            unlockedCells.put(cell, true);
        }
        return this;
    }

    @NotNull
    public Gui setUnlocked(int cell, boolean open) {
        unlockedCells.put(cell, open);
        return this;
    }

    @NotNull
    public Gui setUnlocked(int row, int col, boolean open) {
        final int cell = col + row * 9;
        unlockedCells.put(cell, open);
        return this;
    }

    @NotNull
    public Gui setTitle(String title) {
        if (title == null) title = "";
        if (!title.equals(this.title)) {
            this.title = title;
            if (inventory != null) {
                // update active inventory
                List<Player> toUpdate = getPlayers();
                boolean isAllowClose = allowClose;
                exit();
                Inventory oldInv = inventory;
                createInventory();
                inventory.setContents(oldInv.getContents());
                toUpdate.forEach(player -> player.openInventory(inventory));
                allowClose = isAllowClose;
            }
        }
        return this;
    }

    public int getRows() {
        return rows;
    }

    @NotNull
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

    @NotNull
    public Gui setDefaultAction(@Nullable Clickable action) {
        defaultClicker = action;
        return this;
    }

    @NotNull
    public Gui setDefaultItem(ItemStack item) {
        blankItem = item;
        return this;
    }

    @Nullable
    public ItemStack getDefaultItem() {
        return blankItem;
    }

    @Nullable
    public ItemStack getItem(int cell) {
        if (inventory != null && unlockedCells.getOrDefault(cell, false)) {
            return inventory.getItem(cell);
        }
        return cellItems.get(cell);
    }

    @Nullable
    public ItemStack getItem(int row, int col) {
        final int cell = col + row * 9;
        if (inventory != null && unlockedCells.getOrDefault(cell, false)) {
            return inventory.getItem(cell);
        }
        return cellItems.get(cell);
    }

    @NotNull
    public Gui setItem(int cell, @Nullable ItemStack item) {
        cellItems.put(cell, item);
        if (inventory != null && cell >= 0 && cell < inventory.getSize()) {
            inventory.setItem(cell, item);
        }
        return this;
    }

    @NotNull
    public Gui setItem(int row, int col, @Nullable ItemStack item) {
        final int cell = col + row * 9;
        cellItems.put(cell, item);
        if (inventory != null && cell >= 0 && cell < inventory.getSize()) {
            inventory.setItem(cell, item);
        }
        return this;
    }

    @NotNull
    public Gui highlightItem(int cell) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, ItemUtils.addGlow(item));
        }
        return this;
    }

    @NotNull
    public Gui highlightItem(int row, int col) {
        final int cell = col + row * 9;
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, ItemUtils.addGlow(item));
        }
        return this;
    }

    @NotNull
    public Gui removeHighlight(int cell) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, ItemUtils.removeGlow(item));
        }
        return this;
    }

    @NotNull
    public Gui removeHighlight(int row, int col) {
        final int cell = col + row * 9;
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, ItemUtils.removeGlow(item));
        }
        return this;
    }

    @NotNull
    public Gui updateItemLore(int row, int col, @NotNull String... lore) {
        return updateItemLore(col + row * 9, lore);
    }

    @NotNull
    public Gui updateItemLore(int cell, @NotNull String... lore) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, GuiUtils.updateItemLore(item, lore));
        }
        return this;
    }

    @NotNull
    public Gui updateItemLore(int row, int col, @Nullable List<String> lore) {
        return updateItemLore(col + row * 9, lore);
    }

    @NotNull
    public Gui updateItemLore(int cell, @Nullable List<String> lore) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, GuiUtils.updateItemLore(item, lore));
        }
        return this;
    }

    @NotNull
    public Gui updateItemName(int row, int col, @Nullable String name) {
        return updateItemName(col + row * 9, name);
    }

    @NotNull
    public Gui updateItemName(int cell, @Nullable String name) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, GuiUtils.updateItemName(item, name));
        }
        return this;
    }

    @NotNull
    public Gui updateItem(int row, int col, @Nullable String name, @NotNull String... lore) {
        return updateItem(col + row * 9, name, lore);
    }

    @NotNull
    public Gui updateItem(int cell, @Nullable String name, @NotNull String... lore) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, GuiUtils.updateItem(item, name, lore));
        }
        return this;
    }

    @NotNull
    public Gui updateItem(int row, int col, @Nullable String name, @Nullable List<String> lore) {
        return updateItem(col + row * 9, name, lore);
    }

    @NotNull
    public Gui updateItem(int cell, @NotNull String name, @Nullable List<String> lore) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, GuiUtils.updateItem(item, title, lore));
        }
        return this;
    }

    @NotNull
    public Gui updateItem(int row, int col, @NotNull ItemStack itemTo, @Nullable String title, @NotNull String... lore) {
        return updateItem(col + row * 9, itemTo, title, lore);
    }

    @NotNull
    public Gui updateItem(int cell, @NotNull ItemStack itemTo, @Nullable String title, @NotNull String... lore) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, GuiUtils.updateItem(item, itemTo, title, lore));
        }
        return this;
    }

    @NotNull
    public Gui updateItem(int row, int col, @NotNull CompatibleMaterial itemTo, @Nullable String title, @NotNull String... lore) {
        return updateItem(col + row * 9, itemTo, title, lore);
    }

    @NotNull
    public Gui updateItem(int cell, @NotNull CompatibleMaterial itemTo, @Nullable String title, @Nullable String... lore) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, GuiUtils.updateItem(item, itemTo, title, lore));
        }
        return this;
    }

    @NotNull
    public Gui updateItem(int row, int col, @NotNull ItemStack itemTo, @Nullable String title, @Nullable List<String> lore) {
        return updateItem(col + row * 9, itemTo, title, lore);
    }

    @NotNull
    public Gui updateItem(int cell, @NotNull ItemStack itemTo, @Nullable String title, @Nullable List<String> lore) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, GuiUtils.updateItem(item, itemTo, title, lore));
        }
        return this;
    }

    @NotNull
    public Gui updateItem(int row, int col, @NotNull CompatibleMaterial itemTo, @Nullable String title, @Nullable List<String> lore) {
        return updateItem(col + row * 9, itemTo, title, lore);
    }

    @NotNull
    public Gui updateItem(int cell, @NotNull CompatibleMaterial itemTo, @Nullable String title, @Nullable List<String> lore) {
        ItemStack item = cellItems.get(cell);
        if (item != null && item.getType() != Material.AIR) {
            setItem(cell, GuiUtils.updateItem(item, itemTo, title, lore));
        }
        return this;
    }

    @NotNull
    public Gui setAction(int cell, @Nullable Clickable action) {
        setConditional(cell, null, action);
        return this;
    }

    @NotNull
    public Gui setAction(int row, int col, @Nullable Clickable action) {
        setConditional(col + row * 9, null, action);
        return this;
    }

    @NotNull
    public Gui setAction(int cell, @Nullable ClickType type, @Nullable Clickable action) {
        setConditional(cell, type, action);
        return this;
    }

    @NotNull
    public Gui setAction(int row, int col, @Nullable ClickType type, @Nullable Clickable action) {
        setConditional(col + row * 9, type, action);
        return this;
    }

    @NotNull
    public Gui setActionForRange(int cellFirst, int cellLast, @Nullable Clickable action) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            setConditional(cell, null, action);
        }
        return this;
    }

    @NotNull
    public Gui setActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, @Nullable Clickable action) {
        final int last = cellColLast + cellRowLast * 9;
        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            setConditional(cell, null, action);
        }
        return this;
    }

    @NotNull
    public Gui setActionForRange(int cellFirst, int cellLast, @Nullable ClickType type, @Nullable Clickable action) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            setConditional(cell, type, action);
        }
        return this;
    }

    @NotNull
    public Gui setActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, @Nullable ClickType type, @Nullable Clickable action) {
        final int last = cellColLast + cellRowLast * 9;
        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            setConditional(cell, type, action);
        }
        return this;
    }

    @NotNull
    public Gui clearActions(int cell) {
        conditionalButtons.remove(cell);
        return this;
    }

    @NotNull
    public Gui clearActions(int row, int col) {
        final int cell = col + row * 9;
        conditionalButtons.remove(cell);
        return this;
    }

    @NotNull
    public Gui setButton(int cell, ItemStack item, @Nullable Clickable action) {
        setItem(cell, item);
        setConditional(cell, null, action);
        return this;
    }

    @NotNull
    public Gui setButton(int row, int col, @Nullable ItemStack item, @Nullable Clickable action) {
        final int cell = col + row * 9;
        setItem(cell, item);
        setConditional(cell, null, action);
        return this;
    }

    @NotNull
    public Gui setButton(int cell, @Nullable ItemStack item, @Nullable ClickType type, @Nullable Clickable action) {
        setItem(cell, item);
        setConditional(cell, type, action);
        return this;
    }

    @NotNull
    public Gui setButton(int row, int col, @Nullable ItemStack item, @Nullable ClickType type, @Nullable Clickable action) {
        final int cell = col + row * 9;
        setItem(cell, item);
        setConditional(cell, type, action);
        return this;
    }

    protected void setConditional(int cell, @Nullable ClickType type, @Nullable Clickable action) {
        Map<ClickType, Clickable> conditionals = conditionalButtons.get(cell);
        if (conditionals == null) {
            conditionalButtons.put(cell, conditionals = new HashMap());
        }
        conditionals.put(type, action);
    }

    @NotNull
    public Gui setOnOpen(@Nullable Openable action) {
        opener = action;
        return this;
    }

    @NotNull
    public Gui setOnClose(@Nullable Closable action) {
        closer = action;
        return this;
    }

    @NotNull
    public Gui setOnDrop(@Nullable Droppable action) {
        dropper = action;
        return this;
    }

    @NotNull
    public Gui setOnPage(@Nullable Pagable action) {
        pager = action;
        return this;
    }

    public Gui setNextPage(ItemStack item) {
        nextPage = item;
        return this;
    }

    public Gui setPrevPage(ItemStack item) {
        prevPage = item;
        return this;
    }

    @NotNull
    public Gui setNextPage(int cell, @NotNull ItemStack item) {
        nextPageIndex = cell;
        if (page < pages) {
            setButton(nextPageIndex, nextPage = item, ClickType.LEFT, (event) -> this.nextPage());
        }
        return this;
    }

    @NotNull
    public Gui setNextPage(int row, int col, @NotNull ItemStack item) {
        nextPageIndex = col + row * 9;
        if (page < pages) {
            setButton(nextPageIndex, nextPage = item, ClickType.LEFT, (event) -> this.nextPage());
        }
        return this;
    }

    @NotNull
    public Gui setPrevPage(int cell, @NotNull ItemStack item) {
        prevPageIndex = cell;
        if (page > 1) {
            setButton(prevPageIndex, prevPage = item, ClickType.LEFT, (event) -> this.prevPage());
        }
        return this;
    }

    @NotNull
    public Gui setPrevPage(int row, int col, @NotNull ItemStack item) {
        prevPageIndex = col + row * 9;
        if (page > 1) {
            setButton(prevPageIndex, prevPage = item, ClickType.LEFT, (event) -> this.prevPage());
        }
        return this;
    }

    public void setPage(int page) {
        int lastPage = this.page;
        this.page = Math.max(1, Math.min(pages, page));
        if(pager != null && this.page != lastPage) {
            pager.onPageChange(new GuiPageEvent(this, guiManager, lastPage, page));
            // page markers
            updatePageNavigation();
        }
    }

    public void changePage(int direction) {
        int lastPage = page;
        this.page = Math.max(1, Math.min(pages, page + direction));
        if(pager != null && this.page != lastPage) {
            pager.onPageChange(new GuiPageEvent(this, guiManager, lastPage, page));
            // page markers
            updatePageNavigation();
        }
    }

    public void nextPage() {
        if (page < pages) {
            int lastPage = page;
            ++page;
            // page switch events
            if (pager != null) {
                pager.onPageChange(new GuiPageEvent(this, guiManager, lastPage, page));

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
                pager.onPageChange(new GuiPageEvent(this, guiManager, lastPage, page));

                // page markers
                updatePageNavigation();

                // push new inventory to the view inventory
                // shouldn't be needed since adding inventory update to setItem
                //update();
            }
        }
    }

    protected void updatePageNavigation() {
        if(prevPage != null) {
            if (page > 1) {
                this.setButton(prevPageIndex, prevPage, ClickType.LEFT, (event) -> this.prevPage());
            } else {
                this.setItem(prevPageIndex, null);
                this.clearActions(prevPageIndex);
            }
        }
        if(nextPage != null) {
            if (pages > 1 && page != pages) {
                this.setButton(nextPageIndex, nextPage, ClickType.LEFT, (event) -> this.nextPage());
            } else {
                this.setItem(nextPageIndex, null);
                this.clearActions(nextPageIndex);
            }
        }
    }

    @NotNull
    protected Inventory getOrCreateInventory(@NotNull GuiManager manager) {
        return inventory != null ? inventory : generateInventory(manager);
    }

    @NotNull
    protected Inventory generateInventory(@NotNull GuiManager manager) {
        this.guiManager = manager;
        final int cells = rows * 9;

        createInventory();
        for (int i = 0; i < cells; ++i) {
            final ItemStack item = cellItems.get(i);
            inventory.setItem(i, item != null ? item : (unlockedCells.getOrDefault(i, false) ? AIR : blankItem));
        }

        return inventory;
    }

    protected void createInventory() {
        final InventoryType t = inventoryType == null ? InventoryType.CHEST : inventoryType.type;
        switch (t) {
            case DISPENSER:
            case HOPPER:
                inventory = Bukkit.getServer().createInventory(new GuiHolder(guiManager, this), t,
                        title == null ? "" : trimTitle(title));
                break;
            default:
                inventory = Bukkit.getServer().createInventory(new GuiHolder(guiManager, this), rows * 9,
                        title == null ? "" : trimTitle(title));
        }
    }

    @Nullable
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
            inventory.setItem(i, item != null ? item : (unlockedCells.getOrDefault(i, false) ? AIR : blankItem));
        }
    }

    protected static String trimTitle(String title) {
        if(title == null) {
            return "";
        } else if (title != null && title.length() > 32) {
            return title.substring(0, 31);
        }
        return title;
    }

    protected boolean onClickOutside(@NotNull GuiManager manager, @NotNull Player player, @NotNull InventoryClickEvent event) {
        return dropper != null ? dropper.onDrop(new GuiDropItemEvent(manager, this, player, event)) : true;
    }

    protected boolean onClick(@NotNull GuiManager manager, @NotNull Player player, @NotNull Inventory inventory, @NotNull InventoryClickEvent event) {
        final int cell = event.getSlot();
        Map<ClickType, Clickable> conditionals = conditionalButtons.get(cell);
        Clickable button;
        if (conditionals != null
                && ((button = conditionals.get(event.getClick())) != null || (button = conditionals.get(null)) != null)) {
            button.onClick(new GuiClickEvent(manager, this, player, event, cell, true));
        } else {
            // no event for this button
            if (defaultClicker != null) {
                // this is a default action, not a triggered action
                defaultClicker.onClick(new GuiClickEvent(manager, this, player, event, cell, true));
            }
            return false;
        }
        return true;
    }

    protected boolean onClickPlayerInventory(@NotNull GuiManager manager, @NotNull Player player, @NotNull Inventory openInv, @NotNull InventoryClickEvent event) {
        // no events for this yet
        return false;
    }

    public void onOpen(@NotNull GuiManager manager, @NotNull Player player) {
        open = true;
        guiManager = manager;
        if (opener != null) {
            opener.onOpen(new GuiOpenEvent(manager, this, player));
        }
    }

    public void onClose(@NotNull GuiManager manager, @NotNull Player player) {
        if (!allowClose) {
            manager.showGUI(player, this);
            return;
        }
        boolean showParent = open && parent != null;
        if (open && closer != null) {
            open = !inventory.getViewers().isEmpty();
            closer.onClose(new GuiCloseEvent(manager, this, player));
        }
        if (showParent) {
            manager.showGUI(player, parent);
        }
    }

}
