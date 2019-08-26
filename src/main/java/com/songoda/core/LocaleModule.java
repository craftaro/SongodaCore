package com.songoda.core;

import com.songoda.core.locale.Locale;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LocaleModule implements PluginInfoModule {

    @Override
    public void run(PluginInfo plugin) {
        JSONObject json = plugin.getJson();
        try {
            JSONArray files = (JSONArray) json.get("neededFiles");
            for (Object o : files) {
                JSONObject file = (JSONObject) o;

                if (file.get("type").equals("locale")) {
                    downloadLocale((String) file.get("link"), (String) file.get("name"));
//                    InputStream in = new URL((String) file.get("link")).openStream();
//                    Locale.saveLocale(in, (String) file.get("name"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void downloadLocale(String link, String fileName) throws MalformedURLException, IOException {
        URL url = new URL(link);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        urlConnection.setRequestProperty("Accept", "*/*");
        urlConnection.setConnectTimeout(5000);

        Locale.saveLocale(urlConnection.getInputStream(), fileName);

        urlConnection.disconnect();
    }
}
