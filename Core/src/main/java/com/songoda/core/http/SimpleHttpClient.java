package com.songoda.core.http;

import com.songoda.core.SongodaCore;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleHttpClient implements HttpClient {
    public @NotNull HttpResponse get(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setInstanceFollowRedirects(true);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        connection.setRequestProperty("User-Agent", "SongodaCore/" + SongodaCore.getVersion() + " (+https://github.com/songoda/SongodaCore)");

        return new HttpResponseImpl(connection);
    }
}
