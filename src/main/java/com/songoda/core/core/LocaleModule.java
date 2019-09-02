package com.songoda.core.core;

import com.songoda.core.core.PluginInfoModule;
import com.songoda.core.locale.Locale;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LocaleModule implements PluginInfoModule {

    @Override
    public void run(PluginInfo plugin) {
        if(plugin.getJavaPlugin() == null || plugin.getSongodaId() <= 0) return;
        JSONObject json = plugin.getJson();
        try {
            JSONArray files = (JSONArray) json.get("neededFiles");
            for (Object o : files) {
                JSONObject file = (JSONObject) o;

                if (file.get("type").equals("locale")) {
                    downloadLocale(plugin, (String) file.get("link"), (String) file.get("name"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void downloadLocale(PluginInfo plugin, String link, String fileName) throws MalformedURLException, IOException {
        URL url = new URL(link);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        urlConnection.setRequestProperty("Accept", "*/*");
        urlConnection.setConnectTimeout(5000);

        Locale.saveLocale(plugin.getJavaPlugin(), urlConnection.getInputStream(), fileName);

        urlConnection.disconnect();
    }
}
