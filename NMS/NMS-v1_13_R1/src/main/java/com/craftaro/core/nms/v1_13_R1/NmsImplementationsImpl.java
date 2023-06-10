package com.craftaro.core.nms.v1_13_R1;

import com.craftaro.core.nms.v1_13_R1.anvil.AnvilCore;
import com.craftaro.core.nms.v1_13_R1.entity.NMSPlayerImpl;
import com.craftaro.core.nms.v1_13_R1.nbt.NBTCoreImpl;
import com.craftaro.core.nms.v1_13_R1.world.NmsWorldBorderImpl;
import com.craftaro.core.nms.v1_13_R1.world.WorldCoreImpl;
import com.craftaro.core.nms.NmsImplementations;
import com.craftaro.core.nms.entity.NMSPlayer;
import com.craftaro.core.nms.nbt.NBTCore;
import com.craftaro.core.nms.world.NmsWorldBorder;
import com.craftaro.core.nms.world.WorldCore;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class NmsImplementationsImpl implements NmsImplementations {
    private final NMSPlayer player;
    private final WorldCore world;
    private final NmsWorldBorder worldBorder;
    private final com.craftaro.core.nms.anvil.AnvilCore anvil;
    private final NBTCore nbt;

    public NmsImplementationsImpl() {
        this.player = new NMSPlayerImpl();
        this.world = new WorldCoreImpl();
        this.worldBorder = new NmsWorldBorderImpl();
        this.anvil = new AnvilCore();
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
    public @NotNull com.craftaro.core.nms.anvil.AnvilCore getAnvil() {
        return this.anvil;
    }

    @Override
    public @NotNull NBTCore getNbt() {
        return this.nbt;
    }
}
