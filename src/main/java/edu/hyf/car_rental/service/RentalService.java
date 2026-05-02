package edu.hyf.car_rental.service;

import edu.hyf.car_rental.dto.RentalRequestDTO;
import edu.hyf.car_rental.dto.RentalResponseDTO;
import edu.hyf.car_rental.mapper.CarMapper;
import edu.hyf.car_rental.mapper.RentalMapper;
import edu.hyf.car_rental.model.Car;
import edu.hyf.car_rental.model.CarStatus;
import edu.hyf.car_rental.model.Rental;
import edu.hyf.car_rental.repository.CarRepository;
import edu.hyf.car_rental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepo;
    private final CarRepository carRepo;
    private final RentalMapper mapper;
    private final CarMapper carMapper;

    /* GET ALL RENTALS */

    public List<RentalResponseDTO> findAllRentals() {

        return rentalRepo.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    /* GET RENTAL BY ID */
    public RentalResponseDTO findRentalById(Long id) {
        Rental rental = rentalRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Rental not found"));
        return mapper.toResponseDTO(rental);
    }
    /* GET RENTALS BY CAR ID */
    public List<RentalResponseDTO> findRentalByCarId(Long carId) {
        return rentalRepo.findRentalByCarId(carId).stream().map(mapper::toResponseDTO).toList();
    }

    /* GET OVERDUE RENTALS */
    public List<RentalResponseDTO> getOverdueRentals() {
            return rentalRepo.findOverdueRentals()
                    .stream()
                    .map(mapper::toResponseDTO)
                    .toList();
    }

    /* CREATE RENTAL */
    public RentalResponseDTO createRental(RentalRequestDTO dto) {
        // Check if auto exists.
        Long carId = dto.getCarId();
        Car car = carRepo.findById(carId)
                .orElseThrow(() ->
                        new RuntimeException("Car with id " + carId + " not found"));
        // Check if car is not rented at date.
        if (!isCarAvailable(dto)) {
            throw new RuntimeException("Car already rented");
        }
        // TODO if date include current date.
        Rental rental = mapper.toEntity(dto);
        rental.setCar(car);
        car.setStatus(CarStatus.RENTED);
        Rental saved = rentalRepo.save(rental);
        return mapper.toResponseDTO(saved);
    }
    // Used to check if car is available.
    private boolean isCarAvailable(RentalRequestDTO dto){
        return findRentalByCarId(dto.getCarId()).stream().noneMatch(
              rental  ->
        rental.getStartDate().isBefore(dto.getReturnDate()) &&
                rental.getReturnDate().isAfter(dto.getStartDate())
        );
    }

    /* ACTIVATE RENTAL */
    public RentalResponseDTO activateRental(Long id) {
        Rental rental = rentalRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Rental not found"));
        rental.getCar().setStatus(CarStatus.RENTED);
        Rental updated = rentalRepo.save(rental);
        return mapper.toResponseDTO(updated);
    }

    /* COMPLETE RENTAL */

    public RentalResponseDTO completeRental(Long id) {
        Rental rental = rentalRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Rental not found"));
        rental.getCar().setStatus(CarStatus.AVAILABLE);

        Rental updated = rentalRepo.save(rental);

        return mapper.toResponseDTO(updated);
    }

    /* CANCEL RENTAL */
    public RentalResponseDTO cancelRental(Long id) {
        Rental rental = rentalRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Rental not found"));
        rental.getCar().setStatus(CarStatus.AVAILABLE);
        rentalRepo.delete(rental);
        return mapper.toResponseDTO(rental);
    }
}
