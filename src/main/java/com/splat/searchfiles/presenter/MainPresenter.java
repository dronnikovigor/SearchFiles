package com.splat.searchfiles.presenter;

import com.splat.searchfiles.search.SearchFiles;
import com.splat.searchfiles.view.MainView;

public class MainPresenter extends Thread {
    private MainView mainView;
    private boolean searchFlag;

    private String path;
    private String ext;
    private String text;

    public MainPresenter(MainView view) {
        this.mainView = view;
        searchFlag = false;
    }

    public void startSearch(String path, String ext, String text) {
        this.path = path;
        this.ext = ext;
        this.text = text;
        this.searchFlag = true;
        start();
    }

    public void run(){
        if (searchFlag){
            if (!path.isEmpty() && !text.isEmpty()) {
                SearchFiles searchFiles = new SearchFiles();
                Thread thread = new Thread(searchFiles, "ThreadedSearchFiles");
                searchFiles.setPath(path);
                searchFiles.setExtensions(ext);
                searchFiles.setText(text);
                thread.start();
                try {
                    thread.join();
                    searchFlag = false;
                    mainView.setTreeView(searchFiles.getFiles());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
