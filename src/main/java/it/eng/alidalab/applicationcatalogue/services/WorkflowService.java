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

import it.eng.alidalab.applicationcatalogue.domain.BDAApplication;
import it.eng.alidalab.applicationcatalogue.domain.Dataset;
import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;
import it.eng.alidalab.applicationcatalogue.repository.BDAApplicationRepository;
import it.eng.alidalab.applicationcatalogue.repository.StatusRepository;
import it.eng.alidalab.applicationcatalogue.repository.WorkflowRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
public class WorkflowService
{
    @Inject
    private WorkflowRepository workflowRepository;

    @Inject
    private StatusRepository statusRepository;
    @Inject
    private BDAApplicationRepository bdaApplicationRepository;
    @Inject
    private BDAApplicationService bdaApplicationService;
    @Inject
    private DatasetService datasetService;

    public List<Workflow> getWorkflows(String organizationId, String ownerId)
    {
        return  workflowRepository.getWorkflows_OrganizationIdAndOwnerId(organizationId, ownerId);
        //return workflowRepository.findByOrganizationIdAndOwnerId(organizationId,ownerId);
    }

    public Workflow getWorkflow(Long workflowId, String ownerId, String organizationId)
    {
        return workflowRepository.getWorkflow_WorkflowIdAndOrganizationIdAndOwnerId(workflowId, organizationId, ownerId);

    }

    public Workflow getWorkflow(String workflowName)
    {
        return workflowRepository.findByName(workflowName);

    }

    public Workflow createWorkflow(Workflow workflow)
    {
        return workflowRepository.save(workflow);
    }

    public List<Workflow> createWorkflow(List<Workflow> workflows)
    {
        return workflowRepository.saveAll(workflows);
    }

    public void delete(Long id)
    {
        workflowRepository.deleteById(id);
    }

    public void deleteAll(List<Workflow> workflos)
    {
        workflowRepository.deleteAll(workflos);
    }

    public void deleteOld(BDAApplication bdaNew) {
        List<Workflow> workflowList = workflowRepository.findAllByBdaApplication(bdaNew);
        workflowList.removeAll(bdaNew.getWorkflows());
        List<Dataset> datasets = new ArrayList<>();
        workflowList.forEach(w -> {
            datasets.addAll(datasetService.findAllByWorkflow(w));
        });
        if(datasets != null && datasets.size()>0)datasetService.deleteAll(datasets);
        workflowRepository.deleteAll(workflowList);
    }


//    public List<Workflow> getTeamsWorkflows(String organizationId) {
  //      return workflowRepository.findById(workflowId).orElseThrow(NotFoundException::new);
    //}
}
