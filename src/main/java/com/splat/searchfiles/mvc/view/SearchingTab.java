package com.splat.searchfiles.mvc.view;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.List;

public class SearchingTab extends Tab {
    private List<Integer> indexes;

    private int currentIndex;

    public SearchingTab(String name, ObservableList<String> textLines, List<Integer> indexes_) {
        super(name);
        this.indexes = indexes_;
        currentIndex = 0;

        VBox layout = new VBox();
        HBox h_layout = new HBox();
        ListView<String> listView = new ListView<>(textLines);
        VBox.setVgrow(listView, Priority.ALWAYS);
        Button prev_btn = new Button("<");
        Button next_btn = new Button(">");
        h_layout.setMaxHeight(30);
        h_layout.getChildren().addAll(prev_btn, next_btn);
        layout.getChildren().addAll(listView, h_layout);

        prev_btn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (currentIndex > 0)
                listView.getSelectionModel().select(indexes.get(--currentIndex));
            else
                listView.getSelectionModel().select(indexes.get(currentIndex));
            listView.scrollTo(indexes.get(currentIndex));
        });
        next_btn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (currentIndex + 1 < indexes.size())
                listView.getSelectionModel().select(indexes.get(++currentIndex));
            else
                listView.getSelectionModel().select(indexes.get(currentIndex));
            listView.scrollTo(indexes.get(currentIndex));
        });
        this.setContent(layout);
    }
}
