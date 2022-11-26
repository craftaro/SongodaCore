package com.songoda.core.locale;

import com.songoda.core.http.MockHttpClient;
import com.songoda.core.http.MockHttpResponse;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

class LocaleFileManagerTest {
    private final byte[] validIndexFile = ("# This is a comment\n\nen_US.lang\nen.yml\nde.txt\n").getBytes(StandardCharsets.UTF_8);

    private Path testDirectoryPath;

    @BeforeEach
    void setUp() throws IOException {
        this.testDirectoryPath = Files.createTempDirectory("SongodaCore-LocaleFileManagerTest");
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
    void downloadMissingTranslations_EmptyTargetDir() throws IOException {
        Plugin plugin = Mockito.mock(Plugin.class);
        Mockito.when(plugin.getDataFolder()).thenReturn(this.testDirectoryPath.toFile());

        MockHttpClient httpClient = new MockHttpClient(new MockHttpResponse(200, this.validIndexFile));
        LocaleFileManager localeFileManager = new LocaleFileManager(httpClient, "test");

        List<String> downloadedFiles = localeFileManager.downloadMissingTranslations(plugin.getDataFolder());

        Assertions.assertSame(3, downloadedFiles.size());
        Assertions.assertEquals("en.yml", downloadedFiles.get(1));
        Assertions.assertEquals("en_US.lang", downloadedFiles.get(0));
        Assertions.assertEquals("de.txt", downloadedFiles.get(2));

        String[] localeFiles = plugin.getDataFolder().list();
        Assertions.assertNotNull(localeFiles);
        Arrays.sort(localeFiles);
        Assertions.assertArrayEquals(new String[] {"de.txt", "en.yml", "en_US.lang"}, localeFiles);

        Assertions.assertSame(4, httpClient.callsOnGet.size());
        Assertions.assertTrue(httpClient.callsOnGet.get(0).contains("/test/_index.txt"));
        Assertions.assertTrue(httpClient.callsOnGet.get(1).contains("/test/en_US.lang"));
        Assertions.assertTrue(httpClient.callsOnGet.get(2).contains("/test/en.yml"));
        Assertions.assertTrue(httpClient.callsOnGet.get(3).contains("/test/de.txt"));
    }

    @Test
    void downloadMissingTranslations() throws IOException {
        Plugin plugin = Mockito.mock(Plugin.class);
        Mockito.when(plugin.getDataFolder()).thenReturn(this.testDirectoryPath.toFile());

        Files.createDirectories(plugin.getDataFolder().toPath());
        Files.createFile(new File(plugin.getDataFolder(), "en_US.lang").toPath());
        Files.createFile(new File(plugin.getDataFolder(), "fr.lang").toPath());

        MockHttpClient httpClient = new MockHttpClient(new MockHttpResponse(200, this.validIndexFile));
        LocaleFileManager localeFileManager = new LocaleFileManager(httpClient, "test");

        List<String> downloadedFiles = localeFileManager.downloadMissingTranslations(plugin.getDataFolder());

        Assertions.assertSame(2, downloadedFiles.size());
        Assertions.assertEquals("en.yml", downloadedFiles.get(0));
        Assertions.assertEquals("de.txt", downloadedFiles.get(1));

        String[] localeFiles = plugin.getDataFolder().list();

        Assertions.assertNotNull(localeFiles);
        Arrays.sort(localeFiles);
        Assertions.assertArrayEquals(new String[] {"de.txt", "en.yml", "en_US.lang", "fr.lang"}, localeFiles);

        Assertions.assertSame(3, httpClient.callsOnGet.size());
        Assertions.assertTrue(httpClient.callsOnGet.get(0).contains("/test/_index.txt"));
        Assertions.assertTrue(httpClient.callsOnGet.get(1).contains("/test/en.yml"));
        Assertions.assertTrue(httpClient.callsOnGet.get(2).contains("/test/de.txt"));
    }

    @Test
    void fetchAvailableLanguageFiles() throws IOException {
        MockHttpClient httpClient = new MockHttpClient(new MockHttpResponse(200, this.validIndexFile));
        LocaleFileManager localeFileManager = new LocaleFileManager(httpClient, "test");

        List<String> availableLanguages = localeFileManager.fetchAvailableLanguageFiles();

        Assertions.assertSame(1, httpClient.callsOnGet.size());
        Assertions.assertTrue(httpClient.callsOnGet.get(0).contains("/test/"));

        Assertions.assertNotNull(availableLanguages);
        Assertions.assertSame(3, availableLanguages.size());
        Assertions.assertTrue(availableLanguages.contains("en_US.lang"));
        Assertions.assertTrue(availableLanguages.contains("en.yml"));
        Assertions.assertTrue(availableLanguages.contains("de.txt"));
    }

    @Test
    void fetchAvailableLanguageFiles_SpecialCharsInProjectName() throws IOException {
        MockHttpClient httpClient = new MockHttpClient(new MockHttpResponse(200, this.validIndexFile));
        LocaleFileManager localeFileManager = new LocaleFileManager(httpClient, "test project (special)");

        List<String> availableLanguages = localeFileManager.fetchAvailableLanguageFiles();

        Assertions.assertSame(1, httpClient.callsOnGet.size());
        Assertions.assertTrue(httpClient.callsOnGet.get(0).contains("/test+project+%28special%29/"));

        Assertions.assertNotNull(availableLanguages);
        Assertions.assertSame(3, availableLanguages.size());
        Assertions.assertTrue(availableLanguages.contains("en_US.lang"));
        Assertions.assertTrue(availableLanguages.contains("en.yml"));
        Assertions.assertTrue(availableLanguages.contains("de.txt"));
    }

    @Test
    void fetchAvailableLanguageFiles_EmptyIndex() throws IOException {
        MockHttpClient httpClient = new MockHttpClient(new MockHttpResponse(200, new byte[0]));
        LocaleFileManager localeFileManager = new LocaleFileManager(httpClient, "empty-project");

        List<String> availableLanguages = localeFileManager.fetchAvailableLanguageFiles();

        Assertions.assertNotNull(availableLanguages);
        Assertions.assertTrue(availableLanguages.isEmpty());
    }

    @Test
    void fetchAvailableLanguageFiles_UnknownProject() throws IOException {
        MockHttpClient httpClient = new MockHttpClient(new MockHttpResponse(404, new byte[0]));
        LocaleFileManager localeFileManager = new LocaleFileManager(httpClient, "unknown");

        Assertions.assertNull(localeFileManager.fetchAvailableLanguageFiles());
    }

    @Test
    void fetchAvailableLanguageFiles_HttpStatus500() {
        MockHttpClient httpClient = new MockHttpClient(new MockHttpResponse(500, new byte[0]));
        LocaleFileManager localeFileManager = new LocaleFileManager(httpClient, "test");

        Assertions.assertThrows(IOException.class, localeFileManager::fetchAvailableLanguageFiles);
    }
}
