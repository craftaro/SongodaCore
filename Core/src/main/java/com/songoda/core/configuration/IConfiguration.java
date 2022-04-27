package com.songoda.core.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface IConfiguration {
    /**
     * This method returns whether a given key is set memory, ignoring its possibly null value.
     *
     * {@link #set(String, Object)}
     * {@link #unset(String)}
     */
    boolean has(String key);

    /**
     * This method returns the value for a given key.
     * A value of null can mean that the key does not exist or that the value is null.
     *
     * @see #has(String)
     */
    @Nullable
    Object get(String key);

    /**
     * This method is mostly identical to {@link #get(String)}
     * but returns the given default value if the key doesn't exist or the value is null.
     */
    @Nullable
    Object getOrDefault(String key, @Nullable Object defaultValue);

    /**
     * This method sets a given key to a given value in memory.
     *
     * @return The previous value associated with key, or null if there was no mapping for key
     *
     * @see #save(Writer)
     */
    Object set(@NotNull String key, @Nullable Object value);

    /**
     * This method removes the given key from memory together with its value.
     *
     * @return The previous value associated with key, or null if there was no mapping for key
     */
    Object unset(String key);

    /**
     * This method clears all the configuration values from memory that have been loaded or set.
     *
     * @see #load(Reader)
     */
    void reset();

    /**
     * This method parses and loads the configuration and stores them as key-value pairs in memory.
     * Keys that are not loaded with this call but still exist in memory, are removed.
     * Additional data may be read depending on the implementation (e.g. comments).
     *
     * @see #reset()
     */
    void load(Reader reader) throws IOException;

    /**
     * This method serializes the key-value pairs in memory and writes them to the given writer.
     * Additional data may be written depending on the implementation (e.g. comments).
     */
    void save(Writer writer) throws IOException;
}
