package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.BDAApplication;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BDAApplicationRepositoryCustom{

    public Optional <BDAApplication> findBDAApplicationByBDAApplicationIdAndOrganizationIdAndOwnerId(Long bdaApplicationId, String organizationId, String ownerId, Boolean addDeleted);
    public Collection<BDAApplication> findBDAApplicationsByOrganizationIdAndOwnerId(String organizationId, String ownerId, Boolean addDeleted);
    public BDAApplication updateField(BDAApplication bdaApplication, Map<String,Object> fields);
    public BDAApplication updateField(BDAApplication bdaApplication, String key, Object field);
    public void deleteByIdCustom(Long id);
    public List<Long> getAllId();
    void deleteAllCustom();
}
