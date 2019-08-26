package com.songoda.core;

import com.songoda.core.commands.CommandManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SongodaCore {

    private final static String prefix = "[SongodaCore]";

    private final static int updaterVersion = 1;

    private final static Set<PluginInfo> registeredPlugins = new HashSet<>();

    private static SongodaCore INSTANCE = null;
    private JavaPlugin piggybackedPlugin;
    private final CommandManager commandManager;
    private final EventListener loginListener = new EventListener();
    private final HashMap<UUID, Long> lastCheck = new HashMap();

    public static void registerPlugin(JavaPlugin plugin, int pluginID) {
        if(INSTANCE == null) {
            // First: are there any other instances of SongodaCore active?
            for (Class<?> clazz : Bukkit.getServicesManager().getKnownServices()) {
                if(clazz.getSimpleName().equals("SongodaCore")) {
                    try {
                        // use the active service
                        clazz.getMethod("registerPlugin", JavaPlugin.class, int.class).invoke(null, plugin, pluginID);
                        return;
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {

                    }
                }
            }
            // register ourselves as the SongodaCore service!
            INSTANCE = new SongodaCore(plugin);
            Bukkit.getServicesManager().register(SongodaCore.class, INSTANCE, plugin, ServicePriority.Normal);
        }
        INSTANCE.register(plugin, pluginID);
    }

    public SongodaCore(JavaPlugin javaPlugin) {
        piggybackedPlugin = javaPlugin;
        commandManager = new CommandManager(piggybackedPlugin);
        commandManager.registerCommandDynamically(new SongodaCoreCommand(this))
                .addSubCommand(new SongodaCoreDiagCommand(this));
        Bukkit.getPluginManager().registerEvents(loginListener, javaPlugin);
    }

    private void register(JavaPlugin plugin, int pluginID) {
        System.out.println(getPrefix() + "Hooked " + plugin.getName() + ".");
        PluginInfo info = new PluginInfo(plugin, pluginID);
        // don't forget to check for language pack updates ;)
        info.addModule(new LocaleModule());
        registeredPlugins.add(info);
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> update(info), 20L);
    }

    private void update(PluginInfo plugin) {
        try {
            URL url = new URL("https://update.songoda.com/index.php?plugin=" + plugin.getSongodaId()
                    + "&version=" + plugin.getJavaPlugin().getDescription().getVersion()
                    + "&updaterVersion=" + updaterVersion);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            urlConnection.setRequestProperty("Accept", "*/*");
            urlConnection.setConnectTimeout(5000);
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuilder sb = new StringBuilder();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            urlConnection.disconnect();

            String jsonString = sb.toString();
            JSONObject json = (JSONObject) new JSONParser().parse(jsonString);

            plugin.setLatestVersion((String) json.get("latestVersion"));
            plugin.setMarketplaceLink((String) json.get("link"));
            plugin.setNotification((String) json.get("notification"));
            plugin.setChangeLog((String) json.get("changeLog"));

            plugin.setJson(json);

            for (PluginInfoModule module : plugin.getModules()) {
                module.run(plugin);
            }
        } catch (IOException e) {
            final String er = e.getMessage();
            System.out.println("Connection with Songoda servers failed: " + (er.contains("URL") ? er.substring(0, er.indexOf("URL") + 3) : er));
        } catch (ParseException e) {
            System.out.println("Failed to parse json.");
        }
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
        return INSTANCE == null ? null : INSTANCE.piggybackedPlugin;
    }

    public static SongodaCore getInstance() {
        return INSTANCE;
    }

    private class EventListener implements Listener {
        @EventHandler
        void onLogin(PlayerLoginEvent event) {
            // don't spam players with update checks
            final Player player = event.getPlayer();
            long now = System.currentTimeMillis();
            Long last = lastCheck.get(player.getUniqueId());
            if(last != null && now - 10000 < last) return;
            lastCheck.put(player.getUniqueId(), now);
            // is this player good to revieve update notices?
            if (!event.getPlayer().isOp() && !player.hasPermission("songoda.updatecheck")) return;
            // check for updates! ;)
            for (PluginInfo plugin : getPlugins()) {
                if (plugin.getNotification() != null && plugin.getJavaPlugin().isEnabled())
                    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin.getJavaPlugin(), () ->
                            player.sendMessage("[" + plugin.getJavaPlugin().getName() + "] " + plugin.getNotification()), 10L);
            }
        }

        @EventHandler
        void onDisable(PluginDisableEvent event) {
            // don't track disabled plugins
            PluginInfo pi = registeredPlugins.stream().filter(p -> event.getPlugin() == p.getJavaPlugin()).findFirst().orElse(null);
            if(pi != null) {
                registeredPlugins.remove(pi);
            }
            if(event.getPlugin() == piggybackedPlugin) {
                // uh-oh! Abandon ship!!
                Bukkit.getServicesManager().unregisterAll(piggybackedPlugin);
                // can we move somewhere else?
                if((pi = registeredPlugins.stream().findFirst().orElse(null)) != null) {
                    // move ourselves to this plugin
                    piggybackedPlugin = pi.getJavaPlugin();
                    Bukkit.getServicesManager().register(SongodaCore.class, INSTANCE, piggybackedPlugin, ServicePriority.Normal);
                    Bukkit.getPluginManager().registerEvents(loginListener, piggybackedPlugin);
                    CommandManager.registerCommandDynamically(piggybackedPlugin, "songoda", commandManager, commandManager);
                }
            }
        }
    }
}
