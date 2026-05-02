package edu.hyf.car_rental.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RentalNotFoundException extends RuntimeException {
    public RentalNotFoundException(Long id) {
        super ("Rental with id " + id +" does not exist.");
    }
}
