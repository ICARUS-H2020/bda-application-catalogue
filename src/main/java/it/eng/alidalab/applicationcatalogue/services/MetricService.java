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

import it.eng.alidalab.applicationcatalogue.domain.service.Metric;
import it.eng.alidalab.applicationcatalogue.repository.MetricRepository;
import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.Collection;

@Transactional
public class MetricService
{

    @Inject
    private MetricRepository repo;

    public Metric getMetric(Long id) throws NotFoundException {
        return repo.findById(id).orElseThrow(NotFoundException::new);
    }

    public Collection<Metric> getMetrics()
    {
        return repo.findAll();
    }

    public Collection<Metric> getMetrics(Long serviceId)
    {
        return repo.findByServiceId(serviceId);
    }

    public Metric save(Metric m)
    {
        return repo.save(m);
    }

    public void delete(Long id)
    {
        repo.deleteById(id);
    }
}
