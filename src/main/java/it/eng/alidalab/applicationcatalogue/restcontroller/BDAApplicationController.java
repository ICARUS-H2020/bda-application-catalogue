package it.eng.alidalab.applicationcatalogue.restcontroller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.eng.alidalab.applicationcatalogue.domain.BDAApplication;
import it.eng.alidalab.applicationcatalogue.domain.Organization;
import it.eng.alidalab.applicationcatalogue.domain.Owner;
import it.eng.alidalab.applicationcatalogue.domain.dto.BDAApplicationDetailDTO;
import it.eng.alidalab.applicationcatalogue.domain.dto.BDAApplicationPreviewDTO;
import it.eng.alidalab.applicationcatalogue.services.BDAApplicationService;
import it.eng.alidalab.applicationcatalogue.util.HeaderUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("bda-catalogue/api/v1/")
@Api(value="BDA Application Management System", description="Operations pertaining to BDA Application in BDA Application Catalogue")
public class BDAApplicationController {

    @Autowired
    BDAApplicationService bdaApplicationService;

    @Autowired
    ModelMapper modelMapper;

   /* *//**
     * GET  /bdas/ : get all the BDA Application
     *
     * @return all the bda
     *//*
    @ApiOperation(value = "Get all the bda Application", response = List.class)
    @RequestMapping(value = "/bda",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    List<BDAApplication> getBdas()
    {

        return bdaApplicationService.findAll();
    }
*/


