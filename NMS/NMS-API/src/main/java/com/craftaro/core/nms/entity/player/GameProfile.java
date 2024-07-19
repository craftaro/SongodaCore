package com.craftaro.core.nms.entity.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class GameProfile {
    private final Object mojangGameProfile;

    private final UUID id;
    private final String name;
    private final String textureValue;
    private final String textureSignature;

    public GameProfile(
            Object mojangGameProfile,

            UUID id,
            String name,
            @Nullable String textureValue,
            @Nullable String textureSignature
    ) {
        this.mojangGameProfile = Objects.requireNonNull(mojangGameProfile);

        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.textureValue = textureValue;
        this.textureSignature = textureSignature;
    }

    public Object getMojangGameProfile() {
        return this.mojangGameProfile;
    }

    public @NotNull UUID getId() {
        return this.id;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @Nullable String getTextureValue() {
        return this.textureValue;
    }

    public @Nullable String getTextureSignature() {
        return this.textureSignature;
    }
}
