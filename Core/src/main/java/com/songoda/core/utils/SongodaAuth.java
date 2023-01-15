package com.songoda.core.utils;

import com.songoda.core.commands.AbstractCommand;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Properties;
import java.util.UUID;

public class SongodaAuth {

    public static boolean isAuthorized(boolean allowOffline) {
        String productId = "%%__PLUGIN__%%";
        try {
            Integer.parseInt(productId);
        } catch (NumberFormatException e) {
            //Self compiled, return true
            return true;
        }
        UUID uuid = getUUID();
        try {
            URL url = new URL("https://marketplace.songoda.com/api/v2/products/license/validate");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            String jsonInputString = "{\"product_id\":" + productId + ",\"license\":\"" + uuid + "\",\"user_id\":\"%%__USER__%%\"}";
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.toString());
                if (jsonObject.get("error") != null) {
                    //Got an error, return false and print error
                    Bukkit.getLogger().warning("Error validating license: " + jsonObject.get("error"));
                    return false;
                } else {
                    return (boolean) jsonObject.get("valid");
                }
            }
        } catch (Exception e) {
            return allowOffline;
        }
    }

    public static UUID getUUID() {
        File serverProperties = new File(new File("."),"server.properties");
        Properties prop = new Properties();
        try {
            prop.load(new FileReader(serverProperties));
            String uuid = prop.getProperty("uuid");
            if (uuid == null || uuid.isEmpty()) {
                UUID newUUID = UUID.randomUUID();
                prop.setProperty("uuid", newUUID.toString());
                prop.store(new FileWriter(serverProperties), null);
                return newUUID;
            } else {
                return UUID.fromString(uuid);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Could not fetch UUID for server", ex);
        }
    }

    public static String getIP() {
        try {
            URL url = new URL("https://marketplace.songoda.com/api/v2/products/license/ip");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(response.toString());
                return jsonObject.get("ip").toString();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Could not fetch IP address", ex);
        }
    }
}
