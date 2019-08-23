package com.songoda.core.library.settings;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Config {

    private final JavaPlugin plugin;

    private final String folderName, fileName;
    private FileConfiguration fileConfiguration;
    private File configFile;

    private final Map<String, Category> categories = new HashMap<>();

    public Config(JavaPlugin plugin, String folderName, String fileName) {
        this.plugin = plugin;
        this.folderName = folderName;
        this.fileName = fileName;
    }

    public Category addCategory(String key, String... comments) {
        return categories.put(key, new Category(this, key, comments));
    }

    public Category getCategory(String key) {
        for (String string : categories.keySet())
            if (string.equalsIgnoreCase(key))
                return categories.get(string);
        return null;
    }

    public Setting getSetting(String key) {
        String[] split = key.split(".", 2);
        Category category = getCategory(split[0]);
        return category.getSetting(split[1]);
    }

    public Set<Setting> getSettings() {
        Set<Setting> settings = new HashSet<>();
        for (Category category : categories.values()) {
            settings.addAll(category.getSettings());
        }
        return settings;
    }

    public void reload() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder() + folderName, fileName);
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
        this.setup();
    }

    public void setup() {
        FileConfiguration config = plugin.getConfig();

        for (Setting setting : getSettings()) {
            config.addDefault(setting.getCompleteKey(), setting.getDefaultValue());
        }
        plugin.getConfig().options().copyDefaults(true);
        save();
    }

    void save() {
        // Delete old config values.
        for (String line : fileConfiguration.getConfigurationSection("").getKeys(true)) {
            if (line.contains(".") && getSetting(line) == null)
                fileConfiguration.set(line, null);
            else if (!line.contains(".")) {
                if (((MemorySection) fileConfiguration.get(line)).getKeys(true).size() == 0)
                    fileConfiguration.set(line, null);
            }
        }

        // Add comments.
        String dump = fileConfiguration.saveToString();
        StringBuilder config = new StringBuilder();
        BufferedReader bufReader = new BufferedReader(new StringReader(dump));

        try {
            boolean first = true;

            String line;
            int currentTab = 0;
            String category = "";

            while ((line = bufReader.readLine()) != null) {
                if (line.trim().startsWith("#")) continue;

                int tabChange = line.length() - line.trim().length();
                if (currentTab != tabChange) {
                    category = category.contains(".") && tabChange != 0 ? category.substring(0, category.indexOf(".")) : "";
                    currentTab = tabChange;
                }

                if (line.endsWith(":")) {
                    bufReader.mark(1000);
                    String found = bufReader.readLine();
                    bufReader.reset();

                    if (!found.trim().startsWith("-")) {

                        String newCategory = line.substring(0, line.length() - 1).trim();

                        if (category.equals(""))
                            category = newCategory;
                        else
                            category += "." + newCategory;

                        currentTab = tabChange + 2;

                        if (!first) {
                            config.append("\n\n");
                        } else {
                            first = false;
                        }

                        if (!category.contains("."))
                            config.append("#").append("\n");
                        try {
                            Category categoryObj = getCategory(category);

                            config.append(new String(new char[tabChange]).replace('\0', ' '));
                            for (String l : categoryObj.getComments())
                                config.append("# ").append(l).append("\n");
                        } catch (IllegalArgumentException e) {
                            config.append("# ").append(category).append("\n");
                        }
                        if (!category.contains("."))
                            config.append("#").append("\n");

                        config.append(line).append("\n");

                        continue;
                    }
                }

                if (line.trim().startsWith("-")) {
                    config.append(line).append("\n");
                    continue;
                }

                String key = category + "." + (line.split(":")[0].trim());
                for (Setting setting : getSettings()) {
                    if (!setting.getCompleteKey().equals(key) || setting.getComments() == null) continue;
                    config.append("  ").append("\n");
                    for (String l : setting.getComments()) {
                        config.append(new String(new char[currentTab]).replace('\0', ' '));
                        config.append("# ").append(l).append("\n");
                    }
                }
                config.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (!plugin.getDataFolder().exists())
                plugin.getDataFolder().mkdir();
            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(new File(plugin.getDataFolder() + File.separator + "config.yml")));
            writer.write(config.toString());
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
