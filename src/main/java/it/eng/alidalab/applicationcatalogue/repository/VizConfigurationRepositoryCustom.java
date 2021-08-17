package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.VizConfiguration;

public interface VizConfigurationRepositoryCustom {

    VizConfiguration getVizConfiguration_VizConfigurationIdAndOrganizationIdAndOwnerId(Long VizConfigurationId, Long bdaId, String organizationId, String ownerId);
    }
