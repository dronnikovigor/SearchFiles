package com.splat.searchfiles.mvc.view;

import com.splat.searchfiles.search.GetFile;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.util.List;

public interface MainView {
    void setTreeView(TreeItem<String> files);

    void setNewTab(GetFile file, String name, ObservableList<String> textLines, List<Integer> indexes);
}
