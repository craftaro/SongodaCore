package com.songoda.core;

import com.songoda.core.command.CommandManager;
import com.songoda.core.listeners.LoginListener;
import com.songoda.core.modules.Module;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SongodaCore {

    private static String prefix = "[SongodaCore]";

    private static int updaterVersion = 1;

    private static Set<PluginInfo> registeredPlugins = new HashSet<>();

    private static SongodaCore INSTANCE;

    private static JavaPlugin hijackedPlugin;

    public static void registerPlugin(JavaPlugin plugin, int pluginID) {
        
    }
    
    
    public SongodaCore(JavaPlugin javaPlugin) {
        hijackedPlugin = javaPlugin;
        Bukkit.getPluginManager().registerEvents(new LoginListener(this), hijackedPlugin);

        new CommandManager(this);
    }

    private void update(PluginInfo plugin) {
        try {
            URL url = new URL("http://update.songoda.com/index.php?plugin=" + plugin.getSongodaId()
                    + "&version=" + plugin.getJavaPlugin().getDescription().getVersion()
                    + "&updaterVersion=" + updaterVersion);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuilder sb = new StringBuilder();
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

    public static PluginInfo load(PluginInfo plugin) {
        boolean found = false;
        for (Class<?> clazz : Bukkit.getServicesManager().getKnownServices()) {
            try {
                clazz.getMethod("hook", PluginInfo.class).invoke(null, plugin);
                found = true;
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {

            }
        }
        if (!found) {
            if (INSTANCE == null) INSTANCE = new SongodaCore(plugin.getJavaPlugin());
            Bukkit.getServicesManager().register(SongodaCore.class, INSTANCE, hijackedPlugin, ServicePriority.Normal);
            hook(plugin);
        }
        return plugin;
    }

    public static void hook(PluginInfo plugin) {
        System.out.println(getPrefix() + "Hooked " + plugin.getJavaPlugin().getName() + ".");
        getInstance().update(plugin);
        registeredPlugins.add(plugin);
    }

    public List<PluginInfo> getPlugins() {
        return new ArrayList<>(registeredPlugins);
    }

    public static int getVersion() {
        return updaterVersion;
    }

    public static String getPrefix() {
        return prefix + " ";
    }

    public static JavaPlugin getHijackedPlugin() {
        return hijackedPlugin;
    }

    public static SongodaCore getInstance() {
        return INSTANCE;
    }
}
