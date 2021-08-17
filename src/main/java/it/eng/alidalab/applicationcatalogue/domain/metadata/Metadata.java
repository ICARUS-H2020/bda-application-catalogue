package it.eng.alidalab.applicationcatalogue.domain.metadata;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Metadata  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @OneToOne(cascade = CascadeType.ALL)
    private MetadataGeneralInformation metadataGeneralInformation;

    @OneToOne(cascade = CascadeType.ALL)
    private MetadataLicenseDetails metadataLicenseDetails;

    @OneToOne(cascade = CascadeType.ALL)
    private MetadataPricing metadataPricing ;

    public Metadata() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MetadataLicenseDetails getMetadataLicenseDetails() {
        return metadataLicenseDetails;
    }

    public void setMetadataLicenseDetails(MetadataLicenseDetails metadataLicenseDetails) {
        this.metadataLicenseDetails = metadataLicenseDetails;
    }

    public MetadataGeneralInformation getMetadataGeneralInformation() {
        return metadataGeneralInformation;
    }

    public void setMetadataGeneralInformation(MetadataGeneralInformation metadataGeneralInformation) {
        this.metadataGeneralInformation = metadataGeneralInformation;
    }

    public MetadataPricing getMetadataPricing() {
        return metadataPricing;
    }

    public void setMetadataPricing(MetadataPricing metadataPricing) {
        this.metadataPricing = metadataPricing;
    }
}
