package com.songoda.core.settingsv2;

import com.google.common.base.Charsets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

/**
 * Configuration settings for a plugin
 *
 * @since 2019-08-28
 * @author jascotty2
 */
public class Config extends SongodaConfigurationSection {

    /*
    Serialization notes:
    // implements ConfigurationSerializable:
    //public Map<String, Object> serialize();
    
    // Class must contain one of:
    // public static Object deserialize(@NotNull Map<String, ?> args);
    // public static valueOf(Map<String, ?> args);
    // public new (Map<String, ?> args)
     */
    protected static final String COMMENT_PREFIX = "# ";
    protected static final String BLANK_CONFIG = "{}\n";

    final File file;
    final Plugin plugin;
    final DumperOptions yamlOptions = new DumperOptions();
    final Representer yamlRepresenter = new YamlRepresenter();
    final Yaml yaml = new Yaml(new YamlConstructor(), yamlRepresenter, yamlOptions);
    SaveTask saveTask;
    Timer autosaveTimer;
    /**
     * save file whenever a change is made
     */
    boolean autosave = false;
    /**
     * time in seconds to start a save after a change is made
     */
    int autosaveInterval = 60;
    /**
     * remove nodes not defined in defaults
     */
    boolean autoremove = false;

    public Config(@NotNull File file) {
        this.plugin = null;
        this.file = file.getAbsoluteFile();
    }

    public Config(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "config.yml");
    }

    public Config(@NotNull Plugin plugin, @NotNull String file) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), file);
    }

    public Config(@NotNull Plugin plugin, @NotNull String directory, @NotNull String file) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder() + directory, file);
    }

    public File getFile() {
        return file;
    }

    public boolean getAutosave() {
        return autosave;
    }

    /**
     * Should the configuration automatically save whenever it's been changed? <br>
     * All saves are done asynchronously, so this should not impact server performance.
     * 
     * @param autosave set to true if autosaving is enabled.
     * @return this class
     */
    public Config setAutosave(boolean autosave) {
        this.autosave = autosave;
        return this;
    }

    public int getAutosaveInterval() {
        return autosaveInterval;
    }

    /**
     * If autosave is enabled, this is the delay between a change and when the save is started. <br>
     * If the configuration is changed within this period, the timer is not reset.
     * 
     * @param autosaveInterval time in seconds
     */
    public void setAutosaveInterval(int autosaveInterval) {
        this.autosaveInterval = autosaveInterval;
    }

    public boolean getAutoremove() {
        return autoremove;
    }

    /**
     * This setting is used to prevent users to from adding extraneous settings
     * to the config and to remove deprecated settings. <br>
     * If this is enabled, the config will delete any nodes that are not
     * defined as a default setting.
     *
     * @param autoremove Remove settings that don't exist as defaults
     * @return this class
     */
    public Config setAutoremove(boolean autoremove) {
        this.autoremove = autoremove;
        return this;
    }

    @NotNull
    public Config setHeader(@NotNull String... description) {
        if (description.length == 0) {
            configComments.remove(null);
        } else {
            configComments.put(null, new Comment(description));
        }
        return this;
    }

    @NotNull
    public Config setHeader(@Nullable List<String> description) {
        if (description == null || description.isEmpty()) {
            configComments.remove(null);
        } else {
            configComments.put(null, new Comment(description));
        }
        return this;
    }

    @NotNull
    public List<String> getHeader() {
        if (configComments.containsKey(null)) {
            return configComments.get(null).getLines();
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public void load() throws FileNotFoundException, IOException, InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null");
        FileInputStream stream = new FileInputStream(file);
        this.load(new InputStreamReader((InputStream) stream, Charsets.UTF_16));
    }

    public void load(@NotNull File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null");
        FileInputStream stream = new FileInputStream(file);
        this.load(new InputStreamReader((InputStream) stream, Charsets.UTF_8));
    }

    public void load(@NotNull Reader reader) throws IOException, InvalidConfigurationException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader input = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader)) {
            String line;
            while ((line = input.readLine()) != null) {
                builder.append(line).append('\n');
            }
        }
        this.loadFromString(builder.toString());
    }

    public void loadFromString(@NotNull String contents) throws InvalidConfigurationException {
        Map input;
        try {
            input = (Map) this.yaml.load(contents);
        } catch (YAMLException e2) {
            throw new InvalidConfigurationException(e2);
        } catch (ClassCastException e3) {
            throw new InvalidConfigurationException("Top level is not a Map.");
        }
        if (input != null) {
            this.parseComments(contents, input);
            this.convertMapsToSections(input, this);
        }
    }

    protected void convertMapsToSections(@NotNull Map<?, ?> input, @NotNull SongodaConfigurationSection section) {
        for (Map.Entry<?, ?> entry : input.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            if (value instanceof Map) {
                this.convertMapsToSections((Map) value, section.createSection(key));
                continue;
            }
            section.set(key, value);
        }
    }

    protected void parseComments(@NotNull String contents, @NotNull Map<?, ?> input) {
        // TODO
        // if starts with a comment, load all nonbreaking comments as a header
        // then load all comments and assign to the next valid node loaded
    }

    public void deleteNonDefaultSettings() {
        // Delete old config values (thread-safe)
        List<String> defaultKeys = Arrays.asList((String[]) defaults.keySet().toArray());
        for(String key : (String[]) values.keySet().toArray()) {
            if(!defaultKeys.contains(key)) {
                values.remove(key);
            }
        }
    }

    @Override
    protected void onChange() {
        if (autosave) {
            delaySave();
        }
    }

    public void delaySave() {
        // save async even if no plugin or if plugin disabled
        if (changed && saveTask == null) {
            autosaveTimer = new Timer((plugin != null ? plugin.getName() + "-ConfigSave-" : "ConfigSave-") + file.getName());
            autosaveTimer.schedule(saveTask = new SaveTask(), autosaveInterval * 1000L);
        }
    }

    public boolean saveChanges() {
        boolean saved = true;
        if (changed) {
            saved = save();
        }
        if(saveTask != null) {
            //Close Threads
            saveTask.cancel();
            autosaveTimer.cancel();
            saveTask = null;
            autosaveTimer = null;
        }
        return saved;
    }

    public boolean save() {
        return save(file);
    }

    public boolean save(@NotNull String file) {
        Validate.notNull(file, "File cannot be null");
        return this.save(new File(file));
    }

    public boolean save(@NotNull File file) {
        Validate.notNull(file, "File cannot be null");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        String data = this.saveToString();
        try (OutputStreamWriter writer = new OutputStreamWriter((OutputStream) new FileOutputStream(file), Charsets.UTF_16);) {
            writer.write(data);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @NotNull
    public String saveToString() {
        try {
            if(autoremove) {
                deleteNonDefaultSettings();
            }
            yamlOptions.setIndent(indentation);
            yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            StringWriter str = new StringWriter();
            Comment header = configComments.get(null);
            if (header != null) {
                header.writeComment(str, 0, ConfigFormattingRules.CommentStyle.SPACED);
            }
            String dump = yaml.dump(this.getValues(false));
            if (dump.equals(BLANK_CONFIG)) {
                dump = "";
            } else {
                // line-by-line apply line spacing formatting and comments per-node
            }
            return str.toString() + dump;
        } catch (Throwable ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, "Error saving config", ex);
            delaySave();
        }
        return "";
    }

    class SaveTask extends TimerTask {

        @Override
        public void run() {
            saveChanges();
        }
    }
}
