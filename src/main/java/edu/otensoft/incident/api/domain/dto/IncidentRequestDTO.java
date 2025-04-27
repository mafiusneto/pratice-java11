package edu.otensoft.incident.api.domain.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class IncidentRequestDTO {
    
    @Schema(description = "Name incident", example = "Error in update")
    @NotBlank(message = "Name is required")
    private String name;    
        
    @Schema(description = "Description of incident", example = "Not found parameter file")
    @NotBlank(message = "Description is required")
    private String description;
    
    @Schema(description = "Closed - indicates whether incident is closed", example = "false", defaultValue = "false")
    private boolean closed;
}
