package grid.capstone.controller;

import grid.capstone.dto.v1.ExceptionDTO;
import grid.capstone.exception.AppointmentConflictException;
import grid.capstone.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Javaughn Stephenson
 * @since 11/07/2023
 */

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDTO resourceNotFound(ResourceNotFoundException exception) {
        return ExceptionDTO.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("Resource was not found")
                .details(exception.getMessage())
                .timestamp(LocalTime.now())
                .build();
    }

    @ExceptionHandler(AppointmentConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDTO appointmentConflict(AppointmentConflictException exception) {
        return ExceptionDTO.builder()
                .status(HttpStatus.CONFLICT)
                .message("Appointment conflict")
                .details(exception.getMessage())
                .timestamp(LocalTime.now())
                .build();
    }

    //For validation error handling
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            ConstraintViolationException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(error -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleMethodArgumentExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
