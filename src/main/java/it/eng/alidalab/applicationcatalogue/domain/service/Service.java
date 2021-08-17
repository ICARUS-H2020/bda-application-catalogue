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

package it.eng.alidalab.applicationcatalogue.domain.service;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModelProperty;
import it.eng.alidalab.applicationcatalogue.domain.Model;
import it.eng.alidalab.applicationcatalogue.domain.Status;
import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "service", indexes = {@Index(name = "IDX_MYIDXAREA", columnList = "area")})
public class Service
{
    @Id
    @GeneratedValue
    private Long id;
    @ManyToMany(mappedBy = "services")
    @JsonIgnore
    private Collection<Catalogue> catalogues;

    @ApiModelProperty(example = "INGESTION")
    @Column(name = "area")
    private AREA area;
    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "service", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Metric> metrics;
    @ApiModelProperty(example = "BATCH")
    private MODE mode;

    @ApiModelProperty(example = "1.0.0")
    private String version;

    @ApiModelProperty(example = "Exports a file from an FTP location to HDFS")
    @Column(length = 2048)
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    private Framework framework;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "service", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Property> properties;

    @ApiModelProperty(example = "docker://nodo1.toreador.org:8082/ftptohdfs:1.0.0.BUILD-SNAPSHOT")
    private String url;

    @JsonBackReference
    @ManyToOne
    private Workflow workflow;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Model> models;

    @ElementCollection
    private List<String> tags = new ArrayList<>();

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Service()
    {
        this.catalogues = new ArrayList<>();
        this.metrics = new HashSet<>();
    }

    public Service(AREA area, String name)
    {
        this.catalogues = new ArrayList<>();
        this.area = area;
        this.name = name;
        this.metrics = new HashSet<>();

    }

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Status statusModel;

    public Status getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(Status statusModel) {
        this.statusModel = statusModel;
    }

    @ApiModelProperty(example = "ftptohdfs")
    public String getName()
    {
        return name;
    }

    public Service setName(String name)
    {
        this.name = name;
        return this;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public AREA getArea()
    {
        return area;
    }

    public Service setArea(AREA area)
    {
        this.area = area;
        return this;
    }

    public String getDescription()
    {
        return description;
    }

    public Service setDescription(String description)
    {
        this.description = description;
        return this;
    }

    public Long getId()
    {
        return id;
    }

    public Service setId(Long id)
    {
        this.id = id;
        return this;
    }

    public Collection<Catalogue> getCatalogues()
    {
        return catalogues;
    }

    public Service setCatalogues(Collection<Catalogue> catalogues)
    {
        this.catalogues = catalogues;
        return this;
    }

    public Framework getFramework()
    {
        return framework;
    }

    public void setFramework(Framework framework)
    {
        this.framework = framework;
    }

    public String getUrl()
    {
        return url;
    }

    public Service setUrl(String url)
    {
        this.url = url;
        return this;
    }


    public Set<Metric> getMetrics()
    {
        return metrics;
    }

    public void setMetrics(Set<Metric> metrics)
    {
        this.metrics = metrics;
    }

    public MODE getMode()
    {
        return mode;
    }

    public void setMode(MODE mode)
    {
        this.mode = mode;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public Set<Property> getProperties()
    {
        return properties;
    }

    public void setProperties(Set<Property> properties)
    {
        this.properties = properties;
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public enum AREA
    {
        INGESTION, PREPARATION, PROCESSING, ANALYTICS, VISUALIZATION
    }

    public enum MODE
    {
        BATCH, SINK, SOURCE, PROCESSOR
    }

}
