package com.splat.searchfiles.controller;

import com.splat.searchfiles.presenter.MainPresenter;
import com.splat.searchfiles.view.MainView;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class MainController implements MainView {
    private MainPresenter presenter = new MainPresenter(this);

    @FXML
    private Button search_btn;
    @FXML
    private TextField path_field;
    @FXML
    private TextField extensions_field;
    @FXML
    private TextField text_field;
    @FXML
    private TextArea file_view;
    @FXML
    private ProgressIndicator progress_ind;
    @FXML
    private TreeView<String> res_tree_view;

    @FXML
    public void initialize() {
        progress_ind.setVisible(false);
        search_btn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                progress_ind.setVisible(true);
                if (extensions_field.getText().isEmpty())
                    extensions_field.setText("log");
                presenter.startSearch(path_field.getText(), extensions_field.getText(), text_field.getText());
            }


        });
    }

    @Override
    public void setTreeView(final TreeItem<String> files) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                res_tree_view.setRoot(files);
                progress_ind.setVisible(false);
            }
        });

    }
}
