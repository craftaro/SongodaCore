package com.songoda.core.configuration.yaml;

import com.songoda.core.configuration.HeaderCommentable;
import com.songoda.core.configuration.IConfiguration;
import com.songoda.core.configuration.NodeCommentable;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

// TODO: Allow registering own custom value converter (e.g. Bukkit-Location to Map and back)
//       + move the huge block from #set into such a converter and register it by default
public class YamlConfiguration implements IConfiguration, HeaderCommentable, NodeCommentable {
    protected final @NotNull Yaml yaml;
    protected final @NotNull DumperOptions yamlDumperOptions;
    protected final @NotNull YamlCommentRepresenter yamlCommentRepresenter;

    protected final @NotNull Map<String, Object> values;
    protected final @NotNull Map<String, Supplier<String>> nodeComments;
    protected @Nullable Supplier<String> headerComment;

    public YamlConfiguration() {
        this(new LinkedHashMap<>(), new LinkedHashMap<>());
    }

    protected YamlConfiguration(@NotNull Map<String, Object> values, @NotNull Map<String, Supplier<String>> nodeComments) {
        this.values = Objects.requireNonNull(values);
        this.nodeComments = Objects.requireNonNull(nodeComments);

        this.yamlDumperOptions = createDefaultYamlDumperOptions();
        this.yamlCommentRepresenter = new YamlCommentRepresenter(this.nodeComments);
        this.yaml = createDefaultYaml(this.yamlDumperOptions, this.yamlCommentRepresenter);
    }

    @Override
    @Contract(pure = true, value = "null -> false")
    public boolean has(String key) {
        if (key == null) {
            return false;
        }

        String[] fullKeyPath = key.split("\\.");

        Map<String, ?> innerMap = getInnerMap(this.values, Arrays.copyOf(fullKeyPath, fullKeyPath.length - 1), false);

        if (innerMap != null) {
            return innerMap.containsKey(fullKeyPath[fullKeyPath.length - 1]);
        }

        return false;
    }

    @Override
    @Contract(pure = true, value = "null -> null")
    public @Nullable Object get(String key) {
        if (key == null) {
            return null;
        }

        try {
            return getInnerValueForKey(this.values, key);
        } catch (IllegalArgumentException ignore) {
        }

        return null;
    }

    @Override
    @Contract(pure = true, value = "null,_ -> param2")
    public @Nullable Object getOr(String key, @Nullable Object fallbackValue) {
        Object value = get(key);

        return value == null ? fallbackValue : value;
    }

    public @NotNull Set<String> getKeys(String key) {
        if (key == null) {
            return Collections.emptySet();
        }

        if (key.equals("")) {
            return Collections.unmodifiableSet(this.values.keySet());
        }

        Map<String, ?> innerMap = null;

        try {
            innerMap = getInnerMap(this.values, key.split("\\."), false);
        } catch (IllegalArgumentException ignore) {
        }

        if (innerMap != null) {
            return Collections.unmodifiableSet(innerMap.keySet());
        }

        return Collections.emptySet();

    }

    @Override
    public Object set(@NotNull String key, @Nullable Object value) {
        if (value != null) {
            if (value instanceof Float) {
                value = ((Float) value).doubleValue();
            } else if (value instanceof Character) {
                value = ((Character) value).toString();
            } else if (value.getClass().isEnum()) {
                value = ((Enum<?>) value).name();
            } else if (value.getClass().isArray()) {
                if (value instanceof int[]) {
                    value = Arrays.asList(ArrayUtils.toObject((int[]) value));
                } else if (value instanceof long[]) {
                    value = Arrays.asList(ArrayUtils.toObject((long[]) value));
                } else if (value instanceof short[]) {
                    List<Integer> newValue = new ArrayList<>(((short[]) value).length);
                    for (Short s : (short[]) value) {
                        newValue.add(s.intValue());
                    }
                    value = newValue;
                } else if (value instanceof byte[]) {
                    List<Integer> newValue = new ArrayList<>(((byte[]) value).length);
                    for (Byte b : (byte[]) value) {
                        newValue.add(b.intValue());
                    }
                    value = newValue;
                } else if (value instanceof double[]) {
                    value = Arrays.asList(ArrayUtils.toObject((double[]) value));
                } else if (value instanceof float[]) {
                    List<Double> newValue = new ArrayList<>(((float[]) value).length);
                    for (float f : (float[]) value) {
                        newValue.add(new Float(f).doubleValue());
                    }
                    value = newValue;
                } else if (value instanceof boolean[]) {
                    value = Arrays.asList(ArrayUtils.toObject((boolean[]) value));
                } else if (value instanceof char[]) {
                    List<String> newValue = new ArrayList<>(((char[]) value).length);
                    for (char c : (char[]) value) {
                        newValue.add(String.valueOf(c));
                    }
                    value = newValue;
                } else {
                    value = Arrays.asList((Object[]) value);
                }
            }
        }

        return setInnerValueForKey(this.values, key, value);
    }

    @Override
    public Object unset(String key) {
        String[] fullKeyPath = key.split("\\.");

        Map<String, ?> innerMap = getInnerMap(this.values, Arrays.copyOf(fullKeyPath, fullKeyPath.length - 1), false);

        if (innerMap != null) {
            return innerMap.remove(fullKeyPath[fullKeyPath.length - 1]);
        }

        return null;
    }

    @Override
    public void reset() {
        this.values.clear();
    }

