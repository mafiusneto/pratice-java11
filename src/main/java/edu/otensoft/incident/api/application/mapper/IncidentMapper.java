package edu.otensoft.incident.api.application.mapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.otensoft.incident.api.domain.dto.IncidentRequestDTO;
import edu.otensoft.incident.api.domain.dto.IncidentResponseDTO;
import edu.otensoft.incident.api.domain.entity.Incident;

@Component
public class IncidentMapper {
    
    public Incident toEntity(IncidentRequestDTO dto){
        var incident = new Incident();
        if (dto.getName() != null){
            incident.setName(dto.getName());
        }
        
        if (dto.getDescription() != null){
            incident.setDescription(dto.getDescription());
        }
        if(dto.isClosed()){
            incident.setClosedAt(LocalDateTime.now());
        }
        return incident;
    }

    public IncidentResponseDTO toResponseDTO(Incident incident){
        var response = new IncidentResponseDTO();
        if (incident.getId() >0){
            response.setIdIncident(incident.getId());
        }

        if (incident.getName() != null){
            response.setName(incident.getName());
        }
        
        if (incident.getDescription() != null){
            response.setDescription(incident.getDescription());
        }

        if(incident.getClosedAt() != null){
            response.setClosedAt(incident.getCreatedAt());
        }

        if(incident.getCreatedAt() != null){
            response.setCreatedAt(incident.getCreatedAt());
        }

        if(incident.getUpdatedAt() != null){
            response.setUpdatedAt(incident.getUpdatedAt());
        }
        return response;
    }

    public List<IncidentResponseDTO> toResponseDTOList(List<Incident> incidents){
        if  (incidents.isEmpty()){
            return Collections.emptyList();
        }

        return incidents.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public Incident toUpdate(Incident source, Incident target){
        if (target.getName() != null){
            source.setName(target.getName());
        }
        
        if (target.getDescription() != null){
            source.setDescription(target.getDescription());
        }

        if(target.getClosedAt() != null){
            source.setClosedAt(target.getClosedAt());
        }

        if(target.getCreatedAt() != null){
            source.setCreatedAt(target.getCreatedAt());
        }

        if(target.getUpdatedAt() != null){
            source.setUpdatedAt(target.getUpdatedAt());
        }

        return source;
    }

}
