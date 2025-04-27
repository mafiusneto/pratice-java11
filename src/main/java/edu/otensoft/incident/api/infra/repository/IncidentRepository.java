package edu.otensoft.incident.api.infra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.otensoft.incident.api.domain.entity.Incident;

public interface IncidentRepository extends JpaRepository<Incident, Long>{
    
    Page<Incident> findByNameContainingIgnoreCaseAndClosedAtIsNull(String name, Pageable pageable);

    Page<Incident> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
