package com.craftaro.core.nms.v1_20_R1;

import com.craftaro.core.nms.NMSGetter;
import com.craftaro.core.nms.entity.NMSPlayer;
import com.craftaro.core.nms.v1_20_R1.entity.NMSPlayerImpl;
import com.craftaro.core.nms.v1_20_R1.world.WorldCoreImpl;
import com.craftaro.core.nms.world.WorldCore;

public class NMSGetterImpl implements NMSGetter {

    private final NMSPlayer nmsPlayer;
    private final WorldCore worldCore;
    public NMSGetterImpl() {
        this.nmsPlayer = new NMSPlayerImpl();
        this.worldCore = new WorldCoreImpl();
    }

    @Override
    public NMSPlayer getPlayer() {
        return nmsPlayer;
    }

    @Override
    public WorldCore getWorld() {
        return worldCore;
    }
}
