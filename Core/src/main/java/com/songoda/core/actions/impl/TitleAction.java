package com.songoda.core.actions.impl;

import com.songoda.core.SongodaPlugin;
import com.songoda.core.actions.GameAction;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Map;

public class TitleAction extends GameAction {

    private final SongodaPlugin plugin;
    public TitleAction(SongodaPlugin plugin) {
        super("[Title]");
        this.plugin = plugin;
    }

    @Override
    public void run(Player player, Map<String, String> args) {
        String titleString = args.get("title");
        if (titleString == null) {
            throw new UnsupportedOperationException("Title is null - check your config!");
        }

        String subtitle = args.getOrDefault("subtitle", "");
        int fadeIn = Integer.parseInt(args.getOrDefault("fadeIn", "20"));
        int stay = Integer.parseInt(args.getOrDefault("stay", "60"));
        int fadeOut = Integer.parseInt(args.getOrDefault("fadeOut", "20"));

        Title title = Title.title(MiniMessage.miniMessage().deserialize(titleString), MiniMessage.miniMessage().deserialize(subtitle),
                Title.Times.times(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut)));

        plugin.getAdventure().player(player).showTitle(title);
    }
}
