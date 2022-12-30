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

    @Test
    void testWritingMethodsDoingNothing() {
        YamlConfiguration config = new YamlConfiguration();
        ConfigEntry entry = new ReadOnlyConfigEntry(config, "key");

        assertThrows(UnsupportedOperationException.class, () -> entry.setDefaultValue("value"));
        assertThrows(UnsupportedOperationException.class, () -> entry.withDefaultValue("value"));
        assertThrows(UnsupportedOperationException.class, () -> entry.withComment("A comment."));
        assertThrows(UnsupportedOperationException.class, () -> entry.withUpgradeStep(0, "old-key"));
        assertThrows(UnsupportedOperationException.class, () -> entry.set("value"));

        assertNull(entry.getDefaultValue());
        assertNull(entry.getUpgradeSteps());
        assertNull(config.getNodeComment("key"));
        assertNull(entry.getUpgradeSteps());
        assertNull(entry.get());
    }
}
