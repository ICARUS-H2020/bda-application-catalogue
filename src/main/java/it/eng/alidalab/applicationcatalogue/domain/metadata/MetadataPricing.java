package it.eng.alidalab.applicationcatalogue.domain.metadata;

import lombok.Data;

import javax.persistence.*;

@Entity
public class MetadataPricing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "metadata_pricing_id")
    private Metadata metadata;

    public MetadataPricing() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}



      /*  Calculation Scheme – Compulsory, predefined values e.g.: “Fixed per application”, “PAYG”

        Amount – Compulsory, amount in euro, if not provided “Available upon request”

        Payment Method – Compulsory, predefined multi-select values e.g.: “Credit / Debit Card”, “Bank Transfer”, “Online Payment Services”, “Other”*/