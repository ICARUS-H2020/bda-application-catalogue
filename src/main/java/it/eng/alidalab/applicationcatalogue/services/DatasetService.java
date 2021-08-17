package it.eng.alidalab.applicationcatalogue.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.eng.alidalab.applicationcatalogue.domain.*;
import it.eng.alidalab.applicationcatalogue.domain.service.ApplicationProperty;
import it.eng.alidalab.applicationcatalogue.domain.service.Service;
import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;
import it.eng.alidalab.applicationcatalogue.repository.BDAApplicationRepository;
import it.eng.alidalab.applicationcatalogue.repository.DatasetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Transactional
public class DatasetService {
    @Inject
    private DatasetRepository datasetRepository;

    @Inject
    private OwnerService ownerService;

    @Autowired
    ObjectMapper mapper;

    @Inject
    private BDAApplicationRepository bdaApplicationRepository;

    @Inject
    private JedisPool jedisPool;

    public List<Dataset> getDatasets(String organizationId, String ownerId, Map queryParams)
    {
        return datasetRepository.findByOrganizationIdAndOwnerId(organizationId,ownerId, queryParams);
    }

    public Dataset getDataset(Long datasetId, String ownerId, String organizationId)
    {
        return datasetRepository.getDataset_DatasetIdAndOrganizationIdAndOwnerId(datasetId, organizationId, ownerId);

    }

    @Transactional
    public Dataset save(Dataset dataset)
    {
        dataset.setOwner(ownerService.getOwnerDb(dataset.getOwner()));
        return datasetRepository.save(dataset);
    }

    public List<Dataset> createDataset(List<Dataset> datasets)
    {
        return datasetRepository.saveAll(datasets);
    }

    public void delete(Long id)
    {
        datasetRepository.deleteById(id);
    }

    public Dataset createDatasetFromBDA(Service s, String propertyKey, String keyDatastorage, boolean output, BDAApplication bdaApplication, Workflow w) {
        Long countProperties = (Long) s.getProperties().stream().filter(p -> p instanceof ApplicationProperty).filter(p -> p.getKey().equals(propertyKey)).count();
        if(countProperties == null || countProperties==0)
            return null;

        ApplicationProperty applicationPropertyDataset = (ApplicationProperty) s.getProperties().stream().filter(p -> p instanceof ApplicationProperty).filter(p -> p.getKey().equals(propertyKey)).findFirst().get();

        Dataset dataset = new Dataset();
        if(output) {
            dataset.setType("RESULT"); //TODO --- Icarus
            dataset = createDatasetOutputBase(dataset,w,bdaApplication);
        }else{
            try{
                dataset.setRefId(Long.parseLong(applicationPropertyDataset.getDefaultValue())); //TODO --- Icarus
            }catch (Exception ex){
                return null;
            }
            dataset.setType(applicationPropertyDataset.getDescription()); //TODO --- Icarus
            Dataset datasetCheck = datasetRepository.findByRefIdAndType(dataset.getRefId(), dataset.getType());
            if(datasetCheck!=null) {
                if(datasetCheck.getBdaApplications()==null)datasetCheck.setBdaApplications(new ArrayList<>());
                datasetCheck.getBdaApplications().add(bdaApplication);
                return datasetCheck;
            }
        }
        dataset.setName(w.getName()+"-"+s.getName()+"-"+applicationPropertyDataset.getValue());
        dataset = createDataSourceType(s, dataset, applicationPropertyDataset, keyDatastorage, null);
        return dataset;
    }

    private Dataset createDatasetOutputBase(Dataset dataset, Workflow w, BDAApplication bdaApplication) {
        dataset.setCreatedBy(w);
        dataset.setOwner(bdaApplication.getOwner());
        dataset.setAccessLevel(AccessLevel.ACCESS_LEVEL.TEAM);  //TODO --- Icarus
        dataset.setBdaApplications(Arrays.asList(bdaApplication));
        return dataset;
    }

    private Dataset createDataSourceType(Service s, Dataset dataset, ApplicationProperty applicationPropertyDataset, String keyDatastorage, String outputString) {
        ApplicationProperty applicationPropertyDataStorage = null;
        if( s.getProperties()
                .stream().filter(p -> p instanceof ApplicationProperty)
                .filter(p -> p.getKey().equals(keyDatastorage)).count()>0)
        {
            applicationPropertyDataStorage = (ApplicationProperty) ( s.getProperties()
                    .stream().filter(p -> p instanceof ApplicationProperty)
                    .filter(p -> p.getKey().equals(keyDatastorage)).findFirst().get());
        }else{
            return null;
        }
        String dataStorageType = applicationPropertyDataStorage.getValue().split("-")[0];
        if(dataStorageType.equals("PRESTO") || dataStorageType.equals("HIVE") ){
            DatasetSourceTypeDatabase datasetSourceTypeDatabase = importInformationByRedis(applicationPropertyDataStorage.getValue(),dataStorageType.toUpperCase());
            if(outputString != null && outputString.split("\\.").length>1){//TODO ICARUS (STATISTICS)
                datasetSourceTypeDatabase.setDatabase(outputString.split("\\.")[0]);
                datasetSourceTypeDatabase.setTable(outputString.split("\\.")[1]);
            }
            dataset.setDatasetSourceType(datasetSourceTypeDatabase);

        }
        else if(dataStorageType.equals("HDFS") ){
            DatasetSourceTypeFileSystem datasetSourceTypeFileSystem = new DatasetSourceTypeFileSystem();
            datasetSourceTypeFileSystem.setPath(applicationPropertyDataset.getValue());
            if(outputString != null && outputString.split("\\.").length>1){ //TODO ICARUS (STATISTICS)
                datasetSourceTypeFileSystem.setPath(outputString.split("\\.")[0]);
                datasetSourceTypeFileSystem.setFileName(outputString.split("\\.")[1]);
            }
            dataset.setDatasetSourceType(datasetSourceTypeFileSystem);
        }
        return dataset;
    }


