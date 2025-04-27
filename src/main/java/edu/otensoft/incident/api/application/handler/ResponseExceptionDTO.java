package edu.otensoft.incident.api.application.handler;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseExceptionDTO {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private List<String> errors;

    public ResponseExceptionDTO(LocalDateTime timestamp, int status, String error, String message,
            List<String> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.errors = errors;
    }
}
