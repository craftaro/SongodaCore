package com.craftaro.core.nms.v1_11_R1.entity;

import com.craftaro.core.nms.entity.NMSPlayer;
import com.craftaro.core.nms.entity.player.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_11_R1.Packet;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSPlayerImpl implements NMSPlayer {
    @Override
    public void sendPacket(Player p, Object packet) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket((Packet<?>) packet);
    }

    @Override
    public GameProfile getProfile(Player p) {
        com.mojang.authlib.GameProfile profile = ((CraftPlayer) p).getHandle().getProfile();

        String textureValue = null;
        String textureSignature = null;
        for (Property property : profile.getProperties().get("textures")) {
            if (property.getName().equals("SKIN")) {
                textureValue = property.getValue();
                textureSignature = property.getSignature();
            }
        }

        return new GameProfile(
                profile,

                profile.getId(),
                profile.getName(),
                textureValue,
                textureSignature
        );
    }
}