    private DatasetSourceTypeDatabase importInformationByRedis(String dataStorageType, String type) {
        try (Jedis jedis = jedisPool.getResource()) {
            String storagePropertiesServiceString = jedis.get(dataStorageType).replaceAll("\\\\", "");
            Map<String, String> storagePropertiesService = new HashMap<>();
            DatasetSourceTypeDatabase datasetSourceTypeDatabase = new DatasetSourceTypeDatabase();


            try {
                type= type.toLowerCase();
                storagePropertiesService = mapper.readValue(storagePropertiesServiceString.replaceAll("\"\"", "").replaceAll("'", "\""), Map.class);
                if(storagePropertiesService.containsKey(type+"Port")){
                        datasetSourceTypeDatabase.setPort(Long.parseLong(""+storagePropertiesService.get(type+"Port")));
                }
                if(storagePropertiesService.containsKey(type+"Host")){
                    datasetSourceTypeDatabase.setUrl(""+storagePropertiesService.get(type+"Host"));
                }
                if(storagePropertiesService.containsKey(type+"Db")){
                    datasetSourceTypeDatabase.setDatabase(""+storagePropertiesService.get(type+"Db"));
                }
                if(storagePropertiesService.containsKey(type+"User")){
                    datasetSourceTypeDatabase.setUser(""+storagePropertiesService.get(type+"User"));
                }
                if(storagePropertiesService.containsKey(type+"Password")){
                    datasetSourceTypeDatabase.setPassword_(""+storagePropertiesService.get(type+"Password"));
                }
                if(storagePropertiesService.containsKey(type+"MetastoreUris")){
                    datasetSourceTypeDatabase.setUrl(""+storagePropertiesService.get(type+"MetastoreUris"));
                }
                if(storagePropertiesService.containsKey(type+"UserName")){
                    datasetSourceTypeDatabase.setUser(""+storagePropertiesService.get(type+"UserName"));
                }
                if(storagePropertiesService.containsKey(type+"Catalog")){
                    datasetSourceTypeDatabase.setCatalogue(""+storagePropertiesService.get(type+"Catalog"));
                }
                return datasetSourceTypeDatabase;
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e) {
        e.printStackTrace();
        }
        return null;
    }

    public Dataset findByRefIdAndType(Long refId, String type) {
        return datasetRepository.findByRefIdAndType(refId, type);
    }

    public Dataset createStatisticsDataset(BDAApplication bdaApplication, Workflow w, Service s, String type){

        Long countProperties = 0l;
        countProperties = (Long) s.getProperties().stream().filter(p -> p instanceof ApplicationProperty).filter(p -> p.getKey().equals(type)).count();
        if(countProperties!=0) {
            ApplicationProperty applicationPropertyDataset = (ApplicationProperty) s.getProperties().stream().filter(p -> p instanceof ApplicationProperty).filter(p -> p.getKey().equals(type)).findFirst().get();
            Boolean isPresent = Boolean.parseBoolean(applicationPropertyDataset.getValue());
            if(isPresent) {
                ApplicationProperty applicationPropertyDatasetOutput = (ApplicationProperty) s.getProperties().stream().filter(p -> p instanceof ApplicationProperty).filter(p -> p.getKey().equals("output-statistics-"+type)).findFirst().get();
                Dataset dataset = new Dataset();
                dataset.setType("STATISTICS");
                dataset = createDatasetOutputBase(dataset, w, bdaApplication);
                String name ="Result "+ type.substring(0, 1).toUpperCase() + type.substring(1);; if(!type.toUpperCase().equals("SUMMARY")) name += " Correlation Matrix";
                dataset.setName(name);
                dataset = createDataSourceType(s, dataset, applicationPropertyDataset, "dataStorageType-output-dataset", applicationPropertyDatasetOutput.getValue());
                return dataset;
            }
        }
        return null;
    }

    public Dataset update(Dataset ds, HashMap<String, Object> datasetPartial) {
        HashMap<String, Object> datasetPartialSecurity = new HashMap<>();
        if (datasetPartial.containsKey("refId")) datasetPartialSecurity.put("refId", datasetPartial.get("refId"));
        Dataset datasetStored = datasetRepository.updateField(ds, datasetPartialSecurity);
        return datasetStored;
    }

    public void deleteAll(List<Dataset> datasetsWillDeleted) {
        datasetRepository.deleteAll(datasetsWillDeleted);
    }

    public BDAApplication reloadDatasetsByBdaId(Long id) {
        return null;
    }

    public List<Dataset> findAllByWorkflow (Workflow workflow){
        return datasetRepository.findAllByCreatedBy(workflow);
    }
    public Dataset findById(Long id){
        Optional<Dataset> datasetOp = datasetRepository.findById(id);
        return datasetOp.isPresent()?datasetOp.get():null;
    }
}
