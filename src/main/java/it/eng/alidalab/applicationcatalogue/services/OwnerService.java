package it.eng.alidalab.applicationcatalogue.services;

import it.eng.alidalab.applicationcatalogue.domain.Dataset;
import it.eng.alidalab.applicationcatalogue.domain.Owner;
import it.eng.alidalab.applicationcatalogue.repository.BDAApplicationRepository;
import it.eng.alidalab.applicationcatalogue.repository.DatasetRepository;
import it.eng.alidalab.applicationcatalogue.repository.OrganizationRepository;
import it.eng.alidalab.applicationcatalogue.repository.OwnerRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class OwnerService {
    @Inject
    private OwnerRepository ownerRepository;

    @Inject
    private OrganizationRepository organizationRepository;

    public Owner getOwnerDb(Owner owner) {
        Optional<Owner> ownerOp1 = ownerRepository.findById(owner.getId());
        if(!ownerOp1.isPresent() && owner.getOrganization()!=null) {
            owner.setOrganization(organizationRepository.save(owner.getOrganization()));
            return ownerRepository.save(owner);
        } else{
            return ownerOp1.get();
        }
    }
}
