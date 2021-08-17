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

package it.eng.alidalab.applicationcatalogue.services;

import it.eng.alidalab.applicationcatalogue.domain.service.Service;
import it.eng.alidalab.applicationcatalogue.repository.ServiceRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Transactional
public class ServiceService
{

    @Inject
    private ServiceRepository serviceRepository;


    public ServiceService()
    {

    }


    public Collection<String> getServiceNames(String area)
    {
        Service.AREA areaValue = Service.AREA.valueOf(area);
        return serviceRepository.getServiceNames(areaValue);
    }


    public Optional<Service> getService(Long id)
    {
        Optional<Service> o = serviceRepository.findById(id);
        return o;
    }



    public Collection<Service> getAreaServices(String area)
    {
        Collection<Service> serviceList = serviceRepository.findByAreaAndWorkflowIsNull(Service.AREA.valueOf(area));
        return serviceList;
    }

    public Collection<Service> getCatalogueServices(String catalogueName)
    {
        Collection<Service> serviceList = serviceRepository.findByCatalogues_NameAndWorkflowIsNull(catalogueName);
        return serviceList;
    }


    public Collection<Service> getAllServices()
    {
        //List<Service> services = serviceRepository.findAll();
        List<Service> services = serviceRepository.getServiceByWorkflowIsNull();
        return services;
    }

    public void delete(Long id)
    {
        serviceRepository.deleteById(id);
    }

    public void delete(Service service)
    {
        serviceRepository.delete(service);
    }

    public Service save(Service s)
    {
        return serviceRepository.save(s);
    }

    public List<Service> saveAll(List<Service> s)
    {
        return serviceRepository.saveAll(s);
    }

    public void deleteAll()
    {
        serviceRepository.deleteAll();
    }

    public Collection<Service> getAllServices(String name) {
        Collection<Service> services = serviceRepository.getServiceByNameAndWorkflowIsNullOrderByVersionDesc(name);
        return services;

    }
}

