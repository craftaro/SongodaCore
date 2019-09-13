package com.songoda.core.nms.v1_8_R1;

import com.songoda.core.nms.CoreNMS;
import com.songoda.core.nms.CustomAnvil;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMS implements CoreNMS {

    @Override
    public CustomAnvil createAnvil(Player player) {
        return new AnvilView(((CraftPlayer) player).getHandle());
    }

}
