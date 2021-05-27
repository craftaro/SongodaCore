package com.songoda.core;

import com.songoda.core.commands.CommandManager;
import com.songoda.core.compatibility.ClientVersion;
import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.core.LocaleModule;
import com.songoda.core.core.PluginInfo;
import com.songoda.core.core.PluginInfoModule;
import com.songoda.core.core.SongodaCoreCommand;
import com.songoda.core.core.SongodaCoreDiagCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class SongodaCore {

    private final static String prefix = "[SongodaCore]";

    /**
     * Whenever we make a major change to the core GUI, updater,
     * or other function used by the core, increment this number
     */
    private final static int coreRevision = 8;

    /**
     * This has been added as of Rev 6
     */
    private final static String coreVersion = "2.4.54";

    /**
     * This is specific to the website api
     */
    private final static int updaterVersion = 1;

    private final static Set<PluginInfo> registeredPlugins = new HashSet<>();

    private static SongodaCore INSTANCE = null;
    private JavaPlugin piggybackedPlugin;
    private CommandManager commandManager;
    private EventListener loginListener;
    private ShadedEventListener shadingListener;

    public static boolean hasShading() {
        // sneaky hack to check the package name since maven tries to re-shade all references to the package string
        return !SongodaCore.class.getPackage().getName().equals(new String(new char[]{'c', 'o', 'm', '.', 's', 'o', 'n', 'g', 'o', 'd', 'a', '.', 'c', 'o', 'r', 'e'}));
    }

    public static void registerPlugin(JavaPlugin plugin, int pluginID, CompatibleMaterial icon) {
        registerPlugin(plugin, pluginID, icon == null ? "STONE" : icon.name(), coreVersion);
    }

    public static void registerPlugin(JavaPlugin plugin, int pluginID, String icon) {
        registerPlugin(plugin, pluginID, icon, "?");
    }

    public static void registerPlugin(JavaPlugin plugin, int pluginID, String icon, String coreVersion) {
        boolean showAds = false;
        if (INSTANCE == null) {
            // First: are there any other instances of SongodaCore active?
            for (Class<?> clazz : Bukkit.getServicesManager().getKnownServices()) {
                if (clazz.getSimpleName().equals("SongodaCore")) {
                    try {
                        // test to see if we're up to date
                        int otherVersion;
                        try {
                            otherVersion = (int) clazz.getMethod("getCoreVersion").invoke(null);
                        } catch (Exception ignore) {
                            otherVersion = -1;
                        }
                        if (otherVersion >= getCoreVersion()) {
                            // use the active service
                            // assuming that the other is greater than R6 if we get here ;)
                            clazz.getMethod("registerPlugin", JavaPlugin.class, int.class, String.class, String.class).invoke(null, plugin, pluginID, icon, coreVersion);

                            if (hasShading()) {
                                (INSTANCE = new SongodaCore()).piggybackedPlugin = plugin;
                                INSTANCE.shadingListener = new ShadedEventListener();
                                Bukkit.getPluginManager().registerEvents(INSTANCE.shadingListener, plugin);
                            }
                            return;
                        } else {
                            // we are newer than the registered service: steal all of its registrations
                            // grab the old core's registrations
                            List otherPlugins = (List) clazz.getMethod("getPlugins").invoke(null);
                            // destroy the old core
                            Object oldCore = clazz.getMethod("getInstance").invoke(null);
                            Method destruct = clazz.getDeclaredMethod("destroy");
                            destruct.setAccessible(true);
                            destruct.invoke(oldCore);
                            // register ourselves as the SongodaCore service!
                            INSTANCE = new SongodaCore(plugin);
                            INSTANCE.init();
                            PluginInfo info = INSTANCE.register(plugin, pluginID, icon, coreVersion);
                            Bukkit.getScheduler().runTaskLater(plugin, () -> runAds(info), 100L);
                            Bukkit.getServicesManager().register(SongodaCore.class, INSTANCE, plugin, ServicePriority.Normal);
                            // we need (JavaPlugin plugin, int pluginID, String icon) for our object
                            if (!otherPlugins.isEmpty()) {
                                Object testSubject = otherPlugins.get(0);
                                Class otherPluginInfo = testSubject.getClass();
                                Method otherPluginInfo_getJavaPlugin = otherPluginInfo.getMethod("getJavaPlugin");
                                Method otherPluginInfo_getSongodaId = otherPluginInfo.getMethod("getSongodaId");
                                Method otherPluginInfo_getCoreIcon = otherPluginInfo.getMethod("getCoreIcon");
                                Method otherPluginInfo_getCoreLibraryVersion = otherVersion >= 6 ? otherPluginInfo.getMethod("getCoreLibraryVersion") : null;
                                for (Object other : otherPlugins) {
                                    INSTANCE.register(
                                            (JavaPlugin) otherPluginInfo_getJavaPlugin.invoke(other),
                                            (int) otherPluginInfo_getSongodaId.invoke(other),
                                            (String) otherPluginInfo_getCoreIcon.invoke(other),
                                            otherPluginInfo_getCoreLibraryVersion != null ? (String) otherPluginInfo_getCoreLibraryVersion.invoke(other) : "?");
                                }
                            }
                        }
                        return;
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
                        plugin.getLogger().log(Level.WARNING, "Error registering core service", ignored);
                    }
                }
            }
            // register ourselves as the SongodaCore service!
            INSTANCE = new SongodaCore(plugin);
            INSTANCE.init();
            showAds = true;
            Bukkit.getServicesManager().register(SongodaCore.class, INSTANCE, plugin, ServicePriority.Normal);
        }
        PluginInfo info = INSTANCE.register(plugin, pluginID, icon, coreVersion);
        if (showAds)
            Bukkit.getScheduler().runTaskLater(plugin, () -> runAds(info), 100L);
    }

    SongodaCore() {
        commandManager = null;
    }

    SongodaCore(JavaPlugin javaPlugin) {
        piggybackedPlugin = javaPlugin;
        commandManager = new CommandManager(piggybackedPlugin);
        loginListener = new EventListener();
    }

    private void init() {
        shadingListener = new ShadedEventListener();
        commandManager.registerCommandDynamically(new SongodaCoreCommand())
                .addSubCommand(new SongodaCoreDiagCommand());
        Bukkit.getPluginManager().registerEvents(loginListener, piggybackedPlugin);
        Bukkit.getPluginManager().registerEvents(shadingListener, piggybackedPlugin);
        // we aggressively want to own this command
        tasks.add(Bukkit.getScheduler().runTaskLaterAsynchronously(piggybackedPlugin, () -> {
            CommandManager.registerCommandDynamically(piggybackedPlugin, "songoda", commandManager, commandManager);
        }, 10 * 60 * 1));
        tasks.add(Bukkit.getScheduler().runTaskLaterAsynchronously(piggybackedPlugin, () -> {
            CommandManager.registerCommandDynamically(piggybackedPlugin, "songoda", commandManager, commandManager);
        }, 20 * 60 * 1));
        tasks.add(Bukkit.getScheduler().runTaskLaterAsynchronously(piggybackedPlugin, () -> {
            CommandManager.registerCommandDynamically(piggybackedPlugin, "songoda", commandManager, commandManager);
        }, 20 * 60 * 2));
    }

    /**
     * Used to yield this core to a newer core
     */
    private void destroy() {
        Bukkit.getServicesManager().unregister(SongodaCore.class, INSTANCE);
        tasks.stream().filter(Objects::nonNull)
                .forEach(task -> Bukkit.getScheduler().cancelTask(task.getTaskId()));
        HandlerList.unregisterAll(loginListener);
        if (!hasShading()) {
            HandlerList.unregisterAll(shadingListener);
        }
        registeredPlugins.clear();
        commandManager = null;
        loginListener = null;
    }

    private static void runAds(PluginInfo pluginInfo) {
        if (registeredPlugins.stream().noneMatch(p -> p.getJavaPlugin().getName().toLowerCase().contains("ultimate")))
            return;

        JSONObject json = pluginInfo.getJson();
        JSONArray ads = (JSONArray) json.get("ads");

        if (ads == null || ads.isEmpty())
            return;

        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(String.format("%s---------------------- %sSongoda+ ads %s----------------------", ChatColor.GRAY.toString(),
                ChatColor.LIGHT_PURPLE.toString(), ChatColor.GRAY.toString()));
        for (Object o : ads) {
            JSONObject ad = (JSONObject) o;
            console.sendMessage(String.format("%s" + ad.get("patron") + " - " + ad.get("link")
                    + " - " + ad.get("descr"), ChatColor.DARK_PURPLE));
        }
        console.sendMessage(String.format("%s---------- %sPut your ad here patreon.songoda.com %s----------", ChatColor.GRAY.toString(),
                ChatColor.LIGHT_PURPLE.toString(), ChatColor.GRAY.toString()));
    }

    private ArrayList<BukkitTask> tasks = new ArrayList();

    private PluginInfo register(JavaPlugin plugin, int pluginID, String icon, String libraryVersion) {
        System.out.println(getPrefix() + "Hooked " + plugin.getName() + ".");
        PluginInfo info = new PluginInfo(plugin, pluginID, icon, libraryVersion);
        // don't forget to check for language pack updates ;)
        info.addModule(new LocaleModule());
        registeredPlugins.add(info);
        tasks.add(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> update(info), 60L));
        return info;
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
            System.out.println("Failed to parse json for " + plugin.getJavaPlugin().getName() + " update check");
        }
    }

    public static List<PluginInfo> getPlugins() {
        return new ArrayList<>(registeredPlugins);
    }

    public static int getCoreVersion() {
        return coreRevision;
    }

    public static String getCoreLibraryVersion() {
        return coreVersion;
    }

    public static int getUpdaterVersion() {
        return updaterVersion;
    }

    public static String getPrefix() {
        return prefix + " ";
    }

    public static boolean isRegistered(String plugin) {
        return registeredPlugins.stream().anyMatch(p -> p.getJavaPlugin().getName().equalsIgnoreCase(plugin));
    }

    public static JavaPlugin getHijackedPlugin() {
        return INSTANCE == null ? null : INSTANCE.piggybackedPlugin;
    }

    public static SongodaCore getInstance() {
        return INSTANCE;
    }

    private static class ShadedEventListener implements Listener {
        boolean via = false;
        boolean proto = false;

        ShadedEventListener() {
            if ((via = Bukkit.getPluginManager().isPluginEnabled("ViaVersion"))) {
                Bukkit.getOnlinePlayers().forEach(p -> ClientVersion.onLoginVia(p));
            } else if ((proto = Bukkit.getPluginManager().isPluginEnabled("ProtocolSupport"))) {
                Bukkit.getOnlinePlayers().forEach(p -> ClientVersion.onLoginProtocol(p));
            }
        }

        @EventHandler
        void onLogin(PlayerLoginEvent event) {
            if (via) {
                ClientVersion.onLoginVia(event.getPlayer());
            } else if (proto) {
                ClientVersion.onLoginProtocol(event.getPlayer());
            }
        }

        @EventHandler
        void onLogout(PlayerQuitEvent event) {
            if (via) {
                ClientVersion.onLogout(event.getPlayer());
            }
        }

        @EventHandler
        void onEnable(PluginEnableEvent event) {
            // technically shouldn't have online players here, but idk
            if (!via && (via = event.getPlugin().getName().equals("ViaVersion"))) {
                Bukkit.getOnlinePlayers().forEach(p -> ClientVersion.onLoginVia(p));
            } else if (!proto && (proto = event.getPlugin().getName().equals("ProtocolSupport"))) {
                Bukkit.getOnlinePlayers().forEach(p -> ClientVersion.onLoginProtocol(p));
            }
        }
    }

    private class EventListener implements Listener {
        final HashMap<UUID, Long> lastCheck = new HashMap();

        @EventHandler
        void onLogin(PlayerLoginEvent event) {
            final Player player = event.getPlayer();
            // don't spam players with update checks
            long now = System.currentTimeMillis();
            Long last = lastCheck.get(player.getUniqueId());
            if (last != null && now - 10000 < last) return;
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
            if (pi != null) {
                registeredPlugins.remove(pi);
            }
            if (event.getPlugin() == piggybackedPlugin) {
                // uh-oh! Abandon ship!!
                Bukkit.getServicesManager().unregisterAll(piggybackedPlugin);
                // can we move somewhere else?
                if ((pi = registeredPlugins.stream().findFirst().orElse(null)) != null) {
                    // move ourselves to this plugin
                    piggybackedPlugin = pi.getJavaPlugin();
                    Bukkit.getServicesManager().register(SongodaCore.class, INSTANCE, piggybackedPlugin, ServicePriority.Normal);
                    Bukkit.getPluginManager().registerEvents(loginListener, piggybackedPlugin);
                    Bukkit.getPluginManager().registerEvents(shadingListener, piggybackedPlugin);
                    CommandManager.registerCommandDynamically(piggybackedPlugin, "songoda", commandManager, commandManager);
                }
            }
        }
    }
}
