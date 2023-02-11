package com.songoda.core.actions.impl;

import com.songoda.core.actions.GameAction;
import com.songoda.core.utils.NumberUtils;
import org.bukkit.entity.Player;

import java.util.Map;

public class GiveFoodAction extends GameAction {
    public GiveFoodAction() {
        super("[GiveFood]");
    }

    @Override
    public void run(Player player, Map<String, String> args) {
        String amountString = args.get("amount");
        if (!NumberUtils.isInt(amountString)) {
            throw new UnsupportedOperationException("Cannot use text as integer - check your config!");
        }

        player.setFoodLevel(Math.max(0, Math.min(player.getFoodLevel() + Integer.parseInt(amountString), 20)));
    }
}
