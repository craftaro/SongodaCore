package com.songoda.core.configuration.yaml;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.configuration.ConfigEntry;
import com.songoda.core.configuration.songoda.SongodaYamlConfig;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class YamlConfigEntryTest {
    @Test
    void testGetKey() {
        ConfigEntry entry = new YamlConfigEntry(new YamlConfiguration(), "key-1", null);
        assertEquals("key-1", entry.getKey());
    }

    @Test
    void testGetConfig() {
        YamlConfiguration config = new YamlConfiguration();
        ConfigEntry entry = new YamlConfigEntry(config, "key-1", null);
        assertSame(config, entry.getConfig());
    }

    @Test
    void testGetDefaultValue() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = cfg.createEntry("key", "value");

        assertEquals("value", entry.getDefaultValue());

        entry.setDefaultValue("new-value");
        assertEquals("new-value", entry.getDefaultValue());
    }

    @Test
    void testGetOr() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = cfg.createEntry("key", "value");

        assertEquals("value", entry.getOr("invalid"));

        entry.set(null);
        assertEquals("invalid", entry.getOr("invalid"));
    }

    @Test
    void testGetString() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = cfg.createEntry("key", null);

        entry.set("value");
        assertEquals("value", entry.getString());

        entry.set("new-value");
        assertEquals("new-value", entry.getString());

        entry.set(null);
        assertNull(entry.getString());
        assertNull(entry.getStringOr(null));
        assertEquals("12", entry.getStringOr("12"));

        entry.set(10.5);
        assertEquals("10.5", entry.getString());

        entry.set(true);
        assertEquals("true", entry.getString());

        entry.set(CompatibleMaterial.STONE);
        assertEquals("STONE", entry.getString());
    }

    @Test
    void testGetInt() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = cfg.createEntry("key", null);

        entry.set(1.0);
        assertEquals(1, entry.getInt());

        entry.set("1.5");
        assertEquals(1, entry.getInt());

        entry.set("10");
        assertEquals(10.0, entry.getInt());

        entry.set("10,0");
        assertThrows(NumberFormatException.class, entry::getInt);

        entry.set(null);
        assertEquals(0, entry.getInt());
        assertEquals(11, entry.getIntOr(11));
    }

    @Test
    void testGetDouble() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = cfg.createEntry("key", null);

        entry.set(1.0);
        assertEquals(1.0, entry.getDouble());

        entry.set("1.5");
        assertEquals(1.5, entry.getDouble());

        entry.set("10");
        assertEquals(10.0, entry.getDouble());

        entry.set("10,0");
        assertThrows(NumberFormatException.class, entry::getDouble);

        entry.set(null);
        assertEquals(0.0, entry.getDouble());
        assertEquals(11.5, entry.getDoubleOr(11.5));
    }

    @Test
    void testGetBoolean() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = cfg.createEntry("key", null);

        entry.set(false);
        assertFalse(entry.getBoolean());

        entry.set("false");
        assertFalse(entry.getBoolean());

        entry.set("invalid");
        assertFalse(entry.getBoolean());

        entry.set(1);
        assertFalse(entry.getBoolean());

        entry.set(true);
        assertTrue(entry.getBoolean());

        entry.set("true");
        assertTrue(entry.getBoolean());

        entry.set(null);
        assertFalse(entry.getBoolean());
        assertTrue(entry.getBooleanOr(true));
    }

    @Test
    void testGetStringList() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = cfg.createEntry("key", null);

        final List<String> fallbackValue = Collections.unmodifiableList(new LinkedList<>());

        entry.set(null);
        assertNull(entry.getStringList());
        assertSame(fallbackValue, entry.getStringListOr(fallbackValue));

        entry.set(Collections.singletonList("value"));
        assertEquals(Collections.singletonList("value"), entry.getStringList());

        entry.set(new String[] {"value2"});
        assertEquals(Collections.singletonList("value2"), entry.getStringList());

        entry.set("string-value");
        assertNull(entry.getStringList());
    }

    @Test
    void testGetMaterial() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = cfg.createEntry("key", null);

        entry.set("LOG");
        assertEquals(CompatibleMaterial.BIRCH_LOG, entry.getMaterial());

        entry.set("OAK_LOG");
        assertEquals(CompatibleMaterial.OAK_LOG, entry.getMaterial());

        entry.set("10");
        assertNull(entry.getMaterial());

        entry.set(null);
        assertNull(entry.getMaterial());
        assertEquals(CompatibleMaterial.ACACIA_BOAT, entry.getMaterialOr(CompatibleMaterial.ACACIA_BOAT));

        entry.set(CompatibleMaterial.GRASS);
        assertEquals(CompatibleMaterial.GRASS, entry.getMaterial());

        entry.set(Material.GRASS);
        assertEquals(CompatibleMaterial.GRASS, entry.getMaterial());
    }

    @Test
    void testInvalidWithUpgradeNull() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = cfg.createEntry("key", "value");

        assertThrows(IllegalArgumentException.class, () -> entry.withUpgradeStep(1, null, null));
    }

    @Test
    void testEqualsAndHashCode() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = cfg.createEntry("key", "value");

        assertEquals(entry, entry);
        assertEquals(entry.hashCode(), entry.hashCode());


        ConfigEntry other = new YamlConfigEntry(cfg, "key", "value");
        assertEquals(entry, other);
        assertEquals(entry.hashCode(), other.hashCode());

        other = new YamlConfigEntry(cfg, "key", "value2");
        assertNotEquals(entry, other);
        assertNotEquals(entry.hashCode(), other.hashCode());

        other = new YamlConfigEntry(cfg, "key2", "value");
        assertNotEquals(entry, other);
        assertNotEquals(entry.hashCode(), other.hashCode());

        other = new YamlConfigEntry(cfg, "key", "value2");
        assertNotEquals(entry, other);
        assertNotEquals(entry.hashCode(), other.hashCode());

        other = new YamlConfigEntry(cfg, "key2", "value2");
        assertNotEquals(entry, other);
        assertNotEquals(entry.hashCode(), other.hashCode());
    }
}
