package com.songoda.core.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatMessage {

    private static final Gson gson = new GsonBuilder().create();
    private List<JsonObject> textList = new ArrayList<>();

    public void clear() {
        textList.clear();
    }

    public ChatMessage fromText(String text) {
        return fromText(text, false);
    }

    public ChatMessage fromText(String text, boolean noHex) {
        Pattern pattern = Pattern.compile("(.*?)(?!&([omnlk]))(?=(&([123456789abcdefr#])|$)|#([a-f]|[A-F]|[0-9]){6})",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            ColorContainer color = null;
            String match1 = matcher.group(1);

            if (matcher.groupCount() == 0 || match1.length() == 0) continue;

            char colorChar = '-';

            if (matcher.start() != 0)
                colorChar = text.substring(matcher.start() - 1, matcher.start()).charAt(0);

            if (colorChar != '-') {
                if (colorChar == '#') {
                    color = new ColorContainer(match1.substring(0, 6), noHex);
                    match1 = match1.substring(5);
                } else if (colorChar == '&') {
                    color = new ColorContainer(ColorCode.getByChar(Character.toLowerCase(match1.charAt(0))));
                }
            }

            Pattern subPattern = Pattern.compile("(.*?)(?=&([omnlk])|$)");
            Matcher subMatcher = subPattern.matcher(match1);

            List<ColorCode> stackedCodes = new ArrayList<>();
            while (subMatcher.find()) {
                String match2 = subMatcher.group(1);
                if (match2.length() == 0) continue;

                ColorCode code = ColorCode.getByChar(Character.toLowerCase(match2.charAt(0)));

                if (code != null && code != ColorCode.RESET)
                    stackedCodes.add(code);

                if (color != null)
                    match2 = match2.substring(1);

                if (match2.length() == 0) continue;

                addMessage(match2, color, stackedCodes);
            }
        }

        return this;
    }

    public String toText() {
        return toText(false);
    }

    public String toText(boolean noHex) {
        StringBuilder text = new StringBuilder();
        for (JsonObject object : textList) {
            if (object.has("color")) {
                String color = object.get("color").getAsString();
                text.append("&");
                if (color.length() == 7) {
                    text.append(new ColorContainer(color, noHex).getColor().getCode());
                } else {
                    text.append(ColorCode.valueOf(color.toUpperCase()).getCode());
                }
            }
            for (ColorCode code : ColorCode.values()) {
                if (code.isColor()) continue;
                String c = code.name().toLowerCase();
                if (object.has(c) && object.get(c).getAsBoolean())
                    text.append("&").append(code.getCode());
            }
            text.append(object.get("text").getAsString());
        }
        return text.toString();
    }

    public ChatMessage addMessage(String s) {
        JsonObject txt = new JsonObject();
        txt.addProperty("text", s);
        textList.add(txt);
        return this;
    }

    public ChatMessage addMessage(String text, ColorContainer color) {
        return addMessage(text, color, Collections.emptyList());
    }

    public ChatMessage addMessage(String text, ColorContainer color, List<ColorCode> colorCodes) {
        JsonObject txt = new JsonObject();
        txt.addProperty("text", text);

        if (color != null)
            txt.addProperty("color", color.getHexCode() != null ? "#" + color.getHexCode() : color.getColorCode().name().toLowerCase());
        for (ColorCode code : ColorCode.values()) {
            if (!code.isColor())
                txt.addProperty(code.name().toLowerCase(), colorCodes.contains(code));
        }

        textList.add(txt);
        return this;
    }

    public ChatMessage addRunCommand(String text, String hoverText, String cmd) {
        JsonObject txt = new JsonObject();
        txt.addProperty("text", text);
        JsonObject hover = new JsonObject();
        hover.addProperty("action", "show_text");
        hover.addProperty("value", hoverText);
        txt.add("hoverEvent", hover);
        JsonObject click = new JsonObject();
        click.addProperty("action", "run_command");
        click.addProperty("value", cmd);
        txt.add("clickEvent", click);
        textList.add(txt);
        return this;
    }

    public ChatMessage addPromptCommand(String text, String hoverText, String cmd) {
        JsonObject txt = new JsonObject();
        txt.addProperty("text", text);
        JsonObject hover = new JsonObject();
        hover.addProperty("action", "show_text");
        hover.addProperty("value", hoverText);
        txt.add("hoverEvent", hover);
        JsonObject click = new JsonObject();
        click.addProperty("action", "suggest_command");
        click.addProperty("value", cmd);
        txt.add("clickEvent", click);
        textList.add(txt);
        return this;
    }

    public ChatMessage addURL(String text, String hoverText, String url) {
        JsonObject txt = new JsonObject();
        txt.addProperty("text", text);
        JsonObject hover = new JsonObject();
        hover.addProperty("action", "show_text");
        hover.addProperty("value", hoverText);
        txt.add("hoverEvent", hover);
        JsonObject click = new JsonObject();
        click.addProperty("action", "open_url");
        click.addProperty("value", url);
        txt.add("clickEvent", hover);
        textList.add(txt);
        return this;
    }

    @Override
    public String toString() {
        return gson.toJson(textList);
    }

    public void sendTo(CommandSender sender) {
        sendTo(null, sender);
    }

    public void sendTo(ChatMessage prefix, CommandSender sender) {
        if (sender instanceof Player && enabled) {
            try {
                List<JsonObject> textList = prefix == null ? new ArrayList<>() : new ArrayList<>(prefix.textList);
                textList.addAll(this.textList);

                Object packet;
                if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_16)) {
                    packet = mc_PacketPlayOutChat_new.newInstance(mc_IChatBaseComponent_ChatSerializer_a.invoke(null, gson.toJson(textList)), mc_chatMessageType_Chat.get(null), ((Player) sender).getUniqueId());
                } else {
                    packet = mc_PacketPlayOutChat_new.newInstance(mc_IChatBaseComponent_ChatSerializer_a.invoke(null, gson.toJson(textList)));
                }
                Object cbPlayer = cb_craftPlayer_getHandle.invoke(sender);
                Object mcConnection = mc_entityPlayer_playerConnection.get(cbPlayer);
                mc_playerConnection_sendPacket.invoke(mcConnection, packet);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Bukkit.getLogger().log(Level.WARNING, "Problem preparing raw chat packets (disabling further packets)", ex);
                enabled = false;
            }
        } else {
            sender.sendMessage(TextUtils.formatText((prefix == null ? "" : prefix.toText(true) + " ") + toText(true)));
        }
    }

    private static boolean enabled = ServerVersion.isServerVersionAtLeast(ServerVersion.V1_8);

    private static Class<?> mc_ChatMessageType;
    private static Method mc_IChatBaseComponent_ChatSerializer_a, cb_craftPlayer_getHandle, mc_playerConnection_sendPacket;
    private static Constructor mc_PacketPlayOutChat_new;
    private static Field mc_entityPlayer_playerConnection, mc_chatMessageType_Chat;

    static {
        init();
    }

    static void init() {
        if (enabled) {
            try {

                final String version = ServerVersion.getServerVersionString();
                Class<?> cb_craftPlayerClazz, mc_entityPlayerClazz, mc_playerConnectionClazz, mc_PacketInterface,
                        mc_IChatBaseComponent, mc_IChatBaseComponent_ChatSerializer, mc_PacketPlayOutChat;

                cb_craftPlayerClazz = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
                cb_craftPlayer_getHandle = cb_craftPlayerClazz.getDeclaredMethod("getHandle");
                mc_entityPlayerClazz = Class.forName("net.minecraft.server." + version + ".EntityPlayer");
                mc_entityPlayer_playerConnection = mc_entityPlayerClazz.getDeclaredField("playerConnection");
                mc_playerConnectionClazz = Class.forName("net.minecraft.server." + version + ".PlayerConnection");
                mc_PacketInterface = Class.forName("net.minecraft.server." + version + ".Packet");
                mc_playerConnection_sendPacket = mc_playerConnectionClazz.getDeclaredMethod("sendPacket", mc_PacketInterface);
                mc_IChatBaseComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
                mc_IChatBaseComponent_ChatSerializer = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent$ChatSerializer");
                mc_IChatBaseComponent_ChatSerializer_a = mc_IChatBaseComponent_ChatSerializer.getMethod("a", String.class);
                mc_PacketPlayOutChat = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");

                if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_16)) {
                    mc_ChatMessageType = Class.forName("net.minecraft.server." + version + ".ChatMessageType");
                    mc_chatMessageType_Chat = mc_ChatMessageType.getField("CHAT");
                    mc_PacketPlayOutChat_new = mc_PacketPlayOutChat.getConstructor(mc_IChatBaseComponent, mc_ChatMessageType, UUID.class);
                } else {
                    mc_PacketPlayOutChat_new = mc_PacketPlayOutChat.getConstructor(mc_IChatBaseComponent);
                }
            } catch (Throwable ex) {
                Bukkit.getLogger().log(Level.WARNING, "Problem preparing raw chat packets (disabling further packets)", ex);
                enabled = false;
            }
        }
    }

    public ChatMessage replaceAll(String toReplace, String replaceWith) {
        for (JsonObject object : textList) {
            String text = object.get("text").getAsString()
                    .replaceAll(toReplace, replaceWith);
            object.remove("text");
            object.addProperty("text", text);
        }
        return this;
    }
}
