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
import it.eng.alidalab.applicationcatalogue.domain.service.Service;
import it.eng.alidalab.applicationcatalogue.services.ServiceService;
import it.eng.alidalab.applicationcatalogue.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;



@Api(description = "Service Endpoint")
@RestController
@RequestMapping("/ServiceCatalogue/api/v2")
public class ServiceResource
{
    private final Logger log = LoggerFactory.getLogger(ServiceResource.class);

    @Inject
    private ServiceService serviceService;

    /**
     * GET  /services/{id} : get a service
     *
     * @param id the Service Id
     * @return the ResponseEntity with status 200 (OK) and the service in the body
     * or with status 400 (Bad Request) if the service can't be found
     */
    @ApiOperation(value = "/services/{id}", notes = "get a service", position = 2)
    @RequestMapping(value = "/services/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Service> getService(@ApiParam(value = "Service ID", required = true) @PathVariable Long id)
    {
        Optional<Service> serviceOptional = serviceService.getService(id);
        return serviceOptional
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /services : get all services
     * @return a list of services in the response body
     */
    @ApiOperation(value = "/services", notes = "get all services")
    @RequestMapping(value = "/services",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Service> getServices()
    {

        return serviceService.getAllServices();
    }

    /**
     * GET  /services : get all services by service name (order by Version Desc)
     * @param name
     * @return a list of services in the response body
     */
    @ApiOperation(value = "/services", notes = "get all services")
    @RequestMapping(value = "/services",
            params = {"name"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Service> getServicesParam(String name)
    {

        return serviceService.getAllServices(name);
    }

    /**
     * GET  /services : get all services of a conceptual area
     * @param area Conceptual area, areas include : INGESTION,PREPARATION,PROCESSING,ANALYTICS,VISUALIZATION
     * @return a list of services in the response body
     */
    @ApiOperation(value = "/services", notes = "get all services of a conceptual area", position = 3)
    @RequestMapping(value = "/services", params = "area",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Collection<Service> getAreaServices(
            @ApiParam(
                    value = "Conceptual Area, areas include : INGESTION,PREPARATION,PROCESSING,ANALYTICS,VISUALIZATION",
                    defaultValue = "ANALYTICS",
                    type = "uri",
                    required = true)
            @RequestParam("area") String area)
    {
        log.debug(area);
        return serviceService.getAreaServices(area);
    }

    /**
     * GET  /services/names : get all service names for a conceptual area.
     *
     * @param area Conceptual area, areas include : INGESTION,PREPARATION,PROCESSING,ANALYTICS,VISUALIZATION
     * @return a list of service types in the response body or a response with status 400 (Bad Request) if the area is wrong
     */
    @ApiOperation(value = "/services/names", notes = "get all service names of a conceptual area", position = 4)
    @RequestMapping(value = "/services/names",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Collection<String> getAreaServiceNames(
            @ApiParam(
            value = "Conceptual area, areas include : INGESTION,PREPARATION,PROCESSING,ANALYTICS,VISUALIZATION",
            defaultValue = "ANALYTICS",
            required = true)
            @RequestParam String area)
    {
        log.debug(area);
        return serviceService.getServiceNames(area);
    }


    /**
     * POST  /services : Create a new service.
     *
     * @param s The service to create
     * @return the ResponseEntity with status 201 (Created) and with body the new service, or with status 400 (Bad Request) if the service has already an ID or the field area is null
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */

    @ApiOperation(value = "/services", notes = "post a new service", position = 5)
    @RequestMapping(value = "/services",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Service> createService(
            @ApiParam(

                    name = "service",
                    value = "The service to create"
            )
            @RequestBody Service s
    ) throws URISyntaxException
    {

        log.debug(s.toString());
        if (s.getId() != null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("service", "idexists")).build();
        if (s.getArea() == null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("service", "areaisnull")).build();

        Service service = serviceService.save(s);
        return ResponseEntity.created(new URI("/api/services/" + service.getId())).headers(HeaderUtil.createEntityCreationAlert("service ", service.getId().toString())).body(s);
    }


    /**
     * PUT  /services : Updates an existing service.
     *
     * @param s the service to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated service,
     * or with status 400 (Bad Request) if the service is not valid,
     * or with status 500 (Internal Server Error) if the service couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "/services", notes = "Updates an existing service", position = 6)
    @RequestMapping(value = "/services",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Service> updateService(
            @ApiParam(

                    name = "service",
                    value = "The service to update"
            )
            @RequestBody Service s
    ) throws URISyntaxException
    {
        if (s.getId() == null)
            return createService(s);

        Service service = serviceService.save(s);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("Service updated ", service.getId().toString())).body(s);

    }

    /**
     * DELETE  /services/{id} : delete the "id" service.
     *
     * @param id the id of the service to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "/services/{id}", notes = "delete a service", position = 7)
    @RequestMapping(value = "/services/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Service> deleteService(
            @ApiParam(
                    name = "id",
                    value = "The id of the service to delete"
            )
            @PathVariable Long id
    ) throws URISyntaxException
    {


        serviceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("serviceId", String.valueOf(id))).build();

    }


}
