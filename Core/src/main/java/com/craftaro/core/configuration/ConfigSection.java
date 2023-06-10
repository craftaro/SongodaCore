package com.craftaro.core.configuration;

import com.craftaro.core.compatibility.CompatibleMaterial;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Configuration for a specific node
 */
public class ConfigSection extends MemoryConfiguration {
    final String fullPath, nodeKey;
    final ConfigSection root;
    final ConfigSection parent;
    protected int indentation = 2; // between 2 and 9 (inclusive)
    protected char pathChar = '.';
    final HashMap<String, Comment> configComments;
    final HashMap<String, Comment> defaultComments;
    final LinkedHashMap<String, Object> defaults;
    final LinkedHashMap<String, Object> values;
    /**
     * Internal root state: if any configuration value has changed from file state
     */
    boolean changed = false;
    final boolean isDefault;
    final Object lock = new Object();

    ConfigSection() {
        this.root = this;
        this.parent = null;
        isDefault = false;
        nodeKey = fullPath = "";

        configComments = new HashMap<>();
        defaultComments = new HashMap<>();
        defaults = new LinkedHashMap<>();
        values = new LinkedHashMap<>();
    }

    ConfigSection(ConfigSection root, ConfigSection parent, String nodeKey, boolean isDefault) {
        this.root = root;
        this.parent = parent;
        this.nodeKey = nodeKey;
        this.fullPath = nodeKey != null ? parent.fullPath + nodeKey + root.pathChar : parent.fullPath;
        this.isDefault = isDefault;
        configComments = defaultComments = null;
        defaults = null;
        values = null;
    }

    public int getIndent() {
        return root.indentation;
    }

    public void setIndent(int indentation) {
        root.indentation = indentation;
    }

    protected void onChange() {
        if (parent != null) {
            root.onChange();
        }
    }

    /**
     * Sets the character used to separate configuration nodes. <br>
     * IMPORTANT: Do not change this after loading or adding ConfigurationSections!
     *
     * @param pathChar character to use
     */
    public void setPathSeparator(char pathChar) {
        if (!root.values.isEmpty() || !root.defaults.isEmpty()) {
            throw new RuntimeException("Path change after config initialization");
        }

        root.pathChar = pathChar;
    }

    public char getPathSeparator() {
        return root.pathChar;
    }

    /**
     * @return The full key for this section node
     */
    public String getKey() {
        return !fullPath.endsWith(String.valueOf(root.pathChar)) ? fullPath : fullPath.substring(0, fullPath.length() - 1);
    }

    /**
     * @return The specific key that was used from the last node to get to this node
     */
    public String getNodeKey() {
        return nodeKey;
    }

    /**
     * Create the path required for this node to exist. <br />
     * <b>DO NOT USE THIS IN A SYNCHRONIZED LOCK</b>
     *
     * @param path       full path of the node required. Eg, for foo.bar.node, this will create sections for foo and foo.bar
     * @param useDefault set to true if this is a default value
     */
    protected void createNodePath(@NotNull String path, boolean useDefault) {
        if (path.indexOf(root.pathChar) != -1) {
            // if any intermediate nodes don't exist, create them
            String[] pathParts = path.split(Pattern.quote(String.valueOf(root.pathChar)));
            StringBuilder nodePath = new StringBuilder(fullPath);
            LinkedHashMap<String, Object> writeTo = useDefault ? root.defaults : root.values;
            ConfigSection travelNode = this;

            synchronized (root.lock) {
                for (int i = 0; i < pathParts.length - 1; ++i) {
                    final String node = (i != 0 ? nodePath.append(root.pathChar) : nodePath).append(pathParts[i]).toString();

                    if (!(writeTo.get(node) instanceof ConfigSection)) {
                        writeTo.put(node, travelNode = new ConfigSection(root, travelNode, pathParts[i], useDefault));
                    } else {
                        travelNode = (ConfigSection) writeTo.get(node);
                    }
                }
            }
        }
    }

    @NotNull
    public ConfigSection createDefaultSection(@NotNull String path) {
        createNodePath(path, true);
        ConfigSection section = new ConfigSection(root, this, path, true);

        synchronized (root.lock) {
            root.defaults.put(fullPath + path, section);
        }

        return section;
    }

    @NotNull
    public ConfigSection createDefaultSection(@NotNull String path, String... comment) {
        createNodePath(path, true);
        ConfigSection section = new ConfigSection(root, this, path, true);

        synchronized (root.lock) {
            root.defaults.put(fullPath + path, section);
            root.defaultComments.put(fullPath + path, new Comment(comment));
        }

        return section;
    }

