package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.BDAApplication;
import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkflowRepository extends JpaRepository<Workflow, Long>, WorkflowRepositoryCustom{


    Workflow findByName(String workflowName);

    List<Workflow> findAllByBdaApplication (BDAApplication bda);

    /*
    @Query("select w from BDAApplication b JOIN b.workflows w WHERE w.id = :workflowId AND (b.ACCESS_LEVEL = b.ACCESS_LEVEL.PUBLIC OR (b.ACCESS_LEVEL = b.ACCESS_LEVEL.TEAM AND b.owner.organization.id = :organizationId) OR (b.ACCESS_LEVEL = b.ACCESS_LEVEL.PRIVATE AND b.owner.id = :ownerId)) ")
    Workflow findByWorkflowIdAndOrganizationIdAndOwnerId(
            @Param("workflowId") Long workflowId,
            @Param("ownerId") String ownerId,
            @Param("organizationId") String organizationId);


    @Query("select w from BDAApplication b JOIN b.workflows w WHERE w.id = :workflowId AND (b.ACCESS_LEVEL = :team)")
    Workflow findByWorkflowIdAndOrganizationIdAndOwnerId(
            @Param("workflowId") Long workflowId,
            @Param("team") String team);




    @Query("select w from BDAApplication b JOIN b.workflows w WHERE (b.ACCESS_LEVEL = b.ACCESS_LEVEL.PUBLIC OR (b.ACCESS_LEVEL = b.ACCESS_LEVEL.TEAM AND b.owner.organization.id = :organizationId) OR (b.ACCESS_LEVEL = b.ACCESS_LEVEL.PRIVATE AND b.owner.id = :ownerId)) ")
    List<Workflow> findByOrganizationIdAndOwnerId(
            @Param("ownerId") String ownerId,
            @Param("organizationId") String organizationId);

     */



}
