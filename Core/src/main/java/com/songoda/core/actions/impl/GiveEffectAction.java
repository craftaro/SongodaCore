package com.songoda.core.actions.impl;

import com.songoda.core.actions.GameAction;
import com.songoda.core.utils.NumberUtils;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class GiveEffectAction extends GameAction {
    public GiveEffectAction() {
        super("[GiveEffect]");
    }

    @Override
    public void run(Player player, Map<String, String> args) {
        String typeString = args.get("type");
        if (typeString == null) {
            throw new UnsupportedOperationException("Potion effect type is null - check your config!");
        }

        PotionEffectType type = PotionEffectType.getByName(typeString);
        if (type == null) {
            throw new UnsupportedOperationException("Potion effect type is invalid - check your config!");
        }

        String strengthString = args.get("strength");
        String durationString = args.get("duration");
        if (!NumberUtils.isInt(strengthString) || !NumberUtils.isInt(durationString)) {
            throw new UnsupportedOperationException("Cannot use text for integer values - check your config!");
        }

        int strength = Integer.parseInt(strengthString);
        int duration = Integer.parseInt(durationString);

        player.addPotionEffect(new PotionEffect(type, duration * 20, strength));
    }
}
