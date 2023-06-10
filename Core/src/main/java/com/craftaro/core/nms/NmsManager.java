package com.craftaro.core.nms;

import com.craftaro.core.nms.anvil.AnvilCore;
import com.craftaro.core.nms.entity.NMSPlayer;
import com.craftaro.core.nms.nbt.NBTCore;
import com.craftaro.core.nms.world.NmsWorldBorder;
import com.craftaro.core.nms.world.WorldCore;

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
