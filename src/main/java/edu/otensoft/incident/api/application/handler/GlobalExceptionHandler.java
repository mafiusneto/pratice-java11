package edu.otensoft.incident.api.application.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import edu.otensoft.incident.api.infra.exception.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    // TODO melhoria mapear erros, add logger
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        var response = new ResponseErrorDTO(
            LocalDateTime.now(), 
            HttpStatus.NOT_FOUND.value(), 
            "Not found", 
            ex.getMessage(), 
            null);
            
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> erros = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            erros.add(error.getField() +" - "+error.getDefaultMessage())
        );
        
        var response = new ResponseErrorDTO(
            LocalDateTime.now(), 
            HttpStatus.BAD_REQUEST.value(), 
            "Bad request", 
            "Validation failed for arguments", 
            erros);
            
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        var response = new ResponseErrorDTO(
            LocalDateTime.now(), 
            HttpStatus.INTERNAL_SERVER_ERROR.value(), 
            "Internal Server Error", 
            "Ocorreu um erro inesperado: " + ex.getMessage(), 
            null);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
