package com.edureka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HotelBookingRequest implements Serializable {
    Long customerId;
    String hotelName;
    String roomType;
    Double paymentAmount;
    Long accountNumber;


}
