package it.eng.alidalab.applicationcatalogue.domain.metadata;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
public class MetadataGeneralInformation {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    enum CategoryType {}
    enum AccessibilyType {}



    @Enumerated(EnumType.STRING)
    private CategoryType category;

    @OneToMany(mappedBy="metadataGeneralInformation")
    private List<MetadataTag> tags;

    @Enumerated(EnumType.STRING)
    private AccessibilyType accessibility;

    private String version;

    public MetadataGeneralInformation() {
    }

    public CategoryType getCategory() {
        return category;
    }

    public void setCategory(CategoryType category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<MetadataTag> getTags() {
        return tags;
    }

    public void setTags(List<MetadataTag> tags) {
        this.tags = tags;
    }

    public AccessibilyType getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(AccessibilyType accessibility) {
        this.accessibility = accessibility;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
