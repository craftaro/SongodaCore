package com.songoda.core.http;

import com.songoda.core.SongodaCoreConstants;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleHttpClient implements HttpClient {
    private static final String USER_AGENT = generateUserAgent();

    public @NotNull HttpResponse get(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setInstanceFollowRedirects(true);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        connection.setRequestProperty("User-Agent", USER_AGENT);

        return new HttpResponseImpl(connection);
    }

    private static String generateUserAgent() {
        String projectName = SongodaCoreConstants.getProjectName();
        String version = SongodaCoreConstants.getCoreVersion();
        String projectUrl = SongodaCoreConstants.getGitHubProjectUrl();

        return projectName + "/" + version + " (+" + projectUrl + ")";
    }
}
