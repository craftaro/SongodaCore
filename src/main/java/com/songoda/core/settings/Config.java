package com.songoda.core.settings;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Config {

    private final Plugin plugin;
    private final String folderName, fileName;

    private FileConfiguration fileConfiguration;
    private File configFile;

    private boolean allowUserExpansion = true, categorySpacing = true,
            commentSpacing = true, showNullCategoryComments = true;

    private final Map<String, Category> categories = new LinkedHashMap<>();

    public Config(JavaPlugin plugin, String folderName, String fileName) {
        this.plugin = plugin;
        this.folderName = folderName;
        this.fileName = fileName;
        this.reload();
    }

    public Config(JavaPlugin plugin, String fileName) {
        this(plugin, "", fileName);
    }

    /**
     * This allows users to expand the config and create new lines as well as
     * remove older lines. If this is disabled the config will regenerate
     * removed lines as well as add new lines that are added in the future
     *
     * @param allowUserExpansion allow users to expand config, otherwise don't
     * @return this class
     */
    public Config allowUserExpansion(boolean allowUserExpansion) {
        this.allowUserExpansion = allowUserExpansion;
        return this;
    }

    /**
     * This will add two spaces above each category
     *
     * @param categorySpacing add two spaces above each category, otherwise don't
     * @return this class
     */
    public Config categorySpacing(boolean categorySpacing) {
        this.categorySpacing = categorySpacing;
        return this;
    }

    /**
     * This will add a single space above each commented setting. Useful when
     * you don't want your comments to stand out
     *
     * @param commentSpacing add a space above each comment, otherwise don't
     * @return this class
     */
    public Config commentSpacing(boolean commentSpacing) {
        this.commentSpacing = commentSpacing;
        return this;
    }

    /**
     * This will add a single space above each commented setting. Useful when
     * you don't want your comments to stand out
     *
     * @param showNullCategoryComments shows a placeholder comment when null,
     *                                 otherwise doesn't
     * @return this class
     */
    public Config showNullCategoryComments(boolean showNullCategoryComments) {
        this.showNullCategoryComments = showNullCategoryComments;
        return this;
    }

    public Category addCategory(String key, String... comments) {
        return addCategory(new Category(this, key, comments));
    }

    public Category addCategory(Category category) {
        if (categories.containsKey(category.getKey()))
            return categories.get(category.getKey()).addAll(category);
        else {
            categories.put(category.getKey(), category);
            return category;
        }
    }

    public Category getCategory(String key) {
        for (String string : categories.keySet())
            if (string.equalsIgnoreCase(key))
                return categories.get(string);
        return null;
    }

    public List<Category> getCategories() {
        return new ArrayList<>(categories.values());
    }

    public boolean hasCategory(String key) {
        return getCategory(key) != null;
    }

    public Setting getSetting(String key) {
        return getSetting(key, false);
    }

    public Setting getDefaultSetting(String key) {
        return getSetting(key, true);
    }

    private Setting getSetting(String key, boolean isDefault) {
        String[] split = key.split("\\.", 2);
        if (split.length != 2) return null;
        Category category = getCategory(split[0]);
        if (category == null) return null;
        if (isDefault)
            return category.getDefaultSetting(split[1]);
        else
            return category.getSetting(split[1]);
    }

    public List<FoundSetting> getSettings() {
        return getSettings(false);
    }

    public List<FoundSetting> getDefaultSettings() {
        return getSettings(true);
    }

    private List<FoundSetting> getSettings(boolean isDefault) {
        List<FoundSetting> settings = new ArrayList<>();
        for (Category category : categories.values()) {
            if (isDefault)
                settings.addAll(category.getDefaultSettings());
            else
                settings.addAll(category.getSettings());
        }
        return settings;
    }

    private void loadExisting() {
        this.categories.clear();
        for (String categoryStr : fileConfiguration.getKeys(false)) {
            Category category = new Category(this, categoryStr);
            for (String settingStr : fileConfiguration.getConfigurationSection(categoryStr).getKeys(true)) {
                Object object = fileConfiguration.get(categoryStr + "." + settingStr);
                if (!(object instanceof MemorySection))
                    category.addSetting(settingStr, object);
            }
            addCategory(category);
        }
    }

    public void reload() {
        if (this.configFile == null)
            this.configFile = new File(plugin.getDataFolder() + folderName, fileName);

        this.fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        if (allowUserExpansion)
            this.loadExisting();
    }

    public void setup() {
        if (fileConfiguration.getKeys(false).size() == 0 || !allowUserExpansion) {
            for (FoundSetting setting : getDefaultSettings()) {
                fileConfiguration.addDefault(setting.getCompleteKey(), setting.getDefaultValue());
                setting.getCategory().addSetting(setting);
            }
            fileConfiguration.options().copyDefaults(true);
        }
        this.save();
    }

    public void save() {

        // Delete old config values.
        if (!allowUserExpansion) {
            for (String line : fileConfiguration.getKeys(true)) {
                if (line.contains(".") && getDefaultSetting(line) == null)
                    fileConfiguration.set(line, null);
                else if (!line.contains(".")) {
                    if (((MemorySection) fileConfiguration.get(line)).getKeys(true).size() == 0)
                        fileConfiguration.set(line, null);
                }
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
                            if (categorySpacing)
                                config.append("\n\n");
                        } else {
                            first = false;
                        }

                        if (!category.contains("."))
                            config.append("#").append("\n");
                        try {
                            String categoryStr = category;
                            String commentKey = null;
                            if (category.contains(".")) {
                                String[] split = category.split("\\.", 2);
                                commentKey = split[1];
                                categoryStr = split[0];
                            }

                            Category categoryObj = getCategory(categoryStr);
                            if (categoryObj.getComments(commentKey).size() == 0)
                                throw new NullPointerException();
                            for (String l : categoryObj.getComments(commentKey)) {
                                config.append(new String(new char[tabChange]).replace('\0', ' '));
                                config.append("# ").append(l).append("\n");
                            }
                        } catch (IllegalArgumentException | NullPointerException e) {
                            if (showNullCategoryComments) {
                                config.append(new String(new char[tabChange]).replace('\0', ' '));
                                config.append("# ").append(category).append("\n");
                            }
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
                for (FoundSetting setting : getSettings()) {
                    if (!setting.getCompleteKey().equals(key) || setting.getComments().length == 0) continue;
                    if (commentSpacing)
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
            BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
            writer.write(config.toString());
            writer.flush();
            writer.close();

            fileConfiguration.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public String getConfigName() {
        return fileName;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
