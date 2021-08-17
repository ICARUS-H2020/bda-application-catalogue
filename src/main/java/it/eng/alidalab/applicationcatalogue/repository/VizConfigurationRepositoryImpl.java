package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.VizConfiguration;
import it.eng.alidalab.applicationcatalogue.util.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Optional;

public class VizConfigurationRepositoryImpl implements VizConfigurationRepositoryCustom{

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EntityManagerFactory emf;

    @Override
    @Transactional
    public VizConfiguration getVizConfiguration_VizConfigurationIdAndOrganizationIdAndOwnerId(Long VizConfigurationId, Long bdaId, String organizationId, String ownerId){
        String jpql = "select vc from BDAApplication b JOIN b.vizConfigurations vc WHERE b.id = :bdaId AND vc.id = :vizConfigurationId AND  (b.accessLevel = :public OR (b.accessLevel = :team AND b.owner.organization.id = :organizationId) OR (b.accessLevel = :private AND b.owner.id = :ownerId)) ";

        Query query = em.createQuery(jpql);
        query = QueryUtils.createQueryWithAccesLevel(query,ownerId,organizationId);
        query.setParameter("vizConfigurationId", VizConfigurationId);
        query.setParameter("bdaId", bdaId);


        Optional first = query.getResultList().stream().findFirst();

        return first.isPresent()?(VizConfiguration) first.get():null;
    }
}
