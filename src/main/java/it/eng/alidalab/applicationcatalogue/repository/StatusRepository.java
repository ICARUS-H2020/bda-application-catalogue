package it.eng.alidalab.applicationcatalogue.repository;

import it.eng.alidalab.applicationcatalogue.domain.Owner;
import it.eng.alidalab.applicationcatalogue.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface StatusRepository extends JpaRepository<Status, Long> {


}
