package com.songoda.core.gui.methods;

import com.songoda.core.gui.Gui;

public interface Pagable {

    void onPageChange(Gui gui, int lastPage, int newPage);
}
