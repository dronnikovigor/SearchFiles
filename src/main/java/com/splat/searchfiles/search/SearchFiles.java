package com.splat.searchfiles.search;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class SearchFiles implements Runnable {
    private String path;
    private String[] extensions;
    private String text;

    private ArrayList<File> files;

    public SearchFiles() {
        files = new ArrayList<>();
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions.split(", ");
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    @Override
    public void run() {
        File dir = new File(path);
        dirSearch(dir);
        System.out.println(files.size());
    }

    private void dirSearch(File path) {
        File[] folderEntries = path.listFiles();
        if (folderEntries != null) {
            for (File entry : folderEntries) {
                if (entry.isDirectory()) {
                    dirSearch(entry);
                } else {
                    String name = entry.getName();
                    if (name.lastIndexOf('.') > 0) {
                        int lastIndex = name.lastIndexOf('.');
                        String str = name.substring(lastIndex);
                        for (String ext : extensions) {
                            if (str.equals("." + ext)) {
                                try {
                                    if (fileContainsWord(entry.getAbsolutePath(), text))
                                        files.add(entry);
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