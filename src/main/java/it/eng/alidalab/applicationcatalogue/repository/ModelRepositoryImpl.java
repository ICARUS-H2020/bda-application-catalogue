package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.Model;
import it.eng.alidalab.applicationcatalogue.util.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ModelRepositoryImpl implements ModelRepositoryCustom{

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EntityManagerFactory emf;

    @Override
    @Transactional
    public Model getModelByLocationAndOrganizationIdAndOwnerId(String location,  String organizationId, String ownerId){
        String jpql = "select m from Model m WHERE m.location = :location AND  (m.accessLevel = :public OR (m.accessLevel = :team AND m.owner.organization.id = :organizationId) OR (m.accessLevel = :private AND m.owner.id = :ownerId)) ";

        Query query = em.createQuery(jpql);
        query = QueryUtils.createQueryWithAccesLevel(query,ownerId,organizationId);
        query.setParameter("location", location);

        Optional first = query.getResultList().stream().findFirst();

        return first.isPresent()?(Model) first.get():null;
    }

    @Override
    @Transactional
    public Collection<Model> findModelsByOrganizationIdAndOwnerId(String organizationId, String ownerId) {

            String jpql = "select b from Model b WHERE (b.accessLevel = :public OR (b.accessLevel = :team AND b.owner.organization.id = :organizationId) OR (b.accessLevel = :private AND b.owner.id = :ownerId)) ";
            Query query = em.createQuery(jpql);
            query = QueryUtils.createQueryWithAccesLevel(query,ownerId,organizationId);
            List<Model> models = query.getResultList();
            return models;
        }
}
