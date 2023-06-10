package com.craftaro.core.verification;

import java.util.concurrent.CompletableFuture;

public class AsyncTokenAcquisitionFlow {
    private final String uri;
    private final CompletableFuture<Boolean> verificationResult;

    public AsyncTokenAcquisitionFlow(String uri, CompletableFuture<Boolean> verificationResult) {
        this.uri = uri;
        this.verificationResult = verificationResult;
    }

    public String getUriForTheUserToVisit() {
        return this.uri;
    }

    public CompletableFuture<Boolean> getResultFuture() {
        return this.verificationResult;
    }
}
