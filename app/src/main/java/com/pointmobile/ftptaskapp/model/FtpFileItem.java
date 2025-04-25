package com.pointmobile.ftptaskapp.model;

public class FtpFileItem {
    private String fileName;
    private String modifiedDate;

    public FtpFileItem(String fileName, String modifiedDate) {
        this.fileName = fileName;
        this.modifiedDate = modifiedDate;
    }

    public String getFileName() {
        return fileName;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }
}
