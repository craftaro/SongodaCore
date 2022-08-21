package com.songoda.core.configuration;

import com.songoda.core.configuration.yaml.YamlConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReadOnlyConfigEntryTest {
    @Test
    void testGetKey() {
        ConfigEntry entry = new ReadOnlyConfigEntry(new YamlConfiguration(), "key-1");

        assertEquals("key-1", entry.getKey());
    }

    @Test
    void testGetConfig() {
        IConfiguration config = new YamlConfiguration();
        ConfigEntry entry = new ReadOnlyConfigEntry(config, "key");

        assertSame(config, entry.getConfig());
    }

    @Test
    void testNullGetters() {
        ConfigEntry entry = new ReadOnlyConfigEntry(new YamlConfiguration(), "key-null");

        assertNull(entry.getDefaultValue());
        assertNull(entry.getUpgradeSteps());
    }
}
