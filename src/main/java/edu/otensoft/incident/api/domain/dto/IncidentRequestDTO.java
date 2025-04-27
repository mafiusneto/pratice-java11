package edu.otensoft.incident.api.domain.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class IncidentRequestDTO {
    
    @NotBlank(message = "Name is required")
    private String name;    
    
    @NotBlank(message = "Description is required")
    private String description;
    private boolean closed;
}
