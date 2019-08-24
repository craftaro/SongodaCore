package com.songoda.core.library.settings;

public class Section {

    private final String key;
    private final Narrow narrow = new Narrow();

    public Section(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Narrow getNarrow() {
        return narrow;
    }

    public Setting narrow(String setting) {
        return narrow.getSetting(key + "." + setting);
    }
}
