package edu.hyf.car_rental.service;

import edu.hyf.car_rental.dto.CarRequestDTO;
import edu.hyf.car_rental.dto.CarResponseDTO;
import edu.hyf.car_rental.dto.CarUpdateRequestDTO;
import edu.hyf.car_rental.dto.RentalResponseDTO;
import edu.hyf.car_rental.exception.CarNotFoundException;
import edu.hyf.car_rental.mapper.CarMapper;
import edu.hyf.car_rental.model.Car;
import edu.hyf.car_rental.model.CarStatus;
import edu.hyf.car_rental.repository.CarRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarService {
    private final CarRepository crepo;
    private final CarMapper mapper;

    public CarService(CarRepository repo,CarMapper mapper ){

        this.crepo = repo;
        this.mapper = mapper;
    }
    /* GET */
    // All cars.
    public List<CarResponseDTO> findAllCars(){
        return crepo.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    // Car by id.
    public CarResponseDTO findCarById(Long id){
       Car car = crepo.findById(id).orElseThrow(()-> new CarNotFoundException(id));
       return mapper.toResponseDTO(car);
    }
    // Check if car exist (internal use).
    private Car IsCar(Long id){
        return crepo.findById(id).orElse(null);
    }


    // Rented cars.
    public List<Car> findAllAvailableCars(){
        return crepo.findByStatus(CarStatus.valueOf("AVAILABLE"));
    }

    // Search by criteria
    public List<Car> searchBy(){
        // TODO
        return crepo.findByStatus(CarStatus.valueOf("AVAILABLE"));
    }

    // Get all available cars for a rental period.
    public List<CarResponseDTO> getAvailableCars(LocalDate startDate, LocalDate returnDate){
        return crepo.getAvailableCars(startDate, returnDate).stream().map(mapper::toResponseDTO).toList();
    }

    /* POST */
    public ResponseEntity<?> addCar(CarRequestDTO carDTO){
        crepo.save(mapper.toEntity(carDTO));
        return ResponseEntity.ok(carDTO);
    }

    /* PATCH */
    public ResponseEntity<?> updateCar(Long id, CarUpdateRequestDTO carDTO){
        Car car = IsCar(id);
        if(car == null){return ResponseEntity.notFound().build();}
        System.out.println("Before mapper: " + car);
        mapper.updateFromDTO(carDTO,car);
        System.out.println("After mapper: " + car);
        crepo.save(car);
        return ResponseEntity.ok(car);

    }
    /* DELETE */
    public ResponseEntity<?> deleteCar(Long id){
        Car car = IsCar(id);
        if(car == null){
            return ResponseEntity.notFound().build();
        }
        crepo.deleteById(id);
        return ResponseEntity.ok(car);
    }

}
