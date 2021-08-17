package it.eng.alidalab.applicationcatalogue.domain.workflow;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModelProperty;
import it.eng.alidalab.applicationcatalogue.domain.BDAApplication;
import it.eng.alidalab.applicationcatalogue.domain.Status;
import it.eng.alidalab.applicationcatalogue.domain.service.Service;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="workflows")
public class Workflow
{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @ApiModelProperty(notes = "The database generated Workflow ID")
    private Long id;

    @NonNull
    private String name;

    private String status;

    private Workflow.MODE mode; // batch | stream

    @Column(name="_translation", length = 4096)
    private String translation; //la stringa tradotta in DSL per Spring Cloud Data Flow

    private String targetPlatform;

    @Column(name="_procedure")
    private String procedure; //la stringa in linguaggio OWLS

    private String executionId; //per l'esecuzione di task e stream

    @JsonManagedReference
    @OneToMany(mappedBy="workflow", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Service> services;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Status statusModel;

    @JsonBackReference
    @ManyToOne
    private BDAApplication bdaApplication;

    public Workflow()
    {
    }

    public Workflow(String name, String status, MODE mode, String procedure, String translation)
    {
        this.name = name;
        this.status = status;
        this.mode = mode;
        this.translation = translation;
    }

    public Workflow(String name)
    {
        this.name = name;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Workflow.MODE getMode()
    {
        return mode;
    }

    public void setMode(Workflow.MODE mode)
    {
        this.mode = mode;
    }


    public String getTranslation()
    {
        return translation;
    }

    public void setTranslation(String translation)
    {
        this.translation = translation;
    }

    public String getTargetPlatform()
    {
        return targetPlatform;
    }

    public void setTargetPlatform(String targetPlatform)
    {
        this.targetPlatform = targetPlatform;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

   public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public void addServices(List<Service> services) {
        this.services.addAll(services);
    }

    public void removeService(Service service) {
        this.services.remove(service);
    }
    public String getProcedure()
    {
        return procedure;
    }

    public void setProcedure(String procedure)
    {
        this.procedure = procedure;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BDAApplication getBdaApplication() {
        return bdaApplication;
    }

    public void setBdaApplication(BDAApplication bdaApplication) {
        this.bdaApplication = bdaApplication;
    }

    public Status getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(Status statusModel) {
        this.statusModel = statusModel;
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
