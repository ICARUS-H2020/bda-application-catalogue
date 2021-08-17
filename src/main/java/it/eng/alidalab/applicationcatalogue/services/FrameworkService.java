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

import it.eng.alidalab.applicationcatalogue.domain.service.Framework;
import it.eng.alidalab.applicationcatalogue.repository.FrameworkRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;

@Transactional
public class FrameworkService
{
    @Inject
    private FrameworkRepository fRepo;


    public void deleteByName(String testFW1)
    {
        fRepo.deleteByName(testFW1);
    }

    public List<Framework> getFrameworks()
    {
        return fRepo.findAll();
    }

    public Framework getFramework(Long frameworkId)
    {
        return fRepo.findById(frameworkId).orElseThrow(NotFoundException::new);
    }

    public Framework createFramework(Framework framework)
    {
        return fRepo.save(framework);
    }

    public List<Framework> createFrameworks(List<Framework> frameworks)
    {
        return fRepo.saveAll(frameworks);
    }

    public void delete(Long id)
    {
        fRepo.deleteById(id);
    }
}
