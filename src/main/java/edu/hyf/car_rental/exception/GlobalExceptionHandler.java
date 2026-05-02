package edu.hyf.car_rental.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handle @Valid errors (RequestBody validation).
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationErrors(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        // FIELD ERRORS
        exception.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );
        // CLASS ERRORS (E.g @EndDateAfterStartDate)
        exception.getBindingResult().getGlobalErrors().forEach(err ->
                errors.put(err.getObjectName(), err.getDefaultMessage())
        );

        return errors;
    }
    // Handle @Validated param errors (path variables / request params).
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleConstraintViolation(ConstraintViolationException exception) {
        Map<String, String> violations = new HashMap<>();
        exception.getConstraintViolations().forEach(violation ->
                violations.put(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                )
        );
        return violations;
    }

    // Handle CarNotFoundException
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CarNotFoundException.class)
    public Map<String, String> handleCarNotFound(CarNotFoundException exception) {
       return Map.of("error", exception.getMessage());
    }

    // Handle RentalNotFoundException
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RentalNotFoundException.class)
    public Map<String, String> handleRentalNotFound(RentalNotFoundException exception) {
        return Map.of("error", exception.getMessage());
    }

}
