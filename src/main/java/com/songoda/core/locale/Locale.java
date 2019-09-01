package com.songoda.core.locale;

import com.songoda.core.utils.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Assists in the utilization of localization files. <br>
 * Created to be used by the Songoda Team. <br>
 * NOTE: Using this class in multiple plugins requires shading! <br>
 * Updated 2019-09-01 to support UTF encoded lang files - jascotty2
 *
 * @author Brianna O'Keefe - Songoda
 */
public class Locale {

    private static final Pattern NODE_PATTERN = Pattern.compile("^([^ ]+)\\s*=\\s*\"?(.*?)\"?$");
    private static final String FILE_EXTENSION = ".lang";

    private final Map<String, String> nodes = new HashMap<>();
    private final Plugin plugin;
    private final File file;
    private final String name;

    /**
     * Instantiate the Locale class for future use
     *
     * @param plugin Owning Plugin
     * @param file Location of the locale file
     * @param name The locale name for the language
     */
    public Locale(Plugin plugin, File file, String name) {
        this.plugin = plugin;
        this.file = file;
        this.name = name;
    }

    /**
	 * Load a default-included lang file from the plugin's jar file
     *
     * @param plugin plugin to load from
     * @param name name of the default locale, eg "en_US"
     * @return returns the loaded Locale, or null if there was an error
     */
    public static Locale loadDefaultLocale(JavaPlugin plugin, String name) {
        saveDefaultLocale(plugin, name, name);
        return loadLocale(plugin, name);
    }

    /**
     * Load a locale from this plugin's locale directory
     *
     * @param plugin plugin to load from
     * @param name name of the locale, eg "en_US"
     * @return returns the loaded Locale, or null if there was an error
     */
    public static Locale loadLocale(JavaPlugin plugin, String name) {
        File localeFolder = new File(plugin.getDataFolder(), "locales/");
        if (!localeFolder.exists()) return null;
        File localeFile = new File(localeFolder, name + FILE_EXTENSION);
        if (!localeFolder.exists()) return null;
        // found the lang file, now load it in!
        Locale l = new Locale(plugin, localeFile, name);
        if (!l.reloadMessages()) return null;
        plugin.getLogger().info("Loaded locale \"" + name + "\"");
        return l;
	}

    /**
     * Load all locales from this plugin's locale directory
     *
     * @param plugin plugin to load from
     * @return returns the loaded Locales
     */
    public static List<Locale> loadAllLocales(JavaPlugin plugin) {
        File localeFolder = new File(plugin.getDataFolder(), "locales/");
        List<Locale> all = new ArrayList();
		for (File localeFile : localeFolder.listFiles()) {
            String fileName = localeFile.getName();
            if (!fileName.endsWith(FILE_EXTENSION)) continue;
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            if (fileName.split("_").length != 2) continue;
            Locale l = new Locale(plugin, localeFile, fileName);
            if (l.reloadMessages()) {
                plugin.getLogger().info("Loaded locale \"" + fileName + "\"");
                all.add(l);
            }
        }
        return all;
    }

    /**
     * Get a list of all locale files in this plugin's locale directory
     *
     * @param plugin Plugin to check for
     */
    public static List<String> getLocales(Plugin plugin) {
        File localeFolder = new File(plugin.getDataFolder(), "locales/");
        List<String> all = new ArrayList();
        for (File localeFile : localeFolder.listFiles()) {
            String fileName = localeFile.getName();
            if (!fileName.endsWith(FILE_EXTENSION)) continue; 
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            if (fileName.split("_").length != 2) {
                continue;
            }
            all.add(fileName);
        }
        return all;
    }

    /**
     * Save a locale file from the Plugin's Resources to the locale folder
     *
     * @param plugin plugin owning the locale file
     * @param locale the specific locale file to save
     * @param fileName where to save the file
     * @return true if the operation was successful, false otherwise
     */
    public static boolean saveDefaultLocale(JavaPlugin plugin, String locale, String fileName) {
        return saveLocale(plugin, plugin.getResource(locale + FILE_EXTENSION), fileName, true);
    }

    /**
     * Save a locale file from an InputStream to the locale folder
     *
     * @param plugin plugin owning the locale file
     * @param in file to save
     * @param fileName the name of the file to save
     * @return true if the operation was successful, false otherwise
     */
    public static boolean saveLocale(Plugin plugin, InputStream in, String fileName) {
        return saveLocale(plugin, in, fileName, false);
    }

