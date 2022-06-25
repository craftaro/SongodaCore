package com.songoda.core.configuration.songoda;

import com.songoda.core.compatibility.CompatibleMaterial;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigEntryTest {
    @Test
    void testGetDefaultValue() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = new ConfigEntry(cfg, "key", "value");

        assertEquals("value", entry.getDefaultValue());

        entry.setDefaultValue("new-value");
        assertEquals("new-value", entry.getDefaultValue());
    }

    @Test
    void testGetOr() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = new ConfigEntry(cfg, "key", "value");

        assertEquals("value", entry.getOr("invalid"));

        entry.set(null);
        assertEquals("invalid", entry.getOr("invalid"));
    }

    @Test
    void testGetString() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = new ConfigEntry(cfg, "key");

        entry.set("value");
        assertEquals("value", entry.getString());

        entry.set("new-value");
        assertEquals("new-value", entry.getString());

        entry.set(null);
        assertNull(entry.getString());
        assertNull(entry.getString(null));
        assertEquals("12", entry.getString("12"));

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
        ConfigEntry entry = new ConfigEntry(cfg, "key");

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
        assertEquals(11, entry.getInt(11));
    }

    @Test
    void testGetDouble() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = new ConfigEntry(cfg, "key");

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
        assertEquals(11.5, entry.getDouble(11.5));
    }

    @Test
    void testGetBoolean() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = new ConfigEntry(cfg, "key");

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
        assertTrue(entry.getBoolean(true));
    }


    @Test
    void testGetMaterial() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = new ConfigEntry(cfg, "key");

        entry.set("LOG");
        assertEquals(CompatibleMaterial.BIRCH_LOG, entry.getMaterial());

        entry.set("OAK_LOG");
        assertEquals(CompatibleMaterial.OAK_LOG, entry.getMaterial());

        entry.set("10");
        assertNull(entry.getMaterial());

        entry.set(null);
        assertNull(entry.getMaterial());
        assertEquals(CompatibleMaterial.ACACIA_BOAT, entry.getMaterial(CompatibleMaterial.ACACIA_BOAT));
    }

    @Test
    void testInvalidWithUpgradeNull() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = new ConfigEntry(cfg, "key", "value");

        assertThrows(IllegalArgumentException.class, () -> entry.withUpgradeStep(1, null, null));
    }

    @Test
    void testEqualsAndHashCode() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(new File("ConfigEntryTest.yml"));
        ConfigEntry entry = new ConfigEntry(cfg, "key", "value");

        assertEquals(entry, entry);
        assertEquals(entry.hashCode(), entry.hashCode());

        ConfigEntry other = new ConfigEntry(cfg, "key", "value");
        assertEquals(entry, other);
        assertEquals(entry.hashCode(), other.hashCode());

        other = new ConfigEntry(cfg, "key", "value2");
        assertNotEquals(entry, other);
        assertNotEquals(entry.hashCode(), other.hashCode());

        other = new ConfigEntry(cfg, "key2", "value");
        assertNotEquals(entry, other);
        assertNotEquals(entry.hashCode(), other.hashCode());

        other = new ConfigEntry(cfg, "key", "value2");
        assertNotEquals(entry, other);
        assertNotEquals(entry.hashCode(), other.hashCode());

        other = new ConfigEntry(cfg, "key2", "value2");
        assertNotEquals(entry, other);
        assertNotEquals(entry.hashCode(), other.hashCode());
    }
}
