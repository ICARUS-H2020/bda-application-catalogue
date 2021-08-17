package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.VizConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VizConfigurationRepository extends JpaRepository<VizConfiguration, Long>, VizConfigurationRepositoryCustom{

}
