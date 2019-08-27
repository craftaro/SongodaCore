package com.songoda.core.gui;

import com.songoda.core.gui.methods.Pagable;
import com.songoda.core.gui.methods.Clickable;
import com.songoda.core.gui.methods.Droppable;
import com.songoda.core.gui.methods.Closable;
import com.songoda.core.gui.methods.SimpleClickable;
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
public class GUI {

    protected Inventory inventory;
    protected String title;
    protected GUIType type = GUIType.STANDARD;
    protected int rows, page, pages;
    protected boolean acceptsItems = false;
    protected boolean allowDropItems = true;
    protected boolean allowClose = true;
    protected final Map<Integer, Boolean> unlockedCells = new HashMap<>();
    protected final Map<Integer, ItemStack> cellItems = new HashMap<>();
    protected final Map<Integer, Map<ClickType, Clickable>> conditionalButtons = new HashMap<>();
    protected final Map<Integer, Map<ClickType, SimpleClickable>> conditionalSimpleButtons = new HashMap<>();
    protected ItemStack blankItem = GuiUtils.getBorderGlassItem();
    protected int nextPageIndex, prevPageIndex;
    protected ItemStack nextPage, prevPage;
    protected GUI parent = null;
    protected static ItemStack AIR = new ItemStack(Material.AIR);

    protected boolean open = false;
    protected Closable closer = null;
    protected Droppable dropper = null;
    protected Pagable pager = null;

    public GUI(GUIType type) {
        this.type = type;
        switch (type) {
            case HOPPER:
            case DISPENSER:
                this.rows = 1;
                break;
            default:
                this.rows = 3;
        }
    }

    public GUI(int rows) {
        this.rows = Math.max(1, Math.min(6, rows));
    }

    public GUI(int rows, GUI parent) {
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

    public GUI setAcceptsItems(boolean acceptsItems) {
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
    public GUI setAllowDrops(boolean allow) {
        this.allowDropItems = allow;
        return this;
    }

    public boolean getAllowClose() {
        return allowClose;
    }

    public GUI setAllowClose(boolean allow) {
        this.allowClose = allow;
        return this;
    }

    public void exit() {
        allowClose = true;
        parent = null;
        inventory.getViewers().stream()
                .filter(e -> e instanceof Player)
                .map(e -> (Player) e)
                .collect(Collectors.toList())
                .forEach(Player::closeInventory);
    }

    public GUIType getType() {
        return type;
    }

    public GUI setUnlocked(int cell) {
        unlockedCells.put(cell, true);
        return this;
    }

    public GUI setUnlocked(int row, int col) {
        final int cell = col + row * 9;
        unlockedCells.put(cell, true);
        return this;
    }

    public GUI setUnlockedRange(int cellFirst, int cellLast) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            unlockedCells.put(cell, true);
        }
        return this;
    }

