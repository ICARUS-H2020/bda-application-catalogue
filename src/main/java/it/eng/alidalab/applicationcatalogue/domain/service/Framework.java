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

@Entity
@Table(name = "framework")
public class Framework
{
    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    private Long id;
    @ApiModelProperty(example = "Hadoop")
    private String name;
    @ApiModelProperty(example = "2.7.2")
    private String version;

    private String imageUrl;

    public String getName()
    {
        return name;
    }

    public Framework setName(String name)
    {
        this.name = name;
        return this;
    }

    public String getVersion()
    {
        return version;
    }

    public Framework setVersion(String version)
    {
        this.version = version;
        return this;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
