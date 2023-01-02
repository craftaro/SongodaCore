package com.songoda.core.core;

import com.songoda.core.SongodaCore;
import com.songoda.core.commands.AbstractCommand;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

public class SongodaCoreSetAPIKeyCommand extends AbstractCommand {

    public SongodaCoreSetAPIKeyCommand() {
        super(CommandType.CONSOLE_OK, "key");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length == 0) {
            sender.sendMessage("Please provide an API key.");
            return ReturnType.FAILURE;
        }

        String key = args[0];

        //Let's check if the key is in the correct format
        UUID uuid = UUID.fromString(key);
        if (uuid.version() != 4) {
            sender.sendMessage("The API key you provided is not in the correct format.");
            return ReturnType.FAILURE;
        }
        SongodaCore.setAPIKey(key);
        sender.sendMessage("API key set.");
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "songodacore.admin";
    }

    @Override
    public String getSyntax() {
        return "/songodacore key <key>";
    }

    @Override
    public String getDescription() {
        return "Sets the stored API key.";
    }
}
