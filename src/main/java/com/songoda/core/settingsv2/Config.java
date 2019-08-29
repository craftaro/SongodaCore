package com.songoda.core.settingsv2;

import com.songoda.core.settingsv2.adapters.ConfigOptionsAdapter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
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
    final DumperOptions yamlOptions = new DumperOptions();
    final Representer yamlRepresenter = new YamlRepresenter();
    final Yaml yaml = new Yaml(new YamlConstructor(), yamlRepresenter, yamlOptions);
    protected int indentation = 2; // between 2 and 9 (inclusive)
    protected char pathChar = '.';

    public Config(@NotNull File file) {
        super(null, null);
        this.file = file;
    }

    public Config(@NotNull Plugin plugin, @NotNull String file) {
        super(null, null);
        this.file = new File(plugin.getDataFolder(), file);
    }

    public Config(@NotNull Plugin plugin, @NotNull String directory, @NotNull String file) {
        super(null, null);
        this.file = new File(plugin.getDataFolder() + directory, file);
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

    @NotNull
    public String saveToString() {
        try {
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
            }
            return str.toString() + dump;
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public int getIndent() {
        return indentation;
    }

    public void setIndent(int indentation) {
        this.indentation = indentation;
    }

    public char getPathSeparator() {
        return pathChar;
    }

    public void setPathSeparator(char pathChar) {
        this.pathChar = pathChar;
    }

    @Override
    public ConfigOptionsAdapter options() {
        return new ConfigOptionsAdapter(this);
    }
    
}
