package com.craftaro.core.actions.impl;

import com.craftaro.core.CraftaroPlugin;
import com.craftaro.core.actions.GameAction;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.Map;

public class MessageAction extends GameAction {

    private final CraftaroPlugin plugin;
    public MessageAction(CraftaroPlugin plugin) {
        super("[Message]");
        this.plugin = plugin;
    }

    @Override
    public void run(Player player, Map<String, String> args) {
        String unformatted = args.get("text");
        if (unformatted == null) {
            throw new UnsupportedOperationException("Text is null - check your config!");
        }

        plugin.getAdventure().player(player).sendMessage(MiniMessage.miniMessage().deserialize(unformatted));
    }
}
