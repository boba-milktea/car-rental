package edu.hyf.car_rental.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import edu.hyf.car_rental.validation.DateRange;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EndDateAfterStartDate.Validator.class)
public @interface EndDateAfterStartDate {

    String message() default "Return date must be after start date.";
   Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // Validator class.
    class Validator implements ConstraintValidator<EndDateAfterStartDate, DateRange> {

        @Override
        public boolean isValid(DateRange dateRange, ConstraintValidatorContext context) {
            if (dateRange == null) {
                return true;
            }

            if (dateRange.getStartDate() == null || dateRange.getReturnDate() == null) {
                return true;
            }

            return dateRange.getReturnDate().isAfter(dateRange.getStartDate());

            // exactReturnDate can be null while creating a new rental, we add it while return (completeRental)
        }
    }
}
