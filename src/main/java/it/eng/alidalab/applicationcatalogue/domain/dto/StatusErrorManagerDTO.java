package it.eng.alidalab.applicationcatalogue.domain.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import it.eng.alidalab.applicationcatalogue.domain.AccessLevel;
import it.eng.alidalab.applicationcatalogue.domain.Owner;
import it.eng.alidalab.applicationcatalogue.domain.Status;

public class StatusErrorManagerDTO extends Status {
    private Owner owner;
    private AccessLevel.ACCESS_LEVEL accessLevel;
    private Long workflowId;
    private ObjectNode bda;

    public StatusErrorManagerDTO() {
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public AccessLevel.ACCESS_LEVEL getAccessLevel() {
        return accessLevel;
    }

    public ObjectNode getBda() {
        return bda;
    }

    public void setBda(ObjectNode bda) {
        this.bda = bda;
    }

    public void setAccessLevel(AccessLevel.ACCESS_LEVEL accessLevel) {
        this.accessLevel = accessLevel;
    }
}
