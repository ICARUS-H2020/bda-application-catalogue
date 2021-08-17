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
import it.eng.alidalab.applicationcatalogue.domain.service.Metric;
import it.eng.alidalab.applicationcatalogue.services.MetricService;
import it.eng.alidalab.applicationcatalogue.domain.service.Service;
import it.eng.alidalab.applicationcatalogue.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@Api(description = "Metric Endpoint")
@RestController
@RequestMapping("/ServiceCatalogue/api")
public class MetricResource
{

    private final Logger log = LoggerFactory.getLogger(MetricResource.class);

    @Autowired
    private MetricService metricService;

    /**
     * GET  /metrics/{id} : get a service
     *
     * @param id the Metric Id
     * @return the ResponseEntity with status 200 (OK) and the service in the body
     * or with status 400 (Bad Request) if the service can't be found
     */
    @ApiOperation(value = "/metrics/{id}", notes = "get a service")
    @RequestMapping(value = "/metrics/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity<Metric> getMetric(@ApiParam(value = "Metric ID", required = true) @PathVariable Long id)
    {
        Metric metric = metricService.getMetric(id);
        return Optional.ofNullable(metric)
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /metrics/ : get all services
     *
     * @return the ResponseEntity with status 200 (OK) and the metrics in the body
     * or with status 400 (Bad Request) if the service can't be found
     */
    @ApiOperation(value = "/metrics", notes = "get all metrics")
    @RequestMapping(value = "/metrics",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Metric> getMetrics()
    {
        return metricService.getMetrics();
    }

    /**
     * GET  /services/{serviceId}/metrics : get all metrics for a service
     *
     * @return the ResponseEntity with status 200 (OK) and the metrics in the body
     * or with status 400 (Bad Request) if the service can't be found
     */
    @ApiOperation(value = "/services/{serviceId}/metrics", notes = "get all metrics for a service")
    @RequestMapping(value = "/services/{serviceId}/metrics",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Metric> getMetrics(@ApiParam(value = "Service ID", required = true) @PathVariable Long serviceId)
    {
        return metricService.getMetrics(serviceId);
    }


    /**
     * POST  /metrics : Create a new metric.
     *
     * @param m The metric to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metric, or with status 400 (Bad Request) if the metric has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */

    @ApiOperation(value = "/metrics", notes = "post a new metric", position = 5)
    @RequestMapping(value = "/metrics",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Metric> createMetric(
            @ApiParam(

                    name = "metrics",
                    value = "The metric to create"
            )
            @RequestBody Metric m
    ) throws URISyntaxException
    {

        log.debug(m.toString());
        if (m.getId() != null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("metric", "idexists")).build();

        Metric metric = metricService.save(m);
        return ResponseEntity.created(new URI("/api/metrics/" + metric.getId())).headers(HeaderUtil.createEntityCreationAlert("metric ", metric.getId().toString())).body(metric);

    }


    /**
     * PUT  /metrics : Updates an existing metric.
     *
     * @param m the metric to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated service,
     * or with status 400 (Bad Request) if the service is not valid,
     * or with status 500 (Internal Server Error) if the service couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "/metrics", notes = "Updates an existing service", position = 6)
    @RequestMapping(value = "/metrics",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Metric> updateMetric(
            @ApiParam(

                    name = "metrics",
                    value = "The metric to create"
            )
            @RequestBody Metric m
    ) throws URISyntaxException
    {
        if (m.getId() == null)
            return createMetric(m);

        Metric metric = metricService.save(m);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("Metric updated ", metric.getId().toString())).body(m);

    }

    /**
     * DELETE  /metrics/{id} : delete the "id" metric.
     *
     * @param id the id of the metric to delete
     * @return the ResponseEntity with status 204 (No Content)
     */
    @ApiOperation(value = "/metrics/{id}", notes = "delete a metric", position = 7)
    @RequestMapping(value = "/metrics/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Service> deleteMetric(
            @ApiParam(
                    name = "id",
                    value = "The id of the metric to delete"
            )
            @PathVariable Long id
    ) throws URISyntaxException
    {


        metricService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("metricId", String.valueOf(id))).build();

    }


}
