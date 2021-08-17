/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package it.eng.alidalab.applicationcatalogue.restcontroller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.eng.alidalab.applicationcatalogue.domain.service.Catalogue;
import it.eng.alidalab.applicationcatalogue.services.CatalogueService;
import it.eng.alidalab.applicationcatalogue.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Api(description = "Catalogue Endpoint")
@RestController
@RequestMapping("/ServiceCatalogue/api/v2")
public class CatalogueResource
{
    private final Logger log = LoggerFactory.getLogger(CatalogueResource.class);
    @Inject
    private CatalogueService catalogueService;
    /**
     * GET  /catalogues/ : get all the catalogues
     *
     * @return all the catalogues
     */
    @ApiOperation(value = "/catalogues", notes = "get all the catalogues")
    @RequestMapping(value = "/catalogues",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    List<Catalogue> getCatalogues()
    {
        return catalogueService.getCatalogues();
    }

    /**
     * GET  /catalogues/{id} : get the catalogue
     *
     * @param catalogueId the catalogue id
     * @return the ResponseEntity with status 200 (OK) and the catalogue in the body
     * or with status 400 (Bad Request) if the catalogue can't be found,
     * @throws 400 if the catalogue doesn't exist
     */
    @ApiOperation(value = "/catalogues/{id}", notes = "get the catalogue")
    @RequestMapping(value = "/catalogues/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Catalogue> getCatalogue(@ApiParam(value = "Catalogue id", required = true) @PathVariable("id") Long catalogueId)
    {
        try
        {
            Catalogue cat = catalogueService.getCatalogue(catalogueId);
            return ResponseEntity.ok(cat);
        } catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET  /catalogues?name={name} : get the catalogue by name
     *
     * @param catalogueName the catalogue name
     * @return the ResponseEntity with status 200 (OK) and the catalogue in the body
     * or with status 400 (Bad Request) if the catalogue can't be found,
     * @throws 400 if the catalogue doesn't exist
     */
    @ApiOperation(value = "/catalogues?name={name}", notes = "get the catalogue")
    @RequestMapping(value = "/catalogues",
            method = RequestMethod.GET,
            params = {"name"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Catalogue> getCatalogue(@ApiParam(value = "Catalogue name", required = true) @RequestParam("name") String catalogueName)
    {
        try
        {
            Catalogue cat = catalogueService.getCatalogue(catalogueName);
            return ResponseEntity.ok(cat);
        } catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /**
     * DELETE  /catalogues/{id} : delete the "id" catalogue.
     *
     * @param id the id of the catalogue to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "/catalogues/{id}", notes = "delete a catalogue")
    @RequestMapping(value = "/catalogues/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteCatalogue(
            @ApiParam(

                    name = "id",
                    value = "The id of the catalogue to delete"
            )
            @PathVariable Long id
    ) throws URISyntaxException
    {

        catalogueService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("catalogueId", id.toString())).build();

    }

    /**
     * PUT  /catalogues : Updates an existing catalogue.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated catalogue,
     * or with status 400 (Bad Request) if the catalogue is not valid,
     * or with status 500 (Internal Server Error) if the catalogue couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "/catalogues", notes = "Updates an existing catalogue")
    @RequestMapping(value = "/catalogues",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Catalogue> updateCatalogue(
            @ApiParam(

                    name = "catalogue",
                    value = "The catalogue to update"
            )
            @RequestBody Catalogue c
    ) throws URISyntaxException
    {
        if (c.getId() == null)
            return createCatalogue(c);

        Catalogue catalogue = catalogueService.saveCatalogue(c);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("Catalogue updated ", catalogue.getId().toString())).body(catalogue);

    }


    /**
     * POST  /catalogues : Creates a  catalogue.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated catalogue,
     * or with status 400 (Bad Request) if the catalogue is not valid,
     * or with status 500 (Internal Server Error) if the catalogue couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "/catalogues", notes = "Creates a catalogue")
    @RequestMapping(value = "/catalogues",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Catalogue> createCatalogue(
            @ApiParam(

                    name = "catalogue",
                    value = "The catalogue to create"
            )
            @RequestBody Catalogue c
    ) throws URISyntaxException
    {
        log.debug(c.toString());
        if (c.getId() != null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("catalogue", "idexists")).body(null);


        Catalogue catalogue = catalogueService.saveCatalogue(c);
        return ResponseEntity.created(new URI("/api/v2/catalogues/" + catalogue.getId())).headers(HeaderUtil.createEntityCreationAlert("catalogue ", catalogue.getId().toString())).body(catalogue);

    }


}
