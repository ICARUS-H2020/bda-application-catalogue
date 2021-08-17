package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.Dataset;
import it.eng.alidalab.applicationcatalogue.domain.workflow.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatasetRepository extends JpaRepository<Dataset, Long>, DatasetRepositoryCustom{

    Dataset findByRefIdAndType(Long refId, String type);
    List<Dataset> findAllByCreatedBy (Workflow workflow);
}
