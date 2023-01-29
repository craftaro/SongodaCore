package com.songoda.core.nms;

import com.songoda.core.nms.entity.NMSPlayer;
import com.songoda.core.nms.world.WorldCore;
import org.jetbrains.annotations.NotNull;

public interface NMSGetter {
    @NotNull NMSPlayer getPlayer();
    @NotNull WorldCore getWorld();
}
