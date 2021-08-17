package it.eng.alidalab.applicationcatalogue.domain.metadata;

import io.swagger.annotations.ApiModelProperty;
import it.eng.alidalab.applicationcatalogue.domain.Owner;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;

import java.util.Collection;


@Entity
@Validated
public class MetadataLicenseDetails {

public enum PrivacyLevel {Public, Private, Confidential};
public enum Derivation {Modify, Excerpt, Aggregate};
public enum RequiredYesNot {Required, Not_Required}
public enum AllowedYesNot {Allowed, Prohibited}
public enum TargetPurpose {Business, Academic_Scientific, Personal, Non_profit}
public enum TargetIndustry {Limited_to_Aviation_only, Excluding_Aviation, All}


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "The database generated BDA Application ID")
    private Long id;

    @NonNull
    @ApiModelProperty(notes = "License")
    private String license;

    @OneToOne
    private Owner owner;

    private PrivacyLevel privacyLevel;

    @ElementCollection(targetClass=Derivation.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name="license_derivation")
    @Column(name="derivation")
    private Collection<Derivation> derivation;

    @Enumerated(EnumType.STRING)
    @NonNull
    private  RequiredYesNot attribution;

    @Enumerated(EnumType.STRING)
    @NonNull
    private  AllowedYesNot reproduction;


    @Enumerated(EnumType.STRING)
    @NonNull
    private  AllowedYesNot distribution;

    @Enumerated(EnumType.STRING)
    @NonNull
    private  TargetIndustry targetIndustry;

    @Enumerated(EnumType.STRING)
    @NonNull
    private  TargetPurpose targetPurpose;

    private Long targetIndustryLimitedToAviationOnlyCategoryId;

    private Long targetIndustryLimitedToAviationOnlyOrganizationId;

    @Enumerated(EnumType.STRING)
    @NonNull
    private AllowedYesNot reContext;


    public MetadataLicenseDetails() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public PrivacyLevel getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(PrivacyLevel privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public Collection<Derivation> getDerivation() {
        return derivation;
    }

    public void setDerivation(Collection<Derivation> derivation) {
        this.derivation = derivation;
    }

    public RequiredYesNot getAttribution() {
        return attribution;
    }

    public void setAttribution(RequiredYesNot attribution) {
        this.attribution = attribution;
    }

    public AllowedYesNot getReproduction() {
        return reproduction;
    }

    public void setReproduction(AllowedYesNot reproduction) {
        this.reproduction = reproduction;
    }

    public AllowedYesNot getDistribution() {
        return distribution;
    }

    public void setDistribution(AllowedYesNot distribution) {
        this.distribution = distribution;
    }

    public TargetIndustry getTargetIndustry() {
        return targetIndustry;
    }

    public void setTargetIndustry(TargetIndustry targetIndustry) {
        this.targetIndustry = targetIndustry;
    }

    public TargetPurpose getTargetPurpose() {
        return targetPurpose;
    }

    public void setTargetPurpose(TargetPurpose targetPurpose) {
        this.targetPurpose = targetPurpose;
    }

    public Long getTargetIndustryLimitedToAviationOnlyCategoryId() {
        return targetIndustryLimitedToAviationOnlyCategoryId;
    }

    public void setTargetIndustryLimitedToAviationOnlyCategoryId(Long targetIndustryLimitedToAviationOnlyCategoryId) {
        this.targetIndustryLimitedToAviationOnlyCategoryId = targetIndustryLimitedToAviationOnlyCategoryId;
    }

    public Long getTargetIndustryLimitedToAviationOnlyOrganizationId() {
        return targetIndustryLimitedToAviationOnlyOrganizationId;
    }

    public void setTargetIndustryLimitedToAviationOnlyOrganizationId(Long targetIndustryLimitedToAviationOnlyOrganizationId) {
        this.targetIndustryLimitedToAviationOnlyOrganizationId = targetIndustryLimitedToAviationOnlyOrganizationId;
    }

    public AllowedYesNot getReContext() {
        return reContext;
    }

    public void setReContext(AllowedYesNot reContext) {
        this.reContext = reContext;
    }
}
