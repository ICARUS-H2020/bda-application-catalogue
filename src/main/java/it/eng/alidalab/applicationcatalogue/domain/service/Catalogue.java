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

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "catalogue", indexes = {@Index(name = "IDX_MYIDXCTX", columnList = "name")})
public class Catalogue
{
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "catalogues_services", joinColumns = @JoinColumn(name = "catalogue_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Collection<Service> services;
    @ApiModelProperty(example = "test")
    @Column(unique = true)
    private String name;
    @Id
    @GeneratedValue
    private Long id;

    public Catalogue()
    {
        this.services = new ArrayList<>();
    }

    public Catalogue(Collection<Service> services)
    {
        this.services = services;
    }

    public Catalogue(String catalogueName)
    {
        this.name = catalogueName;
    }

    public Collection<Service> getServices()
    {
        return services;
    }

    public void setServices(Collection<Service> services)
    {
        this.services = services;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }
}
