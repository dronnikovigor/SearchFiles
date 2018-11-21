package com.splat.searchfiles.mvc.presenter;

import com.splat.searchfiles.Constants;
import com.splat.searchfiles.search.GetFile;
import com.splat.searchfiles.search.SearchFiles;
import com.splat.searchfiles.mvc.view.MainView;
import javafx.scene.control.TreeItem;

public class MainPresenter {
    private MainView mainView;
    private boolean searchFlag;

    public MainPresenter(MainView view) {
        this.mainView = view;
        searchFlag = false;
    }

    public void startSearch(String path, String ext, String text) {
        if (!searchFlag) {
            this.searchFlag = true;
            new Thread(() -> {
                if (!path.isEmpty() && !text.isEmpty()) {
                    SearchFiles searchFiles = new SearchFiles();
                    Thread thread = new Thread(searchFiles, "ThreadedSearchFiles");
                    searchFiles.setPath(path);
                    searchFiles.setExtensions(ext);
                    searchFiles.setText(text);
                    thread.start();
                    try {
                        thread.join();
                        mainView.setTreeView(searchFiles.getFiles());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                searchFlag = false;
            }).start();
        }
    }

    public void getFile(TreeItem<String> newItem, String extensions, String text) {
        new Thread(() -> {
            GetFile file = new GetFile(newItem, extensions);
            file.setText(text);
            try {
                mainView.setNewTab(file, newItem.getValue(), file.getLines(0, Constants.LINES_TO_LOAD), file.getIndexes());
            } catch (NullPointerException e) {
                System.out.println("It's not a file!");
                e.printStackTrace();
            }
        }).start();
    }
}
