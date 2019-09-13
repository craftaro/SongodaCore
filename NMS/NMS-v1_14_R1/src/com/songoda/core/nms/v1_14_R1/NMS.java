package com.songoda.core.nms.v1_14_R1;

import com.songoda.core.nms.CoreNMS;
import com.songoda.core.nms.CustomAnvil;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMS implements CoreNMS {

    @Override
    public CustomAnvil createAnvil(Player player) {
        EntityPlayer p = ((CraftPlayer) player).getHandle();
        return new AnvilView(p.nextContainerCounter(), p);
    }

}
