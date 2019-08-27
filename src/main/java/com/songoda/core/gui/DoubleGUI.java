package com.songoda.core.gui;

import com.songoda.core.compatibility.LegacyMaterials;
import com.songoda.core.gui.methods.Clickable;
import com.songoda.core.gui.methods.Closable;
import com.songoda.core.gui.methods.Droppable;
import com.songoda.core.gui.methods.Openable;
import com.songoda.core.gui.methods.Pagable;
import com.songoda.core.gui.methods.SimpleClickable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * DO NOT USE YET! 
 * TODO: does not restore inventory if server is shut down while player inventory is open (1.8) 
 * Method to fix: save inv + ender slot to file, store paper in ender inv with name of cache file, check for paper item in slot when loading
 *
 * @since 2019-08-25
 * @author jascotty2
 */
public class DoubleGUI extends GUI {

    protected int playerRows = 4;
    protected Map<Player, ItemStack[]> stash = new HashMap();

    public DoubleGUI(GUIType type) {
        super(type);
        allowDropItems = false;
    }

    public DoubleGUI(int rows) {
        super(rows);
        allowDropItems = false;
    }

    public DoubleGUI(int rows, GUI parent) {
        super(rows, parent);
        allowDropItems = false;
    }

    public int getPlayerRows() {
        return playerRows;
    }

    // 9  -> 0  -> 54
    // 18 -> 9  -> 63
    // 27 -> 18 -> 72
    // 0  -> 27 -> 81
    // offset required to make click translations
    int clickOffset(int cell) {
        return 54 + (cell < 9 ? cell + 27 : cell - 9);
    }

    // offset required to make inventory translations
    int invOffset(int cell) {
        return 54 + cell;
    }

    public DoubleGUI setPlayerUnlocked(int cell) {
        unlockedCells.put(invOffset(cell), true);
        return this;
    }

    public DoubleGUI setPlayerUnlocked(int row, int col) {
        unlockedCells.put(invOffset(col + row * 9), true);
        return this;
    }

    public DoubleGUI setPlayerUnlocked(int cell, boolean open) {
        unlockedCells.put(invOffset(cell), open);
        return this;
    }

    public DoubleGUI setPlayerUnlocked(int row, int col, boolean open) {
        unlockedCells.put(invOffset(col + row * 9), open);
        return this;
    }

    public DoubleGUI setPlayerItem(int cell, ItemStack item) {
        cellItems.put(invOffset(cell), item);
        if (open && cell >= 0 && cell < 36) {
            cell = cell >= 27 ? cell - 27 : cell + 9;
            for (HumanEntity e : inventory.getViewers()) {
                e.getInventory().setItem(cell, item);
            }
        }
        return this;
    }

    public DoubleGUI setPlayerItem(int row, int col, ItemStack item) {
        int cell = col + row * 9;
        cellItems.put(invOffset(cell), item);
        if (open && cell >= 0 && cell < 36) {
            cell = cell >= 27 ? cell - 27 : cell + 9;
            for (HumanEntity e : inventory.getViewers()) {
                e.getInventory().setItem(cell, item);
            }
        }
        return this;
    }

    public DoubleGUI setPlayerAction(int cell, Clickable action) {
        setConditional(invOffset(cell), null, action, null);
        return this;
    }

    public DoubleGUI setPlayerAction(int cell, SimpleClickable action) {
        setConditional(invOffset(cell), null, null, action);
        return this;
    }

    public DoubleGUI setPlayerAction(int row, int col, Clickable action) {
        setConditional(invOffset(col + row * 9), null, action, null);
        return this;
    }

    public DoubleGUI setPlayerAction(int row, int col, SimpleClickable action) {
        setConditional(invOffset(col + row * 9), null, null, action);
        return this;
    }

    public DoubleGUI setPlayerAction(int cell, ClickType type, Clickable action) {
        setConditional(invOffset(cell), type, action, null);
        return this;
    }

    public DoubleGUI setPlayerAction(int cell, ClickType type, SimpleClickable action) {
        setConditional(invOffset(cell), type, null, action);
        return this;
    }

