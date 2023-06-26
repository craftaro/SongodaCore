package com.craftaro.core.builtin;

import com.craftaro.core.CraftaroCore;
import com.craftaro.core.CraftaroPlugin;
import com.craftaro.core.compatibility.ClassMapping;
import com.craftaro.core.compatibility.ServerProject;
import com.craftaro.core.compatibility.ServerVersion;
import com.craftaro.core.database.DataManager;
import com.craftaro.core.database.DataMigration;
import com.craftaro.core.database.DatabaseType;
import com.craftaro.core.plugins.PluginInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

@Command("craftaro")
@CommandPermission("craftaro.admin")
public class CraftaroCoreCommand {

    private final CraftaroCore core;
    private final DecimalFormat format = new DecimalFormat("##.##");

    private Object serverInstance;
    private Field tpsField;

    public CraftaroCoreCommand(CraftaroCore core) {
        this.core = core;

        try {
            serverInstance = ClassMapping.MINECRAFT_SERVER.getClazz().getMethod("getServer").invoke(null);
            tpsField = serverInstance.getClass().getField("recentTps");
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException
                 | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    @DefaultFor("craftaro")
    public void onDefault(CommandSender sender) {
        // TODO gui
    }

    @Subcommand("diag")
    public void onDiagnostics(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("Craftaro Diagnostics Information");
        sender.sendMessage("");
        sender.sendMessage("Core Version: " + core.getDescription().getVersion());
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
        CraftaroPlugin craftaroPlugin = (CraftaroPlugin) CraftaroCore.getInstance().getRegisteredPlugins().stream()
                .filter(info -> info.getJavaPlugin().getName().equalsIgnoreCase(plugin))
                .findFirst().map(PluginInfo::getJavaPlugin).orElse(null);
        if (craftaroPlugin == null) {
            sender.sendMessage("Invalid plugin.");
            return;
        }
        if (craftaroPlugin.getDataManager().getDatabaseConnector().getType() == type) {
            sender.sendMessage("Database is already " + type.name() + ".");
            return;
        }

        Thread t = new Thread(() -> {
            DataManager newManager = DataMigration.convert(craftaroPlugin, type);
            if (newManager == null || !newManager.getDatabaseConnector().isInitialized()) {
                sender.sendMessage("Failed to convert database.");
                return;
            }
            craftaroPlugin.setDataManager(newManager);
            sender.sendMessage("Converted database to " + type.name() + ".");
        });
    }
}
