package com.splat.searchfiles.mvc.view;

import com.splat.searchfiles.Constants;
import com.splat.searchfiles.search.GetFile;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class SearchingTab extends Tab {
    private GetFile file;
    private ListView<String> listView;
    private ObservableList<String> textLines;
    private List<Integer> indexes;

    private int currentIndex;

    public SearchingTab(GetFile file_, String name, ObservableList<String> textLines_, List<Integer> indexes_) {
        super(name);
        this.file = file_;
        this.textLines = textLines_;
        this.indexes = indexes_;
        currentIndex = 0;

        VBox layout = new VBox();
        HBox h_layout = new HBox();
        listView = new ListView<>(textLines);
        VBox.setVgrow(listView, Priority.ALWAYS);
        Button prev_btn = new Button("<");
        Button next_btn = new Button(">");
        h_layout.setMaxHeight(30);
        h_layout.getChildren().addAll(prev_btn, next_btn);
        layout.getChildren().addAll(listView, h_layout);

        prev_btn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (!indexes.isEmpty()) {
                if (currentIndex > 0)
                    listView.getSelectionModel().select(indexes.get(--currentIndex));
                else
                    listView.getSelectionModel().select(indexes.get(currentIndex));
                listView.scrollTo(indexes.get(currentIndex));
            }
        });
        next_btn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (!indexes.isEmpty()) {
                if (currentIndex + 1 < indexes.size()) {
                    currentIndex++;
                    if (listView.getItems().size() < indexes.get(currentIndex)) {
                        textLines.addAll(file.getLines(listView.getItems().size(), (indexes.get(currentIndex) / Constants.LINES_TO_LOAD + 1) * Constants.LINES_TO_LOAD));
                    } else
                        listView.getSelectionModel().select(indexes.get(currentIndex));
                } else
                    listView.getSelectionModel().select(indexes.get(currentIndex));
                listView.scrollTo(indexes.get(currentIndex));
            }
        });

        this.setContent(layout);

        listView.setOnScroll(event ->
                textLines.addAll(file.getLines(listView.getItems().size(), listView.getItems().size() + Constants.LINES_TO_LOAD)));
    }
}
