package com.songoda.core.nms.v1_11_R1.world;

import com.songoda.core.nms.entity.NMSPlayer;
import com.songoda.core.nms.world.NmsWorldBorder;
import net.minecraft.server.v1_11_R1.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_11_R1.WorldBorder;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NmsWorldBorderImpl implements NmsWorldBorder {
    private final NMSPlayer nmsPlayer;

    public NmsWorldBorderImpl(NMSPlayer nmsPlayer) {
        this.nmsPlayer = nmsPlayer;
    }

    @Override
    public void send(Player player, BorderColor color, double size, @NotNull Location center) {
        Objects.requireNonNull(center.getWorld());

        WorldBorder worldBorder = new WorldBorder();
        worldBorder.world = ((CraftWorld) center.getWorld()).getHandle();

        worldBorder.setCenter(center.getX(), center.getZ());
        worldBorder.setSize(size);
        worldBorder.setWarningTime(0);
        worldBorder.setWarningDistance(0);

        if (color == BorderColor.GREEN) {
            worldBorder.transitionSizeBetween(size - 0.1D, size, Long.MAX_VALUE);
        } else if (color == BorderColor.RED) {
            worldBorder.transitionSizeBetween(size, size - 1.0D, Long.MAX_VALUE);
        }

        this.nmsPlayer.sendPacket(player, new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
    }
}
