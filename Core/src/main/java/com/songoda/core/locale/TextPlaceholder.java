package com.songoda.core.locale;

public class TextPlaceholder {

    private final String placeholder;
    private final String replacement;

    public TextPlaceholder(String placeholder, String replacement) {
        this.placeholder = placeholder;
        this.replacement = replacement;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public String getReplacement() {
        return replacement;
    }
}
