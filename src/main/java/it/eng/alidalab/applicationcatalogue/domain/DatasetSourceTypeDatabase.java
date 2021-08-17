package it.eng.alidalab.applicationcatalogue.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;

@Entity
@JsonIgnoreProperties({"password_"})
public class DatasetSourceTypeDatabase extends DatasetSourceType{


    private String url;
    private Long port;
    private String user_;
    private String password_;
    private String database_;
    private String table_;
    private String catalogue;


    public DatasetSourceTypeDatabase() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public String getUser() {
        return user_;
    }

    public void setUser(String user_) {
        this.user_ = user_;
    }

    public String getUser_() {
        return user_;
    }

    public void setUser_(String user_) {
        this.user_ = user_;
    }

    public String getPassword_() {
        return password_;
    }

    public void setPassword_(String password_) {
        this.password_ = password_;
    }

    public String getDatabase() {
        return database_;
    }

    public void setDatabase(String database) {
        this.database_ = database;
    }

    public String getTable() {
        return table_;
    }

    public void setTable(String table) {
        this.table_ = table;
    }

    public String getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(String catalogue) {
        this.catalogue = catalogue;
    }
}
