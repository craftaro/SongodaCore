package com.craftaro.core.gui.methods;

import com.craftaro.core.gui.events.GuiDropItemEvent;

public interface Droppable {
    boolean onDrop(GuiDropItemEvent event);
}
