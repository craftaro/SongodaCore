package com.songoda.update.listeners;

import com.songoda.update.Plugin;
import com.songoda.update.SongodaUpdate;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginListener implements Listener {

    private SongodaUpdate instance;

    public LoginListener(SongodaUpdate instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (event.getPlayer().isOp()) {
            for (Plugin plugin : instance.getPlugins()) {
                if (plugin.getNotification() != null)
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin.getJavaPlugin(), () ->
                            event.getPlayer().sendMessage("[" + plugin.getJavaPlugin().getName() + "] " + plugin.getNotification()), 10L);
            }
        }
    }
}
