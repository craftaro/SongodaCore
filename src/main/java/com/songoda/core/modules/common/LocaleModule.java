package com.songoda.core.modules.common;

import com.songoda.core.locale.Locale;
import com.songoda.core.PluginInfoModule;
import com.songoda.core.PluginInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
                    InputStream in = new URL((String) file.get("link")).openStream();
                    Locale.saveLocale(in, (String) file.get("name"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
