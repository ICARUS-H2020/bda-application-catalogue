package it.eng.alidalab.applicationcatalogue.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModelProperty;
import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Dataset implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long refId;

    private String name;

    private String type;

    @Column(columnDefinition = "boolean default true")
    private Boolean created;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Workflow createdBy;

    @OneToOne(cascade = CascadeType.ALL)
    private DatasetFileType datasetFileType;

    @OneToOne(cascade = CascadeType.ALL)
    private DatasetSourceType datasetSourceType;

    @ApiModelProperty(notes = "Information to connect to the data storage" )
    private String infoConnection;

    @ApiModelProperty(notes = "Access Level" )
    @Enumerated(EnumType.STRING)
    private AccessLevel.ACCESS_LEVEL accessLevel;

    @OneToOne(cascade = {CascadeType.DETACH})
    private Owner owner;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDateTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @ManyToMany(mappedBy="datasets")
    @JsonBackReference
    private List<BDAApplication> bdaApplications;

    public Dataset() {
    }

    public Boolean getCreated() {
        return created;
    }

    public void setCreated(Boolean created) {
        this.created = created;
    }

    public Workflow getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Workflow createdByWorkflow) {
        this.createdBy = createdByWorkflow;
    }

    public String getInfoConnection() {
        return infoConnection;
    }

    public void setInfoConnection(String infoConnection) {
        this.infoConnection = infoConnection;
    }

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

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BDAApplication> getBdaApplications() {
        return bdaApplications;
    }

    public void setBdaApplications(List<BDAApplication> bdaApplications) {
        this.bdaApplications = bdaApplications;
    }

    public DatasetFileType getDatasetFileType() {
        return datasetFileType;
    }

    public void setDatasetFileType(DatasetFileType datasetFileType) {
        this.datasetFileType = datasetFileType;
    }

    public DatasetSourceType getDatasetSourceType() {
        return datasetSourceType;
    }

    public void setDatasetSourceType(DatasetSourceType datasetSourceType) {
        this.datasetSourceType = datasetSourceType;
    }
}
