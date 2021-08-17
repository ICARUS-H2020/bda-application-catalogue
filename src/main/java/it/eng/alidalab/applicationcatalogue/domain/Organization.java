package it.eng.alidalab.applicationcatalogue.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="Organization")
public class Organization implements Serializable {

    @Id
    private String id;
    private String name;

    public Organization() {
    }

    public Organization(String id) {
        this.id = id;
    }

    public Organization(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

