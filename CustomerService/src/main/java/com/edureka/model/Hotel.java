package com.edureka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
	private Long id;

	private String hotelName;

	private String roomType;

	private String reservationStatus;
}
