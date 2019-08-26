package com.songoda.core.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerUtils {

    static Random random = new Random();

    /**
     * Get a list of all of the players that this player can "see"
     *
     * @param sender user to check against, or null for all players
     * @param startingWith optional query to test: only players whose game names
     * start with this
     * @return list of player names that are "visible" to the player
     */
    public static List<String> getVisiblePlayerNames(CommandSender sender, String startingWith) {
        Player player = sender instanceof Player ? (Player) sender : null;
        final String startsWith = startingWith == null || startingWith.isEmpty() ? null : startingWith.toLowerCase();
        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> p != player)
                .filter(p -> startsWith == null || p.getName().toLowerCase().startsWith(startsWith))
                .filter(p -> player == null || (player.canSee(p) && p.getMetadata("vanished").isEmpty()))
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    /**
     * Get a list of all of the players that this player can "see"
     *
     * @param sender user to check against, or null for all players
     * @param startingWith optional query to test: only players whose game names
     * start with this
     * @return list of player names that are "visible" to the player
     */
    public static List<String> getVisiblePlayerDisplayNames(CommandSender sender, String startingWith) {
        Player player = sender instanceof Player ? (Player) sender : null;
        final String startsWith = startingWith == null || startingWith.isEmpty() ? null : startingWith.replaceAll("[^a-zA-Z]", "").toLowerCase();
        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> p != player)
                .filter(p -> startsWith == null || p.getDisplayName().replaceAll("[^a-zA-Z]", "").toLowerCase().startsWith(startsWith))
                .filter(p -> player == null || (player.canSee(p) && p.getMetadata("vanished").isEmpty()))
                .map(Player::getDisplayName)
                .collect(Collectors.toList());
    }

    /**
     * Get a list of all of the players that this player can "see"
     *
     * @param sender user to check against, or null for all players
     * @param startingWith optional query to test: only players whose game names
     * start with this
     * @return list of players that are "visible" to the player
     */
    public static List<Player> getVisiblePlayers(CommandSender sender, String startingWith) {
        Player player = sender instanceof Player ? (Player) sender : null;
        final String startsWith = startingWith == null || startingWith.isEmpty() ? null : startingWith.toLowerCase();
        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> p != player)
                .filter(p -> startsWith == null || p.getName().toLowerCase().startsWith(startsWith))
                .filter(p -> player == null || (player.canSee(p) && p.getMetadata("vanished").isEmpty()))
                .map(p -> (Player) p)
                .collect(Collectors.toList());
    }

    /**
     * Get a list of all online player names that start with a string.
     * 
     * @param us Ourselves / who is requesting the list. Will not return this player.
     * @param startsWith All names returned must start with this input string
     * @return List of matching player IGN
     */
    public static List<String> getAllPlayers(CommandSender us, String startsWith) {
        final String arg = startsWith.toLowerCase();
        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> us != p && p.getName().startsWith(arg))
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    /**
     * Get a list of all online player names that start with a string.
     * 
     * @param us Ourselves / who is requesting the list. Will not return this player.
     * @param startsWith All names returned must start with this input string
     * @return List of matching player display names
     */
    public static List<String> getAllPlayersDisplay(CommandSender us, String startsWith) {
        final String arg = startsWith.replaceAll("[^a-zA-Z]", "").toLowerCase();
        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> us != p && p.getDisplayName().replaceAll("[^a-zA-Z]", "").startsWith(arg))
                .map(Player::getDisplayName)
                .collect(Collectors.toList());
    }

    /**
     * Search for and grab the closest match for a provided player name. <br />
     * Also checks player display names if there is not an exact match.
     *
     * @param player player to search for
     * @return Player that closest matches the input name, or null if none found
     */
    public static Player findPlayer(String player) {
        Player found = Bukkit.getServer().getPlayer(player);
        if (found == null) {
            final String searchName = player.toLowerCase();
            final String searchDisplayName = player.replaceAll("[^a-zA-Z]", "").toLowerCase();
            int d = 999;
            for (Player p2 : Bukkit.getOnlinePlayers()) {
                final String test;
                if (p2.getName().toLowerCase().startsWith(searchName)) {
                    int d2 = p2.getName().length() - searchName.length();
                    if (d2 < d) {
                        found = p2;
                        d = d2;
                    } else if (d2 == d) {
                        found = null;
                    }
                } else if ((test = p2.getDisplayName().replaceAll("[^a-zA-Z]", "")).toLowerCase().startsWith(searchDisplayName)) {
                    int d2 = test.length() - searchDisplayName.length();
                    if (d2 < d) {
                        found = p2;
                        d = d2;
                    } else if (d2 == d) {
                        found = null;
                    }
                }
            }
        }
        return found;
    }

    public static Player getRandomPlayer() {
        final Collection<? extends Player> all = Bukkit.getOnlinePlayers();
        final Iterator<? extends Player> alli = all.iterator();
        int pick = random.nextInt(all.size());
        for (; pick > 0; --pick) {
            alli.next();
        }
        return alli.hasNext() ? alli.next() : null;
    }
}
