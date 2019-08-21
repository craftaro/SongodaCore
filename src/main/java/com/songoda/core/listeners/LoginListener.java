package com.songoda.core.listeners;

import com.songoda.core.Plugin;
import com.songoda.core.SongodaCore;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LoginListener implements Listener {

    private SongodaCore instance;

    public LoginListener(SongodaCore instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (!event.getPlayer().isOp()) return;
        for (Plugin plugin : instance.getPlugins()) {
            if (plugin.getNotification() != null)
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin.getJavaPlugin(), () ->
                        event.getPlayer().sendMessage("[" + plugin.getJavaPlugin().getName() + "] " + plugin.getNotification()), 10L);
        }
    }
}
