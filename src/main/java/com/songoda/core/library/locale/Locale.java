package com.songoda.ultimateclaims.utils.locale;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Assists in the utilization of localization files.
 * Created to be used by the Songoda Team.
 *
 * @author Brianna O'Keefe - Songoda
 */
public class Locale {

    private static final List<Locale> LOCALES = new ArrayList<>();
    private static final Pattern NODE_PATTERN = Pattern.compile("(\\w+(?:\\.{1}\\w+)*)\\s*=\\s*\"(.*)\"");
    private static final String FILE_EXTENSION = ".lang";
    private static JavaPlugin plugin;
    private static File localeFolder;

    private final Map<String, String> nodes = new HashMap<>();

    private static String defaultLocale;

    private File file;
    private String name;

    /**
     * Instantiate the Locale class for future use
     *
     * @param name the name of the instantiated language
     */
    private Locale(String name) {
        if (plugin == null)
            return;

        this.name = name;
        
        String fileName = name + FILE_EXTENSION;
        this.file = new File(localeFolder, fileName);

        if (!this.reloadMessages()) return;

        plugin.getLogger().info("Loaded locale \"" + fileName + "\"");
    }

    /**
     * Initialize the class to load all existing language files and update them.
     * This must be called before any other methods in this class as otherwise
     * the methods will fail to invoke
     *
     * @param plugin        the plugin instance
     * @param defaultLocale the default language
     */
    public Locale(JavaPlugin plugin, String defaultLocale) {

        Locale.plugin = plugin;
        Locale.localeFolder = new File(plugin.getDataFolder(), "locales/");

        if (!localeFolder.exists()) localeFolder.mkdirs();

        //Save the default locale file.
        Locale.defaultLocale = defaultLocale;
        saveLocale(defaultLocale);

        for (File file : localeFolder.listFiles()) {
            String fileName = file.getName();
            if (!fileName.endsWith(FILE_EXTENSION)) continue;

            String name = fileName.substring(0, fileName.lastIndexOf('.'));

            if (name.split("_").length != 2) continue;
            if (localeLoaded(name)) continue;

            LOCALES.add(new Locale(name));
        }
    }

    /**
     * Save a locale file from the InputStream, to the locale folder
     *
     * @param fileName the name of the file to save
     * @return true if the operation was successful, false otherwise
     */
    public static boolean saveLocale(String fileName) {
        return saveLocale(plugin.getResource(defaultLocale + FILE_EXTENSION), fileName);
    }


    /**
     * Save a locale file from the InputStream, to the locale folder
     *
     * @param in       file to save
     * @param fileName the name of the file to save
     * @return true if the operation was successful, false otherwise
     */
    public static boolean saveLocale(InputStream in, String fileName) {
        if (!localeFolder.exists()) localeFolder.mkdirs();

        if (!fileName.endsWith(FILE_EXTENSION))
            fileName = (fileName.lastIndexOf(".") == -1 ? fileName : fileName.substring(0, fileName.lastIndexOf('.'))) + FILE_EXTENSION;

        File destinationFile = new File(localeFolder, fileName);
        if (destinationFile.exists())
            return compareFiles(in, destinationFile);

        try (OutputStream outputStream = new FileOutputStream(destinationFile)) {
            copy(in, outputStream);

            fileName = fileName.substring(0, fileName.lastIndexOf('.'));

            if (fileName.split("_").length != 2) return false;

            LOCALES.add(new Locale(fileName));
            if (defaultLocale == null) defaultLocale = fileName;
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Write new changes to existing files, if any at all
    private static boolean compareFiles(InputStream in, File existingFile) {
        InputStream defaultFile =
                in == null ? plugin.getResource((defaultLocale != null ? defaultLocale : "en_US") + FILE_EXTENSION) : in;

        boolean changed = false;

        List<String> defaultLines, existingLines;
        try (BufferedReader defaultReader = new BufferedReader(new InputStreamReader(defaultFile));
             BufferedReader existingReader = new BufferedReader(new FileReader(existingFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(existingFile, true))) {
            defaultLines = defaultReader.lines().collect(Collectors.toList());
            existingLines = existingReader.lines().map(s -> s.split("\\s*=")[0]).collect(Collectors.toList());

            for (String defaultValue : defaultLines) {
                if (defaultValue.isEmpty() || defaultValue.startsWith("#")) continue;

                String key = defaultValue.split("\\s*=")[0];

                if (!existingLines.contains(key)) {
                    if (!changed) {
                        writer.newLine();
                        writer.newLine();
                        // Leave a note alerting the user of the newly added messages.
                        writer.write("# New messages for " + plugin.getName() + " v" + plugin.getDescription().getVersion() + ".");

                        // If changes were found outside of the default file leave a note explaining that.
                        if (in == null) {
                            writer.newLine();
                            writer.write("# These translations were found untranslated, join");
                            writer.newLine();
                            writer.write("# our translation Discord https://discord.gg/f7fpZEf");
                            writer.newLine();
                            writer.write("# to request an official update!");
                        }
                    }

                    writer.newLine();
                    writer.write(defaultValue);

                    changed = true;
                }
            }
            if (in != null && !changed) compareFiles(null, existingFile);
        } catch (IOException e) {
            return false;
        }

        return changed;
    }


    /**
     * Check whether a locale exists and is registered or not
     *
     * @param name the whole language tag (i.e. "en_US")
     * @return true if it exists
     */
    public static boolean localeLoaded(String name) {
        for (Locale locale : LOCALES)
            if (locale.getName().equals(name)) return true;
        return false;
    }


    /**
     * Get a locale by its entire proper name (i.e. "en_US")
     *
     * @param name the full name of the locale
     * @return locale of the specified name
     */
    public static Locale getLocale(String name) {
        for (Locale locale : LOCALES)
            if (locale.getName().equalsIgnoreCase(name)) return locale;
        return null;
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

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            for (int lineNumber = 0; (line = reader.readLine()) != null; lineNumber++) {
                if (line.trim().isEmpty() || line.startsWith("#") /* Comment */) continue;

                Matcher matcher = NODE_PATTERN.matcher(line);
                if (!matcher.find()) {
                    System.err.println("Invalid locale syntax at (line=" + lineNumber + ")");
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
        return message.setPrefix(this.nodes.getOrDefault("general.nametag.prefix", "[Plugin]"));
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
