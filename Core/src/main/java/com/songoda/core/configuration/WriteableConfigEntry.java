package com.songoda.core.configuration;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface WriteableConfigEntry extends ConfigEntry {
    @Override
    default void set(@Nullable Object value) {
        getConfig().set(getKey(), value);
    }

    @Override
    default ConfigEntry withComment(Supplier<String> comment) {
        ((NodeCommentable) getConfig()).setNodeComment(getKey(), comment);

        return this;
    }
}
