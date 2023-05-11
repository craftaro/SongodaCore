package com.songoda.core.locale;

import com.songoda.core.configuration.songoda.SongodaYamlConfig;
import com.songoda.core.configuration.yaml.YamlConfiguration;
import com.songoda.core.http.SimpleHttpClient;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LocaleManager {
    protected final Plugin plugin;
    protected final File localesDirectory;

    protected final List<SongodaYamlConfig> loadedLocales = new LinkedList<>();
    protected final @Nullable YamlConfiguration fallbackLocale;

    public LocaleManager(Plugin plugin) throws IOException {
        this.plugin = plugin;
        this.localesDirectory = new File(this.plugin.getDataFolder(), "locales");

        this.fallbackLocale = loadFallbackLocale();
    }

    public List<String> downloadMissingLocales() {
        LocaleFileManager localeFileManager = new LocaleFileManager(new SimpleHttpClient(), this.plugin.getName());

        try {
            return localeFileManager.downloadMissingTranslations(this.localesDirectory);
        } catch (IOException ex) {
            this.plugin.getLogger().warning("Failed to download missing locales: " + ex.getMessage());
        }

        return Collections.emptyList();
    }

    public void load(String locale) throws IOException {
        File fileToLoad = determineAvailableLocaleVariation(locale);
        if (fileToLoad == null) {
            throw new FileNotFoundException("Locale file " + locale + " not found");
        }

        for (SongodaYamlConfig loadedLocale : this.loadedLocales) {
            if (loadedLocale.file.equals(fileToLoad)) {
                return;
            }
        }

        SongodaYamlConfig localeConfig = new SongodaYamlConfig(fileToLoad);
        localeConfig.load();
        this.loadedLocales.add(localeConfig);
    }

    public void loadExclusively(String locale) {
        loadExclusively(Collections.singletonList(locale));
    }

    public void loadExclusively(List<String> locales) {
        unloadAll();
    }

    public void unloadAll() {
        this.loadedLocales.clear();
    }

    protected @Nullable File determineAvailableLocaleVariation(String locale) {
        File localeFile = new File(this.localesDirectory, locale + ".lang");
        if (localeFile.exists()) {
            return localeFile;
        }

        File[] availableLocales = this.localesDirectory.listFiles();
        if (availableLocales == null) {
            return null;
        }

        for (File availableLocale : availableLocales) {
            if (availableLocale.getName().startsWith(locale)) {
                return availableLocale;
            }
        }

        return null;
    }

    protected @Nullable YamlConfiguration loadFallbackLocale() throws IOException {
        URL fallbackLocaleUrl = this.plugin.getClass().getResource("/en_US.lang");
        if (fallbackLocaleUrl == null) {
            return null;
        }

        YamlConfiguration locale = new YamlConfiguration();
        try (Reader reader = new InputStreamReader(fallbackLocaleUrl.openStream(), StandardCharsets.UTF_8)) {
            locale.load(reader);
        }

        return locale;
    }

    protected SongodaYamlConfig parseLocaleFile(File file) throws IOException {
        SongodaYamlConfig locale = new SongodaYamlConfig(file, this.plugin.getLogger());
        locale.load();

        return locale;
    }
}
