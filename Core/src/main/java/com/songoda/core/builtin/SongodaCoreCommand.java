package com.songoda.core.builtin;

import com.songoda.core.SongodaCore;
import com.songoda.core.SongodaPlugin;
import com.songoda.core.compatibility.ClassMapping;
import com.songoda.core.compatibility.ServerProject;
import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.database.DataManager;
import com.songoda.core.database.DataMigration;
import com.songoda.core.database.DatabaseType;
import com.songoda.core.plugins.PluginInfo;
import com.songoda.core.utils.ColorUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

@Command("songoda")
@CommandPermission("songoda.admin")
public class SongodaCoreCommand {

    private final SongodaCore core;
    private final DecimalFormat format = new DecimalFormat("##.##");

    private Object serverInstance;
    private Field tpsField;

    public SongodaCoreCommand(SongodaCore core) {
        this.core = core;

        try {
            serverInstance = ClassMapping.MINECRAFT_SERVER.getClazz().getMethod("getServer").invoke(null);
            tpsField = serverInstance.getClass().getField("recentTps");
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException
                 | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    @DefaultFor("songoda")
    public void onDefault(CommandSender sender) {
        // TODO gui
    }

    @Subcommand("diag")
    public void onDiagnostics(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("Songoda Diagnostics Information");
        sender.sendMessage("");
        sender.sendMessage("Plugins:");

        for (PluginInfo plugin : core.getRegisteredPlugins()) {
            sender.sendMessage(plugin.getJavaPlugin().getName()
                    + " (" + plugin.getJavaPlugin().getDescription().getVersion() + ")");
        }

        sender.sendMessage("");
        sender.sendMessage("Server Version: " + Bukkit.getVersion());
        sender.sendMessage("NMS: " + ServerProject.getServerVersion() + " " + ServerVersion.getServerVersionString());
        sender.sendMessage("Operating System: " + System.getProperty("os.name"));
        sender.sendMessage("Allocated Memory: " + format.format(Runtime.getRuntime().maxMemory() / (1024 * 1024)) + "Mb");
        sender.sendMessage("Online Players: " + Bukkit.getOnlinePlayers().size());

        if (tpsField != null) {
            try {
                double[] tps = ((double[]) tpsField.get(serverInstance));

                sender.sendMessage("TPS from last 1m, 5m, 15m: " + format.format(tps[0]) + ", "
                        + format.format(tps[1]) + ", " + format.format(tps[2]));
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Subcommand("convertdb")
    public void onConvertDB(CommandSender sender, String plugin, String to) {
        DatabaseType type;
        try {
            type = DatabaseType.valueOf(to.toUpperCase());
        } catch (IllegalArgumentException ex) {
            sender.sendMessage("Invalid database type.");
            return;
        }
        SongodaPlugin songodaPlugin = (SongodaPlugin) SongodaCore.getInstance().getRegisteredPlugins().stream()
                .filter(info -> info.getJavaPlugin().getName().equalsIgnoreCase(plugin))
                .findFirst().map(PluginInfo::getJavaPlugin).orElse(null);
        if (songodaPlugin == null) {
            sender.sendMessage("Invalid plugin.");
            return;
        }
        if (songodaPlugin.getDataManager().getDatabaseConnector().getType() == type) {
            sender.sendMessage("Database is already " + type.name() + ".");
            return;
        }

        Thread t = new Thread(() -> {
            DataManager newManager = DataMigration.convert(songodaPlugin, type);
            if (newManager == null || !newManager.getDatabaseConnector().isInitialized()) {
                sender.sendMessage("Failed to convert database.");
                return;
            }
            songodaPlugin.setDataManager(newManager);
            sender.sendMessage("Converted database to " + type.name() + ".");
        });
    }
}
