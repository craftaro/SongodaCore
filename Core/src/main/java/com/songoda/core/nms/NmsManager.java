package com.songoda.core.nms;

import com.songoda.core.nms.anvil.AnvilCore;
import com.songoda.core.nms.entity.NMSPlayer;
import com.songoda.core.nms.nbt.NBTCore;
import com.songoda.core.nms.world.NmsWorldBorder;
import com.songoda.core.nms.world.WorldCore;

/**
 * @deprecated Use {@link Nms} instead.
 */
@Deprecated
public class NmsManager {
    @Deprecated
    public static NMSPlayer getPlayer() {
        return Nms.getImplementations().getPlayer();
    }

    @Deprecated
    public static AnvilCore getAnvil() {
        return Nms.getImplementations().getAnvil();
    }

    @Deprecated
    public static boolean hasAnvil() {
        return getAnvil() != null;
    }

    @Deprecated
    public static NBTCore getNbt() {
        return Nms.getImplementations().getNbt();
    }

    @Deprecated
    public static boolean hasNbt() {
        return getNbt() != null;
    }

    @Deprecated
    public static WorldCore getWorld() {
        return Nms.getImplementations().getWorld();
    }

    @Deprecated
    public static boolean hasWorld() {
        return getWorld() != null;
    }

    @Deprecated
    public static NmsWorldBorder getWorldBorder() {
        return Nms.getImplementations().getWorldBorder();
    }
}
