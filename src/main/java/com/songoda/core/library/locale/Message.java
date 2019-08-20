package com.songoda.ultimateclaims.utils.locale;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The Message object. This holds the message to be sent
 * as well as the plugins prefix so that they can both be
 * easily manipulated then deployed
 */
public class Message {

    private String prefix = null;
    private String message;

    /**
     * create a new message
     *
     * @param message the message text
     */
    public Message(String message) {
        this.message = message;
    }

    /**
     * Format and send the held message to a player
     *
     * @param player player to send the message to
     */
    public void sendMessage(Player player) {
        player.sendMessage(this.getMessage());
    }

    /**
     * Format and send the held message with the
     * appended plugin prefix to a player
     *
     * @param player player to send the message to
     */
    public void sendPrefixedMessage(Player player) {
        player.sendMessage(this.getPrefixedMessage());
    }

    /**
     * Format and send the held message to a player
     *
     * @param sender command sender to send the message to
     */
    public void sendMessage(CommandSender sender) {
        sender.sendMessage(this.getMessage());
    }

    /**
     * Format and send the held message to a player as a title message
     *
     * @param sender command sender to send the message to
     */
    public void sendTitle(CommandSender sender) {
        if(sender instanceof Player) {
            ((Player) sender).sendTitle("", this.getMessage(), 10, 20, 10);
        } else {
            sender.sendMessage(this.getMessage());
        }
    }

    /**
     * Format and send the held message with the
     * appended plugin prefix to a command sender
     *
     * @param sender command sender to send the message to
     */
    public void sendPrefixedMessage(CommandSender sender) {
        sender.sendMessage(this.getPrefixedMessage());
    }

    /**
     * Format the held message and append the plugins
     * prefix
     *
     * @return the prefixed message
     */
    public String getPrefixedMessage() {
        return ChatColor.translateAlternateColorCodes('&',(prefix == null ? "" : this.prefix)
                + " " +  this.message);
    }

    /**
     * Get and format the held message
     *
     * @return the message
     */
    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', this.message);
    }

    /**
     * Get the held message
     *
     * @return the message
     */
    public String getUnformattedMessage() {
        return this.message;
    }

    /**
     *  Replace the provided placeholder with the
     *  provided object
     *
     * @param placeholder the placeholder to replace
     * @param replacement the replacement object
     * @return the modified Message
     */
    public Message processPlaceholder(String placeholder, Object replacement) {
        this.message = message.replace("%" + placeholder + "%", replacement.toString());
        return this;
    }

    Message setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public String toString() {
        return this.message;
    }
}