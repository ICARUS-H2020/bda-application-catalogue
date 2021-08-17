package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.Model;

import java.util.Collection;

public interface ModelRepositoryCustom {

    public Model getModelByLocationAndOrganizationIdAndOwnerId(String location, String organizationId, String ownerId);

    Collection<Model> findModelsByOrganizationIdAndOwnerId(String organizationId, String ownerId);
}
