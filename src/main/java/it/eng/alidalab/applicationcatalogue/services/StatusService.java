package it.eng.alidalab.applicationcatalogue.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.eng.alidalab.applicationcatalogue.domain.Status;
import it.eng.alidalab.applicationcatalogue.domain.dto.StatusErrorManagerDTO;
import it.eng.alidalab.applicationcatalogue.domain.service.Service;
import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;
import it.eng.alidalab.applicationcatalogue.repository.BDAApplicationRepository;
import it.eng.alidalab.applicationcatalogue.repository.ServiceRepository;
import it.eng.alidalab.applicationcatalogue.repository.StatusRepository;
import it.eng.alidalab.applicationcatalogue.repository.WorkflowRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;


@Transactional
public class StatusService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Inject
    private StatusRepository statusRepository;

    @Inject
    private WorkflowRepository workflowRepository;

    @Inject
    private BDAApplicationRepository bdaApplicationRepository;

    @Inject
    private ServiceRepository serviceRepository;

    @Transactional
    public StatusErrorManagerDTO updateStatus(Status status, String wfName) {
        Workflow workflow = workflowRepository.findByName(wfName);
        if(workflow != null){
            workflow.setStatusModel(status);
            statusRepository.delete(workflow.getStatusModel());
            //status.setWorkflow(workflow);
            status = statusRepository.save(status);
            workflow.setStatusModel(status);
            updateAssets(workflow);
            StatusErrorManagerDTO statusErrorManagerDTO = modelMapper.map(status, StatusErrorManagerDTO.class);
            statusErrorManagerDTO.setAccessLevel(workflow.getBdaApplication().getAccessLevel());
            statusErrorManagerDTO.setOwner(workflow.getBdaApplication().getOwner());
            statusErrorManagerDTO.setWorkflowId(workflow.getId());
            ObjectNode bdaJson = objectMapper.createObjectNode();
            bdaJson.put("id", workflow.getBdaApplication().getId());
            bdaJson.put("status", workflow.getBdaApplication().getStatus());
            statusErrorManagerDTO.setBda(bdaJson);
            return statusErrorManagerDTO;
        }
        throw new BadRequestException();
    }

    private void updateAssets(Workflow workflow) {
        workflowRepository.updateField(workflow, "status", workflow.getStatusModel().getStatus());
        //TODO cambiare la logica quando i wf dentro la bda sono più di uno
        bdaApplicationRepository.updateField(workflow.getBdaApplication(), "status", workflow.getStatusModel().getStatus());

    }

    @Transactional
    public StatusErrorManagerDTO updateStatus(Status status, String wfName, String serviceName) {
        Workflow workflow = workflowRepository.findByName(wfName);
        if(workflow != null && workflow.getServices()!= null){
            Service service = workflow.getServices().stream().filter(s -> s.getName().equals(serviceName)).findFirst().get();
            if(service!=null) {
                //status = statusRepository.save(status);
                if(service.getStatusModel()!=null)statusRepository.delete(service.getStatusModel());
                status = statusRepository.save(status);
                service.setStatusModel(status);
                service = serviceRepository.save(service);
                StatusErrorManagerDTO statusErrorManagerDTO = modelMapper.map(service.getStatusModel(), StatusErrorManagerDTO.class);
                statusErrorManagerDTO.setAccessLevel(workflow.getBdaApplication().getAccessLevel());
                statusErrorManagerDTO.setOwner(workflow.getBdaApplication().getOwner());
                statusErrorManagerDTO.setWorkflowId(workflow.getId());
                ObjectNode bdaJson = objectMapper.createObjectNode();
                bdaJson.put("id", workflow.getBdaApplication().getId());
                bdaJson.put("status", workflow.getBdaApplication().getStatus());
                statusErrorManagerDTO.setBda(bdaJson);
                return statusErrorManagerDTO;
            }
        }
        throw new BadRequestException();
    }
}
