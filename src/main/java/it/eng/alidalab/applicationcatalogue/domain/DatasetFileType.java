package it.eng.alidalab.applicationcatalogue.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
public class DatasetFileType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Dataset dataset;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }
}
