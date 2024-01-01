package com.craftaro.core.dependency;

public class Relocation {

    private final String from;
    private final String to;

    public Relocation(String from, String to) {
        this.from = from.replaceAll(";", ".");
        this.to = to.replaceAll(";", ".");
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
