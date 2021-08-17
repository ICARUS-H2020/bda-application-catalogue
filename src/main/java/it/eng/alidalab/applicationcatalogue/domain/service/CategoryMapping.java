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

import javax.persistence.*;

@Entity
@Table(name = "category_mapping")
public class CategoryMapping
{
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "int_value")
    private Integer intValue;
    private String name;
    @JsonBackReference
    @ManyToOne
    private TuningProperty tuning_property;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getIntValue()
    {
        return intValue;
    }

    public CategoryMapping setIntValue(Integer intValue)
    {
        this.intValue = intValue;
        return this;
    }

    public String getName()
    {
        return name;
    }

    public CategoryMapping setName(String name)
    {
        this.name = name;
        return this;
    }

    public TuningProperty getTuning_property()
    {
        return tuning_property;
    }

    public void setTuning_property(TuningProperty tuning_property)
    {
        this.tuning_property = tuning_property;
    }
}
