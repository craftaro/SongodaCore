package com.craftaro.core.verification;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.craftaro.core.SongodaCore;
import com.craftaro.core.http.HttpClient;
import com.craftaro.core.http.HttpResponse;
import com.craftaro.core.http.SimpleHttpClient;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public final class CraftaroProductVerification {
    private static final String TOKEN_URI = "https://craftaro.com/api/v1/verification/token";
    private static final String TOKEN_REFRESH_URI = "https://craftaro.com/api/v1/verification/refresh";
    private static final String VERIFICATION_START_URI = "https://craftaro.com/api/v1/verification/uri";
    static final String VERIFICATION_STATUS_URI = "https://craftaro.com/api/v1/verification/state?request_id=%s";
    private static final String PRODUCT_ACCESS_URI = "https://craftaro.com/api/v1/verification/access?product_id=%d";

    private static final HttpClient httpClient = new SimpleHttpClient();

    private static @Nullable VerificationRequest verificationRequest;

    public static ProductVerificationStatus getOwnProductVerificationStatus() {
        final int productId = getProductId();
        if (productId <= 0) {
            return ProductVerificationStatus.VERIFIED;
        }

        try {
            return getProductVerificationStatus(productId);
        } catch (IOException ex) {
            SongodaCore.getLogger().log(Level.WARNING, "Failed to fetch product verification status", ex);
            return ProductVerificationStatus.VERIFIED;
        }
    }

    public static ProductVerificationStatus getProductVerificationStatus(int productId) throws IOException {
        VerificationToken token = tryLoadExistingToken();
        if (tokenNeedsRefresh(token)) {
            if (token != null) {
                try {
                    token = refreshVerificationToken(token);
                } catch (IOException ex) {
                    SongodaCore.getLogger().log(Level.WARNING, "Failed to refresh verification token", ex);
                    return ProductVerificationStatus.VERIFIED;
                }
            }

            if (token == null) {
                return ProductVerificationStatus.ACTION_NEEDED;
            }
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token.accessToken);
        headers.put("Accept", "application/json");

        HttpResponse productAccessResponse = httpClient.post(String.format(PRODUCT_ACCESS_URI, productId), headers, null);
        if (productAccessResponse.getResponseCode() == 404) {
            SongodaCore.getLogger().warning("The product could not be found on Craftaro!");
            return ProductVerificationStatus.VERIFIED;
        }
        if (productAccessResponse.getResponseCode() != 200) {
            throw new IOException("Failed to check product access – Got {code=" + productAccessResponse.getResponseCode() + ",body=" + productAccessResponse.getBodyAsString() + "}");
        }

        String productAccessResponseJson = productAccessResponse.getBodyAsString();
        JsonObject productAccess = JsonParser.parseString(productAccessResponseJson).getAsJsonObject();

        if (productAccess.get("has_access").getAsBoolean()) {
            return ProductVerificationStatus.VERIFIED;
        } else {
            return ProductVerificationStatus.UNVERIFIED;
        }
    }

    public static AsyncTokenAcquisitionFlow startAsyncTokenAcquisitionFlow() throws IOException {
        tryDeleteTokenFile();

        if (verificationRequest != null) {
            verificationRequest.cancel(true);
            verificationRequest = null;
        }

        HttpResponse verificationStartResponse = httpClient.get(VERIFICATION_START_URI);
        if (verificationStartResponse.getResponseCode() != 200) {
            throw new IOException("Failed to start verification process – Got {code=" + verificationStartResponse.getResponseCode() + ",body=" + verificationStartResponse.getBodyAsString() + "}");
        }

        String verificationStartResponseJson = verificationStartResponse.getBodyAsString();
        JsonObject verificationStart = JsonParser.parseString(verificationStartResponseJson).getAsJsonObject();

        String uri = verificationStart.get("uri").getAsString();
        String requestId = verificationStart.get("request_id").getAsString();
        String code = verificationStart.get("code").getAsString();

        CompletableFuture<Boolean> asyncTokenRefreshWorkflowFuture = new CompletableFuture<>();

        verificationRequest = new VerificationRequest(httpClient, requestId);
        verificationRequest
                .whenComplete((status, ex) -> {
                    if (ex != null) {
                        SongodaCore.getLogger().log(Level.WARNING, SongodaCore.getPrefix() + "Failed to complete verification request", ex);
                        asyncTokenRefreshWorkflowFuture.completeExceptionally(ex);
                        return;
                    }

                    if (status != VerificationRequest.Status.APPROVED) {
                        SongodaCore.getLogger().warning(SongodaCore.getPrefix() + "Craftaro Product Verification request was denied!");
                        asyncTokenRefreshWorkflowFuture.complete(false);
                        return;
                    }

                    try {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Accept", "application/json");

                        JsonObject reqBody = new JsonObject();
                        reqBody.addProperty("request_id", requestId);
                        reqBody.addProperty("code", code);

                        HttpResponse tokenResponse = httpClient.post(TOKEN_URI, headers, reqBody.toString().getBytes(StandardCharsets.UTF_8));
                        if (tokenResponse.getResponseCode() != 200) {
                            throw new IOException("Failed to get verification token – Got {code=" + tokenResponse.getResponseCode() + ",body=" + tokenResponse.getBodyAsString() + "}");
                        }

                        VerificationToken token = VerificationToken.fromJson(tokenResponse.getBodyAsString());
                        VerificationTokenFileManager.saveVerificationToken(token);

                        SongodaCore.getLogger().info(SongodaCore.getPrefix() + "Craftaro Product Verification request was approved!");
                        SongodaCore.getLogger().info(SongodaCore.getPrefix() + "Please restart your server to complete the verification process.");
                        asyncTokenRefreshWorkflowFuture.complete(true);
                    } catch (IOException ioException) {
                        SongodaCore.getLogger().log(Level.WARNING, SongodaCore.getPrefix() + "Failed to save verification token file", ioException);
                        asyncTokenRefreshWorkflowFuture.completeExceptionally(ioException);
                    }
                });

        return new AsyncTokenAcquisitionFlow(uri, asyncTokenRefreshWorkflowFuture);
    }

    public static int getProductId() {
        final String productId = "%%__PRODUCT_ID__%%";
        if (!productId.matches("[0-9]+")) {
            return -1;
        }

        return Integer.parseInt(productId);
    }

    private static @Nullable VerificationToken refreshVerificationToken(VerificationToken token) throws IOException {
        JsonObject reqBody = new JsonObject();
        reqBody.addProperty("access_token", token.accessToken);
        reqBody.addProperty("refresh_token", token.refreshToken);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        HttpResponse response = httpClient.post(TOKEN_REFRESH_URI, headers, reqBody.toString().getBytes(StandardCharsets.UTF_8));
        int statusCode = response.getResponseCode();

        if (statusCode >= 400 && statusCode < 500) {
            return null;
        }
        if (statusCode != 200) {
            throw new IOException("Failed to refresh verification token – Got {code=" + statusCode + ",body=" + response.getBodyAsString() + "}");
        }

        return VerificationToken.fromJson(response.getBodyAsString());
    }

    private static @Nullable VerificationToken tryLoadExistingToken() {
        try {
            return VerificationTokenFileManager.loadVerificationToken();
        } catch (IOException ex) {
            SongodaCore.getLogger().log(Level.WARNING, "Failed to load verification token file", ex);
            return null;
        }
    }

    private static void tryDeleteTokenFile() {
        try {
            VerificationTokenFileManager.deleteVerificationTokenFile();
        } catch (IOException ex) {
            SongodaCore.getLogger().log(Level.WARNING, "Failed to delete verification token file", ex);
        }
    }

    private static boolean tokenNeedsRefresh(@Nullable VerificationToken token) {
        if (token == null) {
            return true;
        }

        return System.currentTimeMillis() >= (token.expiresAt - TimeUnit.DAYS.toSeconds(3));
    }
}
