package it.eng.alidalab.applicationcatalogue.domain.workflow;


import io.swagger.annotations.ApiModelProperty;
import it.eng.alidalab.applicationcatalogue.domain.service.Service;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
public class WorkflowDTO
{
    @ApiModelProperty(notes = "The database generated Workflow ID")
    private Long id;

    @NonNull
    private String name;

    private String status;

    private WorkflowDTO.MODE mode; // batch | stream

    private String translation; //la stringa tradotta in DSL per Spring Cloud Data Flow

    private String targetPlatform;

    private String procedure; //la stringa in linguaggio OWLS

    private String executionId; //per l'esecuzione di task e stream

    private List<Service> services;

    public WorkflowDTO()
    {
    }

    public WorkflowDTO(String name, String status, MODE mode, String procedure, String translation)
    {
        this.name = name;
        this.status = status;
        this.mode = mode;
        this.translation = translation;
    }

    public static enum MODE
    {
        BATCH,
        STREAM;

        private MODE()
        {
        }
    }

}
