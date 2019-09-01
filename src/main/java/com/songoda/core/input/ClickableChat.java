package com.songoda.core.input;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.songoda.core.compatibility.ServerVersion;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Send chat packets with embedded links
 *
 * @since 2019-09-01
 * @author jascotty2
 */
public class ClickableChat {

    private static final Gson gson = new GsonBuilder().create();
    List<JsonObject> textList = new ArrayList();

    public void clear() {
        textList.clear();
    }

    public ClickableChat addMessage(String s) {
        JsonObject txt = new JsonObject();
        txt.addProperty("text", s);
        textList.add(txt);
        return this;
    }

    public ClickableChat addRunCommand(String text, String hoverText, String cmd) {
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

    public ClickableChat addPromptCommand(String text, String hoverText, String cmd) {
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

    public ClickableChat addURL(String text, String hoverText, String url) {
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

    public void sendTo(Player p) {
        if (enabled) {
            try {
                Object packet = mc_PacketPlayOutChat_new.newInstance(mc_IChatBaseComponent_ChatSerializer_a.invoke(null, this.toString()));
                Object cbPlayer = cb_craftPlayer_getHandle.invoke(p);
                Object mcConnection = mc_entityPlayer_playerConnection.get(cbPlayer);
                mc_playerConnection_sendPacket.invoke(mcConnection, packet);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Bukkit.getLogger().log(Level.WARNING, "Problem preparing raw chat packets (disabling further packets)", ex);
                enabled = false;
            }
        }
    }

    private static boolean enabled = ServerVersion.isServerVersionAtLeast(ServerVersion.V1_8);

    private static Method mc_IChatBaseComponent_ChatSerializer_a;
    private static Constructor mc_PacketPlayOutChat_new;
    private static Method cb_craftPlayer_getHandle;
    private static Field mc_entityPlayer_playerConnection;
    private static Method mc_playerConnection_sendPacket;

    static {
        init();
    }

    static void init() {
        if (enabled) {
            try {

                final String version = ServerVersion.getServerVersionString();
                Class cb_craftPlayerClazz;
                Class mc_entityPlayerClazz;
                Class mc_playerConnectionClazz;
                Class mc_PacketInterface;
                Class mc_IChatBaseComponent;
                Class mc_IChatBaseComponent_ChatSerializer;
                Class mc_PacketPlayOutChat;

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
                mc_PacketPlayOutChat_new = mc_PacketPlayOutChat.getConstructor(mc_IChatBaseComponent);

            } catch (Throwable ex) {
                Bukkit.getLogger().log(Level.WARNING, "Problem preparing raw chat packets (disabling further packets)", ex);
                enabled = false;
            }
        }
    }
}
