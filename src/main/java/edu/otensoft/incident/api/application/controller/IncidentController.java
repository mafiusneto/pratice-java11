package edu.otensoft.incident.api.application.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.otensoft.incident.api.application.mapper.IncidentMapper;
import edu.otensoft.incident.api.application.service.IncidentService;
import edu.otensoft.incident.api.domain.dto.IncidentRequestDTO;
import edu.otensoft.incident.api.domain.dto.IncidentResponseDTO;
import edu.otensoft.incident.api.domain.entity.Incident;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Incidents", description = "Incident Management")
@RestController
@RequestMapping("/incidents")
public class IncidentController {

    @Autowired
    private IncidentService service;

    @Autowired
    private IncidentMapper mapper;

    @Operation(summary = "List all incidents")
    @GetMapping
    public ResponseEntity<List<IncidentResponseDTO>> listAll(){
        return ResponseEntity.ok(mapper.toResponseDTOList(service.listAll()));
    }

    @Operation(summary = "List with filters and pagination")
    @GetMapping("/page")
    public ResponseEntity<Page<IncidentResponseDTO>> listPage(        
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Boolean openOnly,
        @Parameter(description = "Number page", example = "0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Size page", example = "20") @RequestParam(defaultValue = "20") int size,
        @Parameter(description = "Field sort", example = "id") @RequestParam(defaultValue = "createdAt") String sortBy,
        @Parameter(description = "Direction", example = "DESC") @RequestParam(defaultValue = "DESC") String direction,    
        @Parameter(hidden = true)@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){

        Page<Incident> incidents = service.pageList(name, openOnly, pageable);
        Page<IncidentResponseDTO> dtos = incidents.map(mapper::toResponseDTO);

        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "List the latest records")
    @GetMapping("/latest")
    public ResponseEntity<List<IncidentResponseDTO>> listLatest(){
        Pageable pageable = PageRequest.of(0,20,Sort.by(Sort.Direction.DESC, "createdAt"));      
        Page<Incident> incidents = service.pageList(null, null, pageable);
        Page<IncidentResponseDTO> dtos = incidents.map(mapper::toResponseDTO);

        return ResponseEntity.ok(dtos.getContent());
    }
    
    @Operation(summary = "Find incident by ID")
    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(mapper.toResponseDTO(service.getById(id)));
    }

    @Operation(summary = "Create incident")
    @PostMapping
    public ResponseEntity<IncidentResponseDTO> create(@Valid @RequestBody IncidentRequestDTO dto){
         Incident incident = mapper.toEntity(dto);
        return ResponseEntity.ok(mapper.toResponseDTO(service.save(incident)));
    }

    @Operation(summary = "Update incident by ID")
    @PutMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> update(@PathVariable Long id, @Valid @RequestBody IncidentRequestDTO dto){
        Incident incident = mapper.toEntity(dto);
        return ResponseEntity.ok(mapper.toResponseDTO(service.update(id, incident)));
    }

    @Operation(summary = "Delete incident by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
}
