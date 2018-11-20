package com.splat.searchfiles.mvc.controller;

import com.splat.searchfiles.mvc.presenter.MainPresenter;
import com.splat.searchfiles.mvc.view.MainView;
import com.splat.searchfiles.mvc.view.SearchingTab;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.List;

public class MainController implements MainView {
    private MainPresenter presenter = new MainPresenter(this);
    private BooleanBinding booleanBind;
    private DirectoryChooser directoryChooser;

    @FXML
    private Button search_btn;
    @FXML
    private Button select_path_btn;
    @FXML
    private TextField path_field;
    @FXML
    private TextField extensions_field;
    @FXML
    private TextField text_field;
    @FXML
    private ProgressIndicator progress_ind;
    @FXML
    private TreeView<String> res_tree_view;
    @FXML
    private TabPane tab_view;

    @FXML
    public void initialize() {
        booleanBind = path_field.textProperty().isEmpty()
                .or(text_field.textProperty().isEmpty())
                .or((progress_ind.visibleProperty()));
        search_btn.disableProperty().bind(booleanBind);
        //
        progress_ind.setVisible(false);
        search_btn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            progress_ind.setVisible(true);
            if (extensions_field.getText().isEmpty())
                extensions_field.setText("log");
            presenter.startSearch(path_field.getText(), extensions_field.getText(), text_field.getText());
        });
        //
        directoryChooser = new DirectoryChooser();
        configurateDirectoryChooser(directoryChooser);
        select_path_btn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            File dir = directoryChooser.showDialog(null);
            if (dir != null)
                path_field.setText(dir.getAbsolutePath());
            else
                path_field.setText(null);

        });
        //
        res_tree_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                presenter.getFile(newValue, extensions_field.getText(), text_field.getText());
            }
        });
    }

    @Override
    public void setTreeView(final TreeItem<String> files) {
        Platform.runLater(() -> {
            res_tree_view.setRoot(files);
            progress_ind.setVisible(false);
            search_btn.disableProperty().bind(booleanBind);
        });
    }

    @Override
    public void setNewTab(String name, ObservableList<String> textLines, List<Integer> indexes) {
        Platform.runLater(() -> {
            SearchingTab tab = new SearchingTab(name, textLines, indexes);

            tab_view.getTabs().add(tab);
            tab_view.getSelectionModel().select(tab);
        });
    }

    private void configurateDirectoryChooser(DirectoryChooser directoryChooser) {
        directoryChooser.setTitle("Select Path");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }
}
