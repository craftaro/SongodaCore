package com.songoda.core.configuration;

import com.songoda.core.SongodaPlugin;
import org.simpleyaml.configuration.file.YamlFile;
import ru.vyarus.yaml.updater.YamlUpdater;

import java.io.File;
import java.util.Objects;

public class Config extends YamlFile {

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
    public Config(SongodaPlugin plugin, File file) {
        super(new File(plugin.getDataFolder(), file.toString()));
        if (!super.exists()) {
            try {
                super.createNewFile();
                YamlUpdater.create(super.getConfigurationFile(), Objects.requireNonNull(plugin.getClass().getResourceAsStream(file.toString().startsWith("/") ? file.toString() : "/"+file))).update();
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
