package com.songoda.core.gui;

import com.songoda.core.compatibility.material.CompatibleMaterial;
import com.songoda.core.gui.methods.Clickable;
import com.songoda.core.nms.NmsManager;
import com.songoda.core.nms.anvil.AnvilCore;
import com.songoda.core.nms.anvil.CustomAnvil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * Anvil GUI for text prompts
 */
public class AnvilGui extends Gui {
    final Player player;
    CustomAnvil anvil;
    List<String> endPrompt = null;

    public AnvilGui(Player player) {
        this.player = player;
    }

    public AnvilGui(Player player, Gui parent) {
        super(parent);

        this.player = player;
    }

    @NotNull
    public AnvilGui setAction(@Nullable Clickable action) {
        return (AnvilGui) setAction(2, action);
    }

    @NotNull
    public AnvilGui setAction(@Nullable ClickType type, @Nullable Clickable action) {
        return (AnvilGui) setAction(2, type, action);
    }

    protected void open() {
        anvil.open();
    }

    public AnvilGui setInput(ItemStack item) {
        return (AnvilGui) this.setItem(0, item);
    }

    public ItemStack getInput() {
        return this.getItem(0);
    }

    public AnvilGui setOutput(ItemStack item) {
        return (AnvilGui) this.setItem(2, item);
    }

    public AnvilGui setOutputPrompt(String str) {
        endPrompt = Arrays.asList(str);
        return this;
    }

    public AnvilGui setOutputPrompt(String... str) {
        endPrompt = Arrays.asList(str);
        return this;
    }

    public AnvilGui setOutputPrompt(List<String> str) {
        endPrompt = str;
        return this;
    }

    void updateOutputPrompt() {
        if (endPrompt != null) {
            ItemStack in = cellItems.get(0);

            if (in != null) {
                setItem(2, GuiUtils.createButtonItem(in, endPrompt));
            }
        }
    }

    public ItemStack getOutput() {
        return this.getItem(2);
    }

    public String getInputText() {
        return anvil != null ? anvil.getRenameText() : null;
    }

    @NotNull
    @Override
    protected Inventory generateInventory(@NotNull GuiManager manager) {
        this.guiManager = manager;

        createInventory();
        ItemStack item;
        if (cellItems.containsKey(0)) {
            item = cellItems.get(0);

            inventory.setItem(0, item);
        } else if (cellItems.containsKey(1)) {
            item = cellItems.get(1);

            inventory.setItem(1, item);
        } else if (!acceptsItems) {
            item = GuiUtils.createButtonItem(CompatibleMaterial.PAPER, " ", " ");

            cellItems.put(0, item);
            inventory.setItem(0, item);
        }

        if (cellItems.containsKey(2)) {
            item = cellItems.get(2);

            inventory.setItem(2, item);
        }

        return inventory;
    }

    @Override
    protected void createInventory() {
        AnvilCore nms = NmsManager.getAnvil();

        if (nms != null) {
            anvil = nms.createAnvil(player, new GuiHolder(guiManager, this));
            anvil.setCustomTitle(title);
            anvil.setLevelCost(0);

            inventory = anvil.getInventory();

            anvil.setOnChange(this::updateOutputPrompt);
        }
    }
}
