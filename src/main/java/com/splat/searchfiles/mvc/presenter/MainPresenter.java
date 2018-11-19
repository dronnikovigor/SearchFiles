package com.splat.searchfiles.mvc.presenter;

import com.splat.searchfiles.search.SearchFiles;
import com.splat.searchfiles.mvc.view.MainView;
import javafx.scene.control.TreeItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

    public void getFile(TreeItem<String> newItem, String extensions) {
        new Thread(() -> {
            String name = newItem.getValue();
            if (name.lastIndexOf('.') > 0) {
                int lastIndex = name.lastIndexOf('.');
                String str = name.substring(lastIndex);
                for (String ext : extensions.replaceAll(" ", "").split(",")) {
                    if (str.equals("." + ext)) {
                        StringBuilder pathBuilder = new StringBuilder();
                        for (TreeItem<String> item = newItem;
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
                                } else
                                    break;
                            }
                            mainView.setNewTab(name, textBuilder.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
