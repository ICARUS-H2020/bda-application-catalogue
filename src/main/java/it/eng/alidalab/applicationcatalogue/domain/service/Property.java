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
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonTypeInfo(include= JsonTypeInfo.As.WRAPPER_OBJECT, use= JsonTypeInfo.Id.CLASS)
public abstract class Property
{
    @ManyToOne(optional = false)
    @JsonBackReference
    @ApiModelProperty(hidden = true)
    protected Service service;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ApiModelProperty(example = "the FTP buffer size")
    @Column(length = 1024)
    private String description;
    private boolean mandatory;
    @ApiModelProperty(example = "262144")
    @Column(name = "default_value", length = 2048)
    private String defaultValue;
    @Column(length = 2048)
    private String value;
    @ApiModelProperty(example = "ftp.bufferzie")
    @Column(name = "param_key")
    private String key;
    private PARAMTYPE type;


    public Property()
    {
    }


    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Service getService()
    {
        return service;
    }

    public Property setService(Service service)
    {
        this.service = service;
        return this;
    }

    public PARAMTYPE getType()
    {
        return type;
    }

    @ApiModelProperty(example = "STRING")
    public Property setType(PARAMTYPE type)
    {
        this.type = type;
        return this;
    }

    @ApiModelProperty(example = "the hdfs path where the file will be written")
    public String getDescription()
    {
        return description;
    }

    public Property setDescription(String description)
    {
        this.description = description;
        return this;
    }

    @ApiModelProperty(example = "hdfs://user/root/")
    public String getDefaultValue()
    {
        return defaultValue;
    }

    public Property setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
        return this;
    }

    public String getValue()
    {
        return value;
    }

    public Property setValue(String value)
    {
        this.value = value;
        return this;
    }

    @ApiModelProperty(example = "hdfsPath")
    public String getKey()
    {
        return key;
    }

    public Property setKey(String key)
    {
        this.key = key;
        return this;
    }

    public boolean isMandatory()
    {
        return mandatory;
    }

    @ApiModelProperty(example = "true")
    public Property setMandatory(boolean mandatory)
    {
        this.mandatory = mandatory;
        return this;
    }

    public enum PARAMTYPE
    {
        STRING, DOUBLE, INT, BOOLEAN, LONG
    }

}
