package com.splat.searchfiles.search;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetFile {
    private String path;
    private String text;
    private List<Integer> indexes = new ArrayList<>();

    public GetFile(TreeItem<String> newItem, String extensions) {
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
                    path = pathBuilder.deleteCharAt(0).toString();
                }
            }
        }
    }

    public ObservableList<String> getAllLines() {
        ObservableList<String> textLines = FXCollections.observableArrayList();
        try (BufferedReader in = new BufferedReader(new FileReader(path))) {
            String line;
            int counter = 0;
            while ((line = in.readLine()) != null) {
                textLines.add(line);
                if (line.contains(text))
                    indexes.add(counter);
                counter++;
            }
            return textLines;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Integer> getIndexes() {
        return indexes;
    }

    public void setText(String text) {
        this.text = text;
    }
}
