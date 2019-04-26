package com.songoda.update.utils.gui;

import com.songoda.update.SongodaUpdate;
import com.songoda.update.utils.version.NMSUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AbstractAnvilGUI {

    private static Class<?> BlockPositionClass;
    private static Class<?> PacketPlayOutOpenWindowClass;
    private static Class<?> IChatBaseComponentClass;
    private static Class<?> ICraftingClass;
    private static Class<?> ContainerAnvilClass;
    private static Class<?> ChatMessageClass;
    private static Class<?> EntityHumanClass;
    private static Class<?> ContainerClass;
    private static Class<?> ContainerAccessClass;
    private static Class<?> WorldClass;
    private static Class<?> PlayerInventoryClass;
    private static Class<?> ContainersClass;

    private Player player;
    private Map<AnvilSlot, ItemStack> items = new HashMap<>();
    private OnClose onClose = null;
    private Inventory inv;
    private Listener listener;

    private Sound closeSound = Sound.ENTITY_PLAYER_LEVELUP;

    static {
        BlockPositionClass = NMSUtil.getNMSClass("BlockPosition");
        PacketPlayOutOpenWindowClass = NMSUtil.getNMSClass("PacketPlayOutOpenWindow");
        IChatBaseComponentClass = NMSUtil.getNMSClass("IChatBaseComponent");
        ICraftingClass = NMSUtil.getNMSClass("ICrafting");
        ContainerAnvilClass = NMSUtil.getNMSClass("ContainerAnvil");
        EntityHumanClass = NMSUtil.getNMSClass("EntityHuman");
        ChatMessageClass = NMSUtil.getNMSClass("ChatMessage");
        ContainerClass = NMSUtil.getNMSClass("Container");
        WorldClass = NMSUtil.getNMSClass("World");
        PlayerInventoryClass = NMSUtil.getNMSClass("PlayerInventory");

        if (NMSUtil.getVersionNumber() > 13) {
            ContainerAccessClass = NMSUtil.getNMSClass("ContainerAccess");
            ContainersClass = NMSUtil.getNMSClass("Containers");
        }
    }

    public AbstractAnvilGUI(final Player player, final AnvilClickEventHandler handler) {
        this.player = player;

        this.listener = new Listener() {
            @EventHandler(priority = EventPriority.LOWEST)
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getWhoClicked() instanceof Player) {

                    if (event.getInventory().equals(inv)) {
                        event.setCancelled(true);

                        ItemStack item = event.getCurrentItem();
                        int slot = event.getRawSlot();
                        String name = "";

                        if (item != null) {
                            if (item.hasItemMeta()) {
                                ItemMeta meta = item.getItemMeta();

                                if (meta != null && meta.hasDisplayName()) {
                                    name = meta.getDisplayName();
                                }
                            }
                        }

                        AnvilClickEvent clickEvent = new AnvilClickEvent(AnvilSlot.bySlot(slot), name);

                        handler.onAnvilClick(clickEvent);

                        if (clickEvent.getWillClose()) {
                            event.getWhoClicked().closeInventory();
                        }

                        if (clickEvent.getWillDestroy()) {
                            destroy();
                        }
                    }
                }
            }

            @EventHandler(priority = EventPriority.LOWEST)
            public void onInventoryClose(InventoryCloseEvent event) {
                if (event.getPlayer() instanceof Player) {
                    Inventory inv = event.getInventory();
                    player.setLevel(player.getLevel() - 1);
                    if (inv.equals(inv)) {
                        inv.clear();
                        player.playSound(player.getLocation(), closeSound, 1F, 1F);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(SongodaUpdate.getHijackedPlugin(), () -> {
                            if (onClose != null) onClose.OnClose(player, inv);
                            destroy();
                        }, 1L);

                    }
                }
            }

            @EventHandler(priority = EventPriority.LOWEST)
            public void onPlayerQuit(PlayerQuitEvent event) {
                if (event.getPlayer().equals(getPlayer())) {
                    player.setLevel(player.getLevel() - 1);
                    destroy();
                }
            }
        };

        Bukkit.getPluginManager().registerEvents(listener, SongodaUpdate.getHijackedPlugin());
    }

    public Player getPlayer() {
        return player;
    }

    public void setSlot(AnvilSlot slot, ItemStack item) {
        items.put(slot, item);
    }

    public void open() {
        player.setLevel(player.getLevel() + 1);

        try {
            Object craftPlayer = NMSUtil.getCraftClass("entity.CraftPlayer").cast(player);
            Method getHandleMethod = craftPlayer.getClass().getMethod("getHandle");
            Object entityPlayer = getHandleMethod.invoke(craftPlayer);
            Object playerInventory = NMSUtil.getFieldObject(entityPlayer, NMSUtil.getField(entityPlayer.getClass(), "inventory", false));
            Object world = NMSUtil.getFieldObject(entityPlayer, NMSUtil.getField(entityPlayer.getClass(), "world", false));
            Object blockPosition = BlockPositionClass.getConstructor(int.class, int.class, int.class).newInstance(0, 0, 0);

            Object container;

            if (NMSUtil.getVersionNumber() > 13) {
                container = ContainerAnvilClass
                        .getConstructor(int.class, PlayerInventoryClass, ContainerAccessClass)
                        .newInstance(7, playerInventory, ContainerAccessClass.getMethod("at", WorldClass, BlockPositionClass).invoke(null, world, blockPosition));
            } else {
                container = ContainerAnvilClass
                        .getConstructor(PlayerInventoryClass, WorldClass, BlockPositionClass, EntityHumanClass)
                        .newInstance(playerInventory, world, blockPosition, entityPlayer);
            }

            NMSUtil.getField(ContainerClass, "checkReachable", true).set(container, false);

            Method getBukkitViewMethod = container.getClass().getMethod("getBukkitView");
            Object bukkitView = getBukkitViewMethod.invoke(container);
            Method getTopInventoryMethod = bukkitView.getClass().getMethod("getTopInventory");
            inv = (Inventory) getTopInventoryMethod.invoke(bukkitView);

            for (AnvilSlot slot : items.keySet()) {
                inv.setItem(slot.getSlot(), items.get(slot));
            }

            Method nextContainerCounterMethod = entityPlayer.getClass().getMethod("nextContainerCounter");
            int c = (int) nextContainerCounterMethod.invoke(entityPlayer);

            Constructor<?> chatMessageConstructor = ChatMessageClass.getConstructor(String.class, Object[].class);
            Object inventoryTitle = chatMessageConstructor.newInstance("Repairing", new Object[]{});

            Object packet;

            if (NMSUtil.getVersionNumber() > 13) {
                packet = PacketPlayOutOpenWindowClass
                        .getConstructor(int.class, ContainersClass, IChatBaseComponentClass)
                        .newInstance(c, ContainersClass.getField("ANVIL").get(null), inventoryTitle);
            } else {
                packet = PacketPlayOutOpenWindowClass
                        .getConstructor(int.class, String.class, IChatBaseComponentClass, int.class)
                        .newInstance(c, "minecraft:anvil", inventoryTitle, 0);
            }

            NMSUtil.sendPacket(player, packet);

            Field activeContainerField = NMSUtil.getField(EntityHumanClass, "activeContainer", true);

            if (activeContainerField != null) {
                activeContainerField.set(entityPlayer, container);
                NMSUtil.getField(ContainerClass, "windowId", true).set(activeContainerField.get(entityPlayer), c);
                Method addSlotListenerMethod = activeContainerField.get(entityPlayer).getClass().getMethod("addSlotListener", ICraftingClass);
                addSlotListenerMethod.invoke(activeContainerField.get(entityPlayer), entityPlayer);

                if (NMSUtil.getVersionNumber() > 13) {
                    ContainerClass.getMethod("setTitle", IChatBaseComponentClass).invoke(container, inventoryTitle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        player = null;
        items = null;

        HandlerList.unregisterAll(listener);

        listener = null;
    }

    private OnClose getOnClose() {
        return onClose;
    }

    public void setOnClose(OnClose onClose) {
        this.onClose = onClose;
    }

    public void setCloseSound(Sound sound) {
        closeSound = sound;
    }

    public enum AnvilSlot {
        INPUT_LEFT(0),
        INPUT_RIGHT(1),
        OUTPUT(2);

        private int slot;

        AnvilSlot(int slot) {
            this.slot = slot;
        }

        public static AnvilSlot bySlot(int slot) {
            for (AnvilSlot anvilSlot : values()) {
                if (anvilSlot.getSlot() == slot) {
                    return anvilSlot;
                }
            }

            return null;
        }

        public int getSlot() {
            return slot;
        }
    }

    @FunctionalInterface
    public interface AnvilClickEventHandler {
        void onAnvilClick(AnvilClickEvent event);
    }

    public class AnvilClickEvent {
        private AnvilSlot slot;

        private String name;

        private boolean close = true;
        private boolean destroy = true;

        public AnvilClickEvent(AnvilSlot slot, String name) {
            this.slot = slot;
            this.name = name;
        }

        public AnvilSlot getSlot() {
            return slot;
        }

        public String getName() {
            return name;
        }

        public boolean getWillClose() {
            return close;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public boolean getWillDestroy() {
            return destroy;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
    }

}