    public DoubleGUI setPlayerAction(int row, int col, ClickType type, Clickable action) {
        setConditional(invOffset(col + row * 9), type, action, null);
        return this;
    }

    public DoubleGUI setPlayerAction(int row, int col, ClickType type, SimpleClickable action) {
        setConditional(invOffset(col + row * 9), type, null, action);
        return this;
    }

    public DoubleGUI setPlayerActionForRange(int cellFirst, int cellLast, Clickable action) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            setConditional(invOffset(cell), null, action, null);
        }
        return this;
    }

    public DoubleGUI setPlayerActionForRange(int cellFirst, int cellLast, SimpleClickable action) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            setConditional(invOffset(cell), null, null, action);
        }
        return this;
    }

    public DoubleGUI setPlayerActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, Clickable action) {
        final int last = cellColLast + cellRowLast * 9;
        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            setConditional(invOffset(cell), null, action, null);
        }
        return this;
    }

    public DoubleGUI setPlayerActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, SimpleClickable action) {
        final int last = cellColLast + cellRowLast * 9;
        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            setConditional(invOffset(cell), null, null, action);
        }
        return this;
    }

    public DoubleGUI setPlayerActionForRange(int cellFirst, int cellLast, ClickType type, Clickable action) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            setConditional(invOffset(cell), type, action, null);
        }
        return this;
    }

    public DoubleGUI setPlayerActionForRange(int cellFirst, int cellLast, ClickType type, SimpleClickable action) {
        for (int cell = cellFirst; cell <= cellLast; ++cell) {
            setConditional(invOffset(cell), type, null, action);
        }
        return this;
    }

    public DoubleGUI setPlayerActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, ClickType type, Clickable action) {
        final int last = cellColLast + cellRowLast * 9;
        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            setConditional(invOffset(cell), type, action, null);
        }
        return this;
    }

    public DoubleGUI setPlayerActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, ClickType type, SimpleClickable action) {
        final int last = cellColLast + cellRowLast * 9;
        for (int cell = cellColFirst + cellRowFirst * 9; cell <= last; ++cell) {
            setConditional(invOffset(cell), type, null, action);
        }
        return this;
    }

    public DoubleGUI clearPlayerActions(int cell) {
        conditionalButtons.remove(cell = invOffset(cell));
        conditionalSimpleButtons.remove(cell);
        return this;
    }

    public DoubleGUI clearPlayerActions(int row, int col) {
        final int cell = invOffset(col + row * 9);
        conditionalButtons.remove(cell);
        conditionalSimpleButtons.remove(cell);
        return this;
    }

    public DoubleGUI setPlayerButton(int cell, ItemStack item, Clickable action) {
        setPlayerItem(cell, item);
        setConditional(invOffset(cell), null, action, null);
        return this;
    }

    public DoubleGUI setPlayerButton(int cell, ItemStack item, SimpleClickable action) {
        setPlayerItem(cell, item);
        setConditional(invOffset(cell), null, null, action);
        return this;
    }

    public DoubleGUI setPlayerButton(int row, int col, ItemStack item, Clickable action) {
        final int cell = col + row * 9;
        setPlayerItem(cell, item);
        setConditional(invOffset(cell), null, action, null);
        return this;
    }

    public DoubleGUI setPlayerButton(int row, int col, ItemStack item, SimpleClickable action) {
        final int cell = col + row * 9;
        setPlayerItem(cell, item);
        setConditional(invOffset(cell), null, null, action);
        return this;
    }

    public DoubleGUI setPlayerButton(int cell, ItemStack item, ClickType type, Clickable action) {
        setItem(cell, item);
        setConditional(invOffset(cell), type, action, null);
        return this;
    }

    public DoubleGUI setPlayerButton(int cell, ItemStack item, ClickType type, SimpleClickable action) {
        setPlayerItem(cell, item);
        setConditional(invOffset(cell), type, null, action);
        return this;
    }

    public DoubleGUI setPlayerButton(int row, int col, ItemStack item, ClickType type, Clickable action) {
        final int cell = col + row * 9;
        setPlayerItem(cell, item);
        setConditional(invOffset(cell), type, action, null);
        return this;
    }

    public DoubleGUI setPlayerButton(int row, int col, ItemStack item, ClickType type, SimpleClickable action) {
        final int cell = col + row * 9;
        setPlayerItem(cell, item);
        setConditional(invOffset(cell), type, null, action);
        return this;
    }

    @Override
    protected boolean onClickPlayerInventory(Player player, Inventory openInv, InventoryClickEvent event) {
        final int cell = event.getSlot(), offsetCell = clickOffset(cell);
        Map<ClickType, Clickable> conditionals = conditionalButtons.get(offsetCell);
        Map<ClickType, SimpleClickable> simpleConditionals;
        Clickable button;
        SimpleClickable simpleButton;
        if (conditionals != null
                && ((button = conditionals.get(event.getClick())) != null || (button = conditionals.get(null)) != null)) {
            button.onClick(player, inventory, this, event.getCursor(), cell, event.getClick());
        } else if ((simpleConditionals = conditionalSimpleButtons.get(offsetCell)) != null
                && ((simpleButton = simpleConditionals.get(event.getClick())) != null || (simpleButton = simpleConditionals.get(null)) != null)) {
            simpleButton.onClick(cell);
        } else {
            // no event for this button
            return false;
        }
        event.setCancelled(!unlockedCells.entrySet().stream().anyMatch(e -> offsetCell == e.getKey() && e.getValue()));
        return true;
    }

    @Override
    protected boolean onClickOutside(Player player, Inventory inventory, ItemStack cursor, ClickType click) {
        if (dropper != null) {
            return dropper.onDrop(player, inventory, this, cursor);
        }
        // do not allow by default
        return false;
    }

    @Override
    public void onOpen(Player player) {
        // replace the player's inventory
        ItemStack[] oldInv = player.getInventory().getContents();
        ItemStack[] newInv = new ItemStack[oldInv.length];

        for (int i = 0; i < newInv.length; ++i) {
            final ItemStack item = cellItems.get(invOffset(i < 9 ? i + 27 : i - 9));
            newInv[i] = item != null ? item : blankItem;
        }

        stash.put(player, oldInv);
        player.getInventory().setContents(newInv);
    }

    @Override
    public void onClose(GUIManager manager, Player player) {
        // restore the player's inventory
        if (stash.containsKey(player)) {
            player.getInventory().setContents(stash.remove(player));
            player.updateInventory();
        }
        // other closing functions
        super.onClose(manager, player);
    }

    /*
	 *********************************************************
	 * Other functions from GUI that we don't actually override 
	 *********************************************************
     */
    @Override
    public DoubleGUI setAcceptsItems(boolean acceptsItems) {
        return (DoubleGUI) super.setAcceptsItems(acceptsItems);
    }

    @Override
    public DoubleGUI setUnlocked(int cell) {
        return (DoubleGUI) super.setUnlocked(cell);
    }

    @Override
    public DoubleGUI setUnlocked(int cell, boolean open) {
        return (DoubleGUI) super.setUnlocked(cell, open);
    }

    @Override
    public DoubleGUI setUnlocked(int row, int col) {
        return (DoubleGUI) super.setUnlocked(row, col);
    }

    @Override
    public DoubleGUI setUnlocked(int row, int col, boolean open) {
        return (DoubleGUI) super.setUnlocked(row, col, open);
    }

    @Override
    public DoubleGUI setUnlockedRange(int cellFirst, int cellLast) {
        return (DoubleGUI) super.setUnlockedRange(cellFirst, cellLast);
    }

    @Override
    public DoubleGUI setUnlockedRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast) {
        return (DoubleGUI) super.setUnlockedRange(cellRowFirst, cellColFirst, cellRowLast, cellColLast);
    }

    @Override
    public DoubleGUI setAllowDrops(boolean allow) {
        return (DoubleGUI) super.setAllowDrops(allow);
    }

    @Override
    public DoubleGUI setAllowClose(boolean allow) {
        return (DoubleGUI) super.setAllowClose(allow);
    }

    @Override
    public DoubleGUI setTitle(String title) {
        return (DoubleGUI) super.setTitle(title);
    }

    @Override
    public DoubleGUI setRows(int rows) {
        return (DoubleGUI) super.setRows(rows);
    }

    @Override
    public DoubleGUI setDefaultItem(ItemStack item) {
        return (DoubleGUI) super.setDefaultItem(item);
    }

    @Override
    public DoubleGUI setItem(int cell, ItemStack item) {
        return (DoubleGUI) super.setItem(cell, item);
    }

    @Override
    public DoubleGUI setItem(int row, int col, ItemStack item) {
        return (DoubleGUI) super.setItem(row, col, item);
    }

    @Override
    public DoubleGUI updateItem(int cell, String name, String... lore) {
        return (DoubleGUI) super.updateItem(cell, name, lore);
    }

    @Override
    public DoubleGUI updateItem(int row, int col, String name, List<String> lore) {
        return (DoubleGUI) super.updateItem(col + row * 9, name, lore);
    }

    @Override
    public DoubleGUI updateItem(int cell, String name, List<String> lore) {
        return (DoubleGUI) super.updateItem(cell, name, lore);
    }

    @Override
    public DoubleGUI updateItem(int row, int col, ItemStack itemTo, String title, String... lore) {
        return (DoubleGUI) super.updateItem(col + row * 9, itemTo, title, lore);
    }

    @Override
    public DoubleGUI updateItem(int cell, ItemStack itemTo, String title, String... lore) {
        return (DoubleGUI) super.updateItem(cell, itemTo, title, lore);
    }

    @Override
    public DoubleGUI updateItem(int row, int col, LegacyMaterials itemTo, String title, String... lore) {
        return (DoubleGUI) super.updateItem(col + row * 9, itemTo, title, lore);
    }

    @Override
    public DoubleGUI updateItem(int cell, LegacyMaterials itemTo, String title, String... lore) {
        return (DoubleGUI) super.updateItem(cell, itemTo, title, lore);
    }

    @Override
    public DoubleGUI updateItem(int row, int col, ItemStack itemTo, String title, List<String> lore) {
        return (DoubleGUI) super.updateItem(col + row * 9, itemTo, title, lore);
    }

    @Override
    public DoubleGUI updateItem(int cell, ItemStack itemTo, String title, List<String> lore) {
        return (DoubleGUI) super.updateItem(cell, itemTo, title, lore);
    }

    @Override
    public DoubleGUI updateItem(int row, int col, LegacyMaterials itemTo, String title, List<String> lore) {
        return (DoubleGUI) super.updateItem(col + row * 9, itemTo, title, lore);
    }

    @Override
    public DoubleGUI updateItem(int cell, LegacyMaterials itemTo, String title, List<String> lore) {
        return (DoubleGUI) super.updateItem(cell, itemTo, title, lore);
    }

    @Override
    public DoubleGUI setAction(int cell, Clickable action) {
        return (DoubleGUI) super.setAction(cell, action);
    }

    @Override
    public DoubleGUI setAction(int cell, SimpleClickable action) {
        return (DoubleGUI) super.setAction(cell, action);
    }

    @Override
    public DoubleGUI setAction(int row, int col, Clickable action) {
        return (DoubleGUI) super.setAction(row, col, action);
    }

    @Override
    public DoubleGUI setAction(int row, int col, SimpleClickable action) {
        return (DoubleGUI) super.setAction(row, col, action);
    }

    @Override
    public DoubleGUI setAction(int cell, ClickType type, Clickable action) {
        return (DoubleGUI) super.setAction(cell, type, action);
    }

    @Override
    public DoubleGUI setAction(int cell, ClickType type, SimpleClickable action) {
        return (DoubleGUI) super.setAction(cell, type, action);
    }

    @Override
    public DoubleGUI setAction(int row, int col, ClickType type, Clickable action) {
        return (DoubleGUI) super.setAction(row, col, type, action);
    }

    @Override
    public DoubleGUI setAction(int row, int col, ClickType type, SimpleClickable action) {
        return (DoubleGUI) super.setAction(row, col, type, action);
    }

    @Override
    public DoubleGUI setActionForRange(int cellFirst, int cellLast, Clickable action) {
        return (DoubleGUI) super.setActionForRange(cellFirst, cellLast, action);
    }

    @Override
    public DoubleGUI setActionForRange(int cellFirst, int cellLast, SimpleClickable action) {
        return (DoubleGUI) super.setActionForRange(cellFirst, cellLast, action);
    }

    @Override
    public DoubleGUI setActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, Clickable action) {
        return (DoubleGUI) super.setActionForRange(cellRowFirst, cellColFirst, cellRowLast, cellColLast, action);
    }

    @Override
    public DoubleGUI setActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, SimpleClickable action) {
        return (DoubleGUI) super.setActionForRange(cellRowFirst, cellColFirst, cellRowLast, cellColLast, action);
    }

    @Override
    public DoubleGUI setActionForRange(int cellFirst, int cellLast, ClickType type, Clickable action) {
        return (DoubleGUI) super.setActionForRange(cellFirst, cellLast, type, action);
    }

    @Override
    public DoubleGUI setActionForRange(int cellFirst, int cellLast, ClickType type, SimpleClickable action) {
        return (DoubleGUI) super.setActionForRange(cellFirst, cellLast, type, action);
    }

    @Override
    public DoubleGUI setActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, ClickType type, Clickable action) {
        return (DoubleGUI) super.setActionForRange(cellRowFirst, cellColFirst, cellRowLast, cellColLast, type, action);
    }

    @Override
    public DoubleGUI setActionForRange(int cellRowFirst, int cellColFirst, int cellRowLast, int cellColLast, ClickType type, SimpleClickable action) {
        return (DoubleGUI) super.setActionForRange(cellRowFirst, cellColFirst, cellRowLast, cellColLast, type, action);
    }

    @Override
    public DoubleGUI clearActions(int cell) {
        return (DoubleGUI) super.clearActions(cell);
    }

    @Override
    public DoubleGUI clearActions(int row, int col) {
        return (DoubleGUI) super.clearActions(row, col);
    }

    @Override
    public DoubleGUI setButton(int cell, ItemStack item, Clickable action) {
        return (DoubleGUI) super.setButton(cell, item, action);
    }

    @Override
    public DoubleGUI setButton(int cell, ItemStack item, SimpleClickable action) {
        return (DoubleGUI) super.setButton(cell, item, action);
    }

    @Override
    public DoubleGUI setButton(int row, int col, ItemStack item, Clickable action) {
        return (DoubleGUI) super.setButton(row, col, item, action);
    }

    @Override
    public DoubleGUI setButton(int row, int col, ItemStack item, SimpleClickable action) {
        return (DoubleGUI) super.setButton(row, col, item, action);
    }

    @Override
    public DoubleGUI setButton(int cell, ItemStack item, ClickType type, Clickable action) {
        return (DoubleGUI) super.setButton(cell, item, type, action);
    }

    @Override
    public DoubleGUI setButton(int cell, ItemStack item, ClickType type, SimpleClickable action) {
        return (DoubleGUI) super.setButton(cell, item, type, action);
    }

    @Override
    public DoubleGUI setButton(int row, int col, ItemStack item, ClickType type, Clickable action) {
        return (DoubleGUI) super.setButton(row, col, item, type, action);
    }

    @Override
    public DoubleGUI setButton(int row, int col, ItemStack item, ClickType type, SimpleClickable action) {
        return (DoubleGUI) super.setButton(row, col, item, type, action);
    }

    @Override
    public DoubleGUI setOnOpen(Openable action) {
        return (DoubleGUI) super.setOnOpen(action);
    }

    @Override
    public DoubleGUI setOnClose(Closable action) {
        return (DoubleGUI) super.setOnClose(action);
    }

    @Override
    public DoubleGUI setOnDrop(Droppable action) {
        return (DoubleGUI) super.setOnDrop(action);
    }

    @Override
    public DoubleGUI setOnPage(Pagable action) {
        return (DoubleGUI) super.setOnPage(action);
    }

    @Override
    public DoubleGUI setNextPage(int row, int col, ItemStack item) {
        return (DoubleGUI) super.setNextPage(row, col, item);
    }

    @Override
    public DoubleGUI setPrevPage(int row, int col, ItemStack item) {
        return (DoubleGUI) super.setPrevPage(row, col, item);
    }
}
