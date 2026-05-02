package edu.hyf.car_rental.mapper;

import edu.hyf.car_rental.dto.CarRequestDTO;
import edu.hyf.car_rental.dto.CarResponseDTO;
import edu.hyf.car_rental.dto.CarUpdateRequestDTO;
import edu.hyf.car_rental.model.Car;
import org.mapstruct.*;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CarMapper {
    CarResponseDTO toResponseDTO(Car car);
    //Ignore fields not provided by DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rentalRecord", ignore = true)
    Car toEntity(CarRequestDTO dto);
    //Used in car_rental.service.updateCar()
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rentalRecord", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDTO(CarUpdateRequestDTO dto, @MappingTarget Car car);
}
