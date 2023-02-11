package com.songoda.core.actions;

import com.songoda.core.SongodaPlugin;
import com.songoda.core.actions.impl.BroadcastAction;
import com.songoda.core.actions.impl.ConsoleCommandAction;
import com.songoda.core.actions.impl.GiveEffectAction;
import com.songoda.core.actions.impl.GiveFoodAction;
import com.songoda.core.actions.impl.GiveHealthAction;
import com.songoda.core.actions.impl.MessageAction;
import com.songoda.core.actions.impl.PlaySoundAction;
import com.songoda.core.actions.impl.PlayerCommandAction;
import com.songoda.core.actions.impl.TitleAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ActionManager {

    private final SongodaPlugin plugin;
    private final Set<GameAction> globalActions = new HashSet<>();

    public ActionManager(SongodaPlugin plugin) {
        this.plugin = plugin;
        loadGlobalActions();
    }

    private void loadGlobalActions() {
        globalActions.add(new ConsoleCommandAction());
        globalActions.add(new GiveEffectAction());
        globalActions.add(new GiveFoodAction());
        globalActions.add(new GiveHealthAction());
        globalActions.add(new MessageAction(plugin));
        globalActions.add(new PlayerCommandAction());
        globalActions.add(new PlaySoundAction(plugin));
        globalActions.add(new BroadcastAction(plugin));
        globalActions.add(new TitleAction(plugin));
    }

    public void executeAction(Player player, String args) {
        globalActions.stream().filter(action -> args.startsWith(action.getPrefix())).findAny().ifPresent(action -> {
            String text = args.replace("%player%", player.getName()).substring(action.getPrefix().length()).trim();
            text = plugin.getPlaceholderResolver().setPlaceholders(player, args);

            String[] availableSplits = substringsBetween(text, "{random:", "}");
            if (availableSplits != null) {
                for (String random : availableSplits) {
                    String[] split = random.split("-");
                    if (split.length != 2) {
                        continue;
                    }

                    int min = Integer.parseInt(split[0]);
                    int max = Integer.parseInt(split[1]);

                    text = text.replace("{random:" + random + "}", Integer.toString(ThreadLocalRandom.current().nextInt(min, max)));
                }
            }

            Map<String, String> argsMap = new HashMap<>();
            String[] keyValues = text.split(";;");
            for (String keyValue : keyValues) {
                String[] parts = keyValue.split("=", 2);
                argsMap.put(parts[0], parts[1]);
            }

            try {
                action.run(player, argsMap);
            }catch (UnsupportedOperationException e) {
                plugin.getLogger().warning("An exception has been thrown in the action " + action.getPrefix() + ": " + e.getMessage());
            }
        });
    }

    public void executeActions(Player player, List<String> args) {
        args.forEach(action -> executeAction(player, action));
    }

    // adapted from apache commons lang3
    public static String[] substringsBetween(String str, String open, String close) {
        if (str == null) {
            return null;
        }

        int strLen = str.length();
        if (strLen == 0) {
            return new String[0];
        }

        int closeLen = close.length();
        int openLen = open.length();
        List<String> list = new ArrayList<>();

        int pos = 0;
        while (pos < strLen - closeLen) {
            int start = str.indexOf(open, pos);
            if (start < 0) {

                break;
            }
            start += openLen;
            final int end = str.indexOf(close, start);
            if (end < 0) {
                break;
            }
            list.add(str.substring(start, end));
            pos = end + closeLen;
        }

        if (list.isEmpty()) {
            return null;
        }

        return list.toArray(new String[0]);
    }
}
