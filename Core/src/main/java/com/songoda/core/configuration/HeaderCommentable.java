package com.songoda.core.configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface HeaderCommentable {
    void setHeaderComment(@Nullable Supplier<String> comment);

    default void setHeaderComment(@Nullable String comment) {
        setHeaderComment(() -> comment);
    }

    @Nullable Supplier<String> getHeaderComment();

    @NotNull String generateHeaderCommentLines();
}
