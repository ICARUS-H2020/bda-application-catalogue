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
import it.eng.alidalab.applicationcatalogue.domain.service.Framework;
import it.eng.alidalab.applicationcatalogue.services.FrameworkService;
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

@Api(description = "Framework Endpoint")
@RestController
@RequestMapping("/ServiceCatalogue/api/v2")
public class FrameworkResource
{

    private final Logger log = LoggerFactory.getLogger(FrameworkResource.class);
    @Inject
    private FrameworkService frameworkService;

    /**
     * GET  /frameworks : get all the frameworks
     *
     * @return all the frameworks
     */
    @ApiOperation(value = "/frameworks", notes = "get all the frameworks")
    @RequestMapping(value = "/frameworks",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    List<Framework> getFrameworks()
    {
        return frameworkService.getFrameworks();
    }

    /**
     * GET  /frameworks/{id} : get the framework
     *
     * @param frameworkId the framework id
     * @return the ResponseEntity with status 200 (OK) and the framework in the body
     * or with status 400 (Bad Request) if the framework can't be found,
     * @throws 400 if the framework doesn't exist
     */
    @ApiOperation(value = "/frameworks/{id}", notes = "get the framework")
    @RequestMapping(value = "/frameworks/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Framework> getFramework(@ApiParam(value = "Frameworks id", required = true) @PathVariable("id") Long frameworkId)
    {
        try
        {
            Framework fw = frameworkService.getFramework(frameworkId);
            return ResponseEntity.ok(fw);
        } catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * POST  /frameworks : Creates a  framework.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated catalogue,
     * or with status 400 (Bad Request) if the catalogue is not valid,
     * or with status 500 (Internal Server Error) if the catalogue couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "/frameworks", notes = "Creates a framework")
    @RequestMapping(value = "/frameworks",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Framework> createFramework(
            @ApiParam(

                    name = "framework",
                    value = "The framework to create"
            )
            @RequestBody Framework framework
    ) throws URISyntaxException
    {
        log.debug(framework.toString());
        if (framework.getId() != null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("framework", "idexists")).body(null);


        Framework fw = frameworkService.createFramework(framework);
        return ResponseEntity.created(new URI("/api/frameworks/" + fw.getId())).headers(HeaderUtil.createEntityCreationAlert("framework ", framework.getId().toString())).body(framework);

    }

    /**
     * PUT  /frameworks : Updates an existing framework.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated framework,
     * or with status 400 (Bad Request) if the catalogue is not valid,
     * or with status 500 (Internal Server Error) if the framework couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "/frameworks", notes = "Updates an existing framework", position = 6)
    @RequestMapping(value = "/frameworks",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Framework> updateFramework(
            @ApiParam(

                    name = "framework",
                    value = "The framework to update"
            )
            @RequestBody Framework c
    ) throws URISyntaxException
    {
        if (c.getId() == null)
            return createFramework(c);

        Framework framework = frameworkService.createFramework(c);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("Framework updated ", framework.getId().toString())).body(framework);

    }

    /**
     * DELETE  /frameworks/{id} : delete the "id" framework.
     *
     * @param id the id of the framework to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "/frameworks/{id}", notes = "delete a framework", position = 8)
    @RequestMapping(value = "/frameworks/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteFramework(
            @ApiParam(

                    name = "id",
                    value = "The id of the framework to delete"
            )
            @PathVariable Long id
    ) throws URISyntaxException
    {

        frameworkService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("frameworkId", id.toString())).build();

    }


}
