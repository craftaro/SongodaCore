package com.craftaro.core.actions.impl;

import com.craftaro.core.actions.GameAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class ConsoleCommandAction extends GameAction {

    public ConsoleCommandAction() {
        super("[Console]");
    }

    @Override
    public void run(Player player, Map<String, String> args) {
        String command = args.get("cmd");
        if (command == null) {
            throw new UnsupportedOperationException("Command is null - check your config!");
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
