package com.craftaro.core.chat;

public class MiniMessagePlaceholder {
    private final String placeholder;
    private final String value;

    public MiniMessagePlaceholder(String placeholder, String value) {
        this.placeholder = "%" + placeholder + "%";
        this.value = value;
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    public String getValue() {
        return this.value;
    }
}
