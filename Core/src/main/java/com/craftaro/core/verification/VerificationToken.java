package com.craftaro.core.verification;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.concurrent.TimeUnit;

final class VerificationToken {
    public final String accessToken;
    public final String refreshToken;
    public final long expiresAt;

    VerificationToken(String accessToken, String refreshToken, long expiresAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    String toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("access_token", this.accessToken);
        json.addProperty("refresh_token", this.refreshToken);
        json.addProperty("expiresAt", this.expiresAt);
        return json.toString();
    }

    static VerificationToken fromJson(String json) {
        JsonObject jsonObj = (JsonObject) JsonParser.parseString(json);

        long expiresAt;
        if (jsonObj.has("expiresAt")) {
            expiresAt = jsonObj.get("expiresAt").getAsLong();
        } else {
            expiresAt = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(jsonObj.get("expires_in").getAsInt());
        }

        return new VerificationToken(
                jsonObj.get("access_token").getAsString(),
                jsonObj.get("refresh_token").getAsString(),
                expiresAt
        );
    }
}
