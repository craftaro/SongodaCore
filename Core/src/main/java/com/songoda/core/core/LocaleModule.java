package com.songoda.core.core;

import com.songoda.core.locale.Locale;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocaleModule implements PluginInfoModule {
    @Override
    public void run(PluginInfo plugin) {
        if (plugin.getJavaPlugin() == null || plugin.getSongodaId() <= 0) {
            return;
        }

        try {
            JSONObject json = plugin.getJson();
            JSONArray files = (JSONArray) json.get("neededFiles");

            for (Object o : files) {
                JSONObject file = (JSONObject) o;

                if (file.get("type").equals("locale")) {
                    downloadLocale(plugin, (String) file.get("link"), (String) file.get("name"));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(LocaleModule.class.getName()).log(Level.INFO, "Failed to check for locale files: " + ex.getMessage());
        }
    }

    void downloadLocale(PluginInfo plugin, String link, String fileName) throws IOException {
        URL url = new URL(link);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        urlConnection.setRequestProperty("Accept", "*/*");
        urlConnection.setInstanceFollowRedirects(true);
        urlConnection.setConnectTimeout(5000);

        // do we need to follow a redirect?
        int status = urlConnection.getResponseCode();
        if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER) {
            // get redirect url from "location" header field
            String newUrl = urlConnection.getHeaderField("Location");
            // get the cookie if needed
            String cookies = urlConnection.getHeaderField("Set-Cookie");
            // open the new connnection again
            urlConnection = (HttpURLConnection) new URL(newUrl).openConnection();
            urlConnection.setRequestProperty("Cookie", cookies);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            urlConnection.setRequestProperty("Accept", "*/*");
            urlConnection.setConnectTimeout(5000);
        }

        Locale.saveLocale(plugin.getJavaPlugin(), urlConnection.getInputStream(), fileName);

        urlConnection.disconnect();
    }
}
