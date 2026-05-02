package edu.hyf.car_rental.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="car")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//Don’t include relationships in toString()
@ToString(exclude = "rentalRecord")


public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String brand;

    @NotNull
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^\\d{1}-[A-Z]{3}-\\d{3}$", message = "Wrong plate format: E.g: 1-RTY-456.")
    private String plate;

    @NotNull
    @Column(nullable = false)
    private int year;

    private String color;

    private String model;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CarStatus status = CarStatus.AVAILABLE;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Rental> rentalRecord = new ArrayList<>();

    public void addRental(Rental rental) {
        rentalRecord.add(rental);
        rental.setCar(this);
    }

    public void removeRental(Rental rental) {
        rentalRecord.remove(rental);
        rental.setCar(null);
    }
}
