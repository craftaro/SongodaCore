package com.craftaro.core.utils;

import com.craftaro.core.nms.Nms;
import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.ProfileInputType;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public final class SkullItemCreator {
    private static final String STEVE_TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTYyMTcxNTMxMjI5MCwKICAicHJvZmlsZUlkIiA6ICJiNTM5NTkyMjMwY2I0MmE0OWY5YTRlYmYxNmRlOTYwYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJtYXJpYW5hZmFnIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzFhNGFmNzE4NDU1ZDRhYWI1MjhlN2E2MWY4NmZhMjVlNmEzNjlkMTc2OGRjYjEzZjdkZjMxOWE3MTNlYjgxMGIiCiAgICB9CiAgfQp9";
    private static final String ALEX_TEXTURE = "rZvLQoZsgLYaoKqEuASopYAs7IAlZlsGkwagoM8ZX38cP9kalseZrWY5OHZVfoiftdQJ+lGOzkiFfyx6kNJDTZniLrnRa8sd3X6D65ZihT1sOm/RInCwxpS1K0zGCM2h9ErkWswfwaviIf7hJtrwk8/zL0bfzDk2IgX/IBvIZpVoYTfmQsVY9jgSwORrS9ObePGIfFgmThMoZnCYWQMVpS2+yTFA2wnw9hmisQK9UWBU+iBZv55bMmkMcyEuXw1w14DaEu+/M0UGD91LU4GmJLPA9T4GCuIV8GxOcraSVIajki1cMlOBQwIaibB2NE6KAwq1Zh6NnsNYucy6qFM+136lXfBchQ1Nx4FDRZQgt8VRqTMy/OQFpr2nTbWWbRU4gRFpKC3R0518DqUH0Qm612kPWniKku/QzUUBSe1PSVljBaZCyyRx0OB1a1/8MexboKRnPXuTDnmPa9UPfuH4VO0q+qYkjV2KUzP6e5vIP5aQ6USPrMie7MmAHFJzwAMIbLjgkTVx91GWtYqg/t7qBlvrdBRLIPPsy/DSOqa+2+4hABouVCPZrBMCMLzstPPQoqZAyiCqcKb2HqWSU0h9Bhx19yoIcbHCeI3zsQs8PqIBjUL4mO6VQT4lzHy0e3M61Xsdd8S1GtsakSetTvEtMdUwCEDfBA5PRRTLOVYTY+g=";

    public static ItemStack byPlayer(Player player) {
        if (Bukkit.getOnlineMode()) {
            return XSkull.createItem().profile(new Profileable.PlayerProfileable(player)).apply();
        }

        String textureValue = Nms.getImplementations().getPlayer().getProfile(player).getTextureValue();
        if (textureValue != null) {
            return byTextureValue(textureValue);
        }

        return createDefaultSkull(player.getUniqueId());
    }

    public static ItemStack byUuid(UUID uuid) {
        return XSkull.createItem().profile(new Profileable.UUIDProfileable(uuid)).apply();
    }

    public static ItemStack byUsername(String username) {
        return XSkull.createItem().profile(new Profileable.StringProfileable(username, ProfileInputType.USERNAME)).apply();
    }

    public static ItemStack byTextureValue(String textureValue) {
        return XSkull.createItem().profile(new Profileable.StringProfileable(textureValue, ProfileInputType.BASE64)).apply();
    }

    public static ItemStack byTextureUrl(String textureUrl) {
        return XSkull.createItem().profile(new Profileable.StringProfileable(textureUrl, ProfileInputType.TEXTURE_URL)).apply();
    }

    public static ItemStack byTextureHash(String textureHash) {
        return XSkull.createItem().profile(new Profileable.StringProfileable(textureHash, ProfileInputType.TEXTURE_HASH)).apply();
    }

    private static ItemStack createDefaultSkull(UUID uuid) {
        String textureValue = STEVE_TEXTURE;
        if ((uuid.hashCode() & 1) != 0) {
            textureValue = ALEX_TEXTURE;
        }

        return XSkull.createItem().profile(new Profileable.StringProfileable(textureValue, ProfileInputType.BASE64)).apply();
    }
}
