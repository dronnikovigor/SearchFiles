package com.splat.searchfiles.mvp.controller;

import com.splat.searchfiles.mvp.presenter.MainPresenter;
import com.splat.searchfiles.mvp.view.MainView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
    private ProgressIndicator progress_ind;
    @FXML
    private TreeView<String> res_tree_view;
    @FXML
    private TabPane tab_view;

    @FXML
    public void initialize() {
        progress_ind.setVisible(false);
        search_btn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            progress_ind.setVisible(true);
            if (extensions_field.getText().isEmpty())
                extensions_field.setText("log");
            presenter.startSearch(path_field.getText(), extensions_field.getText(), text_field.getText());
        });
        res_tree_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                String name = newValue.getValue();
                if (name.lastIndexOf('.') > 0) {
                    int lastIndex = name.lastIndexOf('.');
                    String str = name.substring(lastIndex);
                    for (String ext : extensions_field.getText().split(", ")) {
                        if (str.equals("." + ext)) {
                            StringBuilder pathBuilder = new StringBuilder();
                            for (TreeItem<String> item = newValue;
                                 item != null; item = item.getParent()) {

                                pathBuilder.insert(0, item.getValue());
                                pathBuilder.insert(0, "/");
                            }
                            String path = pathBuilder.deleteCharAt(0).toString();

                            StringBuilder textBuilder = new StringBuilder();
                            try (BufferedReader in = new BufferedReader(new FileReader(path))) {
                                String line = in.readLine();
                                while (true) {
                                    if (line != null) {
                                        textBuilder.append(line);
                                        if ((line = in.readLine()) != null)
                                            textBuilder.append("\n");
                                    }
                                    else
                                        break;
                                }
                                final Tab tab = new Tab(name);
                                TextArea textArea = new TextArea(textBuilder.toString());
                                tab.setContent(textArea);
                                tab_view.getTabs().add(tab);
                                tab_view.getSelectionModel().select(tab);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void setTreeView(final TreeItem<String> files) {
        Platform.runLater(() -> {
            res_tree_view.setRoot(files);
            progress_ind.setVisible(false);
        });
    }
}
