package com.songoda.core.locale;

import com.songoda.core.http.HttpClient;
import com.songoda.core.http.HttpResponse;
import com.songoda.core.http.UnexpectedHttpStatusException;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LocaleFileManager {
    private final HttpClient httpClient;
    private final String projectName;

    public LocaleFileManager(HttpClient httpClient, String projectName) {
        this.httpClient = httpClient;
        this.projectName = projectName;
    }

    public List<String> downloadMissingTranslations(File targetDirectory) throws IOException {
        List<String> availableLanguages = this.fetchAvailableLanguageFiles();
        if (availableLanguages == null) {
            return Collections.emptyList();
        }

        Files.createDirectories(targetDirectory.toPath());

        List<String> downloadedLocales = new LinkedList<>();
        for (String languageFileName : availableLanguages) {
            File languageFile = new File(targetDirectory, languageFileName);
            if (languageFile.exists()) {
                continue;
            }

            String languageFileContents = fetchProjectFile(languageFileName);
            if (languageFileContents == null) {
                throw new IOException("Failed to download language file " + languageFileName);  // TODO: Better exception
            }

            try (Writer writer = new FileWriter(languageFile)) {
                writer.write(languageFileContents);
            }

            downloadedLocales.add(languageFileName);
        }

        return downloadedLocales;
    }

    public @Nullable List<String> fetchAvailableLanguageFiles() throws IOException {
        String projectLanguageIndex = fetchProjectFile("_index.txt");

        if (projectLanguageIndex == null) {
            return null;
        }

        List<String> result = new LinkedList<>();

        for (String line : projectLanguageIndex.split("\r?\n")) {
            line = line.trim();

            if (!line.startsWith("#") && !line.isEmpty()) {
                result.add(line);
            }
        }

        return result;
    }

    public String fetchProjectFile(String fileName) throws IOException {
        String url = formatUrl("https://songoda.github.io/Translations/projects/%s/%s", this.projectName, fileName);
        HttpResponse httpResponse = this.httpClient.get(url);

        if (httpResponse.getResponseCode() == 404) {
            return null;
        }

        if (httpResponse.getResponseCode() != 200) {
            throw new UnexpectedHttpStatusException(httpResponse.getResponseCode(), url);
        }

        return httpResponse.getBodyAsString();
    }

    private static String formatUrl(String url, Object... params) throws UnsupportedEncodingException {
        Object[] encodedParams = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            encodedParams[i] = URLEncoder.encode(params[i].toString(), "UTF-8");
        }

        return String.format(url, encodedParams);
    }
}
