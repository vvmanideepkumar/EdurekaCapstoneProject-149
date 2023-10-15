package com.edureka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customerId", nullable = false)
    private Long customerId;

    @Column(name = "hotelId", nullable = false)
    private Long hotelId;

    @Column(name = "startDate", nullable = true)
    private String startDate;

    @Column(name = "endDate", nullable = true)
    private String endDate;

    // Getters and setters...

}
