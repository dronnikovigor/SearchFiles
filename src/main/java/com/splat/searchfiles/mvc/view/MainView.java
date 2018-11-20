package com.splat.searchfiles.mvc.view;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import java.util.List;

public interface MainView {
    void setTreeView(TreeItem<String> files);

    void setNewTab(String name, ObservableList<String> textLines, List<Integer> indexes);
}
