package edu.otensoft.incident.api.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.otensoft.incident.api.domain.entity.Incident;

public interface IncidentRepository extends JpaRepository<Incident, Long>{
    
}
