package com.songoda.core.nms.v1_19_R3;

import com.songoda.core.nms.NmsImplementations;
import com.songoda.core.nms.anvil.AnvilCore;
import com.songoda.core.nms.entity.NMSPlayer;
import com.songoda.core.nms.nbt.NBTCore;
import com.songoda.core.nms.v1_19_R3.entity.NMSPlayerImpl;
import com.songoda.core.nms.v1_19_R3.nbt.NBTCoreImpl;
import com.songoda.core.nms.v1_19_R3.world.NmsWorldBorderImpl;
import com.songoda.core.nms.v1_19_R3.world.WorldCoreImpl;
import com.songoda.core.nms.world.NmsWorldBorder;
import com.songoda.core.nms.world.WorldCore;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftMagicNumbers;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class NmsImplementationsImpl implements NmsImplementations {
    private final NMSPlayer player;
    private final WorldCore world;
    private final NmsWorldBorder worldBorder;
    private final AnvilCore anvil;
    private final NBTCore nbt;

    public NmsImplementationsImpl() {
        this.player = new NMSPlayerImpl();
        this.world = new WorldCoreImpl();
        this.worldBorder = new NmsWorldBorderImpl();
        this.anvil = new com.songoda.core.nms.v1_19_R3.anvil.AnvilCore();
        this.nbt = new NBTCoreImpl();
    }

    @Override
    public @NotNull NMSPlayer getPlayer() {
        return this.player;
    }

    @Override
    public @NotNull WorldCore getWorld() {
        return this.world;
    }

    @Override
    public @NotNull NmsWorldBorder getWorldBorder() {
        return this.worldBorder;
    }

    @Override
    public @NotNull AnvilCore getAnvil() {
        return this.anvil;
    }

    @Override
    public @NotNull NBTCore getNbt() {
        return this.nbt;
    }
}
