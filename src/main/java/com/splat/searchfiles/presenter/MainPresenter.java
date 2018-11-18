package com.splat.searchfiles.presenter;

import com.splat.searchfiles.controller.MainController;
import com.splat.searchfiles.search.SearchFiles;
import com.splat.searchfiles.view.MainView;

public class MainPresenter {
    private MainView mainView;

    public MainPresenter(MainView view) {
        this.mainView = view;
    }

    public void startSearch(String path, String ext, String text) {
        if (!path.isEmpty() && !text.isEmpty()) {
            SearchFiles searchFiles = new SearchFiles();
            Thread thread = new Thread(searchFiles, "ThreadedSearchFiles");
            searchFiles.setPath(path);
            searchFiles.setExtensions(ext);
            searchFiles.setText(text);
            thread.start();
            try {
                thread.join();
                mainView.setProgressBar(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
