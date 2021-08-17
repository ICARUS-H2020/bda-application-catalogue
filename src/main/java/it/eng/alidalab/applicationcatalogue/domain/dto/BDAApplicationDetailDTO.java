package it.eng.alidalab.applicationcatalogue.domain.dto;

import it.eng.alidalab.applicationcatalogue.domain.*;
import it.eng.alidalab.applicationcatalogue.domain.metadata.Metadata;
import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BDAApplicationDetailDTO implements Serializable {

    private Long id;
    private AccessLevel.ACCESS_LEVEL accessLevel = AccessLevel.ACCESS_LEVEL.TEAM;
    private String name;
    private String description;
    private String status;
    private List<Workflow> workflows;
    private Owner owner;
    private List<VizConfiguration> vizConfigurations;
    private List<String> tags = new ArrayList<>();
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    private Metadata metadata;
    private List<Dataset> datasets;
    private Set<Model> models;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BDAApplicationDetailDTO() {}

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

    public List<Workflow> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(List<Workflow> workflows) {
        this.workflows = workflows;
        Set<Model> models = new HashSet<>();
        workflows.stream().forEach(w -> {
            w.getServices().stream()
                    .filter(s -> s.getModels() != null)
                    .forEach(se -> models.addAll(se.getModels()));
        });
        if(models.size()>0) this.setModels(models);

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
        datasets.forEach(d -> {
            if(d.getCreatedBy()!=null){
                String status = d.getCreatedBy().getStatus();
                Status statusModel = d.getCreatedBy().getStatusModel();
                Long id = d.getCreatedBy().getId();
                String name = d.getCreatedBy().getName();

                d.setCreatedBy(new Workflow());
                d.getCreatedBy().setName(name);
                d.getCreatedBy().setStatus(status);
                d.getCreatedBy().setId(id);
                d.getCreatedBy().setStatusModel(statusModel);
            }
        });
        this.datasets = datasets;

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

    public Set<Model> getModels() {
        return models;
    }

    public void setModels(Set<Model> models) {
        this.models = models;
    }
}
