package com.splat.searchfiles.mvc.view;

import javafx.scene.control.TreeItem;

public interface MainView {
    void setTreeView(TreeItem<String> files);

    void setNewTab(String name, String text);
}
