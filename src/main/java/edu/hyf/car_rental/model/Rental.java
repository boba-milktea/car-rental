package edu.hyf.car_rental.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="rental")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//Don’t include relationships in toString()
@ToString(exclude = "car")

public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private Long rentalId;

    @NotNull
    @Column(name="start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name="return_date", nullable = false)
    private LocalDate returnDate;

    @Column(name="exact_return_date")
    private LocalDate exactReturnDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

}
