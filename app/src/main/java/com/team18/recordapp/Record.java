package com.team18.recordapp;

public class Record {
    public Record() {

    }

    public Record(String name, String path) {
        this.fileName = name;
        this.filePath = path;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private String fileName, filePath;

}
