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

import javax.persistence.Entity;

@Entity
public class ApplicationProperty extends Property
{
    private boolean inputData;
    private boolean outputData;

    public boolean isInputData()
    {
        return inputData;
    }

    public ApplicationProperty setInputData(boolean inputData)
    {
        this.inputData = inputData;
        return this;
    }

    public boolean isOutputData()
    {
        return outputData;
    }

    public ApplicationProperty setOutputData(boolean outputData)
    {
        this.outputData = outputData;
        return this;
    }

}