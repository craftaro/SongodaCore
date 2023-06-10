package com.craftaro.core.verification;

import org.bukkit.ChatColor;

public enum ProductVerificationStatus {
    VERIFIED("Verified", ChatColor.GREEN),
    UNVERIFIED("Unverified", ChatColor.RED),
    ACTION_NEEDED("Verification needed", ChatColor.YELLOW);

    private final String friendlyName;
    private final ChatColor chatColor;

    ProductVerificationStatus(String friendlyName, ChatColor chatColor) {
        this.friendlyName = friendlyName;
        this.chatColor = chatColor;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }

    public String getColoredFriendlyName() {
        return this.chatColor + this.friendlyName;
    }
}
