package com.splat.searchfiles.controller;

import com.splat.searchfiles.presenter.MainPresenter;
import com.splat.searchfiles.view.MainView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
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
    private ProgressIndicator progress_ind;

    @FXML
    public void initialize() {
        search_btn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                progress_ind.setProgress(0);
                if (extensions_field.getText().isEmpty())
                    extensions_field.setText("log");
                presenter.startSearch(path_field.getText(), extensions_field.getText(), text_field.getText());
            }


        });
    }

    @Override
    public void setProgressBar(int value) {
        progress_ind.setProgress(value);
    }
}
