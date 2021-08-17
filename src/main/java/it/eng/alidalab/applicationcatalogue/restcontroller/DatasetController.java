package it.eng.alidalab.applicationcatalogue.restcontroller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.eng.alidalab.applicationcatalogue.domain.Dataset;
import it.eng.alidalab.applicationcatalogue.domain.Organization;
import it.eng.alidalab.applicationcatalogue.domain.Owner;
import it.eng.alidalab.applicationcatalogue.services.DatasetService;
import it.eng.alidalab.applicationcatalogue.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.QueryParam;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Api(description = "Dataset Endpoint")
@RestController
@RequestMapping("bda-catalogue/api/v1/")
public class DatasetController {
    private final Logger log = LoggerFactory.getLogger(DatasetController.class);

    @Inject
    private DatasetService datasetService;

    /**
     * GET  /datasets : get all Datasets of a ownerId, OrganizationId, workflowName and type output/input
     * @param organizationId Organization
     * @param ownerId Owner
     * @param workflowName
     * @param output
     * @return a list of Dataset in the response body
     */
    @ApiOperation(value = "get all Datasets of a ownerId, OrganizationId, workflowName and type output/input")
    @RequestMapping(value = "/datasets",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Collection<Dataset>> getDataset(
            @ApiParam(
                    value = "organizationId",
                    defaultValue = "1",
                    required = true,
                    example = "123")
            @RequestHeader("organizationId") String organizationId,
            @ApiParam(
                    value = "ownerId",
                    defaultValue = "1",
                    required = true,
                    example = "123")
            @RequestHeader("ownerId") String ownerId,
            @ApiParam(
                    value = "workflowName",
                    defaultValue = "",
                    required = false,
                    example = "myWorkflow")
            @QueryParam("workflowName") String workflowName,
            @ApiParam(
                    value = "output",
                    defaultValue = "false",
                    required = false,
                    example = "true")
            @QueryParam("output") String output
    )
    {
        Map<String,Object> queryParams = new HashMap<>();
        if(output!=null)  queryParams.put("output",output);
        if(workflowName!=null)  queryParams.put("workflowName",workflowName);
        Collection<Dataset> ds = datasetService.getDatasets(ownerId, organizationId, queryParams);
        return ds!=null?ResponseEntity.ok().body(ds):new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    /**
     * GET  /datasets/{id} : get the dataset
     *
     * @param datasetId the dataset id
     * @return the ResponseEntity with status 200 (OK) and the dataset in the body
     * or with status 400 (Bad Request) if the dataset can't be found,
     * @throws 404 if the dataset doesn't exist
     */
    @ApiOperation(value = "/datasets/{id}", notes = "get the dataset")
    @RequestMapping(value = "/datasets/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dataset> getDataset(
            @ApiParam(
                    value = "Datasets id",
                    required = true)
            @PathVariable("id") Long datasetId,
            @ApiParam(
                    value = "ownerId",
                    defaultValue = "1",
                    required = true,
                    example = "123")
            @RequestHeader("ownerId") String ownerId,
            @ApiParam(
                    value = "organizationId",
                    defaultValue = "1",
                    required = true,
                    example = "123")
            @RequestHeader("organizationId") String organizationId)
    {
        try
        {
            Dataset ds = datasetService.getDataset(datasetId, ownerId, organizationId);
            return ds!=null?ResponseEntity.ok(ds):new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * PATCH  /datasets/{id} : get the dataset
     *
     * @param datasetId the dataset id
     * @return the ResponseEntity with status 200 (OK) and the dataset in the body
     * or with status 400 (Bad Request) if the dataset can't be found,
     * @throws 404 if the dataset doesn't exist
     */
    @ApiOperation(value = "/datasets/{id}", notes = "get the dataset")
    @RequestMapping(value = "/datasets/{id}",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dataset> patchDataset(
            @ApiParam(
                    value = "Datasets id",
                    required = true)
            @PathVariable("id") Long datasetId,
            @ApiParam(
                    value = "ownerId",
                    defaultValue = "1",
                    required = true,
                    example = "123")
            @RequestHeader("ownerId") String ownerId,
            @ApiParam(
                    value = "organizationId",
                    defaultValue = "1",
                    required = true,
                    example = "123")
            @RequestHeader("organizationId") String organizationId,
            @ApiParam(

                    name = "BDA App",
                    value = "The BDA App to update"
            )
            @RequestBody HashMap<String, Object> datasetPartial
            )
    {
        try
        {
            Dataset ds = datasetService.getDataset(datasetId, ownerId, organizationId);
            return ds==null?ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null):ResponseEntity.ok(datasetService.update(ds,datasetPartial));
        } catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * POST  /datasets : Creates a  dataset.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated catalogue,
     * or with status 400 (Bad Request) if the catalogue is not valid,
     * or with status 500 (Internal Server Error) if the catalogue couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "/datasets", notes = "Creates a dataset")
    @RequestMapping(value = "/datasets",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dataset> createDataset(
            @ApiParam(
                    name = "dataset",
                    value = "The dataset to create")
            @RequestBody Dataset dataset,
            @ApiParam(
                    value = "ownerId",
                    defaultValue = "1",
                    required = true,
                    example = "123")
            @RequestHeader("ownerId") String ownerId,
            @ApiParam(
                    value = "organizationId",
                    defaultValue = "1",
                    required = true,
                    example = "123")
            @RequestHeader("organizationId") String organizationId,
            @ApiParam(
                    value = "organizationName",
                    defaultValue = "1",
                    required = true,
                    example = "Engineering")
            @RequestHeader("organizationName") String organizationName,
            @ApiParam(
                    value = "ownerName",
                    defaultValue = "1",
                    required = true,
                    example = "Mario Rossi")
            @RequestHeader("ownerName") String ownerName
    ) throws URISyntaxException
    {
        log.debug(dataset.toString());
        if (dataset.getId() != null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dataset", "idexists")).body(null);

        dataset.setOwner(new Owner(ownerId,ownerName, new Organization(organizationId,organizationName)));
        Dataset ds = datasetService.save(dataset);
        return ResponseEntity.created(new URI("/api/datasets/" + ds.getId())).headers(HeaderUtil.createEntityCreationAlert("dataset ", dataset.getId().toString())).body(dataset);

    }

  /*  *//**
     * PUT  /datasets : Updates an existing dataset.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataset,
     * or with status 400 (Bad Request) if the catalogue is not valid,
     * or with status 500 (Internal Server Error) if the dataset couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     *//*
    @ApiOperation(value = "/datasets", notes = "Updates an existing dataset", position = 6)
    @RequestMapping(value = "/datasets",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dataset> updateDataset(
            @ApiParam(

                    name = "dataset",
                    value = "The dataset to update"
            )
            @RequestBody Dataset d
    ) throws URISyntaxException
    {
        if (d.getId() != null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dataset", "idexists")).body(null);

        Dataset dataset = datasetService.createDataset(d);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("dataset updated ", dataset.getId().toString())).body(dataset);
    }*/

    /**
     * DELETE  /datasets/{id} : delete the "id" dataset.
     *
     * @param id the id of the dataset to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    /*@ApiOperation(value = "/datasets/{id}", notes = "delete a dataset")
    @RequestMapping(value = "/datasets/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDataset(
            @ApiParam(

                    name = "id",
                    value = "The id of the dataset to delete"
            )
            @PathVariable Long id
    ) throws URISyntaxException
    {

        datasetService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("datasetId", id.toString())).build();

    }*/
}
