package com.craftaro.core.nms.v1_8_R3.world;

import com.craftaro.core.nms.world.SItemStack;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.Vec3D;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SItemStackImpl implements SItemStack {
    private final ItemStack item;

    public SItemStackImpl(ItemStack item) {
        this.item = item;
    }

    @Override
    public void breakItem(Player player, int amount) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        for (int i = 0; i < 5; ++i) {
            Vec3D vec3d = new Vec3D(((double) random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            vec3d = vec3d.a(-entityPlayer.pitch * 0.017453292F);
            vec3d = vec3d.b(-entityPlayer.yaw * 0.017453292F);

            double d0 = (double) (-random.nextFloat()) * 0.6D - 0.3D;

            Vec3D vec3d1 = new Vec3D(((double) random.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
            vec3d1 = vec3d1.a(-entityPlayer.pitch * 0.017453292F);
            vec3d1 = vec3d1.b(-entityPlayer.yaw * 0.017453292F);
            vec3d1 = vec3d1.add(entityPlayer.locX, entityPlayer.locY + (double) entityPlayer.getHeadHeight(), entityPlayer.locZ);

            entityPlayer.world.addParticle(EnumParticle.ITEM_CRACK, vec3d1.a, vec3d1.b, vec3d1.c, vec3d.a, vec3d.b + 0.05D, vec3d.c, Item.getId(CraftItemStack.asNMSCopy(item).getItem()));
        }
    }
}
