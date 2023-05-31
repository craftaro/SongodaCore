package com.songoda.core.utils;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.UUID;

@Deprecated
public class SongodaAuth {
    @Deprecated
    public static boolean isAuthorized(boolean allowOffline) {
        String productId = "%%__PLUGIN__%%";
        if (isPluginSelfCompiled(productId)) {
            return true;
        }

        UUID serverUuid = getUUID();
        try {
            URL url = new URL("https://marketplace.songoda.com/api/v2/products/license/validate");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String requestBodyJson = "{\"product_id\":" + productId + ",\"license\":\"" + serverUuid + "\",\"user_id\":\"%%__USER__%%\"}";
            try (OutputStream os = con.getOutputStream()) {
                byte[] requestBody = requestBodyJson.getBytes(StandardCharsets.UTF_8);
                os.write(requestBody, 0, requestBody.length);
            }

            JSONObject jsonResponse = readHttpResponseJson(con);
            if (jsonResponse.containsKey("error")) {
                Bukkit.getLogger().warning("Error validating license: " + jsonResponse.get("error"));
                return false;
            }

            return (boolean) jsonResponse.get("valid");
        } catch (Exception ex) {
            return allowOffline;
        }
    }

    @Deprecated
    public static UUID getUUID() {
        File serverProperties = new File("./server.properties");
        try {
            Properties prop = new Properties();
            prop.load(new FileReader(serverProperties));

            String uuid = prop.getProperty("uuid");
            if (uuid != null && !uuid.isEmpty()) {
                return UUID.fromString(uuid);
            }

            UUID newUUID = UUID.randomUUID();
            prop.setProperty("uuid", newUUID.toString());
            prop.store(new FileWriter(serverProperties), null);
            return newUUID;
        } catch (Exception ex) {
            throw new RuntimeException("Could not determine UUID for server", ex);
        }
    }

    @Deprecated
    public static String getIP() {
        try {
            URL url = new URL("https://marketplace.songoda.com/api/v2/products/license/ip");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            JSONObject jsonResponse = readHttpResponseJson(con);
            return jsonResponse.get("ip").toString();
        } catch (Exception ex) {
            throw new RuntimeException("Could not fetch IP address", ex);
        }
    }

    private static JSONObject readHttpResponseJson(HttpURLConnection con) throws IOException, ParseException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();

            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return (JSONObject) new JSONParser().parse(response.toString());
        }
    }

    private static boolean isPluginSelfCompiled(String productId) {
        try {
            Integer.parseInt(productId);
            return false;
        } catch (NumberFormatException ignore) {
        }

        return true;
    }
}
