package edu.otensoft.incident.api.application.controller;

import java.net.URI;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.otensoft.incident.api.application.handler.ResponseErrorDTO;
import edu.otensoft.incident.api.application.mapper.IncidentMapper;
import edu.otensoft.incident.api.application.service.IncidentService;
import edu.otensoft.incident.api.domain.dto.IncidentRequestDTO;
import edu.otensoft.incident.api.domain.dto.IncidentResponseDTO;
import edu.otensoft.incident.api.domain.entity.Incident;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Incident updated"),
        @ApiResponse(responseCode = "404", description = "Incident not found", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDTO.class))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(mapper.toResponseDTO(service.getById(id)));
    }

    @Operation(summary = "Create incident")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Incident created with success"),        
        @ApiResponse(responseCode = "400", description = "Bad request", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDTO.class))),
    })
    @PostMapping
    public ResponseEntity<IncidentResponseDTO> create(@Valid @RequestBody IncidentRequestDTO dto){
        Incident incident = mapper.toEntity(dto);
        var response = mapper.toResponseDTO(service.save(incident));
        URI location = URI.create("/incidents");

        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Update incident by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Incident updated"),
        @ApiResponse(responseCode = "400", description = "Bad request", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDTO.class))),
        @ApiResponse(responseCode = "404", description = "Incident not found", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDTO.class))),
    })
    @PutMapping("/{id}")
    public ResponseEntity<IncidentResponseDTO> update(@PathVariable Long id, @Valid @RequestBody IncidentRequestDTO dto){
        Incident incident = mapper.toEntity(dto);
        return ResponseEntity.ok(mapper.toResponseDTO(service.update(id, incident)));
    }

    @Operation(summary = "Delete incident by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Incident deleted"),
        @ApiResponse(responseCode = "404", description = "Incident not found", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDTO.class))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.remove(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Close incident by ID")    
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Incident closed"),
        @ApiResponse(responseCode = "404", description = "Incident not found", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseErrorDTO.class))),
    })
    @PatchMapping("/{id}/close")
    public ResponseEntity<Boolean> close(@PathVariable Long id){        
        return ResponseEntity.ok(service.close(id));
    }
}