    private static boolean saveLocale(Plugin plugin, InputStream in, String fileName, boolean builtin) {
        if(in == null) return false;
        File localeFolder = new File(plugin.getDataFolder(), "locales/");
        if (!localeFolder.exists()) localeFolder.mkdirs();

        if (!fileName.endsWith(FILE_EXTENSION))
            fileName = fileName + FILE_EXTENSION;

        File destinationFile = new File(localeFolder, fileName);
        if (destinationFile.exists())
            return updateFiles(plugin, in, destinationFile, builtin);

        try (OutputStream outputStream = new FileOutputStream(destinationFile)) {
            copy(in, outputStream);

            fileName = fileName.substring(0, fileName.lastIndexOf('.'));

            if (fileName.split("_").length != 2) return false;

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Write new changes to existing files, if any at all
    private static boolean updateFiles(Plugin plugin, InputStream defaultFile, File existingFile, boolean builtin) {
        boolean changed = false;

        List<String> defaultLines, existingLines;
        try (BufferedInputStream defaultIn = new BufferedInputStream(defaultFile);
                BufferedInputStream existingIn = new BufferedInputStream(new FileInputStream(existingFile))) {

            Charset defaultCharset = TextUtils.detectCharset(defaultIn, StandardCharsets.UTF_8);
            Charset existingCharset = TextUtils.detectCharset(existingIn, StandardCharsets.UTF_8);

            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(existingFile, true), existingCharset);
                    BufferedReader defaultReader = new BufferedReader(new InputStreamReader(defaultIn, defaultCharset));
                    BufferedReader existingReader = new BufferedReader(new InputStreamReader(existingIn, existingCharset));) {
                defaultLines = defaultReader.lines().map(s -> s.replaceAll("[\uFEFF\uFFFE\u200B]", "")).collect(Collectors.toList());
                existingLines = existingReader.lines().map(s -> s.replaceAll("[\uFEFF\uFFFE\u200B]", "").split("\\s*=")[0]).collect(Collectors.toList());

                for (String defaultValue : defaultLines) {

                    if (defaultValue.isEmpty() || defaultValue.startsWith("#")) {
                        continue;
                    }

                    String key = defaultValue.split("\\s*=")[0];

                    if (!existingLines.contains(key)) {
                        if (!changed) {
                            writer.write("\n\n");
                            // Leave a note alerting the user of the newly added messages.
                            writer.write("# New messages for " + plugin.getName() + " v" + plugin.getDescription().getVersion() + ".");

                            // If changes were found outside of the default file leave a note explaining that.
                            if (!builtin) {
                                writer.write("\n");
                                writer.write("# These translations were found untranslated, join\n");
                                writer.write("# our translation Discord https://discord.gg/f7fpZEf\n");
                                writer.write("# to request an official update!\n");
                            }
                        }

                        writer.write("\n");

                        // re-encode to target format? (transform down, not up)
//                        if (defaultCharset != existingCharset) {
//                            byte[] encoded = defaultValue.getBytes(defaultCharset);
//                            defaultValue = new String(encoded, 2, encoded.length - 2, existingCharset);
//                        }
                        writer.write(defaultValue);

                        changed = true;
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }

        return changed;
    }

    /**
     * Clear the previous message cache and load new messages directly from file
     *
     * @return reload messages from file
     */
    public boolean reloadMessages() {
        if (!this.file.exists()) {
            plugin.getLogger().warning("Could not find file for locale \"" + this.name + "\"");
            return false;
        }

        this.nodes.clear(); // Clear previous data (if any)

        // guess what encoding this file is in
        Charset charset = TextUtils.detectCharset(file, null);
        if(charset == null) {
            plugin.getLogger().warning("Could not determine charset for locale \"" + this.name + "\"");
            charset = StandardCharsets.UTF_8;
        }

        // load in the file!
        try (FileInputStream stream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream) stream, charset));) {
            String line;
            for (int lineNumber = 0; (line = reader.readLine()) != null; lineNumber++) {
                if ((line = line.trim()).isEmpty() || line.startsWith("#") /* Comment */) continue;
                
                if (lineNumber == 0){
                    // remove BOM markers, if any
                    line = line.replaceAll("[\uFEFF\uFFFE\u200B]", "");
                }
                // convert to UTF-8 ?
                /*if (charset == StandardCharsets.UTF_16LE || charset == StandardCharsets.UTF_16BE) {
                    byte[] encoded = line.getBytes(charset);
                    line = new String(encoded, 0, encoded.length, StandardCharsets.UTF_8);
                } */

                Matcher matcher = NODE_PATTERN.matcher(line);
                if (!matcher.find()) {
                    System.err.println("Invalid locale syntax at (line=" + lineNumber + "): " + line);
                    continue;
                }

                nodes.put(matcher.group(1), matcher.group(2));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * Supply the Message object with the plugins prefix.
     *
     * @param message message to be applied
     * @return applied message
     */
    private Message supplyPrefix(Message message) {
        return message.setPrefix(this.nodes.getOrDefault("general.nametag.prefix", "[" + plugin.getName() + "]"));
    }

    /**
     * Create a new unsaved Message
     *
     * @param message the message to create
     * @return the created message
     */
    public Message newMessage(String message) {
        return supplyPrefix(new Message(message));
    }

    /**
     * Get a message set for a specific node.
     *
     * @param node the node to get
     * @return the message for the specified node
     */
    public Message getMessage(String node) {
        return this.getMessageOrDefault(node, node);
    }

    /**
     * Get a message set for a specific node
     *
     * @param node         the node to get
     * @param defaultValue the default value given that a value for the node was not found
     * @return the message for the specified node. Default if none found
     */
    public Message getMessageOrDefault(String node, String defaultValue) {
        return supplyPrefix(new Message(this.nodes.getOrDefault(node, defaultValue)));
    }

    /**
     * Return the locale name (i.e. "en_US")
     *
     * @return the locale name
     */
    public String getName() {
        return name;
    }

    private static void copy(InputStream input, OutputStream output) {
        int n;
        byte[] buffer = new byte[1024 * 4];

        try {
            while ((n = input.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
