package it.eng.alidalab.applicationcatalogue.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="Owner")
public class Owner implements Serializable {

    @Id
    private String id;
    private String name;

    public Owner() {
    }

    public Owner(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "organization_id")
    private Organization organization;


    public Owner(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Owner(String id, String name, Organization organization) {
        this.id = id;
        this.name = name;
        this.organization=organization;
    }


    public Owner(String id,  Organization organization) {
        this.id = id;
        this.organization=organization;
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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }


}
