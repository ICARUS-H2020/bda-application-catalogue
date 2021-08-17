package it.eng.alidalab.applicationcatalogue.services;

import it.eng.alidalab.applicationcatalogue.domain.Model;
import it.eng.alidalab.applicationcatalogue.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;


@Service
@Transactional
public class ModelService {

    @Autowired
    private ModelRepository modelRepository;

    public Model getModelByLocationAndOrganizationIdAndOwnerId(String location, String organizationId, String ownerId){
        return modelRepository.getModelByLocationAndOrganizationIdAndOwnerId(location,organizationId,ownerId);
    }


    public Collection<Model> findModelsByOrganizationIdAndOwnerId(String organizationId, String ownerId) {
        return modelRepository.findModelsByOrganizationIdAndOwnerId(organizationId, ownerId);
    }

    public Model findById(Long id){
        Optional<Model> modelOp = modelRepository.findById(id);
        return modelOp.isPresent()?modelOp.get():null;
    }

    public Model save(Model model) {
        return modelRepository.save(model);
    }
}
