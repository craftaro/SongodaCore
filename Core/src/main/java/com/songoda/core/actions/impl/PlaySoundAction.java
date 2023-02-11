package com.songoda.core.actions.impl;

import com.songoda.core.SongodaPlugin;
import com.songoda.core.actions.GameAction;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;

import java.util.Map;

public class PlaySoundAction extends GameAction {

    private final SongodaPlugin plugin;
    public PlaySoundAction(SongodaPlugin plugin) {
        super("[PlaySound]");
        this.plugin = plugin;
    }

    @Override
    public void run(Player player, Map<String, String> args) {
        String soundString = args.get("sound");
        if (soundString == null) {
            throw new UnsupportedOperationException("Sound is null - check your config!");
        }

        Sound.Source source = Sound.Source.valueOf(args.getOrDefault("source", "AMBIENT"));
        float volume = Float.parseFloat(args.getOrDefault("volume", "1.0"));
        float pitch = Float.parseFloat(args.getOrDefault("pitch", "1.0"));

        Sound sound = Sound.sound(Key.key(soundString), source, volume, pitch);
        plugin.getAdventure().player(player).playSound(sound);
    }
}
