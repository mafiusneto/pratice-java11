package edu.otensoft.incident.api.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.otensoft.incident.api.application.mapper.IncidentMapper;
import edu.otensoft.incident.api.domain.entity.Incident;
import edu.otensoft.incident.api.infra.exception.NotFoundException;
import edu.otensoft.incident.api.infra.repository.IncidentRepository;

@Service
public class IncidentService {
    
    @Autowired
    private IncidentRepository repository;

    @Autowired
    private  IncidentMapper mapper;

    public List<Incident> listAll(){
        return repository.findAll();
    }

    public Page<Incident> pageList(String name, Boolean openOnly, Pageable pageable){
        if (openOnly != null && openOnly){
            return repository.findByNameContainingIgnoreCaseAndClosedAtIsNull(name == null? "": name, pageable);
        }
        return repository.findByNameContainingIgnoreCase(name == null? "": name, pageable);
    }

    public Incident save(Incident incident){
        return repository.save(incident);
    }

    public Incident update(Long id, Incident incident){
        var oldIncident = repository.findById(id);
       if(oldIncident.isEmpty()){
         throw new NotFoundException("Incident "+ id + " not found.");
       }
        var newIncident = mapper.toUpdate(oldIncident.get(), incident);
        return repository.save(newIncident);
    }

    public Incident getById(Long id){
        return repository.findById(id)
        .orElseThrow(()-> new NotFoundException("Incident "+ id + " not found."));
    }

    public void remove(Long id){
        Optional<Incident> incident = repository.findById(id);
        if (incident.isPresent()){
            repository.delete(incident.get());
        }
    }
}
