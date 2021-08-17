package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;
import it.eng.alidalab.applicationcatalogue.util.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public class WorkflowRepositoryImpl implements WorkflowRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EntityManagerFactory emf;

    @Override
    @Transactional
    public Workflow getWorkflow_WorkflowIdAndOrganizationIdAndOwnerId(Long workflowId, String organizationId, String ownerId){
        String jpql = "select w from BDAApplication b JOIN b.workflows w WHERE w.id = :workflowId AND  (b.accessLevel = :public OR (b.accessLevel = :team AND b.owner.organization.id = :organizationId) OR (b.accessLevel = :private AND b.owner.id = :ownerId)) ";
        Query query = em.createQuery(jpql);
        query = QueryUtils.createQueryWithAccesLevel(query,ownerId,organizationId);
        query.setParameter("workflowId", workflowId);
        Optional first = query.getResultList().stream().findFirst();
        return first.isPresent()?(Workflow) first.get():null;
    }

    @Override
    @Transactional
    public Workflow getWorkflow_WorkflowNameAndOrganizationIdAndOwnerId(String workflowName, String organizationId, String ownerId){
        String jpql = "select w from BDAApplication b JOIN b.workflows w WHERE w.name = :workflowName AND  (b.accessLevel = :public OR (b.accessLevel = :team AND b.owner.organization.id = :organizationId) OR (b.accessLevel = :private AND b.owner.id = :ownerId)) ";
        Query query = em.createQuery(jpql);
        query = QueryUtils.createQueryWithAccesLevel(query,ownerId,organizationId);
        query.setParameter("workflowName", workflowName);
        Optional first = query.getResultList().stream().findFirst();
        return first.isPresent()?(Workflow) first.get():null;
    }

    @Override
    @Transactional
    public List<Workflow> getWorkflows_OrganizationIdAndOwnerId(String organizationId, String ownerId){
        String jpql = "select w from BDAApplication b JOIN b.workflows w WHERE (b.accessLevel = :public OR (b.accessLevel = :team AND b.owner.organization.id = :organizationId) OR (b.accessLevel = :private AND b.owner.id = :ownerId)) ";
        Query query = em.createQuery(jpql);
        query = QueryUtils.createQueryWithAccesLevel(query,ownerId,organizationId);
        return (List<Workflow>) query.getResultList();
    }

    @Override
    @Transactional
    public List<Workflow> findByOrganizationIdAndOwnerId(String ownerId, String organizationId) {
        String jpql = "select w from BDAApplication b JOIN b.workflows w WHERE  (b.accessLevel = :public OR (b.accessLevel = :team AND b.owner.organization.id = :organizationId) OR (b.accessLevel = :private AND b.owner.id = :ownerId)) ";

        Query query = em.createQuery(jpql);
        query = QueryUtils.createQueryWithAccesLevel(query,ownerId,organizationId);
        return (List<Workflow>) query.getResultList();
    }

    @Override
    @Transactional
    public Workflow updateField(Workflow workflow, String key, Object value){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Workflow> updateCriteria = cb.createCriteriaUpdate(Workflow.class);
        Root<Workflow> root = updateCriteria.from(Workflow.class);
// update property
        updateCriteria.set(root.get(key), value);
// set where clause
        updateCriteria.where(cb.equal(root.get("id"), workflow.getId()));
// update
        int affected = em.createQuery(updateCriteria).executeUpdate();
        if (affected==1) workflow.setStatus((String)value);
        return workflow;
    }

}
