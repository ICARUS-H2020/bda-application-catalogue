package it.eng.alidalab.applicationcatalogue.services;

import it.eng.alidalab.applicationcatalogue.domain.BDAApplication;
import it.eng.alidalab.applicationcatalogue.domain.VizConfiguration;
import it.eng.alidalab.applicationcatalogue.repository.VizConfigurationRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import java.util.Optional;


@Transactional
public class VizConfigurationServices {

    @Inject
    VizConfigurationRepository vizConfigurationRepository;

    @Inject
    BDAApplicationService bdaApplicationService;

    public VizConfiguration getVizConfiguration(Long id, Long bdaId, String ownerId, String organizationId) {
        return vizConfigurationRepository.getVizConfiguration_VizConfigurationIdAndOrganizationIdAndOwnerId(id,bdaId, organizationId, ownerId);
    }

    public VizConfiguration save(Long bdaId, String organizationId, String ownerId, VizConfiguration vizConfiguration){
        Optional<BDAApplication> bdaApplication = bdaApplicationService.findBDAApplicationByBDAApplicationIdAndOrganizationIdAndOwnerId(bdaId, organizationId, ownerId);
        if(!bdaApplication.isPresent()){
            throw new BadRequestException();
        }
        vizConfiguration.setBdaApplication(bdaApplication.get());
        return vizConfigurationRepository.save(vizConfiguration);
    }
}