    /**
     * GET  /bdas : get all BDA Apps of a ownerId
     * @param organizationId Organization Owner
     * @return a list of BDA Apps in the response body
     */
    @ApiOperation(value = "get all BDA Apps of a organizationId", notes = "get all BDA Apps of a organizationId")
    @RequestMapping(value = "/bdas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
        Collection<BDAApplicationPreviewDTO> getBdaByOrganization(
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
            @RequestHeader("ownerId") String ownerId)

    {
        Collection<BDAApplication> bdas= bdaApplicationService.findBDAApplicationsByOrganizationIdAndOwnerId(organizationId, ownerId);
        Collection<BDAApplicationPreviewDTO> bdaApplicationPreviewDTOList = new ArrayList<>();
        bdas.forEach(bda -> { bdaApplicationPreviewDTOList.add(modelMapper.map(bda, BDAApplicationPreviewDTO.class)); });
        return bdaApplicationPreviewDTOList;
    }

/*    *//**
     * GET  /bdas : get all BDA Apps of a ownerId
     * @param organizationId Organization Owner
     * @return a list of BDA Apps in the response body
     *//*
    @ApiOperation(value = "get all BDA Apps of a organizationId", notes = "get all BDA Apps of a organizationId")
    @RequestMapping(value = "/bdas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Collection<BDAApplication> getBdaApplicationsAuthCheck(
            @ApiParam(
                    value = "bdaId",
                    defaultValue = "1",
                    required = true,
                    example = "123")
            @RequestHeader("bdaId") String bdaId,
            @ApiParam(
                    value = "organizationId",
                    defaultValue = "1",
                    required = true,
                    example = "123")
            @RequestHeader("organizationId") String organizationId)

    {
        bdaApplicationService.authorizationCheck(bdaId, organizationId);
        return bdaApplicationService.authorizationCheck(bdaId, organizationId);
    }*/


    /**
     * GET  /bdas/{id} : get all BDA Apps of a ownerId
     * @param id the id of the BDA App to find
     * @param organizationId Organization Owner
     * @return a list of BDA Apps in the response body
     */
    @ApiOperation(value = "/bdas/{id}", notes = "get a BDA application")
    @RequestMapping(value = "/bdas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<BDAApplicationDetailDTO> getBdaByIdAndOrganizationIdAndOwnerId(
            @ApiParam(
                    value = "id",
                    defaultValue = "1",
                    type = "uri",
                    required = true,
                    example = "123")
            @PathVariable("id") Long id,
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
        Optional<BDAApplication> bdaApplicationOptional = bdaApplicationService.findBDAApplicationByBDAApplicationIdAndOrganizationIdAndOwnerId(id,organizationId,ownerId);
        if(!bdaApplicationOptional.isPresent())
            return ResponseEntity.notFound().build();
        BDAApplicationDetailDTO bdaApplicationDetailDTO = modelMapper.map(bdaApplicationOptional.get(), BDAApplicationDetailDTO.class);
        return ResponseEntity.ok().body(modelMapper.map(bdaApplicationDetailDTO, BDAApplicationDetailDTO.class));
    }


    /**
     * POST  /bdas/ : save a BDA Application
     * @param bdaApplication BDA App
     * @param ownerId Owner of the BDA App
     * @param organizationId Organization Owner
     * @return BDA Application stored
     */
    @ApiOperation(value = "Add an BDA Application with ownerId")
    @PostMapping("/bdas")
    public ResponseEntity<BDAApplicationDetailDTO> createBdaApplication(
            @ApiParam(
                    value = "BDA Application object to store in database",
                    required = true)
            @Valid @RequestBody BDAApplication bdaApplication,
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
            @RequestHeader("ownerName") String ownerName)
            throws URISyntaxException {
        bdaApplication.setOwner(new Owner(ownerId,ownerName, new Organization(organizationId,organizationName)));
        try{
            bdaApplication.getMetadata().getMetadataLicenseDetails().setOwner(new Owner(ownerId,ownerName, new Organization(organizationId,organizationName)));
        }catch (Exception ex){
            System.out.println("No Metadata License Details");
        }

        //if(bdaApplication.getOwner().getId() != ownerId)
          //  return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();

        BDAApplication bda = bdaApplicationService.save(bdaApplication);
        BDAApplicationDetailDTO bdaDto = modelMapper.map(bdaApplication, BDAApplicationDetailDTO.class);
        bdaDto.getDatasets().forEach(ds -> {
            if(ds.getCreatedBy() != null) ds.getCreatedBy().setServices(null);
        });
        return ResponseEntity.created(new URI("/bdas/" + bda.getId())).headers(HeaderUtil.createEntityCreationAlert("BDA App ", bda.getId().toString())).body(bdaDto);
    }


    /**
     * DELETE  /bdas/{id} : get all BDA Apps of a ownerId
     * @param id the id of the BDA App to delete
     * @param ownerId Owner of the BDA App
     * @param organizationId Organization of the BDA App
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "Delete an BDA App with ownerId")
    @RequestMapping(value = "/bdas/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    ResponseEntity<BDAApplication> deleteBdaWithOwnerId(
            @ApiParam(
                    value = "id",
                    defaultValue = "1",
                    type = "uri",
                    required = true,
                    example = "123")
            @PathVariable("id") Long id,
            @ApiParam(
                    value = "ownerId",
                    defaultValue = "1",
                    required = false,
                    example = "123")
            @RequestHeader("ownerId") String ownerId,
            @ApiParam(
                    value = "organizationId",
                    defaultValue = "1",
                    required = true,
                    example = "123")
            @RequestHeader("organizationId") String organizationId)
    {
        bdaApplicationService.toTrashWithOrganizationId(id,organizationId, ownerId);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("bdaAppId", String.valueOf(id))).build();
    }

    /**
     * PATCH  /bdas/{id} : Updates an existing BDA App with ownerId.
     *
     * @param bdaPartial are the part of the BDA to edit
     * @param ownerId Owner of the BDA App
     * @return the ResponseEntity with status 200 (OK) and with body the updated BDA App,
     * or with status 400 (Bad Request) if the BDA App is not valid,
     * or with status 500 (Internal Server Error) if the BDA App couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "Updates an existing BDA App", notes = "Updates an existing BDA App")
    @RequestMapping(value = "/bdas/{id}",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BDAApplicationDetailDTO> updateBdaPartial(
            @ApiParam(
                    value = "id",
                    defaultValue = "1",
                    type = "uri",
                    required = true,
                    example = "123")
            @PathVariable("id") Long id,
            @ApiParam(

                    name = "BDA App",
                    value = "The BDA App to update"
            )
            @RequestBody BDAApplication bdaPartial,
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
        BDAApplication bdaStored = bdaApplicationService.update(id, bdaPartial, ownerId, organizationId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("BDA App updated ", bdaStored.getId().toString())).body(modelMapper.map(bdaStored, BDAApplicationDetailDTO.class));

    }

/*
    *//**
     * PUT  /bdas : Updates an existing BDA App with ownerId.
     *
     * @param bda the BDA App to update
     * @param ownerId Owner of the BDA App
     * @return the ResponseEntity with status 200 (OK) and with body the updated BDA App,
     * or with status 400 (Bad Request) if the BDA App is not valid,
     * or with status 500 (Internal Server Error) if the BDA App couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     *//*
    @ApiOperation(value = "Updates an existing BDA App", notes = "Updates an existing BDA App")
    @RequestMapping(value = "/bdas/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BDAApplication> updateBda(
            @ApiParam(

                    name = "BDA App",
                    value = "The BDA App to update"
            )
            @RequestBody BDAApplication bda,
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
        if (bda.getId() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        bda.setOwner(new Owner(ownerId,new Organization(organizationId)));

        BDAApplication bdaStored = bdaApplicationService.update(bda);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("BDA App updated ", bda.getId().toString())).body(bdaStored);

    }*/




}
