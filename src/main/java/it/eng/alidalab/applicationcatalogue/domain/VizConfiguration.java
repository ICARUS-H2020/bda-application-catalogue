package it.eng.alidalab.applicationcatalogue.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class VizConfiguration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 4096)
    private ObjectNode queryDetails;
    @Column(length = 4096)
    private ArrayNode mappingSettings;
    private String name;
    @Column(length = 4096)
    private String description;
    @Column(length = 4096)
    private ObjectNode selectedChart;

    @JsonBackReference
    @ManyToOne
    private BDAApplication bdaApplication;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ObjectNode getQueryDetails() {
        return queryDetails;
    }

    public void setQueryDetails(ObjectNode queryDetails) {
        this.queryDetails = queryDetails;
    }

    public ArrayNode getMappingSettings() {
        return mappingSettings;
    }

    public void setMappingSettings(ArrayNode mappingSettings) {
        this.mappingSettings = mappingSettings;
    }

    public BDAApplication getBdaApplication() {
        return bdaApplication;
    }

    public void setBdaApplication(BDAApplication bdaApplication) {
        this.bdaApplication = bdaApplication;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ObjectNode getSelectedChart() {
        return selectedChart;
    }

    public void setSelectedChart(ObjectNode selectedChart) {
        this.selectedChart = selectedChart;
    }
}
