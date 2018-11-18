package com.splat.searchfiles.search;

import javafx.scene.control.TreeItem;

import java.io.*;
import java.nio.file.*;

public class SearchFiles implements Runnable {
    private String path;
    private String[] extensions;
    private String text;

    private TreeItem<String> files;

    public void setPath(String path) {
        this.path = path;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions.split(", ");
    }

    public void setText(String text) {
        this.text = text;
    }

    public TreeItem<String> getFiles() {
        return files;
    }

    @Override
    public void run() {
        files = new TreeItem<>(path);
        files.setExpanded(true);
        dirSearch(new File(path), files);
    }

    private void dirSearch(File path, TreeItem<String> root) {
        File[] folderEntries = path.listFiles();
        if (folderEntries != null) {
            for (File entry : folderEntries) {
                if (entry.isDirectory()) {
                    TreeItem<String> child = new TreeItem<>(entry.getName());
                    child.setExpanded(true);
                    dirSearch(entry, child);
                    if (!child.getChildren().isEmpty())
                        root.getChildren().add(child);
                } else {
                    String name = entry.getName();
                    if (name.lastIndexOf('.') > 0) {
                        int lastIndex = name.lastIndexOf('.');
                        String str = name.substring(lastIndex);
                        for (String ext : extensions) {
                            if (str.equals("." + ext)) {
                                try {
                                    if (fileContainsWord(entry.getAbsolutePath(), text))
                                        root.getChildren().add(new TreeItem<>(entry.getName()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean fileContainsWord(String fileName, String text) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName))).contains(text);
    }
}