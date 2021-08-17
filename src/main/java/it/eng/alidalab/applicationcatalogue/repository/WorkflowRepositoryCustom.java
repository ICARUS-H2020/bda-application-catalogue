package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.BDAApplication;
import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkflowRepositoryCustom {
    Workflow getWorkflow_WorkflowIdAndOrganizationIdAndOwnerId(Long workflowId, String organizationId, String ownerId);
    List<Workflow> getWorkflows_OrganizationIdAndOwnerId(String organizationId, String ownerId);

        Workflow getWorkflow_WorkflowNameAndOrganizationIdAndOwnerId(String workflowName, String organizationId, String ownerId);


    List<Workflow> findByOrganizationIdAndOwnerId(
            String ownerId,
            String organizationId);


    Workflow updateField(Workflow workflow, String key, Object value);

}
