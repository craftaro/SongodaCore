package com.songoda.core.utils.gui;

import com.songoda.core.SongodaCore;
import com.songoda.core.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public abstract class AbstractGUI implements Listener {

    private static boolean listenersInitialized = false;
    protected final Player player;
    protected Inventory inventory = null;
    protected String setTitle = null;
    protected boolean cancelBottom = false;
    private Map<Range, Clickable> clickables = new HashMap<>();
    private List<OnClose> onCloses = new ArrayList<>();
    private Map<Range, Boolean> draggableRanges = new HashMap<>();

    public AbstractGUI(Player player) {
        this.player = player;
    }

    public static void initializeListeners(JavaPlugin plugin) {
        if (listenersInitialized) return;

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onClickGUI(InventoryClickEvent event) {
                Inventory inventory = event.getClickedInventory();
                if (inventory == null) return;
                AbstractGUI gui = getGUIFromInventory(inventory);
                Player player = (Player) event.getWhoClicked();

                boolean bottom = false;

                InventoryType type = event.getClickedInventory().getType();
                if (type != InventoryType.CHEST && type != InventoryType.PLAYER) return;

                if (gui == null && event.getWhoClicked().getOpenInventory().getTopInventory() != null) {
                    Inventory top = event.getWhoClicked().getOpenInventory().getTopInventory();
                    gui = getGUIFromInventory(top);

                    if (gui != null && gui.cancelBottom) event.setCancelled(true);
                    bottom = true;
                }

                if (gui == null) return;

                if (!bottom) event.setCancelled(true);

                if (!gui.draggableRanges.isEmpty() && !bottom) {
                    for (Map.Entry<Range, Boolean> entry : gui.draggableRanges.entrySet()) {
                        Range range = entry.getKey();
                        if (range.getMax() == range.getMin() && event.getSlot() == range.getMin()
                                || event.getSlot() >= range.getMin() && event.getSlot() <= range.getMax()) {
                            event.setCancelled(!entry.getValue());
                            if (!entry.getValue()) break;
                        }
                    }
                }

                Map<Range, Clickable> entries = new HashMap<>(gui.clickables);

                for (Map.Entry<Range, Clickable> entry : entries.entrySet()) {
                    Range range = entry.getKey();
                    if (range.isBottom() && !bottom || !range.isBottom() && bottom || range.getClickType() != null && range.getClickType() != event.getClick())
                        continue;
                    if (event.getSlot() >= range.getMin() && event.getSlot() <= range.getMax()) {
                        entry.getValue().Clickable(player, inventory, event.getCursor(), event.getSlot(), event.getClick());
                        player.playSound(player.getLocation(), entry.getKey().getOnClickSound(), 1F, 1F);
                    }
                }
            }

            @EventHandler
            public void onCloseGUI(InventoryCloseEvent event) {
                Inventory inventory = event.getInventory();
                AbstractGUI gui = getGUIFromInventory(inventory);

                if (gui == null || gui.inventory == null) return;

                for (OnClose onClose : gui.onCloses) {
                    onClose.OnClose((Player) event.getPlayer(), inventory);
                }
            }

            private AbstractGUI getGUIFromInventory(Inventory inventory) {
                if (inventory.getHolder() == null) return null;
                InventoryHolder holder = inventory.getHolder();
                if (!(holder instanceof GUIHolder)) return null;

                return ((AbstractGUI.GUIHolder) holder).getGUI();
            }
        }, plugin);
        listenersInitialized = true;
    }

    public void init(String title, int slots) {
        if (inventory == null
                || inventory.getSize() != slots
                || ChatColor.translateAlternateColorCodes('&', title) != player.getOpenInventory().getTitle()) {
            this.inventory = Bukkit.getServer().createInventory(new GUIHolder(), slots, Methods.formatText(title));
            this.setTitle = Methods.formatText(title);
            if (this.clickables.size() == 0)
                registerClickables();
            if (this.onCloses.size() == 0)
                registerOnCloses();
        }
        constructGUI();
        initializeListeners(SongodaCore.getHijackedPlugin());
        player.openInventory(inventory);
    }

    protected abstract void constructGUI();

    protected void addDraggable(Range range, boolean option) {
        this.draggableRanges.put(range, option);
    }

    protected void removeDraggable() {
        this.draggableRanges.clear();
    }

    protected abstract void registerClickables();

    protected abstract void registerOnCloses();

    protected ItemStack createButton(int slot, Inventory inventory, ItemStack item, String name, String... lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Methods.formatText(name));
        if (lore != null && lore.length != 0) {
            List<String> newLore = new ArrayList<>();
            for (String line : lore) {
                if (line == null) continue;
                for (String string : line.split("\\s*\\r?\\n\\s*")) {
                    int lastIndex = 0;
                    for (int n = 0; n < string.length(); n++) {
                        if (n - lastIndex < 35)
                            continue;

                        if (string.charAt(n) == ' ') {
                            newLore.add(Methods.formatText("&7" + string.substring(lastIndex, n).trim()));
                            lastIndex = n;
                        }
                    }

                    if (lastIndex - string.length() < 35)
                        newLore.add(Methods.formatText("&7" + string.substring(lastIndex, string.length()).trim()));
                }
            }
            meta.setLore(newLore);
        }
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
        return item;
    }

    protected ItemStack createButton(int slot, ItemStack item, String name, ArrayList<String> lore) {
        return createButton(slot, inventory, item, name, lore.toArray(new String[0]));
    }


    protected ItemStack createButton(int slot, ItemStack item, String name, String... lore) {
        return createButton(slot, inventory, item, name, lore);
    }

    protected ItemStack createButton(int slot, Object item, String name, String... lore) {
        if (item instanceof ItemStack)
            return createButton(slot, inventory, (ItemStack) item, name, lore);
        else
            return createButton(slot, inventory, (Material) item, name, lore);
    }

    protected ItemStack createButton(int slot, Inventory inventory, Material material, String name, String... lore) {
        return createButton(slot, inventory, new ItemStack(material), name, lore);
    }

    protected ItemStack createButton(int slot, Material material, String name, String... lore) {
        return createButton(slot, inventory, new ItemStack(material), name, lore);
    }

    protected ItemStack createButton(int slot, Material material, String name, ArrayList<String> lore) {
        return createButton(slot, material, name, lore.toArray(new String[0]));
    }

    protected void registerClickable(int min, int max, ClickType clickType, boolean bottom, Clickable clickable) {
        clickables.put(new Range(min, max, clickType, bottom), clickable);
    }

    protected void registerClickable(int min, int max, ClickType clickType, Clickable clickable) {
        registerClickable(min, max, clickType, false, clickable);
    }

    protected void registerClickable(int slot, ClickType clickType, Clickable clickable) {
        registerClickable(slot, slot, clickType, false, clickable);
    }

    protected void registerClickable(int min, int max, Clickable clickable) {
        registerClickable(min, max, null, false, clickable);
    }

    protected void registerClickable(int slot, boolean bottom, Clickable clickable) {
        registerClickable(slot, slot, null, bottom, clickable);
    }

    protected void registerClickable(int slot, Clickable clickable) {
        registerClickable(slot, slot, null, false, clickable);
    }

    protected void resetClickables() {
        clickables.clear();
    }

    protected void registerOnClose(OnClose onClose) {
        onCloses.add(onClose);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public class GUIHolder implements InventoryHolder {

        @Override
        public Inventory getInventory() {
            return inventory;
        }

        public AbstractGUI getGUI() {
            return AbstractGUI.this;
        }
    }

    public String getSetTitle() {
        return setTitle;
    }
}
