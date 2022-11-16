package com.songoda.core.configuration.songoda;

import com.songoda.core.configuration.ConfigEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SongodaYamlConfigTest {
    Path tmpDir;
    Path cfg;

    @BeforeEach
    void setUp() throws IOException {
        this.tmpDir = Files.createTempDirectory("SongodaYamlConfigTest");

        this.cfg = Files.createTempFile(this.tmpDir, "config", ".yml");
        this.tmpDir.toFile().deleteOnExit();
    }

    @AfterEach
    void tearDown() throws IOException {
        try (Stream<Path> stream = Files.walk(this.tmpDir)) {
            stream
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile).forEach(File::delete);
        }
    }

    @Test
    void testLoad() throws IOException {
        Files.write(this.cfg, "test-key: foo\n".getBytes());

        SongodaYamlConfig cfg = new SongodaYamlConfig(this.cfg.toFile());
        cfg.set("test-key", "bar");
        cfg.load();

        assertEquals("foo", cfg.get("test-key"));
    }

    @Test
    void testSave() throws IOException {
        Files.write(this.cfg, "test-key: foo\n".getBytes());

        SongodaYamlConfig cfg = new SongodaYamlConfig(this.cfg.toFile());
        cfg.set("test-key", "bar");
        cfg.save();

        assertEquals("test-key: bar\n", new String(Files.readAllBytes(this.cfg)));
    }

    @Test
    void testSaveToNonExistingSubDirectory() throws IOException {
        File configFile = new File(this.tmpDir.toFile(), "testSaveToNonExistingSubDirectory/config.yml");

        SongodaYamlConfig cfg = new SongodaYamlConfig(configFile);
        cfg.set("test-key", "bar");
        cfg.save();

        assertEquals("test-key: bar\n", new String(Files.readAllBytes(configFile.toPath())));
    }

    @Test
    void testWithVersion() throws IOException {
        SongodaYamlConfig cfg = new SongodaYamlConfig(this.cfg.toFile());
        cfg.withVersion("version-key", 1, null);

        assertEquals(1, cfg.get("version-key"));

        cfg.save();
        assertEquals("version-key: 1\n", new String(Files.readAllBytes(this.cfg)));

        cfg.withVersion(2);

        assertEquals(2, cfg.get("version"));
        assertNull(cfg.get("version-key"));

        cfg.save();
        assertEquals(
                "# Don't touch this – it's used to track the version of the config.\n" +
                        "version: 2\n",
                new String(Files.readAllBytes(this.cfg))
        );
    }

    @Test
    void testWithNegativeVersion() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(this.cfg.toFile());
        assertThrows(IllegalArgumentException.class, () -> cfg.withVersion("version-key", -1, null));
    }

    @Test
    void testLoadWithTooNewVersion() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(this.cfg.toFile())
                .withVersion(1);

        assertThrows(IllegalStateException.class, () -> cfg.load(new StringReader("version: 10\n")));
    }

    @Test
    void testWithUpToDateVersion() throws IOException {
        SongodaYamlConfig cfg = new SongodaYamlConfig(this.cfg.toFile())
                .withVersion(2);

        assertFalse(cfg.upgradeOldConfigVersion());
    }

    @Test
    void testWithNewerVersion() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(this.cfg.toFile())
                .withVersion(5);

        assertThrows(IllegalStateException.class, cfg::upgradeOldConfigVersionByOne);
    }

    @Test
    void testWithKeyWithoutConfigEntry() throws IOException {
        SongodaYamlConfig cfg = new SongodaYamlConfig(this.cfg.toFile());

        cfg.set("test-key", "foo");
        cfg.load();

        assertNull(cfg.get("test-key"));

        cfg.set("test-key", "foo");
        assertEquals("foo", cfg.get("test-key"));

        cfg.save();
        cfg.load();

        assertEquals("foo", cfg.get("test-key"));
        assertEquals(1, cfg.getKeys("").size());
    }

    @Test
    void testCreateEntryAppliesDefaultValueForNullValue() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(this.cfg.toFile());
        ConfigEntry entry = cfg.createEntry("key", "value");

        cfg.init();

        assertEquals("value", entry.get());
    }

    @Test
    void testCreateDuplicateEntry() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(this.cfg.toFile());
        ConfigEntry entry = cfg.createEntry("key", null);

        assertThrows(IllegalArgumentException.class, () -> cfg.createEntry("key", "other-value"));

        assertNull(entry.get());
    }

    @Test
    void testVersionUpgradePersistsCommentsOnKeyChange() throws IOException {
        SongodaYamlConfig cfg = new SongodaYamlConfig(this.cfg.toFile())
                .withVersion(2);

        cfg.createEntry("newKey", "value")
                .withComment("This is a comment")
                .withUpgradeStep(1, "oldKey");

        cfg.load(new StringReader("version: 1\noldKey: old-value\n"));

        assertNull(cfg.get("oldKey"));
        assertNull(cfg.getNodeComment("oldKey"));

        assertEquals("old-value", cfg.get("newKey"));
        assertEquals("This is a comment", Objects.requireNonNull(cfg.getNodeComment("newKey")).get());

        StringWriter writer = new StringWriter();
        cfg.save(writer);

        assertEquals("# Don't touch this – it's used to track the version of the config.\n" +
                        "version: 2\n" +
                        "# This is a comment\n" +
                        "newKey: old-value\n",
                writer.toString());
    }

    @Test
    void testReadOnlyEntry() {
        SongodaYamlConfig cfg = new SongodaYamlConfig(this.cfg.toFile());
        ConfigEntry entry = cfg.createEntry("key", "default-value");
        ConfigEntry readOnlyConfigEntry = cfg.getReadEntry("key");

        assertThrows(UnsupportedOperationException.class, () -> readOnlyConfigEntry.set("new-value"));
        assertEquals("default-value", entry.get());

        assertThrows(UnsupportedOperationException.class, () -> readOnlyConfigEntry.setDefaultValue("new-default-value"));
        assertEquals("default-value", entry.get());

        assertThrows(UnsupportedOperationException.class, () -> readOnlyConfigEntry.withComment("test-comment"));
        assertThrows(UnsupportedOperationException.class, () -> readOnlyConfigEntry.withComment(() -> "test-comment"));

        assertThrows(UnsupportedOperationException.class, () -> readOnlyConfigEntry.withUpgradeStep(10, "new-key"));
        assertThrows(UnsupportedOperationException.class, () -> readOnlyConfigEntry.withUpgradeStep(10, "new-key", (o) -> "new-value"));

        assertEquals("default-value", entry.get());

        entry.set("new-value");
        assertEquals("new-value", readOnlyConfigEntry.get());
    }
}
