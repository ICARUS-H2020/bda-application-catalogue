package it.eng.alidalab.applicationcatalogue.domain;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

public class Tag {

    private String name;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The database generated Tag ID")
    private Long id;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
