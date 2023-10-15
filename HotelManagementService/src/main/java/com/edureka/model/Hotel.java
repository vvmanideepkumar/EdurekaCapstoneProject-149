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
public class Hotel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "hotelName", nullable = false)
	private String hotelName;

	@Column(name = "roomType", nullable = false)
	private String roomType;

	@Column(name = "reservationStatus", nullable = false)
	private String reservationStatus;
}
