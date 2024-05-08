package com.team18.recordapp;

import java.io.File;

public class Record {

    public Record(String recordPath) {
        this.recordPath = recordPath;
    }

    public String getFilePath() {
        return recordPath;
    }

    public void setFilePath(String filePath) {
        this.recordPath = filePath;
    }

    public String getFileName() {
        return new File(recordPath).getName();
    }

    private String recordPath;

}
