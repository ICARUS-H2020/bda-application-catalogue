package it.eng.alidalab.applicationcatalogue.domain.metadata;

import lombok.Data;

import javax.persistence.*;


@Entity
public class MetadataTag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String value;

    @ManyToOne
    private MetadataGeneralInformation metadataGeneralInformation;

    public MetadataGeneralInformation getMetadataGeneralInformation() {
        return metadataGeneralInformation;
    }

    public void setMetadataGeneralInformation(MetadataGeneralInformation metadataGeneralInformation) {
        this.metadataGeneralInformation = metadataGeneralInformation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}