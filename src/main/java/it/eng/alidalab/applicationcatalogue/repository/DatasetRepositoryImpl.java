package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.Dataset;
import it.eng.alidalab.applicationcatalogue.util.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DatasetRepositoryImpl implements DatasetRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Dataset getDataset_DatasetIdAndOrganizationIdAndOwnerId(Long datasetId, String organizationId, String ownerId){
        String jpql = "select b from Dataset b WHERE b.id = :id AND (b.accessLevel = :public OR (b.accessLevel = :team AND b.owner.organization.id = :organizationId) OR (b.accessLevel = :private AND b.owner.id = :ownerId)) ";

        Query query = em.createQuery(jpql);
        query = QueryUtils.createQueryWithAccesLevel(query,ownerId,organizationId);
        query.setParameter("id", datasetId);

        Optional first = query.getResultList().stream().findFirst();
        return first.isPresent()?(Dataset) first.get():null;
    }

    @Override
    @Transactional
    public List<Dataset> findByOrganizationIdAndOwnerId(String ownerId, String organizationId) {
        return findByOrganizationIdAndOwnerId( ownerId,  organizationId, null);
    }

    @Override
    @Transactional
    public List<Dataset> findByOrganizationIdAndOwnerId(String ownerId, String organizationId, Map queryParams) {
        String jpql = "";
        Query query = null;
        if(queryParams!= null && queryParams.containsKey("workflowName") && queryParams.containsKey("output") && (Boolean.parseBoolean(""+queryParams.get("output")) )) {
            jpql = "select d from Dataset d  WHERE d.createdBy.name = :workflowName AND (d.accessLevel = :public OR (d.accessLevel = :team AND d.owner.organization.id = :organizationId) OR (d.accessLevel = :private AND d.owner.id = :ownerId)) ";
            query = em.createQuery(jpql);
            query.setParameter("workflowName", queryParams.get("workflowName"));
        }else{
            jpql = "select d from Dataset d  WHERE  (d.accessLevel = :public OR (d.accessLevel = :team AND d.owner.organization.id = :organizationId) OR (d.accessLevel = :private AND d.owner.id = :ownerId)) ";
            query = em.createQuery(jpql);
        }

        query = QueryUtils.createQueryWithAccesLevel(query,ownerId,organizationId);
        return (List<Dataset>) query.getResultList();
    }

    @Override
    @Transactional
    public Dataset updateField(Dataset dataset, HashMap<String, Object> datasetPartialSecurity) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Dataset> updateCriteria = cb.createCriteriaUpdate(Dataset.class);
        Root<Dataset> root = updateCriteria.from(Dataset.class);
// update property
        datasetPartialSecurity.forEach((k, v) -> {
            updateCriteria.set(root.get(k), v);
        });

        updateCriteria.where(cb.equal(root.get("id"), dataset.getId()));
// update
        int affected = em.createQuery(updateCriteria).executeUpdate();
        em.refresh(dataset);
        dataset.getCreatedBy();
        return dataset;
    }


}
