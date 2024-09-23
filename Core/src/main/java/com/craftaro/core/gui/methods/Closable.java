package com.craftaro.core.gui.methods;

import com.craftaro.core.gui.events.GuiCloseEvent;

public interface Closable {
    void onClose(GuiCloseEvent event);
}
