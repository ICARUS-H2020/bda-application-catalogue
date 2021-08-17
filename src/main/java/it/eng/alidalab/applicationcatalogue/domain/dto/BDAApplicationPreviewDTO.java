package it.eng.alidalab.applicationcatalogue.domain.dto;

import it.eng.alidalab.applicationcatalogue.domain.*;
import it.eng.alidalab.applicationcatalogue.domain.metadata.Metadata;
import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BDAApplicationPreviewDTO implements Serializable {


    private Long id;
    private Integer workflowCount;
    private Integer serviceCount;
    private Integer modelCount;
    private String description;
    private Integer datasetCount;
    private String name;
    private List<Workflow> workflows;
    private AccessLevel.ACCESS_LEVEL accessLevel = AccessLevel.ACCESS_LEVEL.TEAM;
    private Metadata metadata;
    private List<Dataset> datasets;
    private List<Model> models;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
    private String status;
    private Owner owner;
    private List<VizConfiguration> vizConfigurations;
    private List<String> tags = new ArrayList<>();
    private LocalDateTime deleteDateTime;

    public LocalDateTime getDeleteDateTime() {
        return deleteDateTime;
    }

    public void setDeleteDateTime(LocalDateTime deleteDateTime) {
        this.deleteDateTime = deleteDateTime;
    }

    public List<Workflow> getWorkflows() {
        return workflows;
    }
    public void setWorkflows(List<Workflow> workflows) {
        this.workflowCount = workflows.size();
        if(this.serviceCount == null) this.serviceCount=0;
        if(workflowCount>0) workflows.forEach(w -> {
            this.serviceCount += w.getServices().size();
        });
        this.modelCount = 0;
        Set models = new HashSet();
        workflows.stream().forEach(w -> {
            w.getServices().stream().filter(s -> s.getModels() != null).forEach(se -> models.addAll(se.getModels()));
        });
        this.modelCount += models.size();
    }

    public Integer getWorkflowCount() {
        return workflowCount;
    }

    public void setWorkflowCount(Integer workflowCount) {
        this.workflowCount = workflowCount;
    }

    public Integer getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(Integer serviceCount) {
        this.serviceCount = serviceCount;
    }

    public Integer getModelCount() {
        return modelCount;
    }

    public void setModelCount(Integer modelCount) {
        this.modelCount = modelCount;
    }

    public Integer getDatasetCount() {
        return datasetCount;
    }

    public void setDatasetCount(Integer datasetCount) {
        this.datasetCount = datasetCount;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public BDAApplicationPreviewDTO(@NotBlank(message = "Name is mandatory") String name, Owner owner) {
        this.name = name;
        this.owner = owner;
        this.accessLevel = AccessLevel.ACCESS_LEVEL.TEAM;
    }
    public BDAApplicationPreviewDTO(@NotBlank(message = "Name is mandatory") String name, Owner owner, List<Workflow> workflows) {
        this.name = name;
        this.owner = owner;
        this.accessLevel = AccessLevel.ACCESS_LEVEL.TEAM;
    }



    public BDAApplicationPreviewDTO() {}

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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<VizConfiguration> getVizConfigurations() {
        return vizConfigurations;
    }

    public void setVizConfigurations(List<VizConfiguration> vizConfigurations) {
        this.vizConfigurations = vizConfigurations;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Dataset> getDatasets() {
        return datasets;

    }

    public void setDatasets(List<Dataset> datasets) {
        this.datasetCount = datasets.size();
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public AccessLevel.ACCESS_LEVEL getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel.ACCESS_LEVEL accessLevel) {
        this.accessLevel = accessLevel;
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.modelCount = models.size() ;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
