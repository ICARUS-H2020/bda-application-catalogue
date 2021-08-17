package it.eng.alidalab.applicationcatalogue.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include= JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DatasetSourceTypeDatabase.class, name = "database"),
        @JsonSubTypes.Type(value = DatasetSourceTypeFileSystem.class, name = "fileSystem")
})
public class DatasetSourceType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String KeyServerConfig;

    public String getKeyServerConfig() {
        return KeyServerConfig;
    }

    public void setKeyServerConfig(String keyServerConfig) {
        KeyServerConfig = keyServerConfig;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}