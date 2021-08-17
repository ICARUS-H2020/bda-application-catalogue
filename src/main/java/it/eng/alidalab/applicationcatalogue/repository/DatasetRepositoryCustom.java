package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.Dataset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DatasetRepositoryCustom {
    Dataset getDataset_DatasetIdAndOrganizationIdAndOwnerId(Long datasetId, String organizationId, String ownerId);

    List<Dataset> findByOrganizationIdAndOwnerId(
            String ownerId,
            String organizationId, Map queryParams);

    List<Dataset> findByOrganizationIdAndOwnerId(
            String ownerId,
            String organizationId);

    Dataset updateField(Dataset dataset, HashMap<String, Object> datasetPartialSecurity);
}
