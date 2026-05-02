package edu.hyf.car_rental.repository;

import edu.hyf.car_rental.model.Car;
import edu.hyf.car_rental.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository

public interface RentalRepository extends JpaRepository<Rental, Long> {

    Optional<Rental>findById(Long id);
    List<Rental> findRentalByCarId(Long id);
    // OVERDUE
    @Query("""
           SELECT r
           FROM Rental r
           WHERE r.exactReturnDate IS NULL
           AND r.returnDate < CURRENT_DATE
           """)
    List<Rental> findOverdueRentals();
}
