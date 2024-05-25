package com.craftaro.core.chat;

import com.craftaro.core.SongodaCore;
import com.craftaro.core.SongodaPlugin;
import com.craftaro.core.compatibility.ClassMapping;
import com.craftaro.core.compatibility.ServerProject;
import com.craftaro.core.compatibility.ServerVersion;
import com.craftaro.core.nms.Nms;
import com.craftaro.core.utils.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.IRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatMessage {
    private static final Gson GSON = new GsonBuilder().create();
    private final List<JsonObject> textList = new ArrayList<>();

    public void clear() {
        this.textList.clear();
    }

    public ChatMessage fromText(String text) {
        return fromText(text, false);
    }

    public ChatMessage fromText(String text, boolean noHex) {
        Pattern pattern = Pattern.compile(
                "(.*?)(?!&([omnlk]))(?=(&([123456789abcdefr#])|$)|#([a-f]|[A-F]|[0-9]){6})",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            ColorContainer color = null;
            String match1 = matcher.group(1);

            if (matcher.groupCount() == 0 || match1.isEmpty()) {
                continue;
            }

            char colorChar = '-';

            if (matcher.start() != 0) {
                colorChar = text.substring(matcher.start() - 1, matcher.start()).charAt(0);
            }

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
                if (match2.isEmpty()) {
                    continue;
                }

                ColorCode code = ColorCode.getByChar(Character.toLowerCase(match2.charAt(0)));

                if (code != null && code != ColorCode.RESET) {
                    stackedCodes.add(code);
                }

                if (color != null) {
                    match2 = match2.substring(1);
                }

                if (!match2.isEmpty()) {
                    addMessage(match2, color, stackedCodes);
                }
            }
        }

        return this;
    }

    public String toText() {
        return toText(false);
    }

    public String toText(boolean noHex) {
        StringBuilder text = new StringBuilder();

        for (JsonObject object : this.textList) {
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
                if (code.isColor()) {
                    continue;
                }

                String c = code.name().toLowerCase();
                if (object.has(c) && object.get(c).getAsBoolean()) {
                    text.append("&").append(code.getCode());
                }
            }

            text.append(object.get("text").getAsString());
        }

        return text.toString();
    }

    public ChatMessage addMessage(String s) {
        JsonObject txt = new JsonObject();
        txt.addProperty("text", s);

        this.textList.add(txt);

        return this;
    }

    public ChatMessage addMessage(String text, ColorContainer color) {
        return addMessage(text, color, Collections.emptyList());
    }

    public ChatMessage addMessage(String text, ColorContainer color, List<ColorCode> colorCodes) {
        JsonObject txt = new JsonObject();
        txt.addProperty("text", text);

        if (color != null) {
            txt.addProperty("color", color.getHexCode() != null ? "#" + color.getHexCode() : color.getColorCode().name().toLowerCase());
        }

        for (ColorCode code : ColorCode.values()) {
            if (!code.isColor()) {
                txt.addProperty(code.name().toLowerCase(), colorCodes.contains(code));
            }
        }

        this.textList.add(txt);
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

        this.textList.add(txt);
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

        this.textList.add(txt);
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

        this.textList.add(txt);
        return this;
    }

    @Override
    public String toString() {
        return GSON.toJson(this.textList);
    }

    public void sendTo(CommandSender sender) {
        sendTo(null, sender);
    }

    public void sendTo(ChatMessage prefix, CommandSender sender) {
        AdventureUtils.sendMessage(SongodaCore.getHijackedPlugin(), AdventureUtils.formatComponent(prefix == null ? "" : prefix.toText() + toText()), sender);
    }

    public ChatMessage replaceAll(String toReplace, String replaceWith) {
        for (JsonObject object : this.textList) {
            String text = object.get("text").getAsString().replaceAll(toReplace, replaceWith);

            object.remove("text");
            object.addProperty("text", text);
        }

        return this;
    }
}
