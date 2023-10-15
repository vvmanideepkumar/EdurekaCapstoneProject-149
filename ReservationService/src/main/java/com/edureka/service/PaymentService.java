package com.edureka.service;

import com.edureka.model.Hotel;
import com.edureka.model.HotelBookingRequest;
import com.edureka.model.PaymentDto;
import com.edureka.model.PaymentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {

    @Value("${com.edureka.payment_service_make_payment}")
    String paymentServiceMakePaymentUrl;
    @Autowired
    RestTemplate restTemplate;
    public ResponseEntity callPaymentSvcAndMakePayment(HotelBookingRequest bookingReq){
        try{
            PaymentDto p =PaymentDto.builder().customerId(bookingReq.getCustomerId()).amount(bookingReq.getPaymentAmount()).build();

            HttpEntity<PaymentDto> request=new HttpEntity<>(p);
            System.err.println("calling payment service To make payment"+ paymentServiceMakePaymentUrl +"with payload --> "+request);
            ResponseEntity<PaymentResponseDTO> paymentResponse = restTemplate.exchange(paymentServiceMakePaymentUrl, HttpMethod.POST,request, PaymentResponseDTO.class);
            System.out.println("successfully invoked payment service with url "+ paymentServiceMakePaymentUrl);
            return paymentResponse;
        }catch(RestClientException e){
            String reason = "Sorry!!!, please Ensure Payment serviceis up or not. \n Unable to make a payment as exception occurred by invoking Payment service to with url \n " + paymentServiceMakePaymentUrl
                    +  "\n reason --> "+e.getCause() +e.getMessage();
            System.err.println(reason);
            return new ResponseEntity<>(reason, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