    @NotNull
    public ConfigSection createDefaultSection(@NotNull String path, ConfigFormattingRules.CommentStyle commentStyle, String... comment) {
        createNodePath(path, true);
        ConfigSection section = new ConfigSection(root, this, path, true);

        synchronized (root.lock) {
            root.defaults.put(fullPath + path, section);
            root.defaultComments.put(fullPath + path, new Comment(commentStyle, comment));
        }

        return section;
    }

    @NotNull
    public ConfigSection setComment(@NotNull String path, @Nullable ConfigFormattingRules.CommentStyle commentStyle, String... lines) {
        return setComment(path, lines != null ? new Comment(commentStyle, lines) : null);
    }

    @NotNull
    public ConfigSection setComment(@NotNull String path, @Nullable ConfigFormattingRules.CommentStyle commentStyle, @Nullable List<String> lines) {
        return setComment(path, lines != null ? new Comment(commentStyle, lines) : null);
    }

    @NotNull
    public ConfigSection setComment(@NotNull String path, @Nullable Comment comment) {
        synchronized (root.lock) {
            if (isDefault) {
                root.defaultComments.put(fullPath + path, comment);
            } else {
                root.configComments.put(fullPath + path, comment);
            }
        }

        return this;
    }

    @NotNull
    public ConfigSection setDefaultComment(@NotNull String path, String... lines) {
        return setDefaultComment(path, lines.length == 0 ? null : Arrays.asList(lines));
    }

    @NotNull
    public ConfigSection setDefaultComment(@NotNull String path, @Nullable List<String> lines) {
        synchronized (root.lock) {
            root.defaultComments.put(fullPath + path, new Comment(lines));
        }

        return this;
    }

    @NotNull
    public ConfigSection setDefaultComment(@NotNull String path, ConfigFormattingRules.CommentStyle commentStyle, String... lines) {
        return setDefaultComment(path, commentStyle, lines.length == 0 ? null : Arrays.asList(lines));
    }

    @NotNull
    public ConfigSection setDefaultComment(@NotNull String path, ConfigFormattingRules.CommentStyle commentStyle, @Nullable List<String> lines) {
        synchronized (root.lock) {
            root.defaultComments.put(fullPath + path, new Comment(commentStyle, lines));
        }

        return this;
    }

    @NotNull
    public ConfigSection setDefaultComment(@NotNull String path, @Nullable Comment comment) {
        synchronized (root.lock) {
            root.defaultComments.put(fullPath + path, comment);
        }

        return this;
    }

    @Nullable
    public Comment getComment(@NotNull String path) {
        Comment result = root.configComments.get(fullPath + path);

        if (result == null) {
            result = root.defaultComments.get(fullPath + path);
        }

        return result;
    }

    @Nullable
    public String getCommentString(@NotNull String path) {
        Comment result = root.configComments.get(fullPath + path);

        if (result == null) {
            result = root.defaultComments.get(fullPath + path);
        }

        return result != null ? result.toString() : null;
    }

    @Override
    public void addDefault(@NotNull String path, @Nullable Object value) {
        createNodePath(path, true);

        synchronized (root.lock) {
            root.defaults.put(fullPath + path, value);
        }
    }

    @Override
    public void addDefaults(@NotNull Map<String, Object> defaults) {
        //defaults.entrySet().stream().forEach(m -> root.defaults.put(fullPath + m.getKey(), m.getValue()));
        defaults.entrySet().forEach(m -> addDefault(m.getKey(), m.getValue()));
    }

    @Override
    public void setDefaults(Configuration c) {
        if (fullPath.isEmpty()) {
            root.defaults.clear();
        } else {
            root.defaults.keySet().stream()
                    .filter(k -> k.startsWith(fullPath))
                    .forEach(root.defaults::remove);
        }

        addDefaults(c);
    }

    @Override
    public ConfigSection getDefaults() {
        return new ConfigSection(root, this, null, true);
    }

    @Override
    public ConfigSection getDefaultSection() {
        return new ConfigSection(root, this, null, true);
    }

    @Override
    public ConfigOptionsAdapter options() {
        return new ConfigOptionsAdapter(root);
    }

