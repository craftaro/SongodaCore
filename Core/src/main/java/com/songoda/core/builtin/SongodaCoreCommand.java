package com.songoda.core.builtin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.songoda.core.SongodaCore;
import com.songoda.core.compatibility.ClassMapping;
import com.songoda.core.compatibility.ServerProject;
import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.plugins.PluginInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

@CommandAlias("songoda")
@CommandPermission("songoda.admin")
public class SongodaCoreCommand extends BaseCommand {

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

    @Default
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
}
