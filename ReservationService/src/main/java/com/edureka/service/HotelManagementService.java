package com.edureka.service;

import com.edureka.model.Hotel;
import com.edureka.model.HotelBookingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class HotelManagementService {

    @Value("${com.edureka.hotel_management_service_getHotelAvailabilityStatus_url}")
    String hotelMgmntSvcHoteAvailabilityURL;
    @Value("${com.edureka.hotel_management_service_updateRoomReservationStatus_url}")
    String hotelMgmntSvcHotelUpdateRoomStatusURL;

    @Autowired
    RestTemplate restTemplate;
    public ResponseEntity callHotelMgmntSvcAndGetHotelAvailabilityStatus(HotelBookingRequest bookingReq){
        try{
            Hotel h=Hotel.builder().hotelName(bookingReq.getHotelName()).roomType(bookingReq.getRoomType()).build();
            HttpEntity<Hotel> request=new HttpEntity<>(h);
            System.err.println("calling hotelMgmntSvc To get HotelAvailability"+ hotelMgmntSvcHoteAvailabilityURL+"with payload --> "+request);
            ResponseEntity<Hotel> hotel = restTemplate.exchange(hotelMgmntSvcHoteAvailabilityURL, HttpMethod.POST,request,Hotel.class);
            System.out.println("successfully invoked Hotel Management service with url "+ hotelMgmntSvcHoteAvailabilityURL);
            return hotel;
        }catch(RestClientException e){
            String reason = "Sorry!!!, please Ensure Hotel management is up or not. \n Unable to check the hotel availability as exception occurred by invoking Hotel Management service to with url \n " + hotelMgmntSvcHoteAvailabilityURL
                 +  "\n reason --> "+e.getCause() +e.getMessage();
            System.err.println(reason);
        return new ResponseEntity<>(reason, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity updateHotelStatusTokBookedUsingHotelMgmntService(HotelBookingRequest bookingReq,String newHotelStatus) {
        try{
            Hotel h=Hotel.builder().hotelName(bookingReq.getHotelName()).roomType(bookingReq.getRoomType()).reservationStatus(newHotelStatus).build();
            HttpEntity<Hotel> request=new HttpEntity<>(h);
            System.err.println("calling hotelMgmntSvc To get HotelAvailability"+ hotelMgmntSvcHotelUpdateRoomStatusURL+"with payload --> "+request);
            ResponseEntity<Hotel> hotel = restTemplate.exchange(hotelMgmntSvcHotelUpdateRoomStatusURL, HttpMethod.POST,request,Hotel.class);
            System.out.println("successfully invoked Hotel Management service with url "+ hotelMgmntSvcHotelUpdateRoomStatusURL);
            return hotel;
        }catch(RestClientException e){
            String reason = "Sorry!!!, please Ensure Hotel management is up or not. \n Unable to check the hotel availability as exception occurred by invoking Hotel Management service to with url \n " + hotelMgmntSvcHoteAvailabilityURL
                    +  "\n reason --> "+e.getCause() +e.getMessage();
            System.err.println(reason);
            return new ResponseEntity<>(reason, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
