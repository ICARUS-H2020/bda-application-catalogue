package it.eng.alidalab.applicationcatalogue.restcontroller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.eng.alidalab.applicationcatalogue.domain.VizConfiguration;
import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;
import it.eng.alidalab.applicationcatalogue.services.VizConfigurationServices;
import it.eng.alidalab.applicationcatalogue.util.HeaderUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("bda-catalogue/api/v1/")
public class VizConfigurationController {

    @Inject
    private VizConfigurationServices vizConfigurationServices;


    /**
     * GET  /bdas/{bdaId}/viz-configurations/{id} : get the workflow
     *
     * @param id the viz-configuration id
     * @return the ResponseEntity with status 200 (OK) and the viz-configuration in the body
     * or with status 400 (Bad Request) if the viz-configuration can't be found,
     * @throws 404 if the viz-configuration doesn't exist
     */
    @ApiOperation(value = "/bdas/{bdaId}/viz-configurations/{id}", notes = "get the viz-configuration")
    @RequestMapping(value = "/bdas/{bdaId}/viz-configurations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VizConfiguration> getWorkflow(
            @ApiParam(
                    value = "viz-configuration id",
                    required = true)
            @PathVariable("id") Long id,
            @ApiParam(
                    value = "bda id",
                    required = true)
            @PathVariable("bdaId") Long bdaId,
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
            VizConfiguration vizConfiguration = vizConfigurationServices.getVizConfiguration(id, bdaId, ownerId, organizationId);
            return vizConfiguration!=null?ResponseEntity.ok(vizConfiguration):new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * POST  /bdas/{bdaId}/vizConfigurations : Creates a viz-configuration.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated catalogue,
     * or with status 400 (Bad Request) if the catalogue is not valid,
     * or with status 500 (Internal Server Error) if the vizConfiguration couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "/bdas/{bdaId}/viz-configurations", notes = "Creates a vizConfiguration")
    @RequestMapping(value = "/bdas/{bdaId}/viz-configurations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VizConfiguration> createWorkflow(

            @ApiParam(
                    value = "bda id",
                    required = true)
            @PathVariable("bdaId") Long bdaId,
            @ApiParam(

                    name = "vizConfiguration",
                    value = "The vizConfiguration to create"
            )
            @RequestBody VizConfiguration vizConfiguration,
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
            @RequestHeader("organizationId") String organizationId
    ) throws URISyntaxException
    {
        VizConfiguration vizConfigurationStored = vizConfigurationServices.save(bdaId,organizationId, ownerId, vizConfiguration);
        return ResponseEntity.created(new URI("/api/vizConfigurations/" + vizConfigurationStored.getId())).headers(HeaderUtil.createEntityCreationAlert("vizConfiguration ", vizConfigurationStored.getId().toString())).body(vizConfigurationStored);

    }
}
