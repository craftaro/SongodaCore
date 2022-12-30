package com.songoda.core.configuration.songoda;

import com.songoda.core.configuration.ConfigEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SongodaYamlConfigRoundtripTest {
    private Path testDirectoryPath;

    @BeforeEach
    void setUp() throws IOException {
        this.testDirectoryPath = Files.createTempDirectory("SongodaCore-YamlConfigRoundtripTest");
        this.testDirectoryPath.toFile().deleteOnExit();
    }

    @AfterEach
    void tearDown() throws IOException {
        try (Stream<Path> paths = Files.list(this.testDirectoryPath)) {
            for (Path path : paths.toArray(Path[]::new)) {
                Files.deleteIfExists(path);
            }
        }
        Files.deleteIfExists(this.testDirectoryPath);
    }

    @Test
    void roundtripTest() throws IOException {
        Path testFilePath = this.testDirectoryPath.resolve("config.yml");

        Files.write(testFilePath, ("# Don't touch this – it's used to track the version of the config.\n" +
                "version: 1\n" +
                "messages:\n" +
                "  # This message is shown when the 'foo' command succeeds.\n" +
                "  fooSuccess: Remastered success value\n" +
                "# This is the range of the 'foo' command\n").getBytes());

        SongodaYamlConfig cfg = new SongodaYamlConfig(testFilePath.toFile())
                .withVersion(3);

        ConfigEntry cmdFooSuccess = cfg.createEntry("command.foo.success")
                .withDefaultValue("Default success value")
                .withComment("This message is shown when the 'foo' command succeeds.")
                .withUpgradeStep(1, "messages.fooSuccess");
        ConfigEntry range = cfg.createEntry("range")
                .withComment("This is the range of the 'foo' command")
                .withUpgradeStep(1, null, o -> {
                    if (o == null) {
                        return 10;
                    }

                    return o;
                })
                .withUpgradeStep(2, null, o -> o + " blocks");
        ConfigEntry incrementer = cfg.createEntry("incrementer", 0)
                .withComment("This is the incrementer of the 'foo' command")
                .withUpgradeStep(1, null, o -> {
                    if (o == null) {
                        return null;
                    }

                    return (int) o + 1;
                })
                .withUpgradeStep(3, null, (o) -> "text");
        ConfigEntry entryWithoutUpgradeStep = cfg.createEntry("entryWithoutUpgradeStep")
                .withDefaultValue("Default value")
                .withComment("This is the entry without an upgrade step");

        assertTrue(cfg.init());

        assertNull(cfg.get("messages.fooSuccess"));
        assertEquals("Remastered success value", cfg.get("command.foo.success"));
        assertEquals("Remastered success value", cmdFooSuccess.get());
        assertTrue(cmdFooSuccess.has());

        assertTrue(range.has());
        assertEquals(cfg.get("range"), range.get());

        assertTrue(incrementer.has());
        assertEquals(cfg.get("incrementer"), incrementer.get());

        assertTrue(entryWithoutUpgradeStep.has());
        assertEquals(cfg.get("entryWithoutUpgradeStep"), entryWithoutUpgradeStep.get());

        assertEquals("# Don't touch this – it's used to track the version of the config.\n" +
                "version: 3\n" +
                "command:\n" +
                "  foo:\n" +
                "    # This message is shown when the 'foo' command succeeds.\n" +
                "    success: Remastered success value\n" +
                "# This is the range of the 'foo' command\n" +
                "range: 10 blocks\n" +
                "# This is the incrementer of the 'foo' command\n" +
                "incrementer: 0\n" +
                "# This is the entry without an upgrade step\n" +
                "entryWithoutUpgradeStep: Default value\n", new String(Files.readAllBytes(testFilePath)));
    }
}
