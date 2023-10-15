package com.edureka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentResponseDTO implements Serializable {
    String paymentStatus;
    Long paymentId;
}
