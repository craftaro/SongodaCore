package com.songoda.core;

import com.songoda.core.locale.Locale;
import com.songoda.core.utils.Metrics;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * REMINDER: When converting plugins to use this, REMOVE METRICS <br>
 * Must not have two instances of Metrics enabled!
 *
 * @author jascotty2
 */
public abstract class SongodaPlugin extends JavaPlugin {

    protected Locale locale;

    protected ConsoleCommandSender console = Bukkit.getConsoleSender();
    private boolean emergencyStop = false;

    public abstract void onPluginLoad();

    public abstract void onPluginEnable();

    public abstract void onPluginDisable();

    @Override
    public final void onLoad() {
        onPluginLoad();
    }

    @Override
    public final void onEnable() {
        console.sendMessage(ChatColor.GREEN + "=============================");
        console.sendMessage(String.format("%s%s %s by %sSongoda <3!", ChatColor.GRAY.toString(),
                getDescription().getName(), getDescription().getVersion(), ChatColor.DARK_PURPLE.toString()));
        console.sendMessage(String.format("%sAction: %s%s%s...", ChatColor.GRAY.toString(),
                ChatColor.GREEN.toString(), "Enabling", ChatColor.GRAY.toString()));

        try {
            locale = Locale.loadDefaultLocale(this, "en_US");
            // Starting Metrics
            Metrics.start(this);
            onPluginEnable();
        } catch (Throwable t) {
            getLogger().log(Level.SEVERE, "Unexpected error while loading " + getDescription().getName(), t);
            console.sendMessage(ChatColor.RED + "Disabling plugin!");
            emergencyStop = true;
            setEnabled(false);
        }

        console.sendMessage(ChatColor.GREEN + "=============================");
    }

    @Override
    public final void onDisable() {
        if (emergencyStop) {
            return;
        }
        console.sendMessage(ChatColor.GREEN + "=============================");
        console.sendMessage(String.format("%s%s %s by %sSongoda <3!", ChatColor.GRAY.toString(),
                getDescription().getName(), getDescription().getVersion(), ChatColor.DARK_PURPLE.toString()));
        console.sendMessage(String.format("%sAction: %s%s%s...", ChatColor.GRAY.toString(),
                ChatColor.RED.toString(), "Disabling", ChatColor.GRAY.toString()));
        onPluginDisable();
        console.sendMessage(ChatColor.GREEN + "=============================");
    }

    public ConsoleCommandSender getConsole() {
        return console;
    }

    public Locale getLocale() {
        return locale;
    }

    /**
     * Set the plugin's locale to a specific language
     *
     * @param localeName locale to use, eg "en_US"
     * @param reload optionally reload the loaded locale if the locale didn't
     * change
     * @return true if the locale exists and was loaded successfully
     */
    public boolean setLocale(String localeName, boolean reload) {
        if (locale != null && locale.getName().equals(localeName)) {
            return !reload || locale.reloadMessages();
        } else {
            Locale l = Locale.loadLocale(this, localeName);
            if (l != null) {
                locale = l;
                return true;
            } else {
                return false;
            }
        }
    }
}
