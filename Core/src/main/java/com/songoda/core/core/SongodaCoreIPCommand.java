package com.songoda.core.core;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.core.utils.SongodaAuth;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SongodaCoreIPCommand extends AbstractCommand {

    public SongodaCoreIPCommand() {
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
        return "/songoda myip";
    }

    @Override
    public String getDescription() {
        return "Displays your public IP address.";
    }
}
