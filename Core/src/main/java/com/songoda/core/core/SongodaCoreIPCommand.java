package com.songoda.core.core;

import com.songoda.core.commands.AbstractCommand;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.command.CommandSender;
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
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("https://marketplace.songoda.com/api/v2/products/license/ip");

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try (InputStream inputStream = entity.getContent()) {
                    //Read json and get ip key value
                    JSONParser parser = new JSONParser();
                    JSONObject json = (JSONObject) parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    String ip = (String) json.get("ip");
                    sender.sendMessage("");
                    sender.sendMessage("IP Information");
                    sender.sendMessage("");
                    sender.sendMessage("Public IP: " + ip);
                    sender.sendMessage("");
                    return ReturnType.SUCCESS;
                } catch (Exception ignored) {}
            }

        } catch (Exception ignored) {}

        sender.sendMessage("Error: Could not get IP information.");
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
