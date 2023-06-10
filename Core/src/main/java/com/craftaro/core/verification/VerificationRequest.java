package com.craftaro.core.verification;

import com.google.gson.JsonParser;
import com.craftaro.core.SongodaCore;
import com.craftaro.core.http.HttpClient;
import com.craftaro.core.http.HttpResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public final class VerificationRequest extends CompletableFuture<VerificationRequest.Status> {
    public static final long REQUEST_TTL_MILLIS = TimeUnit.MINUTES.toMillis(15);
    public static final long CHECK_INTERVAL_MILLIS = TimeUnit.SECONDS.toMillis(10);

    private final long requestExpiresAt = System.currentTimeMillis() + REQUEST_TTL_MILLIS;
    private final Timer taskTimer;

    VerificationRequest(HttpClient httpClient, String requestId) {
        this.taskTimer = new Timer(true);
        this.taskTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis() > VerificationRequest.this.requestExpiresAt) {
                    fulfill(Status.DENIED);
                    return;
                }

                try {
                    String urlEncodedRequestId = URLEncoder.encode(requestId, "UTF-8");
                    HttpResponse verificationStatusResponse = httpClient.get(String.format(CraftaroProductVerification.VERIFICATION_STATUS_URI, urlEncodedRequestId));
                    if (verificationStatusResponse.getResponseCode() != 200) {
                        throw new IOException("Failed to check verification status – Got Status-Code " + verificationStatusResponse.getResponseCode());
                    }

                    String verificationStatus = verificationStatusResponse.getBodyAsString();
                    String verificationState = JsonParser.parseString(verificationStatus).getAsJsonObject().get("state").getAsString();

                    if (verificationState == null) {
                        SongodaCore.getLogger().warning(SongodaCore.getPrefix() + "The Craftaro verification process timed out");
                        fulfill(Status.DENIED);
                        return;
                    }

                    Status status = Status.fromResponseValue(verificationState);
                    if (status != null && status != Status.PENDING) {
                        fulfill(status);
                    }
                } catch (IOException ex) {
                    SongodaCore.getLogger().log(Level.WARNING, SongodaCore.getPrefix() + "Failed to check verification status", ex);
                }
            }
        }, CHECK_INTERVAL_MILLIS, CHECK_INTERVAL_MILLIS);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        this.taskTimer.cancel();
        return super.cancel(mayInterruptIfRunning);
    }

    private void fulfill(Status status) {
        this.taskTimer.cancel();
        super.complete(status);
    }

    enum Status {
        PENDING("pending"),
        APPROVED("approved"),
        DENIED("denied");

        final String responseValue;

        Status(String responseValue) {
            this.responseValue = responseValue;
        }

        static Status fromResponseValue(String responseValue) {
            for (Status status : values()) {
                if (status.responseValue.equalsIgnoreCase(responseValue)) {
                    return status;
                }
            }
            return null;
        }
    }
}
