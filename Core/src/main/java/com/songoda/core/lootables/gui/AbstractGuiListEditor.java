package com.songoda.core.lootables.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.AnvilGui;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.utils.TextUtils;
import com.songoda.core.lootables.loot.Loot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractGuiListEditor extends Gui {

    protected final Loot loot;
    private final Gui returnGui;

    public AbstractGuiListEditor(Loot loot ,Gui returnGui) {
        super(1, returnGui);
        this.returnGui = returnGui;
        this.loot = loot;
        setDefaultItem(null);
        paint();
    }

    public void paint() {
        List<String> lore = getData() == null ? new ArrayList<>() : getData();
        setButton(2, GuiUtils.createButtonItem(CompatibleMaterial.OAK_FENCE_GATE,
                TextUtils.formatText("&cBack")),
                (event) -> {
                    guiManager.showGUI(event.player, returnGui);
                    ((GuiLootEditor) returnGui).paint();
                });
        setButton(6, GuiUtils.createButtonItem(CompatibleMaterial.OAK_FENCE_GATE,
                TextUtils.formatText("&cBack")),
                (event) -> {
                    guiManager.showGUI(event.player, returnGui);
                    ((GuiLootEditor) returnGui).paint();
                });
        setButton(3, GuiUtils.createButtonItem(CompatibleMaterial.ARROW,
                TextUtils.formatText("&aAdd new line")),
                (event -> {
                    AnvilGui gui = new AnvilGui(event.player, this);
                    gui.setAction((e -> {
                        String validated = validate(gui.getInputText());
                        if (validated != null) {
                            lore.add(validated);
                            updateData(lore.isEmpty() ? null : lore);
                            e.player.closeInventory();
                            paint();
                        }
                    }));
                    gui.setTitle("Enter a new line");
                    guiManager.showGUI(event.player, gui);
                }));

        setItem(4, GuiUtils.createButtonItem(CompatibleMaterial.WRITABLE_BOOK,
                TextUtils.formatText("&9Lore:"),
                lore.isEmpty()
                        ? TextUtils.formatText(Collections.singletonList("&cNo lore set..."))
                        : TextUtils.formatText(lore)));

        setButton(5, GuiUtils.createButtonItem(CompatibleMaterial.ARROW,
                TextUtils.formatText("&cRemove the last line")),
                (event -> {
                    lore.remove(lore.size() - 1);
                    updateData(lore);
                    paint();
                }));
    }

    protected abstract List<String> getData();

    protected abstract void updateData(List<String> list);

    protected abstract String validate(String line);
}
