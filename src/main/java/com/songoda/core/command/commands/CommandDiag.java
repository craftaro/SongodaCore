package com.songoda.core.command.commands;

import com.songoda.core.Plugin;
import com.songoda.core.SongodaCore;
import com.songoda.core.command.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.List;

public class CommandDiag extends AbstractCommand {

    private final String name = Bukkit.getServer().getClass().getPackage().getName();
    private final String version = name.substring(name.lastIndexOf('.') + 1);

    private final DecimalFormat format = new DecimalFormat("##.##");

    private Object serverInstance;
    private Field tpsField;


    public CommandDiag(AbstractCommand parent) {
        super(parent, false, "diag");

        try {
            serverInstance = getNMSClass("MinecraftServer").getMethod("getServer").invoke(null);
            tpsField = serverInstance.getClass().getField("recentTps");
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected ReturnType runCommand(SongodaCore instance, CommandSender sender, String... args) {

        sender.sendMessage("");
        sender.sendMessage("Songoda Diagnostics Information");
        sender.sendMessage("");
        sender.sendMessage("Plugins:");
        for (Plugin plugin : instance.getPlugins()) {
            sender.sendMessage(plugin.getJavaPlugin().getName()
                    + " (" + plugin.getJavaPlugin().getDescription().getVersion() + ")");
        }
        sender.sendMessage("");
        sender.sendMessage("Server Version: " + Bukkit.getVersion());
        sender.sendMessage("Operating System: " + System.getProperty("os.name"));
        sender.sendMessage("Allocated Memory: " + format.format(Runtime.getRuntime().maxMemory() / (1024 * 1024)) + "Mb");
        sender.sendMessage("Online Players: " +  Bukkit.getOnlinePlayers().size());
        try {
            double[] tps = ((double[]) tpsField.get(serverInstance));

            sender.sendMessage("TPS from last 1m, 5m, 15m: " + format.format(tps[0]) + ", "
                    + format.format(tps[1]) + ", " + format.format(tps[2]));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(SongodaCore instance, CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "songoda.admin";
    }

    @Override
    public String getSyntax() {
        return "/songoda diag";
    }

    @Override
    public String getDescription() {
        return "Display diagnostics information.";
    }

    private Class<?> getNMSClass(String className) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
