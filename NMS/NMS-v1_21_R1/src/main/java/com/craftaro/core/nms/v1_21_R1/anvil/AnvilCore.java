package com.craftaro.core.nms.v1_21_R1.anvil;

import com.craftaro.core.nms.anvil.CustomAnvil;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public class AnvilCore implements com.craftaro.core.nms.anvil.AnvilCore {

    @Override
    public CustomAnvil createAnvil(Player player) {
        ServerPlayer p = ((CraftPlayer) player).getHandle();
        return new AnvilView(p.nextContainerCounter(), p, null);
    }

    @Override
    public CustomAnvil createAnvil(Player player, InventoryHolder holder) {
        try {
            ServerPlayer p = ((CraftPlayer) player).getHandle();
            return new AnvilView(p.nextContainerCounter(), p, holder);
        } catch (NoClassDefFoundError e) {
            //1.21 support
            try {
                Class<?> clazz = Class.forName("com.craftaro.core.nms.v1_21_0.anvil.AnvilCore");
                Object anvilCore = clazz.newInstance();

                return (CustomAnvil) anvilCore.getClass().getMethod("createAnvil", Player.class, InventoryHolder.class).invoke(anvilCore, player, holder);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
