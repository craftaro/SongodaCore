package com.songoda.core.verification;

public enum ProductVerificationStatus {
    VERIFIED("Verified"),
    UNVERIFIED("Unverified"),
    ACTION_NEEDED("Verification needed");

    private final String friendlyName;

    ProductVerificationStatus(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }
}
