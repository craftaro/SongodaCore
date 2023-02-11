package com.songoda.core.actions.impl;

import com.songoda.core.actions.GameAction;
import org.bukkit.entity.Player;

import java.util.Map;

public class PlayerCommandAction extends GameAction {

    public PlayerCommandAction() {
        super("[Sudo]");
    }

    @Override
    public void run(Player player, Map<String, String> args) {
        String command = args.get("cmd");
        if (command == null) {
            throw new UnsupportedOperationException("Command is null - check your config!");
        }

        player.performCommand(command);
    }
}
