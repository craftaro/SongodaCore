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

public class CraftaroCoreIPCommand extends AbstractCommand {

    public CraftaroCoreIPCommand() {
        super(CommandType.CONSOLE_OK, "myip");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        Thread thread = new Thread(() -> {
            String ip = SongodaAuth.getIP();
            sender.sendMessage("");
            sender.sendMessage("IP Information");
            sender.sendMessage("");
            if (sender instanceof Player) {
                TextComponent component = new TextComponent("Your public IP is: " + ip);
                component.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, ip));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("Click to copy")}));
                sender.spigot().sendMessage(component);
            } else {
                sender.sendMessage("Your public IP is: " + ip);
            }
            sender.sendMessage("");
        });
        thread.start();

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
        return "/craftaro myip";
    }

    @Override
    public String getDescription() {
        return "Displays your public IP address.";
    }
}
