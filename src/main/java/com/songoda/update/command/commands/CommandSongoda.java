package com.songoda.update.command.commands;

import com.songoda.update.SongodaUpdate;
import com.songoda.update.command.AbstractCommand;
import com.songoda.update.gui.GUIOverview;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandSongoda extends AbstractCommand {

    public CommandSongoda() {
        super(true, false, "songoda");
    }

    @Override
    protected ReturnType runCommand(SongodaUpdate instance, CommandSender sender, String... args) {
        new GUIOverview(instance, (Player) sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(SongodaUpdate instance, CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "songoda.admin";
    }

    @Override
    public String getSyntax() {
        return "/songoda";
    }

    @Override
    public String getDescription() {
        return "Displays this interface.";
    }
}
