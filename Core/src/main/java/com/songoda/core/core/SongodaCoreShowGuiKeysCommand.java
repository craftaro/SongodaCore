package com.songoda.core.core;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.core.gui.CustomizableGui;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SongodaCoreShowGuiKeysCommand extends AbstractCommand {

    public SongodaCoreShowGuiKeysCommand() {
        super(false, "showguikeys");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        sender.sendMessage(CustomizableGui.toggleShowGuiKeys() ? "Now showing keys." : "No longer showing keys.");

        return ReturnType.SUCCESS;
    }

    @Override
    public String getPermissionNode() {
        return "songoda.admin";
    }

    @Override
    public String getSyntax() {
        return "/songoda showguikeys";
    }

    @Override
    public String getDescription() {
        return "Show the keys for all items in every gui.";
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

}
