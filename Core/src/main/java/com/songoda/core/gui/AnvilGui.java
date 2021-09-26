package com.songoda.core.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
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
 *
 * @author jascotty2
 * @since 2019-09-15
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
        final ItemStack in;
        if (endPrompt != null && (in = cellItems.get(0)) != null) {
            setItem(2, GuiUtils.createButtonItem(in, endPrompt));
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
        if ((item = cellItems.get(0)) != null) {
            inventory.setItem(0, item);
        } else if ((item = cellItems.get(1)) != null) {
            inventory.setItem(1, item);
        } else if (!acceptsItems) {
            cellItems.put(0, item = GuiUtils.createButtonItem(CompatibleMaterial.PAPER, " ", " "));
            inventory.setItem(0, item);
        }
        if ((item = cellItems.get(2)) != null) {
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
