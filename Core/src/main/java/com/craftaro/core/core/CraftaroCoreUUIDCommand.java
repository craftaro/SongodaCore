package com.craftaro.core.core;

import com.craftaro.core.commands.AbstractCommand;
import com.craftaro.core.utils.SongodaAuth;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CraftaroCoreUUIDCommand extends AbstractCommand {

    public CraftaroCoreUUIDCommand() {
        super(CommandType.CONSOLE_OK, "uuid");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        sender.sendMessage("");
        if (sender instanceof Player) {
            TextComponent component = new TextComponent("Your server UUID is: " + SongodaAuth.getUUID());
            component.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, SongodaAuth.getUUID().toString()));
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("Click to copy")}));
            sender.spigot().sendMessage(component);
        } else {
            sender.sendMessage("Your server UUID is: " + SongodaAuth.getUUID());
        }
        sender.sendMessage("");
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "songoda.admin";
    }

    @Override
    public String getSyntax() {
        return "/craftaro uuid";
    }

    @Override
    public String getDescription() {
        return "Returns your server's uuid";
    }
}
