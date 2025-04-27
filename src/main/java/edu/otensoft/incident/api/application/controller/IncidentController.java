package edu.otensoft.incident.api.application.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.otensoft.incident.api.application.mapper.IncidentMapper;
import edu.otensoft.incident.api.application.service.IncidentService;
import edu.otensoft.incident.api.domain.dto.IncidentRequestDTO;
import edu.otensoft.incident.api.domain.dto.IncidentResponseDTO;
import edu.otensoft.incident.api.domain.entity.Incident;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    @Autowired
    private IncidentService service;

    @Autowired
    private IncidentMapper mapper;
/*
ok • Cadastrar Incidentes
ok • Atualizar Incidentes
ok* • Deletar Incidentes // melhor deletar virtual
ok • Listar todos incidentes
ok*• Listar incidente por ID  // find by id?
• Listar os últimos 20 incidentes ordenados por ordem decrescente
*/

// TODO add pagination e filter
    @GetMapping
    public ResponseEntity<List<IncidentResponseDTO>> list(){
        return ResponseEntity.ok(mapper.toResponseDTOList(service.listAll()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(mapper.toResponseDTO(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<IncidentResponseDTO> create(@Valid @RequestBody IncidentRequestDTO dto){
         Incident incident = mapper.toEntity(dto);
        return ResponseEntity.ok(mapper.toResponseDTO(service.save(incident)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> update(@PathVariable Long id, @Valid @RequestBody IncidentRequestDTO dto){
        Incident incident = mapper.toEntity(dto);
        return ResponseEntity.ok(mapper.toResponseDTO(service.update(id, incident)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.remove(id);
        if(id >1){
            throw new RuntimeException("tteste");
        }
        return ResponseEntity.noContent().build();
    }
}
