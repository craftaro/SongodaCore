package com.songoda.core.input;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

public class ChatPrompt implements Listener {

    private static final List<UUID> registered = new ArrayList<>();

    private final ChatConfirmHandler handler;
    private OnClose onClose = null;
    private OnCancel onCancel = null;
    private Listener listener;

    private ChatPrompt(Player player, ChatConfirmHandler hander) {
        this.handler = hander;
        registered.add(player.getUniqueId());
    }

    public static ChatPrompt showPrompt(Plugin plugin, Player player, ChatConfirmHandler hander) {
        ChatPrompt prompt = new ChatPrompt(player, hander);
        prompt.startListener(plugin);
        player.closeInventory();
        return prompt;
    }

    public static ChatPrompt showPrompt(Plugin plugin, Player player, String message, ChatConfirmHandler hander) {
        ChatPrompt prompt = new ChatPrompt(player, hander);
        prompt.startListener(plugin);
        player.closeInventory();
        if (message != null)
            player.sendMessage(message);
        return prompt;
    }

    public static boolean isRegistered(Player player) {
        return registered.contains(player.getUniqueId());
    }

    public static boolean unregister(Player player) {
        return registered.remove(player.getUniqueId());
    }

    public ChatPrompt setOnClose(OnClose onClose) {
        this.onClose = onClose;
        return this;
    }

    public ChatPrompt setOnCancel(OnCancel onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    private void startListener(Plugin plugin) {
        this.listener = new Listener() {
            @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
            public void onChat(AsyncPlayerChatEvent event) {
                Player player = event.getPlayer();
                if (!ChatPrompt.isRegistered(player)) return;

                ChatPrompt.unregister(player);
                event.setCancelled(true);

                ChatConfirmEvent chatConfirmEvent = new ChatConfirmEvent(player, event.getMessage());

                try {
                    handler.onChat(chatConfirmEvent);
                } catch (Throwable t) {
                    plugin.getLogger().log(Level.SEVERE, "Failed to process chat prompt", t);
                }

                if (onClose != null) {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            onClose.onClose(), 0L);
                }
                HandlerList.unregisterAll(listener);
            }
            @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
            public void onCancel(PlayerCommandPreprocessEvent event) {
                Player player = event.getPlayer();
                if (!ChatPrompt.isRegistered(player)) return;

                ChatPrompt.unregister(player);

                if(event.getMessage().toLowerCase().startsWith("/cancel"))
                    event.setCancelled(true);

                if (onCancel != null) {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            onCancel.onCancel(), 0L);
                } else if (onClose != null) {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            onClose.onClose(), 0L);
                }
                HandlerList.unregisterAll(listener);
            }
        };

        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public static interface ChatConfirmHandler {
        void onChat(ChatConfirmEvent event);
    }

    public static interface OnClose {
        void onClose();
    }

    public static interface OnCancel {
        void onCancel();
    }

    public static class ChatConfirmEvent {

        private final Player player;
        private final String message;

        public ChatConfirmEvent(Player player, String message) {
            this.player = player;
            this.message = message;
        }

        public Player getPlayer() {
            return player;
        }

        public String getMessage() {
            return message;
        }
    }

}
