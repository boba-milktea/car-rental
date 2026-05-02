package edu.hyf.car_rental.mapper;

import edu.hyf.car_rental.dto.RentalRequestDTO;
import edu.hyf.car_rental.dto.RentalResponseDTO;
import edu.hyf.car_rental.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)

public interface RentalMapper {
    //Map nested property: rental.car.id → DTO.carId
    @Mapping(source = "car.id", target = "carId")
    RentalResponseDTO toResponseDTO(Rental rental);
    //Ignore fields not provided by DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "car", ignore = true)
    Rental toEntity (RentalRequestDTO dto);
}
