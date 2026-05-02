package edu.hyf.car_rental.repository;


import edu.hyf.car_rental.model.Car;
import edu.hyf.car_rental.model.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository

public interface CarRepository extends JpaRepository <Car, Long>{
    boolean existsById(Long id);
    Optional<Car>findById(Long id);
    List<Car> findByStatus(CarStatus status);

    // GET ALL AVAILABLE CARS.
    @Query("""
    SELECT c FROM Car c 
    LEFT JOIN c.rentalRecord r
    ON (
    (r.startDate < :returnDate AND r.returnDate > :startDate)
    OR (r.exactReturnDate IS NOT NULL AND r.startDate < :returnDate AND r.exactReturnDate > :startDate)
        )
    WHERE r.id IS NULL
    """)
    List<Car> getAvailableCars(
            @Param("startDate") LocalDate startDate,
            @Param("returnDate") LocalDate returnDate);
}
