package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.BDAApplication;
import it.eng.alidalab.applicationcatalogue.domain.service.Property;
import it.eng.alidalab.applicationcatalogue.domain.service.Service;
import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;
import it.eng.alidalab.applicationcatalogue.util.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

public class BDAApplicationRepositoryImpl implements BDAApplicationRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    @Transactional
    public Optional <BDAApplication> findBDAApplicationByBDAApplicationIdAndOrganizationIdAndOwnerId(Long bdaApplicationId, String organizationId, String ownerId, Boolean addDeleted) {
        String jpql = "";
            if(addDeleted==null || !addDeleted)
                jpql = "select b from BDAApplication b WHERE b.id = :bdaApplicationId AND  (b.accessLevel = :public OR (b.accessLevel = :team AND b.owner.organization.id = :organizationId) OR (b.accessLevel = :private AND b.owner.id = :ownerId)) AND b.deleteDateTime is null ";
            else
                jpql = "select b from BDAApplication b WHERE b.id = :bdaApplicationId AND  (b.accessLevel = :public OR (b.accessLevel = :team AND b.owner.organization.id = :organizationId) OR (b.accessLevel = :private AND b.owner.id = :ownerId)) ";
            Query query = em.createQuery(jpql);
            query = QueryUtils.createQueryWithAccesLevel(query,ownerId,organizationId);
            query.setParameter("bdaApplicationId", bdaApplicationId);
            Optional first = query.getResultList().stream().findFirst();
            if (first.isPresent()) {
                BDAApplication bdaApplication = (BDAApplication) first.get();
                bdaApplication.getWorkflows().forEach(w -> {w.getServices().size();});
                bdaApplication.getTags().size();
                bdaApplication.getDatasets().size();
                bdaApplication.getMetadata();
                bdaApplication.getVizConfigurations().size();
                first = Optional.of(bdaApplication);
            }
            return first;
        }

    @Override
    @Transactional
    public Collection<BDAApplication> findBDAApplicationsByOrganizationIdAndOwnerId(String organizationId, String ownerId, Boolean addDeleted) {
        String jpql = "";
        if(addDeleted==null || !addDeleted)
             jpql = "select b from BDAApplication b WHERE (b.accessLevel = :public OR (b.accessLevel = :team AND b.owner.organization.id = :organizationId) OR (b.accessLevel = :private AND b.owner.id = :ownerId))  AND b.deleteDateTime is null ";
        else
            jpql = "select b from BDAApplication b WHERE (b.accessLevel = :public OR (b.accessLevel = :team AND b.owner.organization.id = :organizationId) OR (b.accessLevel = :private AND b.owner.id = :ownerId)) ";

        Query query = em.createQuery(jpql);
        query = QueryUtils.createQueryWithAccesLevel(query,ownerId,organizationId);
        return query.getResultList();
    }

    @Transactional
    public BDAApplication updateField(BDAApplication bdaApplication, Map<String,Object> fields){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<BDAApplication> updateCriteria = cb.createCriteriaUpdate(BDAApplication.class);
        Root<BDAApplication> root = updateCriteria.from(BDAApplication.class);
// update property
        fields.forEach((k, v) -> {
            updateCriteria.set(root.get(k), v);
        });
// set where clause
        updateCriteria.where(cb.equal(root.get("id"), bdaApplication.getId()));
// update
        int affected = em.createQuery(updateCriteria).executeUpdate();
        em.refresh(bdaApplication);
        return bdaApplication;
    }

    @Transactional
    public BDAApplication updateField(BDAApplication bdaApplication, String key, Object field){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<BDAApplication> updateCriteria = cb.createCriteriaUpdate(BDAApplication.class);
        Root<BDAApplication> root = updateCriteria.from(BDAApplication.class);
// update property
        updateCriteria.set(key, field);

        updateCriteria.where(cb.equal(root.get("id"), bdaApplication.getId()));
// update
        int affected = em.createQuery(updateCriteria).executeUpdate();
        em.refresh(bdaApplication);
        return bdaApplication;
    }

    @Transactional
    public List<Long> getAllId(){
    return  em.createQuery("SELECT bda.id FROM BDAApplication bda")
            .getResultList();
    }


    @Transactional
    public void deleteByIdCustom(Long bdaApplicationId){
        CriteriaBuilder cb = em.getCriteriaBuilder();

    //selezione la bda
        CriteriaQuery<BDAApplication> criteriaBda = cb.createQuery(BDAApplication.class);
        Root<BDAApplication> root = criteriaBda.from(BDAApplication.class);
        criteriaBda.select(root).where(cb.equal(root.get("id"), bdaApplicationId));
        BDAApplication bdaApplication = em.createQuery(criteriaBda).getSingleResult();


    //creo un set dei wfId che compongono la bda
       List<Long> listWorkflowId = em.createQuery("SELECT wf.id FROM Workflow wf WHERE wf.bdaApplication.id = :bdaId")
                .setParameter("bdaId", bdaApplicationId)
                .getResultList();
        Set<Long> accept = new HashSet<Long>(listWorkflowId);

 /*
        CriteriaQuery<Workflow> criteriaWorkflow = cb.createQuery(Workflow.class);
        Root<Workflow> rootWorkflowId = criteriaWorkflow.from(Workflow.class);

        criteriaWorkflow.select(rootWorkflowId).where(cb.equal(root.get("id"), bdaApplicationId));
        Workflow workflow = em.createQuery(criteriaBda).getSingleResult();


     //cancello tutti i dataset di cui createdBy.id fa parte della lista su creata (quindi tutti i dataset creati per questa bda)
        CriteriaDelete<Dataset> criteriaDeleteDataset = cb.createCriteriaDelete(Dataset.class);
        Root<Dataset> rootDataset = criteriaDeleteDataset.from(Dataset.class);
        listWorkflowId.forEach(wfId -> {
            criteriaDeleteDataset.where(cb.equal(rootDataset.get("id"), wfId));
            em.createQuery(criteriaDeleteDataset).executeUpdate();
        });
*/

       /*

        em.createQuery("DELETE FROM Dataset ds WHERE ds.createdBy.id IN :wfList")
                .setParameter("wfList", listWorkflowId)
                .getSingleResult();

      CriteriaDelete<Dataset> criteriaDeleteDataset = cb.createCriteriaDelete(Dataset.class);
        Root<Dataset> rootDataset = criteriaDeleteDataset.from(Dataset.class);
        bdaApplication.getDatasets().forEach(ds -> {
            if(ds.getCreatedBy() != null && (accept.contains(ds.getCreatedBy().getId()))){
                criteriaDeleteDataset.where(cb.equal(rootDataset.get("id"), ds.getId()));
                em.createQuery(criteriaDeleteDataset).executeUpdate();
            }
        });*/


        //cancello tutti i workflow, i suoi servizi e le sue properties
        CriteriaDelete<Property> criteriaDeleteProperty = cb.createCriteriaDelete(Property.class);
        Root<Property> rootProperty = criteriaDeleteProperty.from(Property.class);

        CriteriaDelete<Service> criteriaDeleteService = cb.createCriteriaDelete(Service.class);
        Root<Service> rootService = criteriaDeleteService.from(Service.class);

        CriteriaDelete<Workflow> criteriaDeleteWorkflow = cb.createCriteriaDelete(Workflow.class);
        Root<Workflow> rootWf = criteriaDeleteWorkflow.from(Workflow.class);
        bdaApplication.getWorkflows().forEach(wf -> {
            //cancello prima i servizi
            wf.getServices().forEach(service -> {
                service.getProperties().forEach(property -> {
                    criteriaDeleteProperty.where(cb.equal(rootProperty.get("id"), property.getId()));
                    em.createQuery(criteriaDeleteProperty).executeUpdate();
                });
                criteriaDeleteService.where(cb.equal(rootService.get("id"), service.getId()));
                em.createQuery(criteriaDeleteService).executeUpdate();
            });
            criteriaDeleteWorkflow.where(cb.equal(rootWf.get("id"), wf.getId()));
            em.createQuery(criteriaDeleteWorkflow).executeUpdate();
        });
        em.flush();

        //cancello BDA
        CriteriaDelete<BDAApplication> criteriaDeleteBda = cb.createCriteriaDelete(BDAApplication.class);
        Root<BDAApplication> rootBDAApplication = criteriaDeleteBda.from(BDAApplication.class);

        criteriaDeleteBda.where(cb.equal(rootBDAApplication.get("id"), bdaApplicationId));
        em.createQuery(criteriaDeleteBda).executeUpdate();
        em.flush();
    }

    public void deleteAllCustom(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        //selezione la bda
        CriteriaQuery<BDAApplication> criteriaBda = cb.createQuery(BDAApplication.class);
        Root<BDAApplication> root = criteriaBda.from(BDAApplication.class);
        criteriaBda.select(root);
        List<BDAApplication> bdaApplicationList = em.createQuery(criteriaBda).getResultList();
        List<Long> bdasId = new ArrayList<>();

        bdaApplicationList.forEach(bda -> bdasId.add(bda.getId()));
        bdasId.forEach(id -> deleteByIdCustom(id));
    }


}
