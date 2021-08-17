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
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name = "metric")
public class Metric
{
    @Id
    @GeneratedValue
    private Long id;
    @ApiModelProperty(example = "time")
    private String name;
    @ApiModelProperty(example = "0")
    @Column(name = "min_value")
    private Double minValue;
    @ApiModelProperty(example = "100000")
    @Column(name = "max_value")
    private Double maxValue;
    private Double value;
    @ApiModelProperty(example = "MILLISECONDS")
    private MEASURE measure;
    @ApiModelProperty(example = "${historyServerUrl}/api/v1/applications")
    private String url;
    @JsonBackReference
    @ManyToOne
    private Service service;

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

    public Metric setName(String name)
    {
        this.name = name;
        return this;
    }

    public Double getMinValue()
    {
        return minValue;
    }

    public Metric setMinValue(Double minValue)
    {
        this.minValue = minValue;
        return this;
    }

    public Double getMaxValue()
    {
        return maxValue;
    }

    public Metric setMaxValue(Double maxValue)
    {
        this.maxValue = maxValue;
        return this;
    }

    public Double getValue()
    {
        return value;
    }

    public Metric setValue(Double value)
    {
        this.value = value;
        return this;
    }

    public MEASURE getMeasure()
    {
        return measure;
    }

    public Metric setMeasure(MEASURE measure)
    {
        this.measure = measure;
        return this;
    }

    public String getUrl()
    {
        return url;
    }

    public Metric setUrl(String url)
    {
        this.url = url;
        return this;
    }

    public Service getService()
    {
        return service;
    }

    public void setService(Service service)
    {
        this.service = service;
    }

    public enum MEASURE
    {
        MILLISECONDS, BYTES, NONE
    }


}
