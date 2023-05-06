package com.songoda.core.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface NodeCommentable {
    void setNodeComment(@NotNull String key, @Nullable Supplier<String> comment);

    default void setNodeComment(@NotNull String key, @Nullable String comment) {
        setNodeComment(key, () -> comment);
    }

    @Nullable Supplier<String> getNodeComment(@Nullable String key);
}
