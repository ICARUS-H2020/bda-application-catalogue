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

import it.eng.alidalab.applicationcatalogue.domain.Owner;
import it.eng.alidalab.applicationcatalogue.domain.service.Catalogue;
import it.eng.alidalab.applicationcatalogue.repository.CatalogueRepository;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class CatalogueService
{
    @Inject
    private CatalogueRepository repo;

    public List<Catalogue> getCatalogues()
    {
        List<Catalogue> catalogueList = repo.findAll();
        return catalogueList;
    }

    public Catalogue getCatalogue(String name)
    {
        Catalogue c = repo.findOneByName(name);
        Optional.ofNullable(c).orElseThrow(EntityNotFoundException::new);
        return c;
    }

    public Catalogue getCatalogue(Long id)
    {
        Optional<Catalogue> c = repo.findById(id);
        c.orElseThrow(EntityNotFoundException::new);
        return c.get();
    }
    public Catalogue saveCatalogue(Catalogue c)
    {
        return repo.save(c);
    }

    public List<Catalogue> saveCatalogue(List<Catalogue> c)
    {
        return repo.saveAll(c);
    }


    public void deleteCatalogueByContext(String name)
    {
        repo.deleteByName(name);
    }

    public void delete(Long catalogueId)
    {
        repo.deleteById(catalogueId);
    }



}
