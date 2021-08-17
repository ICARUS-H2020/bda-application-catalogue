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

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class TuningProperty extends Property
{
    @ApiModelProperty(example = "1000")
    @Column(name = "min_value")
    private String minValue;
    @ApiModelProperty(example = "1048576")
    @Column(name = "max_value")
    private String maxValue;
    @ApiModelProperty(example = "BYTES")
    private Metric.MEASURE measure;
    @JsonManagedReference
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "tuning_property", fetch = FetchType.EAGER)
    private Collection<CategoryMapping> mappings;
    private boolean category;

    public TuningProperty()
    {
        super();
        this.mappings = new ArrayList<>();
    }

    public boolean isCategory()
    {
        return category;
    }

    public void setCategory(boolean category)
    {
        this.category = category;
    }

    public String getMinValue()
    {
        return minValue;
    }

    public TuningProperty setMinValue(String minValue)
    {
        this.minValue = minValue;
        return this;
    }

    public String getMaxValue()
    {
        return maxValue;
    }

    public TuningProperty setMaxValue(String maxValue)
    {
        this.maxValue = maxValue;
        return this;
    }

    public Metric.MEASURE getMeasure()
    {
        return measure;
    }

    public TuningProperty setMeasure(Metric.MEASURE measure)
    {
        this.measure = measure;
        return this;
    }

    public Collection<CategoryMapping> getMappings()
    {
        return mappings;
    }

    public TuningProperty setMappings(Collection<CategoryMapping> mappings)
    {
        this.mappings = mappings;
        return this;
    }
}
