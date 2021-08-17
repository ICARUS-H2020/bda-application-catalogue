package it.eng.alidalab.applicationcatalogue.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.eng.alidalab.applicationcatalogue.domain.metadata.Metadata;
import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@ApiModel(description = "All details about the BDA Application. ")
public class BDAApplication  implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @ApiModelProperty(notes = "The database generated BDA Application ID")
    private Long id;

    @ApiModelProperty(notes = "Access Level" )
    @Enumerated(EnumType.STRING)
    private AccessLevel.ACCESS_LEVEL accessLevel = AccessLevel.ACCESS_LEVEL.TEAM;

    @NotBlank(message = "Name is mandatory")
    @ApiModelProperty(notes = "BDA Application Name")
    private String name;

    @ApiModelProperty(notes = "BDA Application Description")
    @Column(length = 4096)
    private String description;

    @ApiModelProperty(notes = "BDA Application Status")
    private String status;

    @OneToMany(cascade = {CascadeType.REMOVE}, mappedBy ="bdaApplication")
    @ToString.Exclude
    private List<Workflow> workflows;

    @ApiModelProperty(notes = "BDA Application Owner")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Owner owner;

    @OneToMany(mappedBy = "bdaApplication", cascade = CascadeType.ALL)
    private List<VizConfiguration> vizConfigurations;

    @ElementCollection
    private List<String> tags = new ArrayList<>();

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /*    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "bdaapplications_tags",
            joinColumns = @JoinColumn(name = "bdaapplication_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id",
                    referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet<>();

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }*/

    @OneToOne(cascade = CascadeType.ALL)
    private Metadata metadata;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private List<Dataset> datasets;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    private LocalDateTime deleteDateTime;

    public BDAApplication(@NotBlank(message = "Name is mandatory") String name, Owner owner) {
        this.name = name;
        this.owner = owner;
        this.accessLevel = AccessLevel.ACCESS_LEVEL.TEAM;
    }
    public BDAApplication(@NotBlank(message = "Name is mandatory") String name, Owner owner, List<Workflow> workflows) {
        this.name = name;
        this.owner = owner;
        this.workflows = workflows;
        this.accessLevel = AccessLevel.ACCESS_LEVEL.TEAM;
    }

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

    public BDAApplication() {}

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

    public LocalDateTime getDeleteDateTime() {
        return deleteDateTime;
    }

    public void setDeleteDateTime(LocalDateTime deleteDateTime) {
        this.deleteDateTime = deleteDateTime;
    }
}
