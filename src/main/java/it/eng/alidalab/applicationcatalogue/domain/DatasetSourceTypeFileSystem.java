package it.eng.alidalab.applicationcatalogue.domain;

import javax.persistence.*;

@Entity
public class DatasetSourceTypeFileSystem extends DatasetSourceType{

    private String path;
    private String extension;
    private String fileName;

    public DatasetSourceTypeFileSystem() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