    @Override
    public void load(Reader reader) throws IOException {
        Object yamlData = this.yaml.load(reader);
        if (yamlData == null) {
            yamlData = Collections.emptyMap();
        }

        if (!(yamlData instanceof Map)) {
            throw new IllegalStateException("The YAML file does not have the expected tree structure: " + yamlData.getClass().getName());
        }

        synchronized (this.values) {
            this.values.clear();

            for (Map.Entry<?, ?> yamlEntry : ((Map<?, ?>) yamlData).entrySet()) {
                this.values.put(yamlEntry.getKey().toString(), yamlEntry.getValue());
            }
        }
    }

    @Override
    public void save(Writer writer) throws IOException {
        String headerCommentLines = generateHeaderCommentLines();
        writer.write(headerCommentLines);

        cleanValuesMap(this.values);

        if (this.values.size() > 0) {
            if (headerCommentLines.length() > 0) {
                writer.write(this.yamlDumperOptions.getLineBreak().getString());
            }

            this.yaml.dump(this.values, writer);
        }
    }

    @Override
    public void setHeaderComment(@Nullable Supplier<String> comment) {
        this.headerComment = comment;
    }

    @Override
    public @Nullable Supplier<String> getHeaderComment() {
        return this.headerComment;
    }

    @Override
    public @NotNull String generateHeaderCommentLines() {
        StringBuilder sb = new StringBuilder();

        String headerCommentString = this.headerComment == null ? null : this.headerComment.get();
        if (headerCommentString != null) {
            for (String commentLine : headerCommentString.split("\r?\n")) {
                sb.append("# ")
                        .append(commentLine)
                        .append(this.yamlDumperOptions.getLineBreak().getString());
            }
        }

        return sb.toString();
    }

    @Override
    public void setNodeComment(@NotNull String key, @Nullable Supplier<String> comment) {
        this.nodeComments.put(key, comment);
    }

    @Override
    public @Nullable Supplier<String> getNodeComment(@Nullable String key) {
        return this.nodeComments.get(key);
    }

    public String toYamlString() throws IOException {
        StringWriter writer = new StringWriter();
        save(writer);

        return writer.toString();
    }

    @Override
    public String toString() {
        return "YamlConfiguration{" +
                "values=" + this.values +
                ", headerComment=" + this.headerComment +
                '}';
    }

    protected static DumperOptions createDefaultYamlDumperOptions() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setIndentWithIndicator(true);
        dumperOptions.setIndicatorIndent(2);

        return dumperOptions;
    }

    protected static Yaml createDefaultYaml(DumperOptions dumperOptions, Representer representer) {
        LoaderOptions yamlOptions = new LoaderOptions();
        yamlOptions.setAllowDuplicateKeys(false);

        return new Yaml(new Constructor(yamlOptions), representer, dumperOptions, yamlOptions);
    }

    protected static Object setInnerValueForKey(@NotNull Map<String, Object> map, @NotNull String key, @Nullable Object value) {
        String[] fullKeyPath = key.split("\\.");

        Map<String, ?> innerMap = getInnerMap(map, Arrays.copyOf(fullKeyPath, fullKeyPath.length - 1), true);

        return ((Map<String, Object>) innerMap).put(fullKeyPath[fullKeyPath.length - 1], value);
    }

    protected static Object getInnerValueForKey(@NotNull Map<String, Object> map, @NotNull String key) {
        String[] fullKeyPath = key.split("\\.");

        Map<String, ?> innerMap = getInnerMap(map, Arrays.copyOf(fullKeyPath, fullKeyPath.length - 1), false);

        if (innerMap != null) {
            return innerMap.get(fullKeyPath[fullKeyPath.length - 1]);
        }

        return null;
    }

    @Contract("_,_,true -> !null")
    protected static Map<String, ?> getInnerMap(@NotNull Map<String, ?> map, @NotNull String[] keys, boolean createMissingMaps) {
        if (keys.length == 0) {
            return map;
        }

        int currentKeyIndex = 0;
        Map<String, ?> currentMap = map;

        while (true) {
            Object currentValue = currentMap.get(keys[currentKeyIndex]);

            if (currentValue == null) {
                if (!createMissingMaps) {
                    return null;
                }

                currentValue = new HashMap<>();
                ((Map<String, Object>) currentMap).put(keys[currentKeyIndex], currentValue);
            }

            if (!(currentValue instanceof Map)) {
                if (!createMissingMaps) {
                    throw new IllegalArgumentException("Expected a Map when resolving key '" + String.join(".", keys) + "' at '" + String.join(".", Arrays.copyOf(keys, currentKeyIndex + 1)) + "'");
                }

                currentValue = new HashMap<>();
                ((Map<String, Object>) currentMap).put(keys[currentKeyIndex], currentValue);
            }

            if (currentKeyIndex == keys.length - 1) {
                return (Map<String, ?>) currentValue;
            }

            currentMap = (Map<String, ?>) currentValue;
            ++currentKeyIndex;
        }
    }

    /**
     * This takes a map and removes all keys that have a value of null.<br>
     * Additionally, if the value is a {@link Map}, it will be recursively cleaned too.<br>
     * {@link Map}s that are or get empty, will be removed (recursively).<br>
     */
    protected void cleanValuesMap(Map<?, ?> map) {
        for (Object key : map.keySet().toArray()) {
            Object value = map.get(key);

            if (value instanceof Map) {
                cleanValuesMap((Map<?, ?>) value);
            }

            if (value == null || (value instanceof Map && ((Map<?, ?>) value).isEmpty())) {
                map.remove(key);
            }
        }
    }
}
