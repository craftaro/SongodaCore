package com.craftaro.core.configuration;

import com.craftaro.core.CraftaroCore;
import com.craftaro.core.CraftaroPlugin;
import org.simpleyaml.configuration.file.YamlFile;
import ru.vyarus.yaml.updater.YamlUpdater;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Config extends YamlFile {

    public static final List<String> BUILT_IN_CONFIGS = Arrays.asList("database.yml", "hooks.yml");
    /**
     * Loads a config from the given file
     *
     * @param file The file to load
     */
    public Config(File file) {
        super(file);
        try {
            super.loadWithComments();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Loads a config from the given file
     * and creates the file if it doesn't exist
     * also updates the config if needed
     * (Make the same path in the 'resources' folder)
     *
     * @param plugin The plugin to load the config for
     * @param file   The file to load the config from starting from the plugin's data folder
     */
    public Config(CraftaroPlugin plugin, File file) {
        super(new File(plugin.getDataFolder(), file.toString().replace("plugins" + File.separator + plugin.getName() + File.separator, "")));
        String path = file.toString().replace("plugins" + File.separator + plugin.getName() + File.separator, "");
        if (!super.exists()) {
            try {
                super.createNewFile();
                final String resource = "/" + path;
                if (BUILT_IN_CONFIGS.contains(file.getName())) {
                    YamlUpdater.create(super.getConfigurationFile(), Objects.requireNonNull(CraftaroCore.getInstance().getClass().getResourceAsStream(resource))).update();
                } else {
                    YamlUpdater.create(super.getConfigurationFile(), Objects.requireNonNull(plugin.getClass().getResourceAsStream(resource))).update();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            super.loadWithComments();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
