package com.craftaro.core.nms;

import com.craftaro.core.nms.entity.NMSPlayer;
import com.craftaro.core.nms.world.WorldCore;

public interface NMSGetter {
    NMSPlayer getPlayer();
    WorldCore getWorld();
}
