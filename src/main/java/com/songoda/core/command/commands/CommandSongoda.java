package com.songoda.core.command.commands;

import com.songoda.core.SongodaCore;
import com.songoda.core.command.AbstractCommand;
import com.songoda.core.gui.GUIOverview;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandSongoda extends AbstractCommand {

    public CommandSongoda() {
        super(true, false, "songoda");
    }

    @Override
    protected ReturnType runCommand(SongodaCore instance, CommandSender sender, String... args) {
        new GUIOverview(instance, (Player) sender);
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
        return "/songoda";
    }

    @Override
    public String getDescription() {
        return "Displays this interface.";
    }
}
