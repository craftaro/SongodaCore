package com.songoda.core;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.core.gui.GUIManager;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class SongodaCoreCommand extends AbstractCommand {

    final SongodaCore instance;
    protected SongodaCoreCommand(SongodaCore instance) {
        super(false, "songoda");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if(sender instanceof Player) {
            if(instance.guiManager == null) {
                instance.guiManager = new GUIManager(SongodaCore.getHijackedPlugin());
            }
            instance.guiManager.showGUI((Player) sender, new SongodaCoreOverviewGUI(instance));
        } else {
            sender.sendMessage("/songoda diag");
        }
        return ReturnType.SUCCESS;
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

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

}
