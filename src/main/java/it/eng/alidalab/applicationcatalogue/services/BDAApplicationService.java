package it.eng.alidalab.applicationcatalogue.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.eng.alidalab.applicationcatalogue.domain.BDAApplication;
import it.eng.alidalab.applicationcatalogue.domain.Dataset;
import it.eng.alidalab.applicationcatalogue.domain.Model;
import it.eng.alidalab.applicationcatalogue.domain.metadata.Metadata;
import it.eng.alidalab.applicationcatalogue.domain.service.ApplicationProperty;
import it.eng.alidalab.applicationcatalogue.domain.service.Property;
import it.eng.alidalab.applicationcatalogue.domain.service.Service;
import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;
import it.eng.alidalab.applicationcatalogue.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class BDAApplicationService {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ModelMapper modelMapper;
    @Inject
    private BDAApplicationRepository bdaApplicationRepository;
    @Inject
    private OwnerRepository ownerRepo;
    @Inject
    private DatasetRepository datasetRepository;
    @Inject
    private DatasetService datasetService;
    @Inject
    private OrganizationRepository organizationRepository;
    @Inject
    private WorkflowRepository workflowRepository;
    @Inject
    private MetadataRepository metadataRepository;
    @Inject
    private ServiceService serviceService;
    @Inject
    private ServiceRepository serviceRepository;
    @Inject
    private OwnerService ownerService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private WorkflowService workflowService;

    //public List<BDAApplication> findAll(){ return bdaApplicationRepository.findAll(); }

    //public Optional<BDAApplication> findById(Long bdaId){return bdaApplicationRepository.findById(bdaId);}


    @Transactional
    public BDAApplication save(BDAApplication bdaApplication) {
        if (bdaApplication.getId() != null)
            throw new IllegalArgumentException("BDA Id must be null");
        bdaApplication.getWorkflows().forEach(w -> {
            if(w.getId()!=null) throw new IllegalArgumentException("WorkflowId must be null");});

        if (bdaApplication.getOwner() == null || bdaApplication.getOwner().getId() == null)
            throw new IllegalArgumentException("Owner ID is required");
        if (bdaApplication.getOwner() == null || bdaApplication.getOwner().getName() == null)
            throw new IllegalArgumentException("Owner Name is required");
        if (bdaApplication.getOwner().getOrganization() == null || bdaApplication.getOwner().getOrganization().getId() == null)
            throw new IllegalArgumentException("Organization ID is required");
        if (bdaApplication.getOwner().getOrganization() == null || bdaApplication.getOwner().getOrganization().getName() == null)
            throw new IllegalArgumentException("Organization Name is required");
        if (bdaApplication.getWorkflows() == null || bdaApplication.getWorkflows().size() == 0) {
            throw new IllegalArgumentException("A Workflow is required into BDA Application");
        }
        bdaApplication.getWorkflows().forEach(workflow -> {
            if (workflow.getServices() == null || workflow.getServices().size() == 0) {
                throw new IllegalArgumentException("Services are required into Workflows");
            }
        });

        bdaApplication.setOwner(ownerService.getOwnerDb(bdaApplication.getOwner()));

        if (bdaApplication.getMetadata() != null && bdaApplication.getMetadata().getMetadataLicenseDetails() != null && bdaApplication.getMetadata().getMetadataLicenseDetails().getOwner() != null) {
            bdaApplication.getMetadata().getMetadataLicenseDetails().setOwner(ownerService.getOwnerDb(bdaApplication.getMetadata().getMetadataLicenseDetails().getOwner()));

            /*Optional<Owner> ownerOp2 = ownerRepo.findById(bdaApplication.getMetadata().getMetadataLicenseDetails().getOwner().getId());
            if(!ownerOp2.isPresent())
                bdaApplication.getMetadata().getMetadataLicenseDetails().setOwner(ownerRepo.save(bdaApplication.getMetadata().getMetadataLicenseDetails().getOwner()));
            else{
                bdaApplication.getMetadata().getMetadataLicenseDetails().setOwner(ownerOp2.get());
            }*/
            Metadata metadata = metadataRepository.save(bdaApplication.getMetadata());
            bdaApplication.setMetadata(metadata);
        }
        //List<Dataset> dataSetTemp = bdaApplication.getDatasets();
        //bdaApplication.getDatasets().forEach(ds -> {ds.setOwner(datasetRepository.save(ds.getOwner()))});
        /*bdaApplication.getDatasets().forEach(dt -> {
            dt.setOwner(getOwnerDb(dt.getOwner()));
            *//*Optional<Owner> ownerOp = ownerRepo.findById(dt.getOwner().getId());
            if(!ownerOp.isPresent())
                dt.setOwner(ownerRepo.save(dt.getOwner()));
            else{
                dt.setOwner(ownerOp.get());
            }*//*
        });*/
        bdaApplication.setStatus(bdaApplication.getWorkflows().get(0).getStatus()); //TODO vale per un solo WF (icarus)

        bdaApplication = setWorkflowAndAssets(bdaApplication);

        /*
        if(bdaApplication.getWorkflow().getServices()!=null && bdaApplication.getWorkflow().getServices().size()>0) {
            bdaApplication.getWorkflow().getServices().forEach(s -> {
                s.setWorkflow(bda.getWorkflow());
            });
            bdaApplication.getWorkflow().setServices(serviceService.saveAll(bdaApplication.getWorkflow().getServices()));
        }
        */
        return bdaApplication;
    }

    private BDAApplication setWorkflowAndAssets(BDAApplication bdaApplication) {
        bdaApplication.getWorkflows().forEach(wf -> {
            wf.getServices().forEach(s -> {
                s.setId(null);
                s.getProperties().forEach(p -> {
                    p.setId(null);
                });
            });
        });
        bdaApplication.setDatasets(new ArrayList<>());
        bdaApplication = setDatasets(bdaApplication);
        bdaApplication = setModels(bdaApplication);
        //bdaApplication.setTags(tagRepository.saveAll(bdaApplication.getTags()));

        BDAApplication bda = bdaApplicationRepository.save(bdaApplication);

        if (bdaApplication.getWorkflows() != null) {
            bdaApplication.getWorkflows().forEach(wf -> {
                wf.setBdaApplication(bda);
            });
            List<Workflow> workflows = workflowRepository.saveAll(bdaApplication.getWorkflows());
            bda.setWorkflows(workflows);
        }
        bda.getWorkflows().forEach(workflow -> {
            workflow.getServices().forEach(s -> {
                s.setWorkflow(workflow);
            });
            workflow.setServices(serviceService.saveAll(workflow.getServices()));
        });
        workflowRepository.saveAll(bda.getWorkflows());
        workflowService.deleteOld(bdaApplication);
        return bda;
    }

    //il metodo va bene solo per la creazione di modelli di input e output essendo presenti solo una volta all'interno di un servizio, ad esempio non prevede un servizio che crea due modelli
    private BDAApplication setModels(BDAApplication bdaApplication) {
        if (bdaApplication.getWorkflows() != null && bdaApplication.getWorkflows().size() > 0)
            bdaApplication.getWorkflows().forEach(w -> {
                if (w.getServices() != null && w.getServices().size() > 0) {
                    w.getServices().forEach(s -> {
                        Model model = new Model();
                        model = setPropertiesModel(bdaApplication, s, model, w);
                    });
                }
                searchOutputModel(bdaApplication,w);
            });
        return bdaApplication;
    }

    private void searchOutputModel(BDAApplication bdaApplication, Workflow w) {
        //solo dopo che scorro tutti i servizi creando i modelli di output posso scorrerli nuovamnte cercando quelli di input
        w.getServices().forEach(s -> {
            Optional<Property> optAppPro = s.getProperties().stream().filter(p -> p instanceof ApplicationProperty).filter(ap -> ap.getKey().equals("input-model")).findFirst();
            if (optAppPro.isPresent()) {
                ApplicationProperty applicationProperty = (ApplicationProperty) optAppPro.get();
                //cerca se nel wf c'è output model dato come input a questo servizio
                AtomicBoolean foundIt = new AtomicBoolean(false);
                w.getServices().stream().forEach(se -> {
                    Model modelTemp = null;
                    if (se.getProperties().stream().filter(p -> p instanceof ApplicationProperty).filter(ap -> ap.getKey().equals("output-model")).count() > 0) {
                        if (se.getModels() == null) se.setModels(new ArrayList<>());
                        Optional<Model> optModel = se.getModels().stream().filter(m -> m.getLocation().equals(applicationProperty.getValue())).findFirst();
                        if (optModel.isPresent()) {
                            if (s.getModels() == null) s.setModels(new ArrayList<>());
                            s.getModels().add(optModel.get());
                            foundIt.set(true);
                        }
                    }
                });
                //se non è stato trovata la stessa location nei modelli di input appena creati ci sarà stato un drag end drop dl modello quindi lo cerco sul db
                if (!foundIt.get()) {
                    Model modelTemp = modelService.getModelByLocationAndOrganizationIdAndOwnerId(applicationProperty.getValue(), bdaApplication.getOwner().getOrganization().getId(), bdaApplication.getOwner().getId());
                    if (modelTemp != null) {
                        if(s.getModels()==null) s.setModels(new ArrayList<>());
                        s.getModels().add(modelTemp);
                    }
                }
            }
        });
    }

    private Model setPropertiesModel(BDAApplication bdaApplication, Service s, Model model, Workflow w) {
        if (s.getProperties().stream().filter(p -> p instanceof ApplicationProperty).filter(ap -> ap.getKey().equals("output-model")).count() > 0) {
            ArrayNode properties = objectMapper.createArrayNode();
            model.setFramework(s.getFramework());
            model.setAccessLevel(bdaApplication.getAccessLevel());
            model.setOwner(bdaApplication.getOwner());
            model.setAlgorithmName(s.getName());
            Model finalModel = model;
            s.getProperties().stream().filter(p -> p instanceof ApplicationProperty).forEach(p -> {
                if (p.getKey().equals("output-model")) finalModel.setLocation(p.getValue());
                else if (p.getKey().equals("input-columns")) finalModel.setInputs(p.getValue());
                else if (p.getKey().equals("outputLabel")) finalModel.setTargets(p.getValue());
                else if (p.getKey().equals("input-dataset")) {
                    Dataset dataset = null;
                    try{
                        Long refId = Long.parseLong(p.getDefaultValue());
                        String type = p.getDescription();
                        dataset = bdaApplication.getDatasets().stream()
                                .filter(d -> refId.equals(d.getRefId()) && d.getType().equals(type))
                                .findFirst().get();
                    }catch (Exception ex){
                        dataset = datasetService.createDatasetFromBDA(s, "input-dataset", "dataStorageType-input-dataset", false, bdaApplication, w);
                    }
                    finalModel.setTrainingDataset(dataset); //TODO --- Icarus
                } else {
                    ObjectNode property = objectMapper.createObjectNode();
                    property.put(p.getKey(), p.getValue());
                    properties.add(property);
                }

            });
            model.setProperties(properties);
            model = modelService.save(model);
            if (s.getModels() == null) s.setModels(new ArrayList<>());
            s.getModels().add(model);
        }
        return model;
    }

    private BDAApplication setDatasets(BDAApplication bdaApplication) {
        bdaApplication = setDatasetsOutput(bdaApplication);
        bdaApplication = setDatasetsInput(bdaApplication); //TODO --- will delete (useful only Icarus project)

        if (bdaApplication.getDatasets() != null) {
            bdaApplication = cleanDatasetDuplicated(bdaApplication);
            bdaApplication = checkDatasetIntoDb(bdaApplication);
            List<Dataset> datasetToSave = new ArrayList<>();
            List<Dataset> datasetJustStored = new ArrayList<>();
            List<Dataset> finalDatasetToSave = datasetToSave;
            bdaApplication.getDatasets().forEach(d ->{
                if (d.getId() == null) {
                    finalDatasetToSave.add(d);
                } else {
                    datasetJustStored.add(d);
                }
            });

            datasetToSave = datasetRepository.saveAll(finalDatasetToSave);
            bdaApplication.setDatasets(datasetToSave);
            bdaApplication.getDatasets().addAll(datasetJustStored);
        }
        return bdaApplication;
    }

    private BDAApplication checkDatasetIntoDb(BDAApplication bdaApplication) {
        List<Dataset> datasetsToSave = new ArrayList<>();

        bdaApplication.getDatasets().forEach(ds -> {
            if (ds.getRefId() == null) { //Output dataset
                datasetsToSave.add(ds);
            } else {
                Dataset datasetDb = datasetService.findByRefIdAndType(ds.getRefId(), ds.getType());
                if (datasetDb != null) {
                    datasetsToSave.add(datasetDb);
                } else {
                    datasetsToSave.add(ds);
                }
            }
        });
        bdaApplication.setDatasets(datasetsToSave);
        return bdaApplication;
    }

    private BDAApplication cleanDatasetDuplicated(BDAApplication bdaApplication) { //TODO -- Icarus
        List<Dataset> datasetsWithoutDuplicate = new ArrayList<>();
        bdaApplication.getDatasets().forEach(ds -> {
            if (ds.getRefId() == null) {
                datasetsWithoutDuplicate.add(ds);
            } else {
                AtomicBoolean exist = new AtomicBoolean(false);
                datasetsWithoutDuplicate.forEach(dsToStore -> {
                    if (dsToStore.getRefId() != null && dsToStore.getType().equals(ds.getType()) && (dsToStore.getRefId().equals(ds.getRefId())))
                        exist.set(true);
                });
                if (!exist.get()) datasetsWithoutDuplicate.add(ds);
            }
        });
        bdaApplication.setDatasets(datasetsWithoutDuplicate);
        return bdaApplication;
    }

    @Transactional
    public BDAApplication setDatasetsOutput(BDAApplication bdaApplication) {
        //TODO migliorare questo methodo
        if (bdaApplication.getWorkflows() != null && bdaApplication.getWorkflows().size() > 0) {
            bdaApplication.getWorkflows().forEach(w -> {
                if (w.getServices() != null && w.getServices().size() > 0) {
                    w.getServices().forEach(s -> {
                        int i = 1;
                        String outputKey = "output-dataset";
                        String outputDatastorage = "dataStorageType-output-dataset";
                        do {
                            Dataset dataset = datasetService.createDatasetFromBDA(s, outputKey, outputDatastorage, true, bdaApplication, w);
                            if (dataset == null) break;
                            if (bdaApplication.getDatasets() == null) bdaApplication.setDatasets(new ArrayList<>());
                            bdaApplication.getDatasets().add(dataset);
                            i++;
                            outputKey = "output-dataset-" + i;
                            outputDatastorage = "dataStorageType-output-dataset-" + i;
                        } while (true);
                    });
                }
                if(w.getServices().get(w.getServices().size()-1).getName().toUpperCase().equals("STATISTICS")){
                    Dataset datasetSummary = datasetService.createStatisticsDataset(bdaApplication,w,w.getServices().get(w.getServices().size()-1), "summary");
                    if(datasetSummary!=null) bdaApplication.getDatasets().add(datasetSummary);
                    Dataset datasetPearson = datasetService.createStatisticsDataset(bdaApplication,w,w.getServices().get(w.getServices().size()-1), "pearson");
                    if(datasetPearson!=null) bdaApplication.getDatasets().add(datasetPearson);
                    Dataset datasetSpearman = datasetService.createStatisticsDataset(bdaApplication,w,w.getServices().get(w.getServices().size()-1), "spearman");
                    if(datasetSpearman!=null) bdaApplication.getDatasets().add(datasetSpearman);
                }
            });
        }
        return bdaApplication;
    }

    //TODO --- will delete (useful only Icarus project)
    @Transactional
    public BDAApplication setDatasetsInput(BDAApplication bdaApplication) {
        if (bdaApplication.getWorkflows() != null && bdaApplication.getWorkflows().size() > 0)
            bdaApplication.getWorkflows().forEach(w -> {
                if (w.getServices() != null && w.getServices().size() > 0) {
                    //w.getServices().forEach(s -> {
                    List<Service> servicesList = new ArrayList<>();
                    if (w.getServices().size() == 1) {
                        servicesList.add(w.getServices().get(0));
                    } else {
                        servicesList.add(w.getServices().get(w.getServices().size() - 2));
                        servicesList.add(w.getServices().get(w.getServices().size() - 1));
                    }
                    servicesList.forEach(s -> {
                        int i = 1;
                        String inputKey = "input-dataset";
                        String inputDatastorage = "dataStorageType-input-dataset";
                        do {
                            Dataset dataset = datasetService.createDatasetFromBDA(s, inputKey, inputDatastorage, false, bdaApplication, w);
                            if (dataset == null) break;
                            if (bdaApplication.getDatasets() == null) bdaApplication.setDatasets(new ArrayList<>());
                            bdaApplication.getDatasets().add(dataset);
                            i++;
                            inputKey = "input-dataset-" + i;
                            inputDatastorage = "dataStorageType-input-dataset-" + i;
                        } while (true);
                    });
                }
            });
        return bdaApplication;
    }

   /* @Transactional
    public BDAApplication update(BDAApplication bdaApplication){
        //controllo che la bda sia dell'astessa organizzazione
        if(bdaApplication.getId()!=null){
            Optional<BDAApplication> bdaApplicationTemp = bdaApplicationRepository.findBDAApplicationByBDAApplicationIdAndOrganizationIdAndOwnerId(bdaApplication.getId(), bdaApplication.getOwner().getOrganization().getId(),bdaApplication.getOwner().getId());
            if(!bdaApplicationTemp.isPresent()){
                throw new IllegalArgumentException("permission denied for this BDA Application");
            }
        }

        if(bdaApplication.getMetadata()!=null && bdaApplication.getMetadata().getMetadataLicenseDetails()!= null && bdaApplication.getMetadata().getMetadataLicenseDetails().getOwner() !=null ){
            bdaApplication.getMetadata().getMetadataLicenseDetails().setOwner(ownerService.getOwnerDb( bdaApplication.getMetadata().getMetadataLicenseDetails().getOwner()));
            Metadata metadata = metadataRepository.save(bdaApplication.getMetadata());
            bdaApplication.setMetadata(metadata);
        }

        if(bdaApplication.getDatasets()!=null)
            bdaApplication.setDatasets(datasetRepository.saveAll(bdaApplication.getDatasets()));

        BDAApplication bda = bdaApplicationRepository.save(bdaApplication);

        if(bdaApplication.getWorkflows() != null ) {
            bdaApplication.getWorkflows().forEach(wf -> {wf.setBdaApplication(bda);});
            List<Workflow> workflows = workflowRepository.saveAll(bdaApplication.getWorkflows());
            bda.setWorkflows(workflows);
        }
        bda.getWorkflows().forEach(workflow -> {
            if(workflow.getServices()!=null && workflow.getServices().size()>0) {
                workflow.getServices().forEach(s -> {
                    s.setWorkflow(workflow);
                });
                workflow.setServices(serviceService.saveAll(workflow.getServices()));
            }
        });

        return bda;
    }*/

    @Transactional
    public void delete(BDAApplication bdaApplication) {
        bdaApplicationRepository.deleteByIdCustom(bdaApplication.getId());
        /*if (bdaApplication.getId() != null) {
            Optional<BDAApplication> bdaApplicationTemp = bdaApplicationRepository.findBDAApplicationByBDAApplicationIdAndOrganizationIdAndOwnerId(bdaApplication.getId(), bdaApplication.getOwner().getOrganization().getId(), "", false);
            if (!bdaApplicationTemp.isPresent()) {
                throw new IllegalArgumentException("permission denied for this BDA Application");
            } else {
                if (bdaApplication.getWorkflows() != null && bdaApplication.getWorkflows().size() > 0)
                    bdaApplication.getWorkflows().forEach(w -> {
                        serviceRepository.deleteAll(w.getServices());
                    });

                bdaApplication.getWorkflows().forEach(w -> {
                    if (w.getServices() != null && w.getServices().size() > 0) {
                        w.getServices().forEach(s -> {
                            serviceRepository.delete(s);
                        });
                    }

                });
                workflowRepository.deleteAll(bdaApplication.getWorkflows());
                workflowRepository.flush();
                bdaApplicationRepository.flush();
                //bdaApplication = BDAApplication bdaTemp = bdaApplicationRepository.findById(bdaApplication.getId()).get();
                //bdaApplicationRepository.deleteById(bdaApplication.getId());
                bdaApplicationRepository.delete(bdaApplication);
            }
            }*/


    }

    //public void delete(Long id){  bdaApplicationRepository.deleteById(id); }
    @Transactional
    public Collection<BDAApplication> getAppsByOwnerId(String ownerId) {
        return bdaApplicationRepository.findByOwner_Id(ownerId);
    }

    @Transactional
    public Optional<BDAApplication> findBDAApplicationByBDAApplicationIdAndOrganizationIdAndOwnerId(Long bdaId, String organizationId, String owner) {
        return bdaApplicationRepository.findBDAApplicationByBDAApplicationIdAndOrganizationIdAndOwnerId(bdaId, organizationId, owner, false);
    }

    @Transactional
    //return bdas with ACCESS_LEVEL = TEAM, PUBLIC and PRIVATE
    public Collection<BDAApplication> findBDAApplicationsByOrganizationIdAndOwnerId(String organizationId, String ownerId) {
        Collection<BDAApplication> bdaApplications = bdaApplicationRepository.findBDAApplicationsByOrganizationIdAndOwnerId(organizationId, ownerId, false);
        if (bdaApplications == null) bdaApplications = new ArrayList<>();
        return bdaApplications;
    }


    @Transactional
    public void toTrashWithOrganizationId(Long bdaId, String organizationId, String ownerId) {
        Optional<BDAApplication> bdaApplicationOptional = bdaApplicationRepository.findBDAApplicationByBDAApplicationIdAndOrganizationIdAndOwnerId(bdaId,organizationId,null, false);
        if(!bdaApplicationOptional.isPresent()) throw new IllegalArgumentException("permission denied for this BDA Application");
        BDAApplication bdaApplicationStored = bdaApplicationOptional.get();
        if(bdaApplicationStored.getWorkflows().get(0).getStatus() != null && bdaApplicationStored.getWorkflows().get(0).getStatus().equals("COMPLETED")) throw new IllegalArgumentException("It is not possible to deleted a COMPLETED BDA Application");
        BDAApplication bdaWillDetele = new BDAApplication();
        bdaWillDetele.setDeleteDateTime(LocalDateTime.now());
        update(bdaId,bdaWillDetele,ownerId,organizationId);
        //bdaApplicationStored.getDatasets().forEach(ds -> {if(ds.getCreatedBy() != null && ds.getCreatedBy().getId() == bdaApplicationStored.getWorkflows().get(0).getId())datasetService.delete(ds.getId());});
        //workflowService.delete(bdaApplicationStored.getWorkflows().get(0).getId());
        //bdaApplicationRepository.deleteByIdAndOwner_Organization_id(bdaId, organizationId);
    }

    @Transactional
    public void deleteWithIdAndOrganizationIdAndOwnerId(Long bdaId, String organizationId, String ownerId) {
        Optional<BDAApplication> bdaApplicationOptional = bdaApplicationRepository.findBDAApplicationByBDAApplicationIdAndOrganizationIdAndOwnerId(bdaId,organizationId,null, false);
        if(!bdaApplicationOptional.isPresent()) throw new IllegalArgumentException("permission denied for this BDA Application");
        BDAApplication bdaApplicationStored = bdaApplicationOptional.get();
        if(bdaApplicationStored.getWorkflows().get(0).getStatus() != null && bdaApplicationStored.getWorkflows().get(0).getStatus().equals("COMPLETED")) throw new IllegalArgumentException("It is not possible to deleted a COMPLETED BDA Application");
        delete(bdaApplicationStored);
    }

    @Transactional
    public void deleteWithOwnerId(Long id, String ownerId) {
        bdaApplicationRepository.deleteByIdAndOwner_Id(id, ownerId);
    }

    @Transactional
    public BDAApplication update(Long id, BDAApplication bdaPartial, String ownerId, String organizationId) {
        Optional<BDAApplication> bdaApplication = bdaApplicationRepository.findBDAApplicationByBDAApplicationIdAndOrganizationIdAndOwnerId(id, organizationId, ownerId, false);
        if (!bdaApplication.isPresent())
            throw new IllegalArgumentException("permission denied for this BDA Application");
        BDAApplication bdaApplicationStored = bdaApplication.get();
       // bdaApplicationStored = datasetService.reloadDatasetsByBdaId(bdaApplicationStored.getId());
        HashMap<String, Object> bdaPartialSecurity = new HashMap<>();
        if (bdaPartial.getDescription()!=null) bdaPartialSecurity.put("description", bdaPartial.getDescription());
        if (bdaPartial.getName()!=null) bdaPartialSecurity.put("name", bdaPartial.getName());
        if (bdaPartial.getDeleteDateTime()!=null) bdaPartialSecurity.put("deleteDateTime", bdaPartial.getDeleteDateTime());

        if(bdaPartial.getName()!=null || bdaPartial.getDescription()!=null || bdaPartial.getDeleteDateTime()!=null) {
            bdaApplicationStored = bdaApplicationRepository.updateField(bdaApplication.get(), bdaPartialSecurity);
        }
        if (bdaPartial.getWorkflows()!=null && bdaPartial.getWorkflows().size()>0 ) {
            if (bdaApplication.get().getWorkflows().get(0).getStatusModel() != null && bdaApplication.get().getWorkflows().get(0).getStatusModel().getStatus().equals("COMPLETED")) {
                throw new InputMismatchException("The Workflow is already COMPLETED, it cannot be changed"); //TODO da estendere a lista di WF
            }




            Workflow newWorkflow = bdaPartial.getWorkflows().get(0);
            bdaApplicationStored.setWorkflows(new ArrayList<>());
            bdaApplicationStored.setDatasets(new ArrayList<>());
            bdaApplicationStored.getWorkflows().add(newWorkflow);
            bdaApplicationStored = setWorkflowAndAssets(bdaApplicationStored);


 /*
            BDAApplication bdaApplicationTemp = createBdaTemp(bdaApplicationStored, newWorkflow);

            List<Dataset> datasetsWillDeleted = new ArrayList<>();
            Long oldWorkflowId = bdaApplicationStored.getWorkflows().get(0).getId();

            bdaApplicationStored.setWorkflows(bdaApplicationTemp.getWorkflows());
            bdaApplicationStored.setDatasets(bdaApplicationTemp.getDatasets());
            List<Dataset> datasetsTemp= bdaApplicationStored.getDatasets();

            BDAApplication finalBdaApplicationTemp = bdaApplicationTemp;
            for(int i=0; i<datasetsTemp.size(); i++){
                //datasetsTemp.get(i).setBdaApplications(bdaApplicationRepository.);
                datasetsTemp.get(i).getBdaApplications().add(bdaApplicationStored);

            }


            datasetService.deleteAll(datasetsWillDeleted);
            workflowService.delete(oldWorkflowId);



            Long oldWorkflowId = bdaApplicationStored.getWorkflows().get(0).getId();
            //datasetService.delete(bdaApplicationStored.getDatasets().stream().filter(d -> d.getCreatedBy().getId() == oldWorkflowId).findFirst().get().getId()); // solo per icarus
            //Long workflowIdToDelete =bdaApplicationStored.getWorkflows().stream().filter(w -> w.getId()==oldWorkflowId).findFirst().get().getId();
            //workflowService.delete(workflowIdToDelete);
            List<Dataset> datasetsWillDeleted = new ArrayList<>();
            bdaApplicationStored.getDatasets().stream().filter(d -> (d.getCreatedBy()!=null && d.getCreatedBy().getId() == oldWorkflowId)).forEach(d -> datasetsWillDeleted.add(d));
            //bdaApplicationStored.getWorkflows().remove(bdaApplicationStored.getWorkflows().stream().filter(w -> w.getId()==oldWorkflowId).findFirst().get());
            bdaApplicationStored.getWorkflows().add(newWorkflow);
            bdaApplicationStored = setWorkflowAndAssets(bdaApplicationStored);
            datasetsWillDeleted.remove(bdaApplicationStored.getDatasets());
            datasetService.deleteAll(datasetsWillDeleted); // solo per icarus
        */
        }
        return bdaApplicationStored;
    }

    private BDAApplication createBdaTemp(BDAApplication bdaApplicationStored, Workflow workflow) {
        BDAApplication bdaApplicationTemp = new BDAApplication();
        bdaApplicationTemp.setOwner(bdaApplicationStored.getOwner());
        bdaApplicationTemp.setName(bdaApplicationStored.getName()+"-TEMP");
        bdaApplicationTemp.setWorkflows(new ArrayList<>());
        bdaApplicationTemp.getWorkflows().add(workflow);
        bdaApplicationTemp = setWorkflowAndAssets(bdaApplicationTemp);
        bdaApplicationRepository.findById(bdaApplicationTemp.getId());
        bdaApplicationTemp.getDatasets().size();
        return bdaApplicationTemp;

    }

    @Transactional
    public BDAApplication update(Long id, HashMap<String, Object> bdaPartial, String ownerId, String organizationId) {
        Optional<BDAApplication> bdaApplication = bdaApplicationRepository.findBDAApplicationByBDAApplicationIdAndOrganizationIdAndOwnerId(id, organizationId, ownerId, false);
        if (!bdaApplication.isPresent())
            throw new IllegalArgumentException("permission denied for this BDA Application");
        HashMap<String, Object> bdaPartialSecurity = new HashMap<>();
        if (bdaPartial.containsKey("description")) bdaPartialSecurity.put("description", bdaPartial.get("description"));
        if (bdaPartial.containsKey("name")) bdaPartialSecurity.put("name", bdaPartial.get("name"));
        if (bdaPartial.containsKey("workflow")) {
            //rWorkflow workflowPartial = objectMapper.treeToValue(bdaPartial.get("workflow"), Workflow.class);
            if (bdaApplication.get().getWorkflows().get(0).getStatusModel().getStatus().equals("COMPLETED"))
                throw new InputMismatchException("The Workflow is already COMPLETED, it cannot be changed"); //TODO da estendere a lista di WF
            bdaPartialSecurity.put("workflow", bdaPartial.get("workflow"));
            //TODO eliminare tutti gli assets di output
            bdaPartialSecurity.put("datasets", new ArrayList<>());
        }
        BDAApplication bdaApplicationStored = bdaApplicationRepository.updateField(bdaApplication.get(), bdaPartialSecurity);
        if (bdaPartial.containsKey("workflow")) {

            bdaApplicationStored = setDatasets(bdaApplicationStored);
        }
        return bdaApplicationStored;
    }

    @Transactional
    public Optional<BDAApplication> findById(Long bdaId) {
        Optional<BDAApplication> bdaApplicationOptional = bdaApplicationRepository.findById(bdaId);
        if(bdaApplicationOptional.isPresent()){
                bdaApplicationOptional.get().getWorkflows().size();
        }
        return bdaApplicationOptional;
    }

    @Transactional
    public void deleteAll() {
        List<Long> allIdsList = bdaApplicationRepository.getAllId();
        allIdsList.forEach(bdaId ->{
            Optional<BDAApplication> bdaApplicationOptional = findById(bdaId);
                    if(bdaApplicationOptional.isPresent()){
                        bdaApplicationOptional.get().getDatasets().forEach(ds -> {
                           // if(ds.getCreated()!=null && ds.getCreatedBy().getId().equals())
                            datasetService.delete(ds.getId());

                        }
                        );

                        //cancello tutti i workflow, i suoi servizi e le sue properties
                        bdaApplicationOptional.get().getWorkflows().forEach(wf -> {
                            //cancello prima i servizi
                            wf.getServices().forEach(service -> {
                                //service.getProperties().forEach(property -> {
                                // });
                                serviceService.delete(service.getId());
                            });
                            workflowService.delete(wf.getId());
                        });
                    }
            bdaApplicationRepository.deleteById(bdaId);

        });

    }
}
