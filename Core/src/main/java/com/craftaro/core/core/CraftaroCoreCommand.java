package com.craftaro.core.core;

import com.craftaro.core.CraftaroCore;
import com.craftaro.core.commands.AbstractCommand;
import com.craftaro.core.gui.GuiManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CraftaroCoreCommand extends AbstractCommand {
    protected GuiManager guiManager;

    public CraftaroCoreCommand() {
        super(CommandType.CONSOLE_OK, "craftaro", "songoda");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (sender instanceof Player) {
            if (guiManager == null || guiManager.isClosed()) {
                guiManager = new GuiManager(CraftaroCore.getHijackedPlugin());
            }

            guiManager.showGUI((Player) sender, new CraftaroCoreOverviewGUI());
        } else {
            sender.sendMessage("/craftaro diag");
            sender.sendMessage("/craftaro myip");
            sender.sendMessage("/craftaro uuid");
        }

        return ReturnType.SUCCESS;
    }

    @Override
    public String getPermissionNode() {
        return "songoda.admin";
    }

    @Override
    public String getSyntax() {
        return "/craftaro";
    }

    @Override
    public String getDescription() {
        return "Displays this interface.";
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }
}
