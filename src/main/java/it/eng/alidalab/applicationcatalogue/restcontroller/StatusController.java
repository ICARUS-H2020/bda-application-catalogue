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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.eng.alidalab.applicationcatalogue.domain.Status;
import it.eng.alidalab.applicationcatalogue.domain.dto.StatusErrorManagerDTO;
import it.eng.alidalab.applicationcatalogue.services.StatusService;
import it.eng.alidalab.applicationcatalogue.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(description = "Status Endpoint")
@RestController
@RequestMapping("bda-catalogue/api/v1/")
public class StatusController
{

    private final Logger log = LoggerFactory.getLogger(StatusController.class);
    @Inject
    private StatusService statusService;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * POST  /workflows/{wfId}/status : Add Status into a  workflow.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated status,
     * or with status 400 (Bad Request) if the catalogue is not valid,
     * or with status 500 (Internal Server Error) if the catalogue couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "/workflows/{wfName}/status", notes = "Update status")
    @RequestMapping(value = "/workflows/{wfName}/status",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateStatusWorkflow(
            @ApiParam(
                    value = "wfId",
                    defaultValue = "1",
                    type = "uri",
                    required = true,
                    example = "123")
            @PathVariable("wfName") String wfName,
            @RequestBody Status status, HttpServletRequest request
    ) {

        try {
            Status statusDb = statusService.updateStatus(status, wfName);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("Status updated ", statusDb.getId().toString())).body(statusDb);

        }catch (BadRequestException drE ){
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("timestamp",new Date());
            errorMap.put("path",request.getRequestURI());
            errorMap.put("status", 400);
            errorMap.put("error", drE.getResponse().getStatus());
            errorMap.put("message",drE.getResponse().getStatusInfo().getReasonPhrase());
            JsonNode jsonNode = objectMapper.valueToTree(errorMap);
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonNode);
        }
    }


    /**
     * POST  /workflows/{wfName}/services/{serviceName}/status : Add Status into a service of a workflow.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated status,
     * or with status 400 (Bad Request) if the catalogue is not valid,
     * or with status 500 (Internal Server Error) if the catalogue couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "/workflows/{wfName}/services/{serviceName}/status", notes = "Update status")
    @RequestMapping(value = "/workflows/{wfName}/services/{serviceName}/status",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusErrorManagerDTO> updateStatusService(
            @ApiParam(
                    value = "wfName",
                    defaultValue = "1",
                    type = "uri",
                    required = true,
                    example = "123")
            @PathVariable("wfName") String wfName,
            @ApiParam(
                    value = "serviceName",
                    defaultValue = "1",
                    type = "uri",
                    required = true,
                    example = "123")
            @PathVariable("serviceName") String serviceName,
            @RequestBody Status status
    ) {
        try {
            StatusErrorManagerDTO statusDb = statusService.updateStatus(status, wfName, serviceName);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("Status updated ", statusDb.getId().toString())).body(statusDb);

        }catch (BadRequestException drE ){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
