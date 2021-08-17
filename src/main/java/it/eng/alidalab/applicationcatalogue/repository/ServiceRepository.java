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

package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.service.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long>
{
    public static final String FIND_NAMES = "SELECT s.name FROM Service s where s.area=:area and s.workflow is null";

    public Collection<Service> findByAreaAndWorkflowIsNull(Service.AREA area);

    public Collection<Service> findByCatalogues_NameAndWorkflowIsNull(String catalogueName);

    @Query(value = FIND_NAMES)
    public Collection<String> getServiceNames(@Param("area") Service.AREA area);

    Collection<Service> getServiceByNameAndWorkflowIsNullOrderByVersionDesc(String name);

    List<Service> getServiceByWorkflowIsNull();

}
