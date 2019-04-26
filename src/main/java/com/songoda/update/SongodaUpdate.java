package com.songoda.update;

import com.songoda.update.command.CommandManager;
import com.songoda.update.listeners.LoginListener;
import com.songoda.update.utils.ServerVersion;
import org.apache.commons.lang.ArrayUtils;
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

    private static String prefix = "[SongodaUpdate] ";

    private ServerVersion serverVersion = ServerVersion.fromPackageName(Bukkit.getServer().getClass().getPackage().getName());


    private static int version = 1;

    private static List<Plugin> registeredPlugins = new ArrayList<>();

    private static SongodaUpdate INSTANCE;

    private static JavaPlugin hijackedPlugin;

    public SongodaUpdate() {
        hijackedPlugin = registeredPlugins.get(0).getJavaPlugin();
        Bukkit.getPluginManager().registerEvents(new LoginListener(this), hijackedPlugin);

        new CommandManager(this);
    }

    private void update(Plugin plugin) {
        try {
            URL url = new URL("http://update.songoda.com/index.php?plugin=" + plugin.getSongodaId() +
                    "&version=" + plugin.getJavaPlugin().getDescription().getVersion());
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
            plugin.setMarketplaceLink((String) json.get("link"));
            plugin.setNotification((String) json.get("notification"));
            plugin.setChangeLog((String) json.get("changeLog"));

            plugin.setJson(json);

            for (Module module : plugin.getModules()) {
                module.run(plugin);
            }
        } catch (IOException e) {
            System.out.println("Connection with Songoda servers failed...");
        } catch (ParseException e) {
            System.out.println("Failed to parse json.");
        }
    }

    public static Plugin load(Plugin plugin) {
        registeredPlugins.add(plugin);
        System.out.println(prefix + "Hooked " + plugin.getJavaPlugin().getName() + ".");
        if (INSTANCE == null) INSTANCE = new SongodaUpdate();
        getInstance().update(plugin);
        return plugin;
    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public boolean isServerVersion(ServerVersion version) {
        return serverVersion == version;
    }
    public boolean isServerVersion(ServerVersion... versions) {
        return ArrayUtils.contains(versions, serverVersion);
    }

    public boolean isServerVersionAtLeast(ServerVersion version) {
        return serverVersion.ordinal() >= version.ordinal();
    }

    public List<Plugin> getPlugins() {
        return new ArrayList<>(registeredPlugins);
    }

    public static int getVersion() {
        return version;
    }

    public String getPrefix() {
        return prefix;
    }

    public static JavaPlugin getHijackedPlugin() {
        return hijackedPlugin;
    }

    public static SongodaUpdate getInstance() {
        return INSTANCE;
    }
}
