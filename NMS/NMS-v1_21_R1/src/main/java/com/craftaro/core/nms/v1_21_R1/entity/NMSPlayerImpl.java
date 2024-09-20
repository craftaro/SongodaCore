package com.craftaro.core.nms.v1_21_R1.entity;

import com.craftaro.core.nms.entity.NMSPlayer;
import com.craftaro.core.nms.entity.player.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSPlayerImpl implements NMSPlayer {
    @Override
    public void sendPacket(Player p, Object packet) {
        ((CraftPlayer) p).getHandle().connection.send((Packet<?>) packet);
    }

    @Override
    public GameProfile getProfile(Player p) {
        com.mojang.authlib.GameProfile profile = ((CraftPlayer) p).getHandle().getGameProfile();

        String textureValue = null;
        String textureSignature = null;
        for (Property property : profile.getProperties().get("textures")) {
            if (property.name().equals("SKIN")) {
                textureValue = property.value();
                textureSignature = property.signature();
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
