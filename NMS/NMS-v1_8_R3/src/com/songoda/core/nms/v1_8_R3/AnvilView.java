package com.songoda.core.nms.v1_8_R3;

import com.songoda.core.nms.CustomAnvil;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.ContainerAnvil;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AnvilView extends ContainerAnvil implements CustomAnvil {

    private final EntityPlayer entity;
    private final Inventory inventory;
    private String title = "Repairing";
    private int cost = -1;
    private boolean canUse = true;

    static Field mc_ContainerAnvil_renameText;

    static {
        try {
            mc_ContainerAnvil_renameText = ContainerAnvil.class.getDeclaredField("l");
            mc_ContainerAnvil_renameText.setAccessible(true);
        } catch (Exception ex) {
            Logger.getLogger(AnvilView.class.getName()).log(Level.SEVERE, "Anvil Error", ex);
        }
    }

    public AnvilView(EntityPlayer entity) {
        super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
        this.entity = entity;
        this.inventory = getBukkitView().getTopInventory();
    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return canUse;
    }

    @Override
    public void e() {
        super.e();
        if (cost >= 0) {
            this.a = cost;
        }
    }

    @Override
    public String getRenameText() {
        try {
            //return this.l;
            return (String) mc_ContainerAnvil_renameText.get(this);
        } catch (Exception ex) {
            Logger.getLogger(AnvilView.class.getName()).log(Level.SEVERE, "Anvil Error", ex);
        }
        return null;
    }

    @Override
    public void setRenameText(String text) {
        this.a(text);
    }

    @Override
    public String getCustomTitle() {
        return title;
    }

    @Override
    public void setCustomTitle(String title) {
        this.title = title;
    }

    @Override
    public void setLevelCost(int cost) {
        this.cost = cost;
    }

    @Override
    public int getLevelCost() {
        return cost >= 0 ? cost : this.a;
    }

    @Override
    public void setCanUse(boolean bool) {
        this.canUse = bool;
    }

    @Override
    public ItemStack getLeftInput() {
        return inventory.getItem(0);
    }

    @Override
    public ItemStack getRightInput() {
        return inventory.getItem(1);
    }

    @Override
    public ItemStack getOutput() {
        return inventory.getItem(2);
    }

    @Override
    public void setLeftInput(ItemStack item) {
        inventory.setItem(0, item);
    }

    @Override
    public void setRightInput(ItemStack item) {
        inventory.setItem(1, item);
    }

    @Override
    public void setOutput(ItemStack item) {
        inventory.setItem(2, item);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void open() {

        // Counter stuff that the game uses to keep track of inventories
        int id = entity.nextContainerCounter();

        // Send the packet
        entity.playerConnection.sendPacket(new PacketPlayOutOpenWindow(id, "minecraft:anvil", new ChatMessage(title != null ? title : ""), 0));

        // Set their active container to this anvil
        entity.activeContainer = this;

        // Set their active container window id to that counter stuff
        entity.activeContainer.windowId = id;

        // Add the slot listener
        entity.activeContainer.addSlotListener(entity);
    }

}