    @NotNull
    @Override
    public Set<String> getKeys(boolean deep) {
        LinkedHashSet<String> result = new LinkedHashSet<>();
        int pathIndex = fullPath.lastIndexOf(root.pathChar);

        if (deep) {
            result.addAll(root.defaults.keySet().stream()
                    .filter(k -> k.startsWith(fullPath))
                    .map(k -> !k.endsWith(String.valueOf(root.pathChar)) ? k.substring(pathIndex + 1) : k.substring(pathIndex + 1, k.length() - 1))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
            result.addAll(root.values.keySet().stream()
                    .filter(k -> k.startsWith(fullPath))
                    .map(k -> !k.endsWith(String.valueOf(root.pathChar)) ? k.substring(pathIndex + 1) : k.substring(pathIndex + 1, k.length() - 1))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        } else {
            result.addAll(root.defaults.keySet().stream()
                    .filter(k -> k.startsWith(fullPath) && k.lastIndexOf(root.pathChar) == pathIndex)
                    .map(k -> !k.endsWith(String.valueOf(root.pathChar)) ? k.substring(pathIndex + 1) : k.substring(pathIndex + 1, k.length() - 1))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
            result.addAll(root.values.keySet().stream()
                    .filter(k -> k.startsWith(fullPath) && k.lastIndexOf(root.pathChar) == pathIndex)
                    .map(k -> !k.endsWith(String.valueOf(root.pathChar)) ? k.substring(pathIndex + 1) : k.substring(pathIndex + 1, k.length() - 1))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        }

        return result;
    }

    @NotNull
    @Override
    public Map<String, Object> getValues(boolean deep) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        int pathIndex = fullPath.lastIndexOf(root.pathChar);

        if (deep) {
            result.putAll((Map<String, Object>) root.defaults.entrySet().stream()
                    .filter(k -> k.getKey().startsWith(fullPath))
                    .collect(Collectors.toMap(
                            e -> !e.getKey().endsWith(String.valueOf(root.pathChar)) ? e.getKey().substring(pathIndex + 1) : e.getKey().substring(pathIndex + 1, e.getKey().length() - 1),
                            Map.Entry::getValue,
                            (v1, v2) -> {
                                throw new IllegalStateException();
                            }, // never going to be merging keys
                            LinkedHashMap::new)));

            result.putAll((Map<String, Object>) root.values.entrySet().stream()
                    .filter(k -> k.getKey().startsWith(fullPath))
                    .collect(Collectors.toMap(
                            e -> !e.getKey().endsWith(String.valueOf(root.pathChar)) ? e.getKey().substring(pathIndex + 1) : e.getKey().substring(pathIndex + 1, e.getKey().length() - 1),
                            Map.Entry::getValue,
                            (v1, v2) -> {
                                throw new IllegalStateException();
                            }, // never going to be merging keys
                            LinkedHashMap::new)));
        } else {
            result.putAll((Map<String, Object>) root.defaults.entrySet().stream()
                    .filter(k -> k.getKey().startsWith(fullPath) && k.getKey().lastIndexOf(root.pathChar) == pathIndex)
                    .collect(Collectors.toMap(
                            e -> !e.getKey().endsWith(String.valueOf(root.pathChar)) ? e.getKey().substring(pathIndex + 1) : e.getKey().substring(pathIndex + 1, e.getKey().length() - 1),
                            Map.Entry::getValue,
                            (v1, v2) -> {
                                throw new IllegalStateException();
                            }, // never going to be merging keys
                            LinkedHashMap::new)));

            result.putAll((Map<String, Object>) root.values.entrySet().stream()
                    .filter(k -> k.getKey().startsWith(fullPath) && k.getKey().lastIndexOf(root.pathChar) == pathIndex)
                    .collect(Collectors.toMap(
                            e -> !e.getKey().endsWith(String.valueOf(root.pathChar)) ? e.getKey().substring(pathIndex + 1) : e.getKey().substring(pathIndex + 1, e.getKey().length() - 1),
                            Map.Entry::getValue,
                            (v1, v2) -> {
                                throw new IllegalStateException();
                            }, // never going to be merging keys
                            LinkedHashMap::new)));
        }

        return result;
    }

    @NotNull
    public List<ConfigSection> getSections(String path) {
        ConfigSection rootSection = getConfigurationSection(path);

        if (rootSection == null) {
            return Collections.emptyList();
        }

        ArrayList<ConfigSection> result = new ArrayList<>();
        rootSection.getKeys(false).stream()
                .map(rootSection::get)
                .filter(ConfigSection.class::isInstance)
                .forEachOrdered(object -> result.add((ConfigSection) object));

        return result;
    }

    @Override
    public boolean contains(@NotNull String path) {
        return root.defaults.containsKey(fullPath + path) || root.values.containsKey(fullPath + path);
    }

    @Override
    public boolean contains(@NotNull String path, boolean ignoreDefault) {
        return (!ignoreDefault && root.defaults.containsKey(fullPath + path)) || root.values.containsKey(fullPath + path);
    }

    @Override
    public boolean isSet(@NotNull String path) {
        return root.defaults.get(fullPath + path) != null || root.values.get(fullPath + path) != null;
    }

    @Override
    public String getCurrentPath() {
        return fullPath.isEmpty() ? "" : fullPath.substring(0, fullPath.length() - 1);
    }

    @Override
    public String getName() {
        if (fullPath.isEmpty()) {
            return "";
        }

        String[] parts = fullPath.split(Pattern.quote(String.valueOf(root.pathChar)));
        return parts[parts.length - 1];
    }

    @Override
    public ConfigSection getRoot() {
        return root;
    }

    @Override
    public ConfigSection getParent() {
        return parent;
    }

    @Nullable
    @Override
    public Object get(@NotNull String path) {
        Object result = root.values.get(fullPath + path);

        if (result == null) {
            result = root.defaults.get(fullPath + path);
        }

        return result;
    }

    @Nullable
    @Override
    public Object get(@NotNull String path, @Nullable Object def) {
        Object result = root.values.get(fullPath + path);

        return result != null ? result : def;
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        if (isDefault) {
            addDefault(path, value);
            return;
        }

        createNodePath(path, false);
        Object last;
        synchronized (root.lock) {
            if (value != null) {
                root.changed |= (last = root.values.put(fullPath + path, value)) != value;
            } else {
                root.changed |= (last = root.values.remove(fullPath + path)) != null;
            }
        }

        if (last != value && last instanceof ConfigSection) {
            // clean up orphaned nodes
            final String trim = fullPath + path + root.pathChar;
            synchronized (root.lock) {
                root.values.keySet().stream()
                        .filter(k -> k.startsWith(trim))
                        .collect(Collectors.toSet())
                        .forEach(root.values::remove);
            }
        }

        onChange();
    }

    @NotNull
    public ConfigSection set(@NotNull String path, @Nullable Object value, String... comment) {
        set(path, value);
        return setComment(path, null, comment);
    }

    @NotNull
    public ConfigSection set(@NotNull String path, @Nullable Object value, List<String> comment) {
        set(path, value);
        return setComment(path, null, comment);
    }

    @NotNull
    public ConfigSection set(@NotNull String path, @Nullable Object value, @Nullable ConfigFormattingRules.CommentStyle commentStyle, String... comment) {
        set(path, value);
        return setComment(path, commentStyle, comment);
    }

    @NotNull
    public ConfigSection set(@NotNull String path, @Nullable Object value, @Nullable ConfigFormattingRules.CommentStyle commentStyle, List<String> comment) {
        set(path, value);
        return setComment(path, commentStyle, comment);
    }

    @NotNull
    public ConfigSection setDefault(@NotNull String path, @Nullable Object value) {
        addDefault(path, value);
        return this;
    }

    @NotNull
    public ConfigSection setDefault(@NotNull String path, @Nullable Object value, String... comment) {
        addDefault(path, value);
        return setDefaultComment(path, comment);
    }

    @NotNull
    public ConfigSection setDefault(@NotNull String path, @Nullable Object value, List<String> comment) {
        addDefault(path, value);
        return setDefaultComment(path, comment);
    }

    @NotNull
    public ConfigSection setDefault(@NotNull String path, @Nullable Object value, ConfigFormattingRules.CommentStyle commentStyle, String... comment) {
        addDefault(path, value);
        return setDefaultComment(path, commentStyle, comment);
    }

    @NotNull
    public ConfigSection setDefault(@NotNull String path, @Nullable Object value, ConfigFormattingRules.CommentStyle commentStyle, List<String> comment) {
        addDefault(path, value);
        return setDefaultComment(path, commentStyle, comment);
    }

    @NotNull
    @Override
    public ConfigSection createSection(@NotNull String path) {
        createNodePath(path, false);
        ConfigSection section = new ConfigSection(root, this, path, false);

        synchronized (root.lock) {
            root.values.put(fullPath + path, section);
        }

        root.changed = true;
        onChange();

        return section;
    }

    @NotNull
    public ConfigSection createSection(@NotNull String path, String... comment) {
        return createSection(path, null, comment.length == 0 ? null : Arrays.asList(comment));
    }

    @NotNull
    public ConfigSection createSection(@NotNull String path, @Nullable List<String> comment) {
        return createSection(path, null, comment);
    }

    @NotNull
    public ConfigSection createSection(@NotNull String path, @Nullable ConfigFormattingRules.CommentStyle commentStyle, String... comment) {
        return createSection(path, commentStyle, comment.length == 0 ? null : Arrays.asList(comment));
    }

    @NotNull
    public ConfigSection createSection(@NotNull String path, @Nullable ConfigFormattingRules.CommentStyle commentStyle, @Nullable List<String> comment) {
        createNodePath(path, false);
        ConfigSection section = new ConfigSection(root, this, path, false);

        synchronized (root.lock) {
            root.values.put(fullPath + path, section);
        }

        setComment(path, commentStyle, comment);
        root.changed = true;
        onChange();

        return section;
    }

    @NotNull
    @Override
    public ConfigSection createSection(@NotNull String path, Map<?, ?> map) {
        createNodePath(path, false);
        ConfigSection section = new ConfigSection(root, this, path, false);

        synchronized (root.lock) {
            root.values.put(fullPath + path, section);
        }

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map) {
                section.createSection(entry.getKey().toString(), (Map<?, ?>) entry.getValue());
                continue;
            }

            section.set(entry.getKey().toString(), entry.getValue());
        }

        root.changed = true;
        onChange();

        return section;
    }

    @Nullable
    @Override
    public String getString(@NotNull String path) {
        Object result = get(path);

        return result != null ? result.toString() : null;
    }

    @Nullable
    @Override
    public String getString(@NotNull String path, @Nullable String def) {
        Object result = get(path);

        return result != null ? result.toString() : def;
    }

    public char getChar(@NotNull String path) {
        Object result = get(path);

        return result != null && !result.toString().isEmpty() ? result.toString().charAt(0) : '\0';
    }

    public char getChar(@NotNull String path, char def) {
        Object result = get(path);

        return result != null && !result.toString().isEmpty() ? result.toString().charAt(0) : def;
    }

    @Override
    public int getInt(@NotNull String path) {
        Object result = get(path);

        return result instanceof Number ? ((Number) result).intValue() : 0;
    }

    @Override
    public int getInt(@NotNull String path, int def) {
        Object result = get(path);

        return result instanceof Number ? ((Number) result).intValue() : def;
    }

    @Override
    public boolean getBoolean(@NotNull String path) {
        Object result = get(path);

        return result instanceof Boolean ? (Boolean) result : false;
    }

    @Override
    public boolean getBoolean(@NotNull String path, boolean def) {
        Object result = get(path);

        return result instanceof Boolean ? (Boolean) result : def;
    }

    @Override
    public double getDouble(@NotNull String path) {
        Object result = get(path);

        return result instanceof Number ? ((Number) result).doubleValue() : 0;
    }

    @Override
    public double getDouble(@NotNull String path, double def) {
        Object result = get(path);

        return result instanceof Number ? ((Number) result).doubleValue() : def;
    }

    @Override
    public long getLong(@NotNull String path) {
        Object result = get(path);

        return result instanceof Number ? ((Number) result).longValue() : 0;
    }

    @Override
    public long getLong(@NotNull String path, long def) {
        Object result = get(path);

        return result instanceof Number ? ((Number) result).longValue() : def;
    }

    @Nullable
    @Override
    public List<?> getList(@NotNull String path) {
        Object result = get(path);

        return result instanceof List ? (List<?>) result : null;
    }

    @Nullable
    @Override
    public List<?> getList(@NotNull String path, @Nullable List<?> def) {
        Object result = get(path);

        return result instanceof List ? (List<?>) result : def;
    }

    @Nullable
    public CompatibleMaterial getMaterial(@NotNull String path) {
        String val = getString(path);

        return val != null ? CompatibleMaterial.getMaterial(val) : null;
    }

    @Nullable
    public CompatibleMaterial getMaterial(@NotNull String path, @Nullable CompatibleMaterial def) {
        String val = getString(path);

        CompatibleMaterial mat = val != null ? CompatibleMaterial.getMaterial(val) : null;

        return mat != null ? mat : def;
    }

    @Nullable
    @Override
    public <T> T getObject(@NotNull String path, @NotNull Class<T> clazz) {
        Object result = get(path);

        return clazz.isInstance(result) ? clazz.cast(result) : null;
    }

    @Nullable
    @Override
    public <T> T getObject(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def) {
        Object result = get(path);

        return clazz.isInstance(result) ? clazz.cast(result) : def;
    }

    @Override
    public ConfigSection getConfigurationSection(@NotNull String path) {
        Object result = get(path);

        return result instanceof ConfigSection ? (ConfigSection) result : null;
    }

    @NotNull
    public ConfigSection getOrCreateConfigurationSection(@NotNull String path) {
        Object result = get(path);

        return result instanceof ConfigSection ? (ConfigSection) result : createSection(path);
    }
}
