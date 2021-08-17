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
import it.eng.alidalab.applicationcatalogue.domain.Status;
import it.eng.alidalab.applicationcatalogue.domain.service.Framework;
import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;
import it.eng.alidalab.applicationcatalogue.services.FrameworkService;
import it.eng.alidalab.applicationcatalogue.services.WorkflowService;
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
import java.util.Optional;

@Api(description = "Workflow Endpoint")
@RestController
@RequestMapping("bda-catalogue/api/v1/")
public class WorkflowController
{

    private final Logger log = LoggerFactory.getLogger(WorkflowController.class);
    @Inject
    private WorkflowService workflowService;

    /**
     * GET  /workflows : get all the workflows
     *
     * @return all the workflows
     */
    @ApiOperation(value = "/workflows", notes = "get all the workflows")
    @RequestMapping(value = "/workflows",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public
    @ResponseBody
    List<Workflow> getWorkflows(
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
            @RequestHeader("ownerId") String ownerId
    )
    {
        return workflowService.getWorkflows(ownerId, organizationId);
    }

    /**
     * GET  /workflows/{id} : get the workflow
     *
     * @param id the workflow id
     * @return the ResponseEntity with status 200 (OK) and the workflow in the body
     * or with status 400 (Bad Request) if the workflow can't be found,
     * @throws 404 if the workflow doesn't exist
     */
    @ApiOperation(value = "/workflows/{id}", notes = "get the workflow")
    @RequestMapping(value = "/workflows/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Workflow> getWorkflow(
            @ApiParam(
                    value = "Workflows id",
                    required = true)
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
        try
        {
            Workflow wf = workflowService.getWorkflow(id, ownerId, organizationId);
            return wf!=null?ResponseEntity.ok(wf):new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (EntityNotFoundException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * POST  /workflows : Creates a  workflow.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated catalogue,
     * or with status 400 (Bad Request) if the catalogue is not valid,
     * or with status 500 (Internal Server Error) if the catalogue couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "/workflows", notes = "Creates a workflow")
    @RequestMapping(value = "/workflows",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Workflow> createWorkflow(
            @ApiParam(

                    name = "workflow",
                    value = "The workflow to create"
            )
            @RequestBody Workflow workflow
    ) throws URISyntaxException
    {
        log.debug(workflow.toString());
        if (workflow.getId() != null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workflow", "idexists")).body(null);


        Workflow fw = workflowService.createWorkflow(workflow);
        return ResponseEntity.created(new URI("/api/workflows/" + fw.getId())).headers(HeaderUtil.createEntityCreationAlert("workflow ", workflow.getId().toString())).body(workflow);

    }

/*
* moved into statusController
* *//**
     * PATCH  /workflows/{name} : Updates an existing workflow.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated workflow,
     * or with status 400 (Bad Request) if the catalogue is not valid,
     * or with status 500 (Internal Server Error) if the workflow couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     *//*

    @ApiOperation(value = "/workflows", notes = "Updates status of an existing workflow")
    @RequestMapping(value = "/workflows",
            //params = {"name"},
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Workflow> updateWorkflowStatus(
            @RequestParam(name = "name") String name,
            @ApiParam(

                    name = "status",
                    value = "The workflow to update"
            )
            @RequestBody Status status
    ) {
            Workflow workflow = workflowService.updateStatus(name, status);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("workflow updated ", workflow.getId().toString())).body(workflow);

        }*/




    /**
     * PUT  /workflows : Updates an existing workflow.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated workflow,
     * or with status 400 (Bad Request) if the catalogue is not valid,
     * or with status 500 (Internal Server Error) if the workflow couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "/workflows", notes = "Updates an existing workflow", position = 6)
    @RequestMapping(value = "/workflows",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Workflow> updateWorkflow(
            @ApiParam(

                    name = "workflow",
                    value = "The workflow to update"
            )
            @RequestBody Workflow c
    ) throws URISyntaxException
    {
        if (c.getId() == null)
            return createWorkflow(c);

        Workflow workflow = workflowService.createWorkflow(c);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("Workflow updated ", workflow.getId().toString())).body(workflow);

    }

    /**
     * DELETE  /workflows/{id} : delete the "id" workflow.
     *
     * @param id the id of the workflow to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "/workflows/{id}", notes = "delete a workflow")
    @RequestMapping(value = "/workflows/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteWorkflow(
            @ApiParam(

                    name = "id",
                    value = "The id of the workflow to delete"
            )
            @PathVariable Long id
    ) throws URISyntaxException
    {

        workflowService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("workflowId", id.toString())).build();

    }


}
