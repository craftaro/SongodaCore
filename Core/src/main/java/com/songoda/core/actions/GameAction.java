package com.songoda.core.actions;

import org.bukkit.entity.Player;

import java.util.Map;

public abstract class GameAction {

    private final String prefix;
    public GameAction(String prefix) {
        this.prefix = prefix;
    }

    public abstract void run(Player player, Map<String, String> args) throws UnsupportedOperationException;

    public String getPrefix() {
        return prefix;
    }
}
