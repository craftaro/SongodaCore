package com.songoda.update;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class SongodaUpdate {
    
    private static int version = 1;

    private static List<Plugin> registeredPlugins = new ArrayList<>();

    private static SongodaUpdate INSTANCE;

    public SongodaUpdate() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(registeredPlugins.get(0).getJavaPlugin(), this::update, 20L);
    }

    private void update() {
        for (Plugin plugin : registeredPlugins) {
            try {
                JavaPlugin javaPlugin = plugin.getJavaPlugin();
                System.out.println("Establishing connection with the Songoda update server.");
                URL url = new URL("http://update.songoda.com/index.php?plugin=" + javaPlugin.getName() +
                        "&version=" + javaPlugin.getDescription().getVersion());
                URLConnection urlConnection = url.openConnection();
                InputStream is = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int numCharsRead;
                char[] charArray = new char[1024];
                StringBuffer sb = new StringBuffer();
                while ((numCharsRead = isr.read(charArray)) > 0) {
                    sb.append(charArray, 0, numCharsRead);
                }
                String jsonString = sb.toString();
                JSONObject json = (JSONObject) new JSONParser().parse(jsonString);

                plugin.setLatestVersion((String) json.get("latestVersion"));
                plugin.setNotification((String) json.get("notification"));

                plugin.setJson(json);

                for (Module module : plugin.getModules()) {
                    module.run(plugin);
                }
            } catch (IOException e) {
                System.out.println("Connection failed...");
                e.printStackTrace(); //ToDo: This cannot be here in final.
            } catch (ParseException e) {
                System.out.println("Failed to parse json.");
                e.printStackTrace(); //ToDo: This cannot be here in final.
            }
        }
    }

    public static void load(Plugin plugin) {
        registeredPlugins.add(plugin);
        System.out.println("Hooked " + plugin.getJavaPlugin().getName() + ".");
        if (INSTANCE == null) INSTANCE = new SongodaUpdate();
    }

    public static int getVersion() {
        return version;
    }

    public static SongodaUpdate getInstance() {
        return INSTANCE;
    }
}
