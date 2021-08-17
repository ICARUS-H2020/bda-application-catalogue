package it.eng.alidalab.applicationcatalogue.restcontroller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.eng.alidalab.applicationcatalogue.domain.Model;
import it.eng.alidalab.applicationcatalogue.services.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("bda-catalogue/api/v1/")
public class ModelController {
    @Autowired
    private ModelService modelService;

    /**
     * GET  /models : get all Model of a ownerId
     * @param organizationId Organization
     * @param ownerId Owner
     * @return a list of Model in the response body
     */
    @ApiOperation(value = "get all BDA Apps of a organizationId and ownerId", notes = "get all Models of a organizationId and ownerId")
    @RequestMapping(value = "/models",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Collection<Model> getBdaByOrganization(
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
        Collection<Model> models= modelService.findModelsByOrganizationIdAndOwnerId(organizationId, ownerId);
        return models;
    }

}