    public GUI setUnlockedRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast) {
        final int last = cellColLast + cellRowLast * 9;
        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            unlockedCells.put(cell, true);
        }
        return this;
    }

    public GUI setUnlocked(int cell, boolean open) {
        unlockedCells.put(cell, open);
        return this;
    }

    public GUI setUnlocked(int row, int col, boolean open) {
        final int cell = col + row * 9;
        unlockedCells.put(cell, open);
        return this;
    }

    public GUI setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getRows() {
        return rows;
    }

    public GUI setRows(int rows) {
        switch (type) {
            case HOPPER:
            case DISPENSER:
                break;
            default:
                this.rows = Math.max(1, Math.min(6, rows));
        }
        return this;
    }

    public GUI setDefaultItem(ItemStack item) {
        blankItem = item;
        return this;
    }

    public GUI setItem(int cell, ItemStack item) {
        cellItems.put(cell, item);
        if (open && cell >= 0 && cell < inventory.getSize()) {
            inventory.setItem(cell, item);
        }
        return this;
    }

    public GUI setItem(int row, int col, ItemStack item) {
        final int cell = col + row * 9;
        cellItems.put(cell, item);
        if (open && cell >= 0 && cell < inventory.getSize()) {
            inventory.setItem(cell, item);
        }
        return this;
    }

    public GUI setAction(int cell, Clickable action) {
        setConditional(cell, null, action, null);
        return this;
    }

    public GUI setAction(int cell, SimpleClickable action) {
        setConditional(cell, null, null, action);
        return this;
    }

    public GUI setAction(int row, int col, Clickable action) {
        setConditional(col + row * 9, null, action, null);
        return this;
    }

    public GUI setAction(int row, int col, SimpleClickable action) {
        setConditional(col + row * 9, null, null, action);
        return this;
    }

    public GUI setAction(int cell, ClickType type, Clickable action) {
        setConditional(cell, type, action, null);
        return this;
    }

    public GUI setAction(int cell, ClickType type, SimpleClickable action) {
        setConditional(cell, type, null, action);
        return this;
    }

    public GUI setAction(int row, int col, ClickType type, Clickable action) {
        setConditional(col + row * 9, type, action, null);
        return this;
    }

    public GUI setAction(int row, int col, ClickType type, SimpleClickable action) {
        setConditional(col + row * 9, type, null, action);
        return this;
    }

    public GUI setActionForRange(int cellFirst, int cellLast, Clickable action) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            setConditional(cell, null, action, null);
        }
        return this;
    }

    public GUI setActionForRange(int cellFirst, int cellLast, SimpleClickable action) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            setConditional(cell, null, null, action);
        }
        return this;
    }

    public GUI setActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, Clickable action) {
        final int last = cellColLast + cellRowLast * 9;
        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            setConditional(cell, null, action, null);
        }
        return this;
    }

    public GUI setActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, SimpleClickable action) {
        final int last = cellColLast + cellRowLast * 9;
        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            setConditional(cell, null, null, action);
        }
        return this;
    }

    public GUI setActionForRange(int cellFirst, int cellLast, ClickType type, Clickable action) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            setConditional(cell, type, action, null);
        }
        return this;
    }

    public GUI setActionForRange(int cellFirst, int cellLast, ClickType type, SimpleClickable action) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            setConditional(cell, type, null, action);
        }
        return this;
    }

    public GUI setActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, ClickType type, Clickable action) {
        final int last = cellColLast + cellRowLast * 9;
        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            setConditional(cell, type, action, null);
        }
        return this;
    }

    public GUI setActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, ClickType type, SimpleClickable action) {
        final int last = cellColLast + cellRowLast * 9;
        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            setConditional(cell, type, null, action);
        }
        return this;
    }

    public GUI clearActions(int cell) {
        conditionalButtons.remove(cell);
        conditionalSimpleButtons.remove(cell);
        return this;
    }

    public GUI clearActions(int row, int col) {
        final int cell = col + row * 9;
        conditionalButtons.remove(cell);
        conditionalSimpleButtons.remove(cell);
        return this;
    }

    public GUI setButton(int cell, ItemStack item, Clickable action) {
        setItem(cell, item);
        setConditional(cell, null, action, null);
        return this;
    }

    public GUI setButton(int cell, ItemStack item, SimpleClickable action) {
        setItem(cell, item);
        setConditional(cell, null, null, action);
        return this;
    }

    public GUI setButton(int row, int col, ItemStack item, Clickable action) {
        final int cell = col + row * 9;
        setItem(cell, item);
        setConditional(cell, null, action, null);
        return this;
    }

    public GUI setButton(int row, int col, ItemStack item, SimpleClickable action) {
        final int cell = col + row * 9;
        setItem(cell, item);
        setConditional(cell, null, null, action);
        return this;
    }

    public GUI setButton(int cell, ItemStack item, ClickType type, Clickable action) {
        setItem(cell, item);
        setConditional(cell, type, action, null);
        return this;
    }

    public GUI setButton(int cell, ItemStack item, ClickType type, SimpleClickable action) {
        setItem(cell, item);
        setConditional(cell, type, null, action);
        return this;
    }

    public GUI setButton(int row, int col, ItemStack item, ClickType type, Clickable action) {
        final int cell = col + row * 9;
        setItem(cell, item);
        setConditional(cell, type, action, null);
        return this;
    }

    public GUI setButton(int row, int col, ItemStack item, ClickType type, SimpleClickable action) {
        final int cell = col + row * 9;
        setItem(cell, item);
        setConditional(cell, type, null, action);
        return this;
    }

    protected void setConditional(int cell, ClickType type, Clickable action, SimpleClickable simpleAction) {
        Map<ClickType, Clickable> conditionals = conditionalButtons.get(cell);
        Map<ClickType, SimpleClickable> simpleConditionals = conditionalSimpleButtons.get(cell);
        if (action != null) {
            if (conditionals == null) {
                conditionalButtons.put(cell, conditionals = new HashMap());
            }
            conditionals.put(type, action);
            if (simpleConditionals != null) {
                simpleConditionals.remove(type);
            }
        } else {
            if (simpleConditionals == null) {
                conditionalSimpleButtons.put(cell, simpleConditionals = new HashMap());
            }
            simpleConditionals.put(type, simpleAction);
            if (conditionals != null) {
                conditionals.remove(type);
            }
        }
    }

    public GUI setOnClose(Closable action) {
        closer = action;
        return this;
    }

    public GUI setOnDrop(Droppable action) {
        dropper = action;
        return this;
    }

    public GUI setOnPage(Pagable action) {
        pager = action;
        return this;
    }

    public GUI setNextPage(int row, int col, ItemStack item) {
        nextPageIndex = col + row * 9;
        if (page < pages) {
            setButton(nextPageIndex, item, ClickType.LEFT, (slot) -> this.nextPage());
        }
        return this;
    }

    public GUI setPrevPage(int row, int col, ItemStack item) {
        prevPageIndex = col + row * 9;
        if (page > 1) {
            setButton(prevPageIndex, item, ClickType.LEFT, (slot) -> this.prevPage());
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
                update();
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
                update();
            }
        }
    }

    protected void updatePageNavigation() {
        if (page > 1) {
            this.setButton(prevPageIndex, prevPage, ClickType.LEFT, (slot) -> this.prevPage());
        } else {
            this.setItem(prevPageIndex, null);
            this.clearActions(prevPageIndex);
        }
        if (pages > 1 && page != pages) {
            this.setButton(nextPageIndex, nextPage, ClickType.LEFT, (slot) -> this.nextPage());
        } else {
            this.setItem(nextPageIndex, null);
            this.clearActions(nextPageIndex);
        }
    }

    protected Inventory getOrCreateInventory() {
        return inventory != null ? inventory : generateInventory();
    }

    protected Inventory generateInventory() {
        final int cells = rows * 9;
        InventoryType t = type == null ? InventoryType.CHEST : type.type;
        switch (t) {
            case DISPENSER:
            case HOPPER:
                inventory = Bukkit.getServer().createInventory(new GUIHolder(this), t,
                        title == null ? "" : trimTitle(ChatColor.translateAlternateColorCodes('&', title)));
                break;
            default:
                inventory = Bukkit.getServer().createInventory(new GUIHolder(this), cells,
                        title == null ? "" : trimTitle(ChatColor.translateAlternateColorCodes('&', title)));
        }

        for (int i = 0; i < cells; ++i) {
            final ItemStack item = cellItems.get(i);
            inventory.setItem(i, item != null ? item : blankItem);
        }

        return inventory;
    }

    public GUI getParent() {
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

    protected boolean onClickOutside(Player player, Inventory inventory, ItemStack cursor, ClickType click) {
        return dropper != null ? dropper.onDrop(player, inventory, this, cursor) : true;
    }

    protected boolean onClick(Player player, Inventory inventory, InventoryClickEvent event) {
        final int cell = event.getSlot();
        Map<ClickType, Clickable> conditionals = conditionalButtons.get(cell);
        Map<ClickType, SimpleClickable> simpleConditionals;
        Clickable button;
        SimpleClickable simpleButton;
        if (conditionals != null
                && ((button = conditionals.get(event.getClick())) != null || (button = conditionals.get(null)) != null)) {
            button.onClick(player, inventory, this, event.getCursor(), cell, event.getClick());
        } else if ((simpleConditionals = conditionalSimpleButtons.get(cell)) != null
                && ((simpleButton = simpleConditionals.get(event.getClick())) != null || (simpleButton = simpleConditionals.get(null)) != null)) {
            simpleButton.onClick(cell);
        } else {
            // no event for this button
            return false;
        }
        return true;
    }

    protected boolean onClickPlayerInventory(Player player, Inventory openInv, InventoryClickEvent event) {
        // no events for this yet
        return false;
    }

    public void onOpen(Player player) {
        open = true;
    }

    public void onClose(GUIManager manager, Player player) {
        if (!allowClose) {
            manager.showGUI(player, this);
            return;
        }
        if (open && closer != null) {
            open = inventory.getViewers().isEmpty();
            closer.onClose(player, this);
        }
        if (parent != null) {
            manager.showGUI(player, parent);
        }
    }

}
