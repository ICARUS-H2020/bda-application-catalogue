package it.eng.alidalab.applicationcatalogue.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.annotations.ApiModelProperty;
import it.eng.alidalab.applicationcatalogue.domain.service.Framework;
import it.eng.alidalab.applicationcatalogue.domain.service.Service;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Model {
    public Model() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String algorithmName;

    @OneToOne(fetch = FetchType.EAGER)
    private Framework framework;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Dataset trainingDataset;


    @ElementCollection
    private List<String> tags = new ArrayList<>();

    @Column(length = 4096)
    private String inputs;

    private String targets;

    private String description;

    @Column(length = 2048)
    private String location;

    private ArrayNode properties;

    private String name;

    @ApiModelProperty(notes = "Access Level")
    @Enumerated(EnumType.STRING)
    private AccessLevel.ACCESS_LEVEL accessLevel;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private Owner owner;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @ManyToMany(mappedBy="models")
    @JsonBackReference
    private List<Service> services;

    public AccessLevel.ACCESS_LEVEL getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel.ACCESS_LEVEL accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
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

/*    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }*/

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public Framework getFramework() {
        return framework;
    }

    public void setFramework(Framework framework) {
        this.framework = framework;
    }

    public Dataset getTrainingDataset() {
        return trainingDataset;
    }

    public void setTrainingDataset(Dataset trainingDataset) {
        this.trainingDataset = trainingDataset;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getInputs() {
        return inputs;
    }

    public void setInputs(String inputs) {
        this.inputs = inputs;
    }

    public String getTargets() {
        return targets;
    }

    public void setTargets(String targets) {
        this.targets = targets;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayNode getProperties() {
        return properties;
    }

    public void setProperties(ArrayNode properties) {
        this.properties = properties;
    }
}