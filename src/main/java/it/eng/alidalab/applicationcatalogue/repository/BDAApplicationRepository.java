package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.BDAApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Collection;

public interface BDAApplicationRepository extends JpaRepository<BDAApplication, Long>, BDAApplicationRepositoryCustom {

    Collection<BDAApplication> findByOwner_Id(String ownerId);

    void deleteByIdAndOwner_Id(Long id, String ownerId);

    Collection<BDAApplication> findByOwner_Organization_id(String organizationId);

    //BDAApplication findByIdAndOwner_Organization_id(Long bdaId, String organizationId);

    void deleteByIdAndOwner_Organization_id(Long bdaId, String organizationId);

}
