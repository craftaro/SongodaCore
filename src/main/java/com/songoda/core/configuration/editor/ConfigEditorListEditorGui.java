package com.songoda.core.configuration.editor;

import com.songoda.core.compatibility.LegacyMaterials;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.gui.SimplePagedGui;
import com.songoda.core.input.ChatPrompt;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.ClickType;

/**
 * Edit a string list
 *
 * @since 2019-08-31
 * @author jascotty2
 */
public class ConfigEditorListEditorGui extends SimplePagedGui {

    final ConfigEditorGui current;

    public boolean saveChanges = false;
    public List<String> value;

    public ConfigEditorListEditorGui(ConfigEditorGui current, String key, List<String> value) {
        this.current = current;
        this.blankItem = current.getDefaultItem();
        headerBackItem = footerBackItem = current.getHeaderBackItem();
        setTitle(ChatColor.DARK_BLUE + "String List Editor");
        this.setUseHeader(true);
        this.setItem(4, current.configItem(LegacyMaterials.FILLED_MAP, key, current.getCurrentNode(), key, null));
        this.setButton(8, GuiUtils.createButtonItem(LegacyMaterials.OAK_DOOR, "Exit"), (event) -> event.player.closeInventory());
        this.value = new ArrayList(value);

        this.setButton(8, GuiUtils.createButtonItem(LegacyMaterials.LAVA_BUCKET, ChatColor.RED + "Discard Changes"), (event) -> event.player.closeInventory());
        this.setButton(0, GuiUtils.createButtonItem(LegacyMaterials.REDSTONE, ChatColor.GREEN + "Save"), (event) -> {
            saveChanges = true;
            event.player.closeInventory();
        });
        this.setButton(1, GuiUtils.createButtonItem(LegacyMaterials.CHEST, ChatColor.BLUE + "Add Item"), 
                (event) -> {
                    ChatPrompt.showPrompt(event.manager.getPlugin(), event.player, "Enter a new value to add:", response -> {
                        value.add(response.getMessage().trim());
                        redraw();
                    }).setOnClose(() -> event.manager.showGUI(event.player, this))
                      .setOnCancel(() -> {event.player.sendMessage(ChatColor.RED + "Edit canceled"); event.manager.showGUI(event.player, this);});
                });
    }
    
    void redraw() {
        page = 1;
        // clear old display
        for (Integer oldI : (Integer[]) cellItems.keySet().toArray()) {
            if (oldI > 8) {
                cellItems.remove(oldI);
            }
        }
        // update items
        int i = 9;
        for (String item : value) {
            final int index = i - 9;
            setButton(i++, GuiUtils.createButtonItem(LegacyMaterials.PAPER, item, "Right-click to remove"), ClickType.RIGHT, (event) -> {
                value.remove(index);
                redraw();
            });
        }
        // update display
        showPage();

    }

}
