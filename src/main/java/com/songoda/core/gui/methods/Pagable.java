package com.songoda.core.gui.methods;

import com.songoda.core.gui.GUI;

public interface Pagable {

    void onPageChange(GUI gui, int lastPage, int newPage);
}